package com.ejetool.videoai.client.content.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

public class UpdateContentItemRequest {

    @Getter @Setter
    @JsonProperty("image_prompt")
    private String imagePrompt;

}
