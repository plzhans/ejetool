package com.ejetool.videoai.client.setting;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties("ejetool")
public class EjetoolVideoAIClientSetting {
    @Getter @Setter
    @Value("${ejetool.host:https://api.ejetool.com}")
    private String host;

    @Getter @Setter
    @Value("${ejetool.api_key}")
    private String apiKey;
}
