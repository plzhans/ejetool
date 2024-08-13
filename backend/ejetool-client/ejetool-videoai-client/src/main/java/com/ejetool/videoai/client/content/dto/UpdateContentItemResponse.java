package com.ejetool.videoai.client.content.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

public class UpdateContentItemResponse {
    @Getter @Setter
    @JsonProperty("text_status")
    private String textStatus;

    @Getter @Setter
    @JsonProperty("image_status")
    private String imageStatus;

    @Getter @Setter
    @JsonProperty("voice_status")
    private String voiceStatus;

}
