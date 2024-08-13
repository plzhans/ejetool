package com.ejetool.lib.creatomate;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.ejetool.lib.creatomate.dto.CommonResponse;
import com.ejetool.lib.creatomate.dto.ListResponse;
import com.ejetool.lib.creatomate.dto.RenderResponse;
import com.ejetool.lib.creatomate.dto.CreateRenderRequest;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CreatomateApi {

    @UtilityClass
    public static class Const {
        public static final String HEADER_AUTHORIZATION = "Authorization";
        public static final String HEADER_VALUE_PREFIX_BEARER = "Bearer ";  
    }

    @Getter
    private final CreatomateSettings settings;

    @Getter
    private final RestTemplate restTemplate;

    public CreatomateApi(CreatomateSettings settings){
        this.settings = settings;
        this.restTemplate = createRestTemplate();
    }

    private RestTemplate createRestTemplate(){
        return new RestTemplate();
    }

    /**
     * 
     * @param path
     * @return
     */
    public URI createEndpointURI(String path){
        return UriComponentsBuilder
            .fromUriString(this.settings.getHost())
            .path(path)
            .build()
            .toUri();
    }

    /**
     * 
     * @param method
     * @param path
     * @return
     */
    private HttpHeaders createDefualtHeader(String apiKey){
        HttpHeaders headers = new HttpHeaders();
        headers.set(Const.HEADER_AUTHORIZATION, Const.HEADER_VALUE_PREFIX_BEARER + apiKey);
        return headers;
    }

    /**
     * 
     * @param method
     * @param path
     * @return
     */
    private <T> HttpEntity<T> createHttpEntity(String apiKey){
        HttpHeaders headers = this.createDefualtHeader(apiKey);
        HttpEntity<T> entity = new HttpEntity<>(headers);
        return entity;
    }

    public CommonResponse<RenderResponse> getRender(String rederId) {
        return this.getRender(this.settings.getApiKey(), rederId);
    }

    /**
     * {@link} https://creatomate.com/docs/api/rest-api/get-v1-renders
     * @param apiKey
     * @param request
     * @return
     */
    public CommonResponse<RenderResponse> getRender(String apiKey, String rederId) {
        URI uri = this.createEndpointURI("/v1/renders/"+rederId);
        
        var httpEntity = this.createHttpEntity(apiKey);
        try {
            ResponseEntity<RenderResponse> restResponse = this.restTemplate.exchange(uri, HttpMethod.GET, httpEntity, RenderResponse.class);
            var response = restResponse.getBody();
            return new CommonResponse<>(200, "OK", response);
        } catch(HttpStatusCodeException e){
            log.info(String.format("getRender('{}'): Error. status=%s, message=%s", rederId, e.getStatusCode(), e.getMessage()), e);
            return new CommonResponse<>(e.getStatusCode().value(), e.getMessage(), null);
        } catch(Exception e){
            log.error(String.format("getRender('{}'): Exception. message=%s", rederId, e.getMessage()), e);
            throw e;
        }
    }

    public CommonResponse<RenderResponse> createRenderThenFirst(String apiKey, CreateRenderRequest request) {
        var listResponse = this.createRender(apiKey, request);
        if(listResponse.getList().isEmpty()){
            return new CommonResponse<>(listResponse.getCode(), listResponse.getMessage(), null);
        } else {
            return listResponse.getList().stream()
                .findFirst()
                .map(x->new CommonResponse<>(listResponse.getCode(), listResponse.getMessage(), x))
                .get();
        }
    }

    /**
     * {@link} https://creatomate.com/docs/api/rest-api/post-v1-renders
     * @param apiKey
     * @param request
     * @return
     */
    public ListResponse<RenderResponse> createRender(String apiKey, CreateRenderRequest request) {
        URI uri = this.createEndpointURI("/v1/renders");
        
        RequestEntity<CreateRenderRequest> restRequest = RequestEntity
            .post(uri) 
            .header(Const.HEADER_AUTHORIZATION, Const.HEADER_VALUE_PREFIX_BEARER + apiKey)
            .contentType(MediaType.APPLICATION_JSON)
            .body(request);

        try {
            ResponseEntity<RenderResponse[]> restResponse = this.restTemplate.exchange(restRequest, RenderResponse[].class);
            var reders = Arrays.asList(restResponse.getBody());
            var response = new ListResponse<>(200, "OK", reders);
            log.info("[{}] code={}", uri, response.getCode());
            return response;
        } catch(HttpStatusCodeException e){
            log.info(String.format("[%s] status=%s, message=%s", uri, e.getStatusCode(), e.getMessage()), e);
            return new ListResponse<>(e.getStatusCode().value(), e.getMessage(), Collections.emptyList());
        } catch(Exception e){
            log.error(String.format("[%s] error. message=%s", uri, e.getMessage()), e);
            throw e;
        }
    }

}
