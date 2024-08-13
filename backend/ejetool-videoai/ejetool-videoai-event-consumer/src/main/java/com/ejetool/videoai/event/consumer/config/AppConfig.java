package com.ejetool.videoai.event.consumer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import com.ejetool.mq.redis.publisher.RedisStreamPublisher;
import com.ejetool.videoai.event.publisher.publisher.VideoAIApplicationEventPublisherImpl;
import com.ejetool.videoai.event.publisher.publisher.VideoAIEventPublisher;

import lombok.RequiredArgsConstructor;


@Configuration
@RequiredArgsConstructor
public class AppConfig {
    
    @Bean
    RedisStreamPublisher redisStreamPublisher(RedisTemplate<String, Object> redisTemplate){
        return new RedisStreamPublisher(redisTemplate);
    }

    @Bean
    VideoAIEventPublisher videoAIEventPublisher(RedisStreamPublisher redisStreamPublisher){
        return new VideoAIApplicationEventPublisherImpl(redisStreamPublisher);
    }
   
}
