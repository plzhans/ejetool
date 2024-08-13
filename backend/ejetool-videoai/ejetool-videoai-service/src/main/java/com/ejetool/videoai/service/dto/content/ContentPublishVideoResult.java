package com.ejetool.videoai.service.dto.content;

import com.ejetool.videoai.common.content.ContentPublishStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ContentPublishVideoResult {
    @Getter
    private final ContentPublishStatus contentStatus;
}
