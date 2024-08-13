package com.ejetool.videoai.service.dto.content;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ContentItemGenerateParam {
    private final long contentId;
    private final boolean failRetry;
}
