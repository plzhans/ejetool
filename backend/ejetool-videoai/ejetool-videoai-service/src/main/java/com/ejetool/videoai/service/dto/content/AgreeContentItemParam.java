package com.ejetool.videoai.service.dto.content;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class AgreeContentItemParam {
    private final String requestId;
    private final long contentId;
    private final boolean agree;
}
