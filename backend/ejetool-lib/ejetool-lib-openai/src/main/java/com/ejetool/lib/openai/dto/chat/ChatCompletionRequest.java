package com.ejetool.lib.openai.dto.chat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


public class ChatCompletionRequest {

    @Getter @Setter
    @JsonIgnore
    private String requestId;

    @Getter @Setter
    @JsonProperty("model")
    private String model;

    @Getter @Setter
    @JsonProperty("messages")
    private ChatMessage[] messages;

    @Builder
    public ChatCompletionRequest(String model, ChatMessage[] messages){
        this.model = model;
        this.messages = messages;
    }

    public static ChatCompletionRequest createByUser(String model, String message){
        return new ChatCompletionRequest(model, new ChatMessage[]{
            ChatMessage.createByUser(message)
        });
    }
}
