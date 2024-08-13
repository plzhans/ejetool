package com.ejetool.videoai.service.dto.content;

import com.ejetool.videoai.common.content.ContentStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ContentCreateResult {
    private long contentId;

    private ContentStatus status;
    
}
