package com.ejetool.lib.creatomate.dto;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ListResponse<T> {
    @Getter
    private final int code;

    @Getter
    private final String message;
    
    @Getter
    private final List<T> list;

    public boolean isSucceed() {
        return this.code == 200;
    }
}
