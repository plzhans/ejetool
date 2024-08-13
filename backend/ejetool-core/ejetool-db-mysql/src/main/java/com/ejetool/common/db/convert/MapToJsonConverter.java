package com.ejetool.common.db.convert;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;
import java.util.Collections;

@Converter
public class MapToJsonConverter<K,V> implements AttributeConverter<Map<K, V>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<K, V> attribute) {
        if (attribute == null) {
            return null;
        }

        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Could not convert map to JSON string.", e);
        }
    }

    @Override
    public Map<K, V> convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return Collections.emptyMap();
        }

        try {
            return objectMapper.readValue(dbData, new TypeReference<Map<K, V>>(){});
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not convert JSON string to map.", e);
        }
    }
}
