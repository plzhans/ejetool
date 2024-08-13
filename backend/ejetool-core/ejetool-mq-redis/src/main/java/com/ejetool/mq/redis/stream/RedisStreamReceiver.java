package com.ejetool.mq.redis.stream;

import java.util.Map;
import java.util.HashMap;

import java.lang.reflect.Method;

import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;

import com.ejetool.mq.redis.annotation.RedisConsumerStream;
import com.ejetool.mq.redis.annotation.RedisStreamListener;

import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.core.RedisTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * ref : https://docs.spring.io/spring-data/redis/docs/3.1.9/reference/html/
 * ref : https://github.com/russell-seo/TIL/blob/main/DB/RedisStreams.md
 */
@Slf4j
@RequiredArgsConstructor
public class RedisStreamReceiver {

    private final RedisTemplate<String, Object> redisTemplate;
    private final Consumer consumer; 
    private final ApplicationContext applicationContext;
    private final HashMap<String, RedisStreamAdapter> adapterMap;

    public RedisStreamReceiver(RedisTemplate<String, Object> redisTemplate, Consumer consumer, ApplicationContext applicationContext){
        this.redisTemplate = redisTemplate;
        this.consumer = consumer;
        this.applicationContext = applicationContext;
        this.adapterMap = new HashMap<>();
        this.init();
    }

    /**
     * redis stream container 생성
     * @param redisTemplate
     * @param consumer
     * @param applicationContext
     */
    @SuppressWarnings("squid:S3776")
    private void init() {
        if(this.adapterMap.size() > 0){
            return;
        }

        Map<String, Object> listeners = this.applicationContext.getBeansWithAnnotation(RedisStreamListener.class);
        for(var listener : listeners.values()){
            RedisStreamListener streamListener = AnnotationUtils.findAnnotation(listener.getClass(), RedisStreamListener.class);
            if(streamListener != null){
                boolean findMethod = false;
                for (Method method : listener.getClass().getDeclaredMethods()) {
                    if (method.isAnnotationPresent(RedisConsumerStream.class)) {
                        RedisConsumerStream anno = method.getAnnotation(RedisConsumerStream.class);
                        if(this.adapterMap.containsKey(anno.value())){
                            log.warn("listener stream event alreay. key={}", anno.value());
                            continue;
                        }
                        RedisStreamSettings settings = RedisStreamSettings.create(anno);
                        RedisStreamContext context = RedisStreamContext.create(this.redisTemplate, this.consumer, settings);
                        RedisStreamAdapter adapter = new RedisStreamAdapter(listener, method, context);
                        this.adapterMap.put(anno.value(), adapter);
                        if(!findMethod){
                            findMethod = true;
                        }
                    }
                }
                if(!findMethod){
                    log.warn("listener stream event notfound. class={}", listener.getClass().getName());
                }
            }
        }
    }

    /**
     * 시작
     * @param notConsumerThenCreate 컨슈머가 없으면 생성
     */
    public void start(boolean notConsumerThenCreate) {
        this.adapterMap.forEach((stream, adapter)->adapter.start(notConsumerThenCreate));
    }
}
