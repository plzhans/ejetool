package com.ejetool.mq.redis.event;

import org.springframework.data.redis.connection.stream.RecordId;

import com.ejetool.mq.redis.stream.RedisStreamContext;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EventMessageHeader {
    @Getter
    private final RedisStreamContext context;

    @Getter
    private final RecordId recordId;

    public Long ack(){
        return this.context.ack(this.recordId);
    }
}
