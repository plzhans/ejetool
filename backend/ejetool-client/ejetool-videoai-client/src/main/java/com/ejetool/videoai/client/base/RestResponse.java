package com.ejetool.videoai.client.base;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class RestResponse <T> {
    @Getter
    int code;

    @Getter
    String message;

    @Getter
    T data;

    public boolean isSucceed(){
        return this.code == 200;
    }

    public RestResponse(int code, T data){
        this.code = code;
        this.message = "OK";
        this.data = data;
    }

    public static <T> RestResponse<T> createError(int code, String message){
        RestResponse<T> inst = new RestResponse<>();
        inst.code = code;
        inst.message = message;
        inst.data = null;
        return inst;
    }
}
