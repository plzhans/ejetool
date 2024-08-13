package com.ejetool.common.util;

import com.ejetool.common.exception.JsonConvertException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.micrometer.common.lang.Nullable;
import lombok.experimental.UtilityClass;

@UtilityClass
public class JsonUtils 
{
    public static String convertToString(Object obj) 
        throws JsonConvertException
    {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new JsonConvertException("ObjectMapper writeValueAsString exception.", e);
        }
    }


    public static <T> T convertToObject(String json, Class<T> clazz) 
        throws JsonConvertException
    {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new JsonConvertException("ObjectMapper readValue exception.", e);
        }
    }

    @Nullable
    public static <T> T convertToObject(String json, TypeReference<T> valueTypeRef) 
        throws JsonConvertException
    {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, valueTypeRef);
        } catch (Exception e) {
            throw new JsonConvertException("ObjectMapper readValue exception.", e);
        }
    }

    @Nullable
    public static <T> T convertToObjectOrNull(String json, Class<T> clazz) {
        try {
            return convertToObject(json, clazz);
        } catch(JsonConvertException e){
            return null;
        }
    }

    @Nullable
    public static <T> T convertToObjectOrNull(String json, TypeReference<T> valueTypeRef) 
        throws JsonConvertException
    {
        try {
            return convertToObject(json, valueTypeRef);
        } catch(JsonConvertException e){
            return null;
        }
    }
}
