package com.ejetool.mq.redis.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

import org.springframework.core.annotation.AliasFor;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@RedisStream(value = "")
public @interface RedisConsumerStream {
    @AliasFor(annotation = RedisStream.class)
    String value();

    @AliasFor(annotation = RedisStream.class, attribute = "interval" )
    int interval() default 10;
}
