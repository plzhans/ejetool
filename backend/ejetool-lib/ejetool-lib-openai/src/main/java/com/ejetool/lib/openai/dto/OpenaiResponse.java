package com.ejetool.lib.openai.dto;

import lombok.Getter;
import lombok.Setter;

public class OpenaiResponse<T> {
    @Getter @Setter
    int status;

    @Getter @Setter
    String message;

    @Getter @Setter
    T result;

    public OpenaiResponse(int status, T result){
        this.status = status;
        this.result = result;
    }

    public OpenaiResponse(T result){
        this.status = 200;
        this.result = result;
    }

    public OpenaiResponse(int status){
        this.status = status;
    }

    public OpenaiResponse(int status, String message){
        this.status = status;
        this.message = message;
    }

    public boolean isOk() {
        return this.status == 200;
    }
    
}
