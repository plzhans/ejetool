package com.ejetool.videoai.service.dto.content;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ContentPublishVideoParam {
    @Getter
    private final long contentId;

    @Getter
    private final boolean failRetry;
}
