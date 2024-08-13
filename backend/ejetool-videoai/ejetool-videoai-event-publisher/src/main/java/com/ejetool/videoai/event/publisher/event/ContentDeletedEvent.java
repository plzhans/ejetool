package com.ejetool.videoai.event.publisher.event;

import com.ejetool.mq.event.BaseEvent;

import lombok.Builder;
import lombok.Getter;

public class ContentDeletedEvent extends BaseEvent {
    
    @Getter
    private final long contentId;

    @Builder
    ContentDeletedEvent(String requestId, long contentId){
        super(requestId);
        this.contentId = contentId;
    }
}
