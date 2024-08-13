package com.ejetool.videoai.api.dto.content;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ContentPublishVideoResponse {
    @JsonProperty("content_status")
    private String contentStatus;
}
