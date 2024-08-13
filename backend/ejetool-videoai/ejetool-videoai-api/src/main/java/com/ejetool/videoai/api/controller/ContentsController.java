
package com.ejetool.videoai.api.controller;

import java.util.NoSuchElementException;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ejetool.videoai.api.dto.content.ContentResponse;
import com.ejetool.videoai.api.dto.content.CreateContentByAIRequest;
import com.ejetool.videoai.api.dto.content.CreateContentRequest;
import com.ejetool.videoai.api.dto.content.CreateContentResponse;
import com.ejetool.videoai.api.dto.content.AgreeContentItemResponse;
import com.ejetool.videoai.api.dto.content.UpdateContentItemRequest;
import com.ejetool.videoai.api.dto.content.UpdateContentItemResponse;
import com.ejetool.videoai.api.enums.ContentRole;
import com.ejetool.videoai.api.dto.content.ContentItemGenerateResponse;
import com.ejetool.videoai.api.dto.content.ContentPublishVideoResponse;
import com.ejetool.videoai.api.mapper.ContentMapper;
import com.ejetool.videoai.service.application.ContentService;
import com.ejetool.videoai.service.dto.content.ContentItemGenerateResult;
import com.ejetool.videoai.service.dto.content.ContentPublishVideoParam;
import com.ejetool.videoai.service.dto.content.ContentPublishVideoResult;
import com.ejetool.videoai.service.dto.content.AgreeContentItemParam;
import com.ejetool.videoai.service.dto.content.AgreeContentItemResult;
import com.ejetool.videoai.service.dto.content.UpdateContentItemParam;
import com.ejetool.videoai.service.dto.content.UpdateContentItemResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.ejetool.videoai.service.dto.content.ContentItemGenerateParam;

import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;

/**
 * 컨텐츠 컨트롤러
 */
@Secured(ContentRole.IMAGE)
@RestController
@RequiredArgsConstructor
@RequestMapping("/contents")
@Tag(name = "Contents", description = "Contents API")
public class ContentsController {

    @SuppressWarnings("squid:S1075")
    @UtilityClass
    public static class Const {
        public static final String PATH_CREATE = "";
        public static final String PATH_CREATE_AI = "/ai";
        public static final String PATH_GET = "/{content_id}";
        public static final String PATH_UPDATE_CONTENT_ITEM = "/{content_id}/{item_id}";
        public static final String PATH_UPDATE_ITEM = "/-/{item_id}";
        public static final String PATH_GENERATE_ITEM_TEXT = "/{content_id}/generate-item-text";
        public static final String PATH_GENERATE_ITEM_IMAGE = "/{content_id}/generate-item-image";
        public static final String PATH_GENERATE_ITEM_VOICE = "/{content_id}/generate-item-voice";
        public static final String PATH_GENERATE_VIDEO = "/{content_id}/generate-video";
        public static final String PATH_AGREE_ITEM_TEXT = "/{content_id}/agree-item-text";
        public static final String PATH_AGREE_ITEM_IMAGE = "/{content_id}/agree-item-image";
        public static final String PATH_AGREE_ITEM_VOICE = "/{content_id}/agree-item-voice";
        public static final String PATH_PUBLISH_VIDEO = "/{content_id}/publish-video";
        
    }

    private final ContentService service;
    private final ContentMapper mapper;

    /**
     * 컨텐츠 생성
     * @param request 요청
     * @return 응답
     */
    @Operation(summary = "컨텐츠 생성", description = "컨텐츠 생성")
    @PostMapping(Const.PATH_CREATE)
    public CreateContentResponse create(@RequestBody CreateContentRequest request) {
        var param = mapper.to(request);
        var result = service.create(param);

        return mapper.to(result);
    }

    /**
     * 아이템 수정
     * @param request 요청
     * @return 응답
     */
    @Operation(summary = "컨텐츠 아이템 수정", description = "컨텐츠의 아이템 수정")
    @PostMapping(Const.PATH_UPDATE_ITEM)
    public UpdateContentItemResponse updateItem(
        @PathVariable("item_id") long itemId, 
        @RequestBody UpdateContentItemRequest request
    ) {
        return this.updateItem(null, itemId, request);
    }

    /**
     * 아이템 수정
     * @param request 요청
     * @return 응답
     */
    @Operation(summary = "컨텐츠 아이템 수정", description = "컨텐츠의 아이템 수정")
    @PostMapping(Const.PATH_UPDATE_CONTENT_ITEM)
    public UpdateContentItemResponse updateItem(
        @PathVariable("content_id") Long contentId,
        @PathVariable("item_id") long itemId, 
        @RequestBody UpdateContentItemRequest request
    ) {
        UpdateContentItemParam param = mapper.to(request, contentId, itemId);
        UpdateContentItemResult result = service.updateItem(param);

        return mapper.to(result);
    }

    /**
     * 컨텐츠 생성
     * @param request 요청
     * @return 응답
     */
    @Operation(summary = "컨텐츠 생성", description = "키워드를 통해 AI로 생성")
    @PostMapping(Const.PATH_CREATE_AI)
    public CreateContentResponse createByAI(@RequestBody CreateContentByAIRequest request) {
        var param = mapper.to(request);
        var result = service.createByAI(param);

        return mapper.to(result);
    }

    /**
     * 
     * @param contentId 컨텐츠 id
     * @return 응답
     */
    @Operation(summary = "컨텐츠 조회", description = "컨텐츠 조회")
    @GetMapping(Const.PATH_GET)
    public ContentResponse get(@PathVariable("content_id") long contentId) {
        var data = service.get(contentId)
            .orElseThrow(NoSuchElementException::new);

        return mapper.to(data);
    }
    
