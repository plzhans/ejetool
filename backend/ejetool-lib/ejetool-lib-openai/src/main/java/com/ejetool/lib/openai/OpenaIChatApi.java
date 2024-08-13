package com.ejetool.lib.openai;

import java.net.URI;

import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.ejetool.lib.openai.dto.OpenaiResponse;
import com.ejetool.lib.openai.dto.chat.ChatCompletionRequest;
import com.ejetool.lib.openai.dto.chat.ChatCompletionResponse;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OpenaIChatApi {
    
    @Getter
    private final OpenaiSettings settings;

    @UtilityClass
    public class Const {
        public static final String HEADER_AUTHORIZATION = "Authorization";
    
        public static final String HEADER_VALUE_PREFIX_BEARER = "Bearer ";
    }
    

    public OpenaIChatApi(OpenaiSettings settings){
        this.settings = settings;
    }

    /**
     * API : https://api.openai.com/v1/chat/completions
     * @param request
     * @return
     */
    public OpenaiResponse<ChatCompletionResponse> sendCompletion(ChatCompletionRequest request) {
        URI uri = this.settings.createEndpointURI("/v1/chat/completions");
   
        RequestEntity<ChatCompletionRequest> restRequest = RequestEntity
            .post(uri) 
            .header(Const.HEADER_AUTHORIZATION, Const.HEADER_VALUE_PREFIX_BEARER + settings.getApiKey())
            .contentType(MediaType.APPLICATION_JSON)
            .body(request);

        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<ChatCompletionResponse> restResponse = restTemplate.exchange(restRequest, ChatCompletionResponse.class);
            var response = new OpenaiResponse<>(restResponse.getBody());
            log.info("[{}] status={}, model={}", uri, response.getStatus(), response.getResult().getModel());
            return response;
        } catch(HttpStatusCodeException e){
            log.info(String.format("[%s] status=%s, message=%s", uri, e.getStatusCode(), e.getMessage()), e);
            return new OpenaiResponse<>(e.getStatusCode().value(), e.getStatusText());
        } catch(Exception e){
            log.error(String.format("[%s] error. message=%s", uri, e.getMessage()), e);
            throw e;
        }
    }
}
