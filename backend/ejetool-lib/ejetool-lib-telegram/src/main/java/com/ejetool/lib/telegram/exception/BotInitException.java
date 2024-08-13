package com.ejetool.lib.telegram.exception;

public class BotInitException extends RuntimeException{
    public BotInitException(Throwable cause) {
        super(cause);
    }

    public BotInitException(String message, Throwable cause) {
        super(message, cause);
    }
}
