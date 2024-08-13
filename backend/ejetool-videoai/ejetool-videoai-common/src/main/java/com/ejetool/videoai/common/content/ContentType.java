package com.ejetool.videoai.common.content;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;

/**
 * 컨텐츠 타입
 */
public enum ContentType {
    // None
    NONE(0, "none", "None"),
    // 명언
    QUOTE(1, "quote", "명언");

    @Getter
    private final int value;

    @JsonValue
    @Getter
    private final String name;

    @Getter
    private final String desc;

    ContentType(int value, String name, String desc) {
        this.value = value;
        this.name = name;
        this.desc = desc;
    }

    @JsonCreator
    public static ContentType of(String name) {
        return Arrays.stream(ContentType.values())
            .filter(i -> i.name.equals(name))
            .findAny()
            .orElse(null);
    }
}