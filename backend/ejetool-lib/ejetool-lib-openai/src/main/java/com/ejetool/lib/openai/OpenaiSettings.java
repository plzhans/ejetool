package com.ejetool.lib.openai;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.Getter;

@Configuration
@ConfigurationProperties
public class OpenaiSettings {
    @Getter
    @Value("${openai.api_key}")
    private String apiKey;

    @Getter
    @Value("${openai.api.host:https://api.openai.com}")
    private String host;

    /**
     * openai api 대시보드에셔 표시되는 key 정보와 동일하게 표시
     * @return
     */
    public String getMaskApiKey(){
        if (this.apiKey == null) {
            return ""; 
        }

        int length = this.apiKey != null ? this.apiKey.length() : 0;
        if(length > 8){
            return this.apiKey.substring(0, 3) + "..." + this.apiKey.substring(length - 3);
        }
        if(length > 0){
            return "...";
        }
        return "";
    }

    /**
     * 
     * @param path
     * @return
     */
    public URI createEndpointURI(String path){
        return UriComponentsBuilder
            .fromUriString(this.host)
            .path(path)
            .build()
            .toUri();
    }
}

//https://api.openai.com