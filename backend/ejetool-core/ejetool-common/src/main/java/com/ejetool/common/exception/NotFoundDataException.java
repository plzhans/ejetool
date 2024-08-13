package com.ejetool.common.exception;

public class NotFoundDataException extends BaseException {
    
    public NotFoundDataException(String message){
        super(message);
    }

    public NotFoundDataException(String message, Throwable cause){
        super(message, cause);
    }
}
