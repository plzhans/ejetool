package com.ejetool.common.exception;

import lombok.experimental.StandardException;

@StandardException
public abstract class BaseException extends RuntimeException {
    
    public String getSimpleName(){
        return this.getClass().getSimpleName().replace("Exception", "");
    }
}

