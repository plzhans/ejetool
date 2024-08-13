
package com.ejetool.lib.openai.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;

public enum ChatRoleType {
    
    SYSTEM("system"),
    ASSISTANT("assistant"),
    USER("user");

    @JsonValue
    @Getter 
    private String value;

    ChatRoleType(String value) {
        this.value = value;
    }

    @JsonCreator
    public static ChatRoleType forValue(String value) {
        for (ChatRoleType myEnum : values()) {
            if (myEnum.getValue().equals(value)) {
                return myEnum;
            }
        }
        throw new IllegalArgumentException("Unexpected value: " + value);
    }
}
