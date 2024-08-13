package com.ejetool.videoai.event.consumer.redis.listener;

import com.ejetool.mq.redis.annotation.RedisConsumerStream;
import com.ejetool.mq.redis.annotation.RedisStreamListener;
import com.ejetool.mq.redis.event.EventMessageHeader;
import com.ejetool.videoai.event.consumer.dto.content.GenerateItemImageParam;
import com.ejetool.videoai.event.consumer.dto.content.GenerateItemVoiceParam;
import com.ejetool.videoai.event.consumer.dto.content.GenerateVideoParam;
import com.ejetool.videoai.event.consumer.dto.content.PublishContentParam;
import com.ejetool.videoai.event.consumer.service.ContentService;
import com.ejetool.videoai.event.publisher.event.ContentCreatedEvent;
import com.ejetool.videoai.event.publisher.event.ContentItemImageGenerateRequestEvent;
import com.ejetool.videoai.event.publisher.event.ContentItemVoiceGenerateRequestEvent;
import com.ejetool.videoai.event.publisher.event.ContentPublishRequestEvent;
import com.ejetool.videoai.event.publisher.event.ContentVideoGenerateRequestEvent;
import com.ejetool.videoai.event.publisher.publisher.VideoAIContentStreamKey;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RedisStreamListener
public class ContentEventListener{
    
    private final ContentService service;

    @RedisConsumerStream(VideoAIContentStreamKey.CREATED)
    public void created(EventMessageHeader header, ContentCreatedEvent event) {
        log.info("{}", event);
    }

    @RedisConsumerStream(VideoAIContentStreamKey.ITEM_TEXT_GENERATE_REQUEST)
    public void itemTextGenerateRequest(EventMessageHeader header, ContentItemImageGenerateRequestEvent event) {
        header.ack();

        try{
            var param = GenerateItemImageParam.builder()
                .contentId(event.getContentId())
                .requestId(event.getRequestId())
                .failRetry(event.isFailRetry())
                .force(event.isForce())
                .build();
            this.service.generateItemText(param);
        } catch(Exception e){
            log.error("service.generateItemText():Exception.", e);
        }
    }

    @RedisConsumerStream(VideoAIContentStreamKey.ITEM_IMAGE_GENERATE_REQUEST)
    public void itemImageGenerateRequest(EventMessageHeader header, ContentItemImageGenerateRequestEvent event) {
        header.ack();

        try{
            var param = GenerateItemImageParam.builder()
                .contentId(event.getContentId())
                .requestId(event.getRequestId())
                .failRetry(event.isFailRetry())
                .force(event.isForce())
                .build();
            this.service.generateItemImage(param);
        } catch(Exception e){
            log.error("service.generateItemImage():Exception.", e);
        }
    }

    @RedisConsumerStream(VideoAIContentStreamKey.ITEM_VOICE_GENERATE_REQUEST)
    public void itemVoiceGenerateRequest(EventMessageHeader header, ContentItemVoiceGenerateRequestEvent event) {
        header.ack();

        try{
            var param = GenerateItemVoiceParam.builder()
                .contentId(event.getContentId())
                .requestId(event.getRequestId())
                .failRetry(event.isFailRetry())
                .force(event.isForce())
                .build();
            this.service.generateItemVoice(param);

            
        } catch(Exception e){
            log.error("service.generateItemVoice():Exception.", e);
        }
    }

    @RedisConsumerStream(VideoAIContentStreamKey.VIDEO_GENERATE_REQUEST)
    public void videoGenerateRequest(EventMessageHeader header, ContentVideoGenerateRequestEvent event) {
        try{
            var param = GenerateVideoParam.builder()
                .contentId(event.getContentId())
                .requestId(event.getRequestId())
                .failRetry(event.isFailRetry())
                .force(event.isForce())
                .build();
            this.service.generateVideo(param);
        } catch(Exception e){
            log.error("service.videoGenerateRequest():Exception.", e);
        } finally {
            header.ack();
        }
        
    }

    @RedisConsumerStream(VideoAIContentStreamKey.PUBLISH_REQUEST)
    public void publishRequest(EventMessageHeader header, ContentPublishRequestEvent event) {
        header.ack();
        
        try{
            var param = PublishContentParam.builder()
                .contentId(event.getContentId())
                .requestId(event.getRequestId())
                .failRetry(event.isFailRetry())
                .force(event.isForce())
                .build();
            this.service.publishVideo(param);
        } catch(Exception e){
            log.error("service.videoGenerateRequest():Exception.", e);
        }
    }
}