package com.ejetool.videoai.event.consumer.dto.content;

import lombok.Builder;
import lombok.Getter;

public class PublishContentParam {
    @Getter
    private long contentId;

    @Getter
    private String requestId;

    @Getter
    private boolean failRetry;

    @Getter
    private boolean force;

    @Builder
    public PublishContentParam(long contentId, String requestId, boolean failRetry, boolean force){
        this.contentId = contentId;
        this.requestId = requestId;
        this.failRetry = failRetry;
        this.force = force;
    }
}
