package com.ejetool.mq.redis.stream;

import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.core.RedisTemplate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RedisStreamContextImpl implements RedisStreamContext {
    @Getter
    private final RedisTemplate<String, Object> redisTemplate;

    @Getter
    private final Consumer consumer;

    @Getter
    private final RedisStreamSettings settings;

    public Long ack(RecordId... recordIds){
        return this.redisTemplate.opsForStream().acknowledge(this.settings.getKey(), consumer.getGroup(), recordIds);
    }
}
