
package com.ejetool.common.exception;

public class BadRequestException extends BaseException {
    
    public BadRequestException(String message){
        super(message);
    }

    public BadRequestException(String message, Throwable cause){
        super(message, cause);
    }
}
