package com.ejetool.videoai.event.consumer.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.core.RedisTemplate;

import com.ejetool.mq.redis.stream.RedisStreamReceiver;
import com.ejetool.videoai.event.publisher.publisher.VideoAIEventPublisher;

public class RedisStreamRunner implements CommandLineRunner{

    private final RedisStreamReceiver redisStreamReceiver;
    private final VideoAIEventPublisher videoAIEventPublisher;
    

    public RedisStreamRunner(RedisTemplate<String, Object> redisTemplate, Consumer consumer, ApplicationContext applicationContext, VideoAIEventPublisher videoAIEventPublisher){
        this.redisStreamReceiver = new RedisStreamReceiver(redisTemplate, consumer, applicationContext);
        this.videoAIEventPublisher = videoAIEventPublisher;
    }

    @Override
    public void run(String... args) throws Exception {
        //xx.contentPublishRequest(46, true, false);
        //xx.contentVideoGenerateRequest(46), true, false);

        this.redisStreamReceiver.start(true);
    }
     
}
