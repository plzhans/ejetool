package com.ejetool.videoai.service.dto.content;

import com.ejetool.videoai.common.content.ContentItemStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AgreeContentItemResult {
    @Getter
    private final ContentItemStatus itemStatus;

}
