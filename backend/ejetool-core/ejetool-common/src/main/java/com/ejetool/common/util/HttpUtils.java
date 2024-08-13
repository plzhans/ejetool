package com.ejetool.common.util;

import org.springframework.http.HttpStatus;

import lombok.experimental.UtilityClass;

@UtilityClass
public class HttpUtils {
    public static boolean equals(int value, HttpStatus httpStatus){
        return httpStatus.value() == value;
    }
}
