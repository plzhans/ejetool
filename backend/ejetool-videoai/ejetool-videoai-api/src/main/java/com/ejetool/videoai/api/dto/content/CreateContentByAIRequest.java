package com.ejetool.videoai.api.dto.content;

import java.util.HashMap;

import com.ejetool.videoai.common.content.ContentType;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

public class CreateContentByAIRequest {
    @Getter @Setter
    @JsonProperty("content_type")
    private ContentType contentType;

    @Getter @Setter
    private String subject;

    @Getter @Setter
    private String gptModel;

    @Getter @Setter
    private String gptPrompt;

    @Getter @Setter
    private HashMap<String, String> gptVariables;

    @Getter @Setter
    @JsonProperty("telegram_chat_id")
    private Long telegramChatId;

    @Getter @Setter
    @JsonProperty("telegram_msg_id")
    private Integer telegramMessageId;
}
