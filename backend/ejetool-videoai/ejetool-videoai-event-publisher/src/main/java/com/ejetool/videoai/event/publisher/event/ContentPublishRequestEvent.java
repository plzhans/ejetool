package com.ejetool.videoai.event.publisher.event;

import com.ejetool.mq.event.BaseEvent;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Getter;

@JsonDeserialize(builder = ContentPublishRequestEvent.JsonBuilder.class)
public class ContentPublishRequestEvent extends BaseEvent {
    
    @Getter
    private final long contentId;

    @Getter
    @JsonProperty("fail_retry") 
    private final boolean failRetry;

    @Getter
    @JsonProperty("force") 
    private final boolean force;

    @Builder
    @JsonCreator
    public ContentPublishRequestEvent(
        @JsonProperty("request_id") String requestId,
        @JsonProperty("timestamp") long timestamp, 
        @JsonProperty("content_id") long contentId,
        @JsonProperty(value="fail_retry", defaultValue="false") boolean failRetry,
        @JsonProperty(value="force", defaultValue="false") boolean force
        
    ){
        super(requestId, timestamp);
        this.contentId = contentId;
        this.failRetry = failRetry;
        this.force = force;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class JsonBuilder {}
}
