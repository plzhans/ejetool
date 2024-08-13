package com.ejetool.videoai.api.dto.content;

import com.ejetool.videoai.common.content.ContentType;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ContentResponse {
    /**
     * id
     */
    private long id;

    /**
     * 컨텐츠 타입
     */
    private ContentType contentType;
    
    /**
     * 제목
     */
    private String title;

    /**
     * 컨텐츠 아이템
     */
    private ContentItemResponse[] items;

    /**
     * 이미지 자동 생성 여부
     */
    private boolean autoGenerateImage;

    /**
     * 음성 자동 생성 여부
     */
    private boolean autoGenerateVoice;

    /**
     * 텍스트 컨펌 여부
     */
    private boolean itemTextConfirmAgree;

    /**
     * 이미지 컨펌 여부
     */
    private boolean itemImageConfirmAgree;

    /**
     * 음성 컨펌 여부
     */
    private boolean itemVoiceConfirmAgree;

    /**
     * 비디오 컨펌 여부
     */
    private boolean videoConfirmAgree;

}
