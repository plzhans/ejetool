package com.ejetool.videoai.event.publisher.event;

import com.ejetool.mq.event.BaseEvent;

import lombok.Builder;
import lombok.Getter;

public class ContentItemTextCreatedEvent extends BaseEvent {
    
    @Getter
    private final long contentId;

    @Builder
    ContentItemTextCreatedEvent(String requestId, long contentId){
        super(requestId);
        this.contentId = contentId;
    }
}
