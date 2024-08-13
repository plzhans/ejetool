package com.ejetool.videoai.client.content.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

public class ContentGenerateItemRequest {
    /**
     * (Required)
     */
    @Getter @Setter
    @JsonProperty("content_id")
    private long contentId;

    @Getter @Setter
    @JsonProperty("fail_retry")
    private boolean failRetry;

}
