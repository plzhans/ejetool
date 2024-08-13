package com.ejetool.videoai.event.publisher.publisher;

public interface VideoAIEventPublisher {

    void contentCreated(long contentId);

    void contentItemTextCreated(long contentId);

    void contentItemTextGenerateRequest(long contentId, boolean failRetry, boolean force);

    void contentItemImageGenerateRequest(long contentId, boolean failRetry, boolean force);

    void contentItemImageCreated(long contentId);

    void contentItemVoiceGenerateRequest(long contentId, boolean failRetry, boolean force);

    void contentItemVoiceCreated(long contentId);

    void contentVideoGenerateRequest(long contentId, boolean failRetry, boolean force);

    void contentVideoCreated(long contentId);

    void contentPublishRequest(long contentId, boolean failRetry, boolean force);

    void contentPublishCompleted(long contentId);
}