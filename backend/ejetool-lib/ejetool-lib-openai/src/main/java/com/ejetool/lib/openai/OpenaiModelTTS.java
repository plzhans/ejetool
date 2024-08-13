package com.ejetool.lib.openai;

import lombok.Getter;

/**
 * Model
 * link : https://platform.openai.com/docs/models/tts
 */
public enum OpenaiModelTTS {

    TTS_1("tts-1"),

    TTS_1_HD("tts-1-hd");

    @Getter
    private String value;

    OpenaiModelTTS(String value) {
        this.value = value;
    }

}
