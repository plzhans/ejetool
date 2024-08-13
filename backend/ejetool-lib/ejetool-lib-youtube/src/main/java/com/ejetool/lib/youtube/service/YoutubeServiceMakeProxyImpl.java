package com.ejetool.lib.youtube.service;

import java.net.URI;

import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.UnknownContentTypeException;

import com.ejetool.lib.youtube.dto.CreateYoutubeVideoParam;
import com.ejetool.lib.youtube.dto.YoutubeVideo;
import com.ejetool.lib.youtube.exception.YoutubeExecuteException;
import com.ejetool.lib.youtube.setting.MakeYoutubeWebhookSettings;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class YoutubeServiceMakeProxyImpl implements YoutubeService {

    @UtilityClass
    public static class Const {
        public static final String HEADER_AUTHORIZATION = "Authorization";
        public static final String HEADER_VALUE_PREFIX_BEARER = "Bearer ";  
    }
    
    @Getter
    private final MakeYoutubeWebhookSettings settings;

    @Getter
    private final RestTemplate restTemplate;


    public YoutubeServiceMakeProxyImpl(MakeYoutubeWebhookSettings settings){
        this.settings = settings;
        this.restTemplate = this.createRestTemplate();
    }

    private RestTemplate createRestTemplate(){
        return new RestTemplate();
    }

    public YoutubeVideo insert(CreateYoutubeVideoParam param){
        URI uri = URI.create(this.settings.getWebhook());
        
        RequestEntity<CreateYoutubeVideoParam> restRequest = RequestEntity
            .post(uri) 
            .header(Const.HEADER_AUTHORIZATION, Const.HEADER_VALUE_PREFIX_BEARER + this.settings.getApiKey())
            .contentType(MediaType.APPLICATION_JSON)
            .body(param);

        try {
            ResponseEntity<YoutubeVideo> restResponse = this.restTemplate.exchange(restRequest, YoutubeVideo.class);
            YoutubeVideo video = restResponse.getBody();
            return video;
        } catch(HttpStatusCodeException e){
            log.info(String.format("[%s] status=%s, message=%s", uri, e.getStatusCode(), e.getMessage()), e);
            throw new YoutubeExecuteException(e.getMessage(), e);
        }catch(UnknownContentTypeException e){
            log.error(String.format("[%s] status=%s, message=%s, body=%s", uri, e.getStatusCode(), e.getMessage(), e.getResponseBodyAsString()), e);
            throw new YoutubeExecuteException(e.getMessage(), e);
        } catch(Exception e){
            log.error(String.format("[%s] error. message=%s", uri, e.getMessage()), e);
            throw e;
        }
    }
}
