package com.ejetool.mq.redis.stream;

import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.core.RedisTemplate;

public interface RedisStreamContext {
    
    RedisTemplate<String, Object> getRedisTemplate();

    Consumer getConsumer();

    Long ack(RecordId... recordIds);

    RedisStreamSettings getSettings();

    public static RedisStreamContext create(RedisTemplate<String, Object> redisTempate, Consumer consumer, RedisStreamSettings settings){
        return new RedisStreamContextImpl(redisTempate, consumer, settings);
    }
}
