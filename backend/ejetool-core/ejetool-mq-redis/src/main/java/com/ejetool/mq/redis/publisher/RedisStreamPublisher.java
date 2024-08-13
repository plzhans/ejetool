package com.ejetool.mq.redis.publisher;

import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.ejetool.mq.redis.event.EventMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RedisStreamPublisher {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisStreamPublisher(RedisTemplate<String, Object> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    @Async
    public void publish(String streamKey, Object data) {
        try {
            EventMessage message = new EventMessage(data);
            ObjectRecord<String, EventMessage> objRecord = StreamRecords.newRecord()
                .ofObject(message)
                .withStreamKey(streamKey);

            redisTemplate.opsForStream().add(objRecord);
        } catch (Exception e) {
            log.error("publish(): error.", e);
            throw e;
        }
    }
}
