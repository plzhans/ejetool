package com.ejetool.lib.useapi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import com.ejetool.lib.useapi.midjourney.UseapiMidjourneySettings;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ConfigurationProperties(prefix = "useapi")
public class UseapiSettings {

    @Getter
    @Value("${useapi.api_key}")
    private final String token;

    @Getter
    private final UseapiMidjourneySettings midjourney;

}