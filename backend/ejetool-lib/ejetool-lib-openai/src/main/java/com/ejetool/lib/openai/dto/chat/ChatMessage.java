package com.ejetool.lib.openai.dto.chat;

import com.ejetool.lib.openai.enums.ChatRoleType;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class ChatMessage {
    @Getter @Setter
    @JsonProperty("role")
    private ChatRoleType role;

    @Getter @Setter
    @JsonProperty("content")
    private String content;

    @Builder
    public ChatMessage(ChatRoleType role, String content) {
        this.role = role;
        this.content = content;
    }

    public static ChatMessage createBySystem(String content){
        return new ChatMessage(ChatRoleType.SYSTEM, content);
    }

    public static ChatMessage createByAssistant(String content){
        return new ChatMessage(ChatRoleType.ASSISTANT, content);
    }

    public static ChatMessage createByUser(String content){
        return new ChatMessage(ChatRoleType.USER, content);
    }
}
