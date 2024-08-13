package com.ejetool.videoai.service.dto.content;

import com.ejetool.videoai.common.content.ContentStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ContentItemGenerateResult {
    @Getter
    private final ContentStatus status;

}
