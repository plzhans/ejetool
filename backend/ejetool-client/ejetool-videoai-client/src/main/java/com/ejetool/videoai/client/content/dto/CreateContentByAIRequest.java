package com.ejetool.videoai.client.content.dto;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

public class CreateContentByAIRequest {
    /**
     * (Required)
     */
    @Getter @Setter
    @JsonProperty("content_type")
    private ContentType contentType;

    /**
     * (Required)
     */
    @Getter @Setter
    @JsonProperty("subject")
    private String subject;

    /**
     * (Optional)
     */
    @Getter @Setter
    @JsonProperty("gpt_model")
    private String gptModel;

    /**
     * (Optional)
     */
    @Getter @Setter
    @JsonProperty("gpt_prompt")
    private String gptPrompt;

    /**
     * (Optional)
     */
    @Getter @Setter
    @JsonProperty("gpt_variables")
    private HashMap<String, String> gptVariables;

    /**
     * (Optional)
     */
    @Getter @Setter
    @JsonProperty("telegram_chat_id")
    private Long telegramChatId;

    /**
     * (Optional)
     */
    @Getter @Setter
    @JsonProperty("telegram_msg_id")
    private Integer telegramMsgId;
}
