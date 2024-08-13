package com.ejetool.videoai.client.content.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

public class AgreeContentItemResponse {
    @Getter @Setter
    @JsonProperty("item_status")
    private String itemStatus;
}
