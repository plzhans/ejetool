package com.ejetool.videoai.chatbot.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ejetool.common.util.StringMakerUtils;
import com.ejetool.videoai.client.EjetoolVideoAIClient;
import com.ejetool.videoai.client.setting.EjetoolVideoAIClientSetting;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Configuration
@EnableConfigurationProperties(EjetoolVideoAIClientSetting.class)
public class EjetoolConfig {
    @Bean
    EjetoolVideoAIClient ejetoolVideoAIClient(EjetoolVideoAIClientSetting settings){
        log.info("settings.host={}", settings.getHost());
        log.info("settings.apiKey={}", StringMakerUtils.mask(settings.getApiKey()));
        return EjetoolVideoAIClient.build(settings);
    }
}
