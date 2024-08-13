package com.ejetool.lib.telegram.handler;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CommandMapping {
    String value();
    String description() default "";
    String[] roles() default {};
}
