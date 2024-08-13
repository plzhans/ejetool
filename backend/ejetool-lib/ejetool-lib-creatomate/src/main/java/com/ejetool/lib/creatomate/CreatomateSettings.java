package com.ejetool.lib.creatomate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;

@ConfigurationProperties(prefix = "creatomate")
public class CreatomateSettings {

    @Getter
    @Value("${creatomate.api_key:}")
    private String apiKey;

    @Getter
    @Value("${creatomate.host:https://api.creatomate.com}")
    private String host;
}