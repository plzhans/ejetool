package com.ejetool.videoai.api.dto.content;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UpdateContentItemRequest {
    @JsonProperty("image_prompt")
    private String imagePrompt;
}
