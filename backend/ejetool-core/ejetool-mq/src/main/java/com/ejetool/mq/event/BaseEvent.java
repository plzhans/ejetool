package com.ejetool.mq.event;

import java.time.Clock;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

public class BaseEvent{

    @Getter
    @JsonProperty("request_id") 
    private final String requestId;

    @Getter
    @JsonProperty("timestamp") 
	private final long timestamp;

    protected BaseEvent(String requestId){
        this.requestId = requestId;
        this.timestamp = System.currentTimeMillis();
    }

    protected BaseEvent(String requestId, long timestamp){
        this.requestId = requestId;
        this.timestamp = timestamp;
    }

    protected BaseEvent(String requestId, Clock clock){
        this.requestId = requestId;
        this.timestamp = clock.millis();
    }
}
