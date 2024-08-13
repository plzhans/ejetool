package com.ejetool.lib.openai.dto.chat;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

public class ChatChoice {
    @Getter @Setter
    @JsonProperty("index")
    private int index;

    @Getter @Setter
    @JsonProperty("message")
    private ChatMessage message;

    // @Getter @Setter
    // @JsonProperty("logprobs")
    // private Object logprobs; 

    @Getter @Setter
    @JsonProperty("finish_reason")
    private String finishReason;

}
