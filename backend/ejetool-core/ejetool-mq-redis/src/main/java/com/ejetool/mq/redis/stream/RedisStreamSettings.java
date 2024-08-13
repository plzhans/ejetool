package com.ejetool.mq.redis.stream;

import org.springframework.data.redis.connection.stream.ReadOffset;

import com.ejetool.mq.redis.annotation.RedisConsumerStream;

import lombok.Getter;

public class RedisStreamSettings {
    @Getter
    private final String key;

    @Getter
    private final ReadOffset offset;

    @Getter
    private final int interval;

    public RedisStreamSettings(String key, String offset, int interval){
        this.key = key;
        this.offset = ReadOffset.from(offset);
        this.interval = interval;
    }

    public RedisStreamSettings(String key, ReadOffset offset, int interval){
        this.key = key;
        this.offset = offset;
        this.interval = interval;
    }

    public static RedisStreamSettings create(RedisConsumerStream source){
        RedisStreamSettings inst = new RedisStreamSettings(source.value(), ReadOffset.lastConsumed(), source.interval());
        return inst;
    }
}
