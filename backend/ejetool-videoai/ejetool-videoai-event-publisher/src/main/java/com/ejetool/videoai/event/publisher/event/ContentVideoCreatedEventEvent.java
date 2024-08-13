package com.ejetool.videoai.event.publisher.event;

import com.ejetool.mq.event.BaseEvent;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Getter;

@JsonDeserialize(builder = ContentVideoCreatedEventEvent.JsonBuilder.class)
public class ContentVideoCreatedEventEvent extends BaseEvent {
    
    @Getter
    private final long contentId;

    @Builder
    ContentVideoCreatedEventEvent(String requestId, long contentId){
        super(requestId);
        this.contentId = contentId;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class JsonBuilder {}
}