    /**
     * 
     * @param contentId 컨텐츠 id
     * @param failRetry 실패 재시도
     * @return
     */
    @Operation(summary = "아이템 텍스트 생성", description = "AI를 통해 컨텐츠 아이템의 텍스트 생성 요청")
    @PostMapping(Const.PATH_GENERATE_ITEM_TEXT)
    public ContentItemGenerateResponse generateItemText(@PathVariable("content_id") long contentId, @RequestParam("fail_retry") boolean failRetry) {
        ContentItemGenerateParam param = new ContentItemGenerateParam(contentId, failRetry);
        ContentItemGenerateResult result = service.generateItemText(param);

        return mapper.to(result);
    }

    /**
     * 
     * @param contentId 컨텐츠 id
     * @param failRetry 실패 재시도
     * @return
     */
    @Operation(summary = "아이템 이미지 생성", description = "AI를 통해 컨텐츠 아이템의 이미지 생성 요청")
    @PostMapping(Const.PATH_GENERATE_ITEM_IMAGE)
    public ContentItemGenerateResponse generateItemImage(@PathVariable("content_id") long contentId, @RequestParam("fail_retry") boolean failRetry) {
        ContentItemGenerateParam param = new ContentItemGenerateParam(contentId, failRetry);
        ContentItemGenerateResult result = service.generateItemImage(param);

        return mapper.to(result);
    }

    /**
     * 
     * @param contentId 컨텐츠 id
     * @param failRetry 실패 재시도
     * @return
     */
    @Operation(summary = "아이템 음성 생성", description = "AI를 통해 컨텐츠 아이템의 음성 생성 요청")
    @PostMapping(Const.PATH_GENERATE_ITEM_VOICE)
    public ContentItemGenerateResponse generateItemVoice(@PathVariable("content_id") long contentId, @RequestParam("fail_retry") boolean failRetry) {
        ContentItemGenerateParam param = new ContentItemGenerateParam(contentId, failRetry);
        ContentItemGenerateResult result = service.generateItemVoice(param);

        return mapper.to(result);
    }

    /**
     * 
     * @param contentId 컨텐츠 id
     * @param failRetry 실패 재시도
     * @return 
     */
    @Operation(summary = "컨텐츠 비디오 생성", description = "컨텐츠의 비디오 생성 요청")
    @PostMapping(Const.PATH_GENERATE_VIDEO)
    public ContentItemGenerateResponse generateVideo(@PathVariable("content_id") long contentId, @RequestParam("fail_retry") boolean failRetry) {
        ContentItemGenerateParam param = new ContentItemGenerateParam(contentId, failRetry);
        ContentItemGenerateResult result = service.generateVideo(param);

        return mapper.to(result);
    }

    /**
     * 
     * @param contentId 컨텐츠 id
     * @param failRetry 실패 재시도
     * @return
     */
    @Operation(summary = "컨텐츠 텍스트 확인", description = "컨텐츠의 텍스트 수락 or 거절")
    @PostMapping(Const.PATH_AGREE_ITEM_TEXT)
    public AgreeContentItemResponse agreeItemText(@PathVariable("content_id") long contentId, @RequestParam("agree") boolean agree) {
        AgreeContentItemParam param = new AgreeContentItemParam("", contentId, agree);
        AgreeContentItemResult result = service.agreeItemText(param);

        return mapper.to(result);
    }

    /**
     * 
     * @param contentId 컨텐츠 id
     * @param failRetry 실패 재시도
     * @return
     */
    @Operation(summary = "컨텐츠 텍스트 컨펌", description = "컨텐츠의 이미지 수락 or 거절")
    @PostMapping(Const.PATH_AGREE_ITEM_IMAGE)
    public AgreeContentItemResponse agreeItemImage(@PathVariable("content_id") long contentId, @RequestParam("agree") boolean agree) {
        AgreeContentItemParam param = new AgreeContentItemParam("", contentId, agree);
        AgreeContentItemResult result = service.agreeItemImage(param);

        return mapper.to(result);
    }

    /**
     * 
     * @param contentId 컨텐츠 id
     * @param failRetry 실패 재시도
     * @return
     */
    @Operation(summary = "컨텐츠 음성 컨펌", description = "컨텐츠의 음성 수락 or 거절")
    @PostMapping(Const.PATH_AGREE_ITEM_VOICE)
    public AgreeContentItemResponse agreeItemVoice(@PathVariable("content_id") long contentId, @RequestParam("agree") boolean agree) {
        AgreeContentItemParam param = new AgreeContentItemParam("", contentId, agree);
        AgreeContentItemResult result = service.agreeItemVoice(param);

        return mapper.to(result);
    }

    /**
     * 
     * @param contentId 컨텐츠 id
     * @param failRetry 실패 재시도
     * @return
     */
    @Operation(summary = "컨텐츠 비디오 발행", description = "컨텐츠의 비디오를 유튜브등에 발행")
    @PostMapping(Const.PATH_PUBLISH_VIDEO)
    public ContentPublishVideoResponse publishVideo(@PathVariable("content_id") long contentId, @RequestParam("fail_retry") boolean failRetry) {
        ContentPublishVideoParam param = new ContentPublishVideoParam(contentId, failRetry);
        ContentPublishVideoResult result = service.publishVideo(param);

        return mapper.to(result);
    }
    
}
