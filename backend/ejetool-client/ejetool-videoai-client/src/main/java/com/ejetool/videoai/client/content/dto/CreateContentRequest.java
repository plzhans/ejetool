package com.ejetool.videoai.client.content.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateContentRequest {
    /**
     * 컨텐츠 타입
     */
    private ContentType contentType;

    /**
     * 타이틀
     */
    private String title;

    /**
     * 컨텐츠 아이템
     */
    private CreateContentItemRequest[] items;

    /**
     * 자동 생성 이미지
     */
    private boolean autoGenerateImage;

    /**
     * 자동생성 보이스
     */
    private boolean autoGenerateVoice;

}
