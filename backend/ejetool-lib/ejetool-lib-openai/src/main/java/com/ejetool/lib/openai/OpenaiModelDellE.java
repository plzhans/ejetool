package com.ejetool.lib.openai;

import lombok.Getter;

/**
 * Model
 * link : https://platform.openai.com/docs/models/dall-e
 */
public enum OpenaiModelDellE {

    DALL_E_3("dall-e-3"),

    DALL_E_4("dall-e-4");

    @Getter
    private String value;

    OpenaiModelDellE(String value) {
        this.value = value;
    }

}
