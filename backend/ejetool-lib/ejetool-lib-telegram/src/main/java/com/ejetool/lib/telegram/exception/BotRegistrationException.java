package com.ejetool.lib.telegram.exception;

public class BotRegistrationException extends RuntimeException{
    public BotRegistrationException(Throwable cause) {
        super(cause);
    }

    public BotRegistrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
