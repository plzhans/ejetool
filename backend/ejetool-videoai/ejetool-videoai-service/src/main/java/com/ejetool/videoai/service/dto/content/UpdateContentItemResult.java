package com.ejetool.videoai.service.dto.content;

import com.ejetool.videoai.common.content.ContentItemStatus;

import lombok.Builder;
import lombok.Getter;

@Builder
public class UpdateContentItemResult {
    @Getter
    private ContentItemStatus textStatus;

    @Getter
    private ContentItemStatus imageStatus;

    @Getter
    private ContentItemStatus voiceStatus;
}
