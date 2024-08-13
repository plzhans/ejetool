package com.ejetool.lib.useapi.midjourney;

import java.net.URI;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.RequestEntity.BodyBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.ejetool.lib.useapi.dto.midjourney.*;
import com.ejetool.lib.useapi.helper.EndpointHelper;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UseapiMidjourneyApi {

    private static final String HOST = "https://api.useapi.net";
    private static final String AUTHORIZATION_NAME = "Authorization";
    private static final String TOKEN_BEARER = "Bearer ";

    @SuppressWarnings("squid:S1075")
    private class Const {
        static final String PATH_JOBS_JOBID = "/v2/jobs/";
        static final String PATH_JOBS_CANCEL_JOBID = "/v2/jobs/cancel/";
        static final String PATH_JOBS_IMAGINE = "/v2/jobs/imagine";
        static final String PATH_JOBS_BUTTON= "/v2/jobs/button";
    }

    @Getter
    private final String token;

    @Getter
    private final UseapiMidjourneySettings midjourneySettings;
   
    @Getter
    private final RestTemplate restTemplate;

    public UseapiMidjourneyApi(String token, UseapiMidjourneySettings midjourneySettings){
        this.token = token;
        this.midjourneySettings = midjourneySettings;
        this.restTemplate = createRestTemplate();
    }

    private RestTemplate createRestTemplate(){
        return new RestTemplate();
    }

    /**
     * 
     * @param method
     * @param path
     * @return
     */
    private HttpHeaders createDefualtHeader(){
        HttpHeaders headers = new HttpHeaders();
        headers.set(AUTHORIZATION_NAME, TOKEN_BEARER + this.getToken());
        return headers;
    }

    /**
     * 
     * @param method
     * @param path
     * @return
     */
    private <T> HttpEntity<T> createHttpEntity(){
        HttpHeaders headers = this.createDefualtHeader();
        HttpEntity<T> entity = new HttpEntity<>(headers);
        return entity;
    }

    /**
     * 
     * @param method
     * @param path
     * @return
     */
    private BodyBuilder createRequestBuilder(HttpMethod method, String path){
        var uri = EndpointHelper.endpointUri(HOST, path);
        return this.createRequestBuilder(method, uri);
    }

    /**
     * 
     * @param method
     * @param path
     * @return
     */
    private BodyBuilder createRequestBuilder(HttpMethod method, URI uri){
        return RequestEntity
            .method(method, uri)
            .header(AUTHORIZATION_NAME, TOKEN_BEARER + this.getToken())
            .contentType(MediaType.APPLICATION_JSON);
    }

    /**
     * 
     * docs : https://useapi.net/docs/api-v2/get-jobs-jobid
     * @param jobId
     * @return
     */
    public JobResponse getJob(String jobId) {
        var uri = EndpointHelper.endpointUriBuilder(HOST, Const.PATH_JOBS_JOBID)
            .queryParam("jobid", jobId)
            .build()
            .toString();
            
        var httpEntity = this.createHttpEntity();
        try {
            ResponseEntity<JobResponse> restResponse = this.restTemplate.exchange(uri, HttpMethod.GET, httpEntity, JobResponse.class);
            var response = restResponse.getBody();
            if(response != null){
                log.info("getJob('{}'): code={}, jobId={}", jobId, response.getCode(), response.getJobId());
                return response;
            } else {
                return JobResponse.createError(jobId, -1, "job body empty", "ResponseEntity body empty");
            }
        } catch(HttpStatusCodeException e){
            log.info("getJob('{}'): Error. status={}, message={}", jobId, e.getStatusCode(), e.getMessage());
            return JobResponse.createError(jobId, e.getStatusCode().value(), e.getStatusText(), e.getMessage());
        } catch(Exception e){
            log.error(String.format("getJob('{}'): Exception. message=%s", jobId, e.getMessage()), e);
            throw e;
        }
    }

    /**
     * 
     * docs : https://useapi.net/docs/api-v2/get-jobs-cancel
     * @param jobId
     * @return
     */
    public JobCancelResponse jobCancel(String jobId) {
        var uri = EndpointHelper.endpointUriBuilder(HOST, Const.PATH_JOBS_CANCEL_JOBID)
            .queryParam("jobid", jobId)
            .build()
            .toString();
            
        var httpEntity = this.createHttpEntity();
        try {
            ResponseEntity<JobCancelResponse> restResponse = this.restTemplate.exchange(uri, HttpMethod.GET, httpEntity, JobCancelResponse.class);
            var response = restResponse.getBody();
            if(response != null){
                log.info("jobCancel('{}'): code={}, jobId={}", jobId, response.getCode(), response.getJobId());
                return response;
            } else {
                return JobCancelResponse.createError(jobId, -1, "job body empty", "job body empty");
            }
        } catch(HttpStatusCodeException e){
            log.info("jobCancel('{}'): Error. status=%s, message=%s",jobId, e.getStatusCode(), e.getMessage());
            return JobCancelResponse.createError(jobId, e.getStatusCode().value(), e.getStatusText(), e.getMessage());
        } catch(Exception e){
            log.error(String.format("jobCancel('{}'): Exception. message=%s", jobId, e.getMessage()), e);
            throw e;
        }
    }

    /**
     * 
     * docs : https://useapi.net/docs/api-v2/post-jobs-imagine
     * @param request
     * @return
     */
    public JobImagineResponse jobImagine(JobImagineRequest request) {
        if(request.getDiscordToken() == null){
            request.setDiscordToken(this.midjourneySettings.getDiscordToken());
        }
        if(request.getDiscordServer() == null){
            request.setDiscordServer(this.midjourneySettings.getDiscordServer());
        }
        if(request.getDiscordChannel() == null){
            request.setDiscordChannel(this.midjourneySettings.getDiscordChannel());
        }
   
        var restRequest = this.createRequestBuilder(HttpMethod.POST, Const.PATH_JOBS_IMAGINE)
            .body(request);
        try {
            ResponseEntity<JobImagineResponse> restResponse = this.restTemplate.exchange(restRequest, JobImagineResponse.class);
            var response = restResponse.getBody();
            if(response != null){
                log.info("jobImagine(): code={}, jobId={}", response.getCode(), response.getJobId());
                return response;
            } else {
                return JobImagineResponse.createError(-1, "imagine body empty", "imagine body empty");
            }
        } catch(HttpStatusCodeException e){
            log.info("jobImagine(): Error. status={}, message={}", e.getStatusCode(), e.getMessage());
            return JobImagineResponse.createError(e.getStatusCode().value(), e.getStatusText(), e.getMessage());
        } catch(Exception e){
            log.error(String.format("jobImagine(): Exception. message=%s", e.getMessage()), e);
            throw e;
        }
    }

    /**
     * 
     * docs : https://useapi.net/docs/api-v2/post-jobs-button
     * @param request
     * @return
     */
    public JobButtonResponse jobButton(JobButtonRequest request) {
        if(request.getDiscordToken() == null){
            request.setDiscordToken(this.midjourneySettings.getDiscordToken());
        }
       
        var restRequest = this.createRequestBuilder(HttpMethod.POST, Const.PATH_JOBS_BUTTON)
            .body(request);
        try {
            ResponseEntity<JobButtonResponse> restResponse = this.restTemplate.exchange(restRequest, JobButtonResponse.class);
            var response = restResponse.getBody();
            if(response != null){
                log.info("jobButton('{}','{}'): code={}, jobId={}", request.getJobId(), request.getButton(), response.getCode(), response.getJobId());
                return response;
            } else {
                return JobButtonResponse.createError(-1, "button body empty", "button body empty");
            }
        } catch(HttpStatusCodeException e){
            log.info("jobButton('{}','{}'): Error. status={}, message={}", request.getJobId(), request.getButton(), e.getStatusCode(), e.getMessage());
            return JobButtonResponse.createError(e.getStatusCode().value(), e.getStatusText(), e.getMessage());
        } catch(Exception e){
            log.error(String.format("jobButton('%s','%s'): Exception. message=%s", request.getJobId(), request.getButton(), e.getMessage()), e);
            throw e;
        }
    }
}
