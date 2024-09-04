package com.ejetool.videoai.client.content;

import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.ejetool.videoai.client.base.BaseConst;
import com.ejetool.videoai.client.base.RestResponse;
import com.ejetool.videoai.client.content.dto.ContentPublishVideoRequest;
import com.ejetool.videoai.client.content.dto.ContentResponse;
import com.ejetool.videoai.client.content.dto.CreateContentByAIRequest;
import com.ejetool.videoai.client.content.dto.CreateContentResponse;
import com.ejetool.videoai.client.content.dto.AgreeContentItemResponse;
import com.ejetool.videoai.client.content.dto.UpdateContentItemRequest;
import com.ejetool.videoai.client.content.dto.UpdateContentItemResponse;
import com.ejetool.videoai.client.content.dto.ContentGenerateItemRequest;
import com.ejetool.videoai.client.content.dto.ContentGenerateVideoRequest;
import com.ejetool.videoai.client.exception.EjetoolRemoteExecuteException;
import com.ejetool.videoai.client.setting.EjetoolVideoAIClientSetting;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class ContentApiImpl implements ContentApi {

    @UtilityClass
    
    @SuppressWarnings("squid:S1075")
    public static class Const {
        public static final String PATH_BASE = "/videoai";

        public static final String PATH_CONTENTS_CREATE_AI = "/contents/ai";
        public static final String PATH_CONTENTS_GET = "/contents/{content_id}";
        public static final String PATH_CONTENTS_UPDATE_ITEM = "/contents/{content_id}/{item_id}";
        public static final String PATH_CONTENTS_GENERATE_ITEM_TEXT = "/contents/{content_id}/generate-item-text";
        public static final String PATH_CONTENTS_GENERATE_ITEM_IMAGE = "/contents/{content_id}/generate-item-image";
        public static final String PATH_CONTENTS_GENERATE_ITEM_VOICE = "/contents/{content_id}/generate-item-voice";
        public static final String PATH_CONTENTS_GENERATE_VIDEO = "/contents/{content_id}/generate-video";
        public static final String PATH_CONTENTS_ITEM_TEXT_AGREE = "/contents/{content_id}/agree-item-text";
        public static final String PATH_CONTENTS_ITEM_IMAGE_AGREE = "/contents/{content_id}/agree-item-image";
        public static final String PATH_CONTENTS_ITEM_VOICE_AGREE = "/contents/{content_id}/agree-item-voice";
        public static final String PATH_CONTENTS_PUBLISH_VIDEO = "/contents/{content_id}/publish-video";

        public static final String QUERYPARAM_FAIL_RETRY = "fail_retry";
        public static final String QUERYPARAM_AGREE = "agree";
        
    }

    @Getter
    private final EjetoolVideoAIClientSetting settings;

    @Getter
    private final RestTemplate restTemplate;

    /** */
    public ContentApiImpl(EjetoolVideoAIClientSetting settings, RestTemplate restTemplate){
        this.settings = settings;
        this.restTemplate = restTemplate;
    }

    /**
     * 
     * @param path
     * @return
     */
    public String toEndpoint(String path){
        return this.settings.getHost() + Const.PATH_BASE + path;
    }
    
     /**
     * 
     * @param path
     * @return
     */
    public UriComponentsBuilder toEndpointAsBuilder(String path){
        return UriComponentsBuilder.fromUriString(this.settings.getHost())
                    .path(Const.PATH_BASE + path);
    }

    /*
     * 컨텐츠 생성 (by AI)
     */
    @Override
    public RestResponse<CreateContentResponse> createByAI(CreateContentByAIRequest request){
        String uri = this.toEndpoint(Const.PATH_CONTENTS_CREATE_AI);
        RequestEntity<CreateContentByAIRequest> restRequest = RequestEntity
            .post(uri)
            .header(BaseConst.HEADER_AUTHORIZATION, BaseConst.HEADER_VALUE_PREFIX_BEARER + this.getSettings().getApiKey())
            .contentType(MediaType.APPLICATION_JSON)
            .body(request);

        try {
            ResponseEntity<CreateContentResponse> restResponse = this.restTemplate.exchange(restRequest, CreateContentResponse.class);
            CreateContentResponse body = restResponse.getBody();
            return new RestResponse<>(200, body);
        } catch(HttpStatusCodeException e){
            log.info("[{} status={}, message={}", uri, e.getStatusCode(), e.getMessage());
            return RestResponse.createError(e.getStatusCode().value(), e.getMessage());
        } catch(Exception e){
            log.error(String.format("[%s] error. message=%s", uri, e.getMessage()), e);
            throw new EjetoolRemoteExecuteException("createByAI(): Exception.", e);
        }
    }

    /**
     * 컨텐츠 조회
     */
    @Override
    public RestResponse<ContentResponse> getContent(long contentId){
        String uri = this.toEndpoint(Const.PATH_CONTENTS_GET);
        RequestEntity<Void> restRequest = RequestEntity
            .get(uri, contentId)
            .header(BaseConst.HEADER_AUTHORIZATION, BaseConst.HEADER_VALUE_PREFIX_BEARER + this.getSettings().getApiKey())
            .build();

        try {
            ResponseEntity<ContentResponse> restResponse = this.restTemplate.exchange(restRequest, ContentResponse.class);
            ContentResponse body = restResponse.getBody();
            return new RestResponse<>(200, body);
        } catch(HttpStatusCodeException e){
            log.info("[{} status={}, message={}", uri, e.getStatusCode(), e.getMessage());
            return RestResponse.createError(e.getStatusCode().value(), e.getMessage());
        } catch(Exception e){
            log.error(String.format("[%s] error. message=%s", restRequest.getUrl().getPath(), e.getMessage()), e);
            throw new EjetoolRemoteExecuteException(String.format("getContent(%s): Exception.", contentId), e);
        }
    }

    /**
     * 아이템 텍스트 생성
     */
    @Override
    public RestResponse<String> generateItemText(ContentGenerateItemRequest request) {
        String uri = this.toEndpointAsBuilder(Const.PATH_CONTENTS_GENERATE_ITEM_TEXT)
            .queryParam(Const.QUERYPARAM_FAIL_RETRY, request.isFailRetry())
            .buildAndExpand(request.getContentId()).toUriString();

        RequestEntity<ContentGenerateItemRequest> restRequest = RequestEntity
            .post(uri)
            .header(BaseConst.HEADER_AUTHORIZATION, BaseConst.HEADER_VALUE_PREFIX_BEARER + this.getSettings().getApiKey())
            .contentType(MediaType.APPLICATION_JSON)
            .body(request);

        try {
            ResponseEntity<String> restResponse = this.restTemplate.exchange(restRequest, String.class);
            String body = restResponse.getBody();
            return new RestResponse<>(200, body);
        } catch(HttpStatusCodeException e){
            log.info("[{} status={}, message={}", uri, e.getStatusCode(), e.getMessage());
            return RestResponse.createError(e.getStatusCode().value(), e.getMessage());
        } catch(Exception e){
            log.error(String.format("[%s] error. message=%s", restRequest.getUrl().getPath(), e.getMessage()), e);
            throw new EjetoolRemoteExecuteException("generateItemText(): Exception.", e);
        }
    }

    /**
     * 아이템 이미지 생성
     */
    @Override
    public RestResponse<String> generateItemImage(ContentGenerateItemRequest request) {
        String uri = this.toEndpointAsBuilder(Const.PATH_CONTENTS_GENERATE_ITEM_IMAGE)
            .queryParam(Const.QUERYPARAM_FAIL_RETRY, request.isFailRetry())
            .buildAndExpand(request.getContentId()).toUriString();

        RequestEntity<ContentGenerateItemRequest> restRequest = RequestEntity
            .post(uri)
            .header(BaseConst.HEADER_AUTHORIZATION, BaseConst.HEADER_VALUE_PREFIX_BEARER + this.getSettings().getApiKey())
            .contentType(MediaType.APPLICATION_JSON)
            .body(request);

        try {
            ResponseEntity<String> restResponse = this.restTemplate.exchange(restRequest, String.class);
            String body = restResponse.getBody();
            return new RestResponse<>(200, body);
        } catch(HttpStatusCodeException e){
            log.info("[{} status={}, message={}", uri, e.getStatusCode(), e.getMessage());
            return RestResponse.createError(e.getStatusCode().value(), e.getMessage());
        } catch(Exception e){
            log.error(String.format("[%s] error. message=%s", restRequest.getUrl().getPath(), e.getMessage()), e);
            throw new EjetoolRemoteExecuteException("generateItemImage(): Exception.", e);
        }
    }

    /**
     * 아이템 음성 생성
     */
    @Override
    public RestResponse<String> generateItemVoice(ContentGenerateItemRequest request) {
        String uri = this.toEndpointAsBuilder(Const.PATH_CONTENTS_GENERATE_ITEM_VOICE)
            .queryParam(Const.QUERYPARAM_FAIL_RETRY, request.isFailRetry())
            .buildAndExpand(request.getContentId()).toUriString();

        RequestEntity<ContentGenerateItemRequest> restRequest = RequestEntity
            .post(uri)
            .header(BaseConst.HEADER_AUTHORIZATION, BaseConst.HEADER_VALUE_PREFIX_BEARER + this.getSettings().getApiKey())
            .contentType(MediaType.APPLICATION_JSON)
            .body(request);

        try {
            ResponseEntity<String> restResponse = this.restTemplate.exchange(restRequest, String.class);
            String body = restResponse.getBody();
            return new RestResponse<>(200, body);
        } catch(HttpStatusCodeException e){
            log.info("[{} status={}, message={}", uri, e.getStatusCode(), e.getMessage());
            return RestResponse.createError(e.getStatusCode().value(), e.getMessage());
        } catch(Exception e){
            log.error(String.format("[%s] error. message=%s", restRequest.getUrl().getPath(), e.getMessage()), e);
            throw new EjetoolRemoteExecuteException("generateItemVoice(): Exception.", e);
        }
    }

    /**
     * 비디오 생성
     */
    @Override
    public RestResponse<String> generateVideo(ContentGenerateVideoRequest request) {
        String uri = this.toEndpointAsBuilder(Const.PATH_CONTENTS_GENERATE_VIDEO)
            .queryParam(Const.QUERYPARAM_FAIL_RETRY, request.isFailRetry())
            .buildAndExpand(request.getContentId()).toUriString();
        
        RequestEntity<ContentGenerateVideoRequest> restRequest = RequestEntity
            .post(uri)
            .header(BaseConst.HEADER_AUTHORIZATION, BaseConst.HEADER_VALUE_PREFIX_BEARER + this.getSettings().getApiKey())
            .contentType(MediaType.APPLICATION_JSON)
            .body(request);

        try {
            ResponseEntity<String> restResponse = this.restTemplate.exchange(restRequest, String.class);
            String body = restResponse.getBody();
            return new RestResponse<>(200, body);
        } catch(HttpStatusCodeException e){
            log.info("[{} status={}, message={}", uri, e.getStatusCode(), e.getMessage());
            return RestResponse.createError(e.getStatusCode().value(), e.getMessage());
        } catch(Exception e){
            log.error(String.format("[%s] error. message=%s", restRequest.getUrl().getPath(), e.getMessage()), e);
            throw new EjetoolRemoteExecuteException("generateVideo(): Exception.", e);
        }
    }

    @Override
    public RestResponse<AgreeContentItemResponse> agreeItemText(long contentId, boolean agree) {
        String uri = this.toEndpointAsBuilder(Const.PATH_CONTENTS_ITEM_TEXT_AGREE)
            .queryParam(Const.QUERYPARAM_AGREE, agree)
            .buildAndExpand(contentId).toUriString();
        
        RequestEntity<Void> restRequest = RequestEntity
            .post(uri, contentId)
            .header(BaseConst.HEADER_AUTHORIZATION, BaseConst.HEADER_VALUE_PREFIX_BEARER + this.getSettings().getApiKey())
            .build();

        try {
            ResponseEntity<AgreeContentItemResponse> restResponse = this.restTemplate.exchange(restRequest, AgreeContentItemResponse.class);
            AgreeContentItemResponse body = restResponse.getBody();
            return new RestResponse<>(200, body);
        } catch(HttpStatusCodeException e){
            log.info("[{} status={}, message={}", uri, e.getStatusCode(), e.getMessage());
            return RestResponse.createError(e.getStatusCode().value(), e.getMessage());
        } catch(Exception e){
            log.error(String.format("[%s] error. message=%s", restRequest.getUrl().getPath(), e.getMessage()), e);
            throw new EjetoolRemoteExecuteException("generateVideo(): Exception.", e);
        }
    }

    @Override
    public RestResponse<AgreeContentItemResponse> agreeItemImage(long contentId, boolean agree) {
        String uri = this.toEndpointAsBuilder(Const.PATH_CONTENTS_ITEM_TEXT_AGREE)
            .queryParam(Const.QUERYPARAM_AGREE, agree)
            .buildAndExpand(contentId).toUriString();
        
        RequestEntity<Void> restRequest = RequestEntity
            .post(uri, contentId)
            .header(BaseConst.HEADER_AUTHORIZATION, BaseConst.HEADER_VALUE_PREFIX_BEARER + this.getSettings().getApiKey())
            .build();

        try {
            ResponseEntity<AgreeContentItemResponse> restResponse = this.restTemplate.exchange(restRequest, AgreeContentItemResponse.class);
            AgreeContentItemResponse body = restResponse.getBody();
            return new RestResponse<>(200, body);
        } catch(HttpStatusCodeException e){
            log.info("[{} status={}, message={}", uri, e.getStatusCode(), e.getMessage());
            return RestResponse.createError(e.getStatusCode().value(), e.getMessage());
        } catch(Exception e){
            log.error(String.format("[%s] error. message=%s", restRequest.getUrl().getPath(), e.getMessage()), e);
            throw new EjetoolRemoteExecuteException("generateVideo(): Exception.", e);
        }
    }

    @Override
    public RestResponse<AgreeContentItemResponse> agreeItemVoice(long contentId, boolean agree) {
        String uri = this.toEndpointAsBuilder(Const.PATH_CONTENTS_ITEM_TEXT_AGREE)
            .queryParam(Const.QUERYPARAM_AGREE, agree)
            .buildAndExpand(contentId).toUriString();
        
        RequestEntity<Void> restRequest = RequestEntity
            .post(uri, contentId)
            .header(BaseConst.HEADER_AUTHORIZATION, BaseConst.HEADER_VALUE_PREFIX_BEARER + this.getSettings().getApiKey())
            .build();

        try {
            ResponseEntity<AgreeContentItemResponse> restResponse = this.restTemplate.exchange(restRequest, AgreeContentItemResponse.class);
            AgreeContentItemResponse body = restResponse.getBody();
            return new RestResponse<>(200, body);
        } catch(HttpStatusCodeException e){
            log.info("[{} status={}, message={}", uri, e.getStatusCode(), e.getMessage());
            return RestResponse.createError(e.getStatusCode().value(), e.getMessage());
        } catch(Exception e){
            log.error(String.format("[%s] error. message=%s", restRequest.getUrl().getPath(), e.getMessage()), e);
            throw new EjetoolRemoteExecuteException("generateVideo(): Exception.", e);
        }
    }

    /**
     * 비디오 발행
     */
    @Override
    public RestResponse<String> publishVideo(ContentPublishVideoRequest request) {
        String uri = this.toEndpointAsBuilder(Const.PATH_CONTENTS_PUBLISH_VIDEO)
            .queryParam(Const.QUERYPARAM_FAIL_RETRY, request.isFailRetry())
            .buildAndExpand(request.getContentId()).toUriString();

        RequestEntity<ContentPublishVideoRequest> restRequest = RequestEntity
            .post(uri)
            .header(BaseConst.HEADER_AUTHORIZATION, BaseConst.HEADER_VALUE_PREFIX_BEARER + this.getSettings().getApiKey())
            .contentType(MediaType.APPLICATION_JSON)
            .body(request);

        try {
            ResponseEntity<String> restResponse = this.restTemplate.exchange(restRequest, String.class);
            String body = restResponse.getBody();
            return new RestResponse<>(200, body);
        } catch(HttpStatusCodeException e){
            log.info("[{} status={}, message={}", uri, e.getStatusCode(), e.getMessage());
            return RestResponse.createError(e.getStatusCode().value(), e.getMessage());
        } catch(Exception e){
            log.error(String.format("[%s] error. message=%s", restRequest.getUrl().getPath(), e.getMessage()), e);
            throw new EjetoolRemoteExecuteException("publishVideo(): Exception.", e);
        }
    }

    @Override
    public RestResponse<UpdateContentItemResponse> updateItem(long itemId, UpdateContentItemRequest request) {
        String uri = this.toEndpointAsBuilder(Const.PATH_CONTENTS_UPDATE_ITEM)
            .buildAndExpand("-", itemId).toUriString();
        return this.updateItem(uri, request);
    }

    @Override
    public RestResponse<UpdateContentItemResponse> updateItem(long contentId, long itemId, UpdateContentItemRequest request) {
        String uri = this.toEndpointAsBuilder(Const.PATH_CONTENTS_UPDATE_ITEM)
            .buildAndExpand(contentId, itemId).toUriString();
        return this.updateItem(uri, request);
    }

    private RestResponse<UpdateContentItemResponse> updateItem(String uri, UpdateContentItemRequest request) {
        
        RequestEntity<UpdateContentItemRequest> restRequest = RequestEntity
            .post(uri)
            .header(BaseConst.HEADER_AUTHORIZATION, BaseConst.HEADER_VALUE_PREFIX_BEARER + this.getSettings().getApiKey())
            .contentType(MediaType.APPLICATION_JSON)
            .body(request);

        try {
            ResponseEntity<UpdateContentItemResponse> restResponse = this.restTemplate.exchange(restRequest, UpdateContentItemResponse.class);
            UpdateContentItemResponse body = restResponse.getBody();
            return new RestResponse<>(200, body);
        } catch(HttpStatusCodeException e){
            log.info("[{} status={}, message={}", uri, e.getStatusCode(), e.getMessage());
            return RestResponse.createError(e.getStatusCode().value(), e.getMessage());
        } catch(Exception e){
            log.error(String.format("[%s] error. message=%s", uri, e.getMessage()), e);
            throw new EjetoolRemoteExecuteException("updateItem(): Exception.", e);
        }
    }

}
