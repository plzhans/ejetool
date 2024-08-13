package com.ejetool.videoai.event.publisher.publisher;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.ejetool.mq.redis.publisher.RedisStreamPublisher;
import com.ejetool.videoai.event.publisher.event.*;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class VideoAIApplicationEventPublisherImpl implements VideoAIEventPublisher {

    private final RedisStreamPublisher publisher;

    @Async
    @Override
    public void contentCreated(long contentId) {
        var event = ContentCreatedEvent.builder()
            .contentId(contentId)
            .build();

        publisher.publish(VideoAIContentStreamKey.CREATED, event);
    }

    @Async
    @Override
    public void contentItemTextGenerateRequest(long contentId, boolean failRetry, boolean force) {
        var event = ContentItemImageGenerateRequestEvent.builder()
            .contentId(contentId)
            .failRetry(failRetry)
            .force(force)
            .build();

        publisher.publish(VideoAIContentStreamKey.ITEM_TEXT_GENERATE_REQUEST, event);
    }


    @Async
    @Override
    public void contentItemTextCreated(long contentId) {
        var event = ContentItemTextCreatedEvent.builder()
            .contentId(contentId)
            .build();

        publisher.publish(VideoAIContentStreamKey.ITEM_TEXT_CREATED, event);
    }
    

    @Async
    @Override
    public void contentItemImageGenerateRequest(long contentId, boolean failRetry, boolean force) {
        var event = ContentItemImageGenerateRequestEvent.builder()
            .contentId(contentId)
            .failRetry(failRetry)
            .force(force)
            .build();

        publisher.publish(VideoAIContentStreamKey.ITEM_IMAGE_GENERATE_REQUEST, event);
    }

    @Async
    @Override
    public void contentItemImageCreated(long contentId) {
       var event = ContentItemImageCreatedEvent.builder()
            .contentId(contentId)
            .build();

        publisher.publish(VideoAIContentStreamKey.ITEM_IMAGE_CREATED, event);
    }

    @Async
    @Override
    public void contentItemVoiceGenerateRequest(long contentId, boolean failRetry, boolean force) {
        var event = ContentItemVoiceGenerateRequestEvent.builder()
            .contentId(contentId)
            .failRetry(failRetry)
            .force(force)
            .build();

        publisher.publish(VideoAIContentStreamKey.ITEM_VOICE_GENERATE_REQUEST, event);
    }

    @Async
    @Override
    public void contentItemVoiceCreated(long contentId) {
        var event = ContentItemVoiceCreatedEvent.builder()
            .contentId(contentId)
            .build();

        publisher.publish(VideoAIContentStreamKey.ITEM_VOICE_CREATED, event);
    }

    @Async
    @Override
    public void contentVideoGenerateRequest(long contentId, boolean failRetry, boolean force) {
        var event = ContentVideoGenerateRequestEvent.builder()
            .contentId(contentId)
            .failRetry(failRetry)
            .force(force)
            .build();

        publisher.publish(VideoAIContentStreamKey.VIDEO_GENERATE_REQUEST, event);
    }

    @Async
    @Override
    public void contentVideoCreated(long contentId) {
        var event = ContentVideoCreatedEventEvent.builder()
            .contentId(contentId)
            .build();

        publisher.publish(VideoAIContentStreamKey.VIDEO_CREATED, event);
    }

    @Async
    @Override
    public void contentPublishRequest(long contentId, boolean failRetry, boolean force) {
        var event = ContentPublishRequestEvent.builder()
            .contentId(contentId)
            .failRetry(failRetry)
            .force(force)
            .build();

        publisher.publish(VideoAIContentStreamKey.PUBLISH_REQUEST, event);
    }

    @Async
    @Override
    public void contentPublishCompleted(long contentId) {
        var event = ContentPublishedEvent.builder()
            .contentId(contentId)
            .build();

        publisher.publish(VideoAIContentStreamKey.PUBLISH_COMPLETED, event);
    }

}
