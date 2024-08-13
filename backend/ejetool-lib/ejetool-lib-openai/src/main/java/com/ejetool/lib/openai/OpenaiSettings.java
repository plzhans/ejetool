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