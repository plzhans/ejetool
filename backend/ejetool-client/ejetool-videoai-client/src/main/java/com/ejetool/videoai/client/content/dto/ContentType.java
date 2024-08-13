package com.ejetool.videoai.client.content.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 컨텐츠 타입
 */
public enum ContentType {
    // None
    NONE("none"),

    // 명언
    QUOTE("quote");

    @JsonValue
    private final String name;

    ContentType(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName(){
        return this.name;
    }

    @JsonCreator
    public static ContentType forName(String name) {
        for (ContentType type : ContentType.values()) {
            if (type.getName().equalsIgnoreCase(name)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown name: " + name);
    }
}