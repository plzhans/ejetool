package com.ejetool.account.api.dto.error;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private final int code;
    private final String message;

    public ErrorResponse(int code, String message){
        this.code = code;
        this.message = message;
    }

    public ErrorResponse(HttpStatus httpStatus) {
        this.code = httpStatus.value();
        this.message = httpStatus.getReasonPhrase();
    }

    public ErrorResponse(HttpStatus httpStatus, String message) {
        this.code = httpStatus.value();
        this.message = message;
    }
}
