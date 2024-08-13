package com.ejetool.common.exception;

public class ExternAPIException extends BaseException {
    
    public ExternAPIException(String message){
        super(message);
    }

    public ExternAPIException(String message, Throwable cause){
        super(message, cause);
    }
}
