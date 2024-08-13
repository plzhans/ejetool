package com.ejetool.lib.openai.dto.chat;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

public class ChatUsage {

    @Getter @Setter
    @JsonProperty("prompt_tokens")
    private int promptTokens;

    @Getter @Setter
    @JsonProperty("completion_tokens")
    private int completionTokens;

    @Getter @Setter
    @JsonProperty("total_tokens")
    private int totalTokens;
}
