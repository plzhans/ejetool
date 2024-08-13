package com.ejetool.videoai.api.dto.content;

import com.ejetool.videoai.common.content.ContentItemStatus;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateContentItemResponse {
    
    private ContentItemStatus textStatus;
    
    private ContentItemStatus imageStatus;
    
    private ContentItemStatus voiceStatus;
}
