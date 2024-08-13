package com.ejetool.lib.youtube.setting;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
    
@Getter
@RequiredArgsConstructor
@ConfigurationProperties("make.youtube")
public class MakeYoutubeWebhookSettings {
    @Value("${make.youtube.webhook}")
    private final String webhook;

    @Value("${make.youtube.api_key}")
    private final String apiKey;
}
