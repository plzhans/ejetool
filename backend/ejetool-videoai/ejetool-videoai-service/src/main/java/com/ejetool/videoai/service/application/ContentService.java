package com.ejetool.videoai.service.application;

import java.util.Optional;

import com.ejetool.videoai.service.dto.content.*;

/**
 * 컨텐츠 서비스
 */
public interface ContentService {
    
    /** 
     * 컨텐츠 생성
     */
    ContentCreateResult create(ContentCreateParam param);

    /** 
     * 컨텐츠 생성 (by AI)
     */
    ContentCreateResult createByAI(ContentCreateByAIParam param);
    
    /**
     * 컨텐츠 조회
     * @param contentId 컨텐트 id
     * @return Optional 컨텐츠 정보
     */
    Optional<ContentDto> get(long contentId);

    /** 
     * 컨텐츠 수정
     */
    Optional<ContentDto> update(long contentId, ContentUpdateParam param);

    /** 
     * 컨텐츠 삭제
     */
    boolean delete(long id);

    /** 
     * 컨텐츠 텍스트 생성 
     */
    ContentItemGenerateResult generateItemText(ContentItemGenerateParam param);
    
    /**
     * 컨텐트 아이템 이미지 생성
     */
    ContentItemGenerateResult generateItemImage(ContentItemGenerateParam param);

    /**
     * 컨텐트 아이템 음성 생성
     */
    ContentItemGenerateResult generateItemVoice(ContentItemGenerateParam param);

    /** 
     * 비디오 생성 
     */
    ContentItemGenerateResult generateVideo(ContentItemGenerateParam param);
   
    /**
     * 컨텐츠 아이템 텍스트 컨펌
     */
    ContentItemConfirmResult setItemTextConfirm(ContentItemConfirmParam param);

    /**
     * 컨텐츠 아이템 이미지 컨펌
     */
    ContentItemConfirmResult setItemImageConfirm(ContentItemConfirmParam param);

    /**
     * 컨텐츠 아이템 음성 컨펌
     */
    ContentItemConfirmResult setItemVoiceConfirm(ContentItemConfirmParam param);

    /**
     * 컨텐츠 비디오 컨펌
     */
    ContentConfirmResult setVideoConfirm(ContentConfirmParam param);

    /**
     * 컨텐츠 발행 요청
     */
    ContentPublishVideoResult publishVideo(ContentPublishVideoParam param);

    /**
     * 컨텐츠 아이템 수정
     * @param param
     * @return
     */
    UpdateContentItemResult updateItem(UpdateContentItemParam param);

    /**
     * 아이템 텍스트 동의
     * @param param
     * @return
     */
    AgreeContentItemResult agreeItemText(AgreeContentItemParam param);

    /**
     * 아이템 이미지 동의
     * @param param
     * @return
     */
    AgreeContentItemResult agreeItemImage(AgreeContentItemParam param);

    /**
     * 아이템 보이스 동의
     * @param param
     * @return
     */
    AgreeContentItemResult agreeItemVoice(AgreeContentItemParam param);

}
