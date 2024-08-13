package com.ejetool.videoai.service.dto.content;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ContentItemDto {
    /**
     * 텍스트
     */
    private String[] texts;

    /**
     * 이미지
     */
    private String[] images;

    /**
     * 음성
     */
    private String[] voices;

    /**
     * 배경 음악
     */
    private String[] bgms;
}
