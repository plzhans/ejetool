package com.ejetool.videoai.api.dto.content;

import com.ejetool.videoai.common.content.ContentItemStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AgreeContentItemResponse {
    @JsonProperty("item_status")
    private ContentItemStatus itemStatus;
    
}
