package com.ejetool.videoai.event.publisher.event;

import com.ejetool.mq.event.BaseEvent;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Getter;

@JsonDeserialize(builder = ContentCreatedEvent.JsonBuilder.class)
public class ContentCreatedEvent extends BaseEvent {
    
    @Getter
    private final long contentId;

    @Builder
    @JsonCreator
    ContentCreatedEvent(
        @JsonProperty("request_id") String requestId, 
        @JsonProperty("content_id") long contentId, 
        @JsonProperty("timestamp") long timestamp)
    {
        super(requestId, timestamp);
        this.contentId = contentId;
    }

     @JsonPOJOBuilder(withPrefix = "")
    public static class JsonBuilder {}
}
