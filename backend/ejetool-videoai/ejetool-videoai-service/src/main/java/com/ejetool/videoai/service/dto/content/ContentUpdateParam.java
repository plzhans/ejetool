package com.ejetool.videoai.service.dto.content;

import lombok.Getter;
import lombok.Setter;

/**
 * 컨텐츠 수정 파라메터
 */
@Getter
@Setter
public class ContentUpdateParam {
    /**
     * 제목
     */
    private String title;

    /**
     * 아이템 갯수
     */
    private int itemCount;

    /**
     * 자동 생성 키워드
     */
    private String generateKeyword;

    /**
     * 자동 생성 프롬프트
     */
    private String generatePrompt;

    /**
     * 이미지 자동 생성 여부
     */
    private boolean generateImage;

    /**
     * 음성 자동 생성 여부
     */
    private boolean generateVoice;
}
