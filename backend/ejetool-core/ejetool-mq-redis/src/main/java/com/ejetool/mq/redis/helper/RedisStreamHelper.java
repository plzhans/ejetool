package com.ejetool.mq.redis.helper;

import org.springframework.data.redis.core.RedisTemplate;

import com.ejetool.mq.redis.exception.RedisStreamConsumerException;

import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.RedisStreamCommands.XClaimOptions;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.PendingMessage;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.connection.stream.StreamRecords;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@UtilityClass
public class RedisStreamHelper {

    /**
     * .을 제거하고 제일 처음 스트림키를 가져온다.
     * @param streamKey
     * @return
     */
    public static String getDefaultStreamKey(String streamKey) {
        var indexOf = streamKey.indexOf('.');
        if(indexOf != -1 ){
            return streamKey.substring(0, indexOf);
        } 
        return streamKey;
    }

    /**
     * 컨슈머를 자동 초기화하기 위한 편의성 인프라 함수
     * @param <K>
     * @param <V>
     * @param redisTemplate
     * @param streamKey
     * @param group
     * @param defaultValue
     */
    public static <K,V> boolean autoCreate(RedisTemplate<K, V> redisTemplate, K streamKey, String group, Function<K,V> defaultValueFunc) {
        // 스트림이 키가 없는 경우 생성
        var streamKeyType = redisTemplate.type(streamKey);
        if(streamKeyType == DataType.NONE){
            V value = defaultValueFunc.apply(streamKey);
            ObjectRecord<K, V> newRecord = StreamRecords.newRecord()
                .in(streamKey)
                .ofObject(value);

            redisTemplate.opsForStream().add(newRecord);
        } else if(streamKeyType != DataType.STREAM){
            throw new RedisStreamConsumerException(String.format("stream key type. key=%s type=%s", streamKey, streamKeyType));
        }

            // 컨슈머 그룹 존재 여부 확인
            boolean groupExists = redisTemplate
            .opsForStream()
            .groups(streamKey)
            .stream()
            .anyMatch(groupInfo -> {
                String x = groupInfo.groupName();
                return x.equals(group);
            });
        if(!groupExists){
            String result = createConsumer(redisTemplate, streamKey, group, ReadOffset.from("0"));
            if(!"OK".equalsIgnoreCase(result)){
                throw new RedisStreamConsumerException("consumer create fail.");
            }
            return true;
        }
        return false;
    }

    public static <K,V> String createConsumer(RedisTemplate<K, V> redisTemplate, K streamKey, String group, ReadOffset readOffset){
        return redisTemplate.opsForStream().createGroup(streamKey, readOffset, group);
    }

    /**
     * 팬딩 메세지 재시도
     * @param redisTemplate
     * @param streamKey
     * @param consumer
     * @param minIdleMs
     * @param retyCount
     */
    public static <K,V> List<RecordId> pendingToAdd(RedisTemplate<K, V> redisTemplate, K streamKey, Consumer consumer, long minIdleMs, int retyCount) {
        var pendingMessages = redisTemplate.opsForStream().pending(streamKey, consumer, Range.unbounded(), 10);
        if(pendingMessages.isEmpty()){
            return Collections.emptyList();
        }

        var ids = pendingMessages.map(PendingMessage::getId).stream().collect(Collectors.toList());
        var xClaimOptions = XClaimOptions
                    .minIdleMs(minIdleMs)
                    .ids(ids);
        if(retyCount > 0){
            xClaimOptions.retryCount(retyCount);
        }
        List<MapRecord<K, Object, Object>> messages = redisTemplate.opsForStream().claim(streamKey, consumer.getGroup(), consumer.getName(), xClaimOptions);
        for (MapRecord<K, Object, Object> message : messages) {
            var newMessage = StreamRecords.newRecord()
                .ofObject(message.getValue())
                .withStreamKey(message.getRequiredStream());
                
            redisTemplate.opsForStream().acknowledge(streamKey, consumer.getGroup(), message.getId());
            redisTemplate.opsForStream().add(newMessage);
        }
        return messages.stream().map(x->x.getId()).toList();
    }

    public static <K,V> void pendingToPublish(RedisTemplate<K, V> redisTemplate, K streamKey, Consumer consumer, long minIdleMs, int retyCount) {
        var pendingMessages = redisTemplate.opsForStream().pending(streamKey, consumer, Range.unbounded(), 10);
        if(!pendingMessages.isEmpty()){
            var ids = pendingMessages.map(PendingMessage::getId).stream().collect(Collectors.toList());
            var xClaimOptions = XClaimOptions
                        .minIdleMs(minIdleMs)
                        .ids(ids);
            if(retyCount > 0){
                xClaimOptions.retryCount(retyCount);
            }

            // 
        }
    }
}
