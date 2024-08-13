package com.ejetool.lib.creatomate.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommonResponse<T> {
    @Getter
    private final int code;

    @Getter
    private final String message;

    @Getter
    private final T data;

    public boolean isSucceed(){
        return this.code == 200;
    }
}
