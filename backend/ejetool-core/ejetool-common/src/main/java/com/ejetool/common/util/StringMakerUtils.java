package com.ejetool.common.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringMakerUtils {
    
    /**
     * 문자열 값을 마스킹해서 * 처리
     * @param input
     * @return
     */
    public static String mask(String input){
        if (input == null) {
            return ""; 
        }

        int length = input != null ? input.length() : 0;
        if(length > 8){
            return input.substring(0, 2) + "****" + input.substring(length - 2);
        }
        if(length > 0){
            return "****";
        }
        return "";
    }
}
