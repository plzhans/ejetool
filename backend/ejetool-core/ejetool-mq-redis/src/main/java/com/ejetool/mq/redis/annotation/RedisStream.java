package com.ejetool.mq.redis.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisStream {
    /**
     * stream key
     * @return
     */
    String value();

    /**
     * read offset
     * @return
     */
    String offset() default "";

    /**
     * poll timeout
     * @return
     */
    int interval() default 60;
}
