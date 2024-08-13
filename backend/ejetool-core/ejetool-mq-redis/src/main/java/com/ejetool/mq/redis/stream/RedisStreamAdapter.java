package com.ejetool.mq.redis.stream;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.RedisStreamCommands.XClaimOptions;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.PendingMessage;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.stream.Subscription;

import com.ejetool.mq.redis.event.EventMessageHeader;
import com.ejetool.mq.redis.exception.RedisStreamConsumerException;
import com.ejetool.mq.redis.event.EventMessage;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RedisStreamAdapter {
    
    private final Object listenerInst;
    private final Method listenerMethod;
    private final RedisStreamContext context;

    @Getter
    private StreamMessageListenerContainer<String, ObjectRecord<String, EventMessage>> container;
    private Subscription subscription;

    public RedisStreamAdapter(Object listenerInst, Method listenerMethod, RedisStreamContext context){
        this.listenerInst = listenerInst;
        this.listenerMethod = listenerMethod;
        this.context = context;
        this.container = this.createContainer();
    }

    private StreamMessageListenerContainer<String, ObjectRecord<String, EventMessage>> createContainer(){
        
        RedisStreamSettings settings = this.context.getSettings();
        int pollTimeout = settings.getInterval();
        if(pollTimeout < 1){
            pollTimeout = 60;
        }
        
        var options = StreamMessageListenerContainer.StreamMessageListenerContainerOptions
            .builder()
            .pollTimeout(Duration.ofSeconds(pollTimeout))
            .targetType(EventMessage.class)
            .errorHandler(t->this.errorHandler(t))
            .build();
        return StreamMessageListenerContainer.create(context.getRedisTemplate().getConnectionFactory(), options);
    }

    public void start(){
        this.start(false);
    }

    /**
     * 컨테이너 시작
     * @param notConsumerThenCreate 컨슈머가 없으며 생성
     */
    public void start(boolean notConsumerThenCreate){
        var consumer = this.context.getConsumer();
        var settings = this.context.getSettings();
        if(this.subscription != null){
            log.warn("[{}] redis stream container already start.", settings.getKey());
            return;
        }
        
        if(notConsumerThenCreate && !this.notConsumerGroupThenCreate()){
            this.pendingToReSend(0, 0);
        }

        StreamOffset<String> streamOffset = StreamOffset.create(settings.getKey(), settings.getOffset());
        this.subscription = this.container.receive(consumer, streamOffset, message->this.receiveHandler(message));
        this.container.start();
        log.info("[{}] redis stream container receive start. group={}, name={}, offset={}", settings.getKey(), consumer.getGroup(), consumer.getName(), streamOffset.getOffset());
    }

    private void receiveHandler(ObjectRecord<String, EventMessage> objectRecord){
        EventMessage message = objectRecord.getValue();
        Object messageData = message.getData();
        if(messageData != null){
            EventMessageHeader header = new EventMessageHeader(this.context, objectRecord.getId());

            Class<?>[] parameterTypes = this.listenerMethod.getParameterTypes();
            Object[] parameterValues = new Object[parameterTypes.length];

            parameterValues[0] = header;
            parameterValues[1] = messageData;

            if(!parameterTypes[1].equals(parameterValues[1].getClass())){
                log.error("[{}] method parameter type error. method={}, message_id={}", this.context.getSettings().getKey(), this.listenerMethod.getName(), objectRecord.getId());
                context.ack(objectRecord.getId());
                return;
            }

            try {
                this.listenerMethod.invoke(this.listenerInst, parameterValues);
            } catch (InvocationTargetException e) {
                log.error(String.format("[%s] method invoke fail. method=%s", this.context.getSettings().getKey(), this.listenerMethod.getName()), e);
            } catch (IllegalAccessException e) {
                log.error(String.format("[%s] method invoke fail. method=%s", this.context.getSettings().getKey(), this.listenerMethod.getName()), e);
            }
        } else {
            context.ack(objectRecord.getId());
        }
    }

    private void errorHandler(Throwable t){
        log.error(String.format("[%s] listener.error(): t=%s", this.context.getSettings().getKey(), t.getMessage()), t);
    }

    /**
     * 컨슈머 그룹이 없으면 생성한다
     * @param <K>
     * @param <V>
     * @param redisTemplate
     * @param streamKey
     * @param group
     * @param defaultValue
     */
    private boolean notConsumerGroupThenCreate() {
        var consumer = this.context.getConsumer();
        var settings = this.context.getSettings();
        var redisTemplate = this.context.getRedisTemplate();
        
        var streamKeyType = redisTemplate.type(settings.getKey());
        if(streamKeyType == DataType.NONE){
            EventMessage message = new EventMessage();
            ObjectRecord<String, EventMessage> newRecord = StreamRecords.newRecord()
                .in(settings.getKey())
                .ofObject(message);
            var recordId = redisTemplate.opsForStream().add(newRecord);
            log.info("[{}] redis stream empty message sended. record_id={}",  settings.getKey(), recordId);
        } else if(streamKeyType != DataType.STREAM){
            throw new RedisStreamConsumerException(String.format("stream key type. key=%s type=%s", settings.getKey(), streamKeyType));
        }

        boolean groupExists = redisTemplate
            .opsForStream()
            .groups(settings.getKey())
            .stream()
            .anyMatch(groupInfo -> {
                String x = groupInfo.groupName();
                return x.equals(consumer.getGroup());
            });
        if(!groupExists){
            String result = redisTemplate.opsForStream().createGroup(settings.getKey(), ReadOffset.from("0"), consumer.getGroup());
            if("OK".equalsIgnoreCase(result)){
                log.info("[{}] redis stream consumet group created. group={}", settings.getKey(), consumer.getGroup());
                return true;    
            } else {
                throw new RedisStreamConsumerException("consumer create fail.");
            }
        }
        return false;
    }

    /**
     * 팬딩 메세지 다시 추가
     * @param minIdleMs
     * @param retyCount
     */
    private void pendingToReSend(int minIdleMs, int retyCount) {
        var consumer = this.context.getConsumer();
        var settings = this.context.getSettings();
        var redisTemplate = this.context.getRedisTemplate();

        while(true){
            var pendingMessages = redisTemplate.opsForStream().pending(settings.getKey(), consumer, Range.unbounded(), 10);
            if(pendingMessages.isEmpty()){
                return;
            }

            var ids = pendingMessages.map(PendingMessage::getId).stream().collect(Collectors.toList());
            var xClaimOptions = XClaimOptions
                        .minIdleMs(minIdleMs)
                        .ids(ids);
            if(retyCount > 0){
                xClaimOptions.retryCount(retyCount);
            }

            List<MapRecord<String, Object, Object>> messages = redisTemplate.opsForStream().claim(settings.getKey(), consumer.getGroup(), consumer.getName(), xClaimOptions);
            for (MapRecord<String, Object, Object> message : messages) {
                var newMessage = StreamRecords.newRecord()
                    .ofObject(message.getValue())
                    .withStreamKey(message.getRequiredStream());
                    
                redisTemplate.opsForStream().acknowledge(settings.getKey(), consumer.getGroup(), message.getId());
                redisTemplate.opsForStream().add(newMessage);
                log.warn("[{}] redis stream pending resend. id={}", settings.getKey(), message.getId());
            }
        }
    }
}
