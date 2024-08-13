package com.ejetool.lib.openai.dto.chat;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

public class ChatCompletionResponse {
    @Getter @Setter
    @JsonProperty("id")
    private String id;

    @Getter @Setter
    @JsonProperty("object")
    private String object;

    @Getter @Setter
    @JsonProperty("created")
    private long created;

    @Getter @Setter
    @JsonProperty("model")
    private String model;


    @Getter @Setter
    @JsonProperty("system_fingerprint")
    private String systemFingerprint;

    @Getter @Setter
    @JsonProperty("choices")
    private ChatChoice[] choices;

    @Getter @Setter
    @JsonProperty("usage")
    private ChatUsage usage;

    public String getFirstContent(){
        var message = this.choices[0].getMessage();
        var content = message.getContent();
        return content;
    }
}
