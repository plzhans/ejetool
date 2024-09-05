package com.ejetool.videoai.service.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ejetool.lib.openai.OpenaiClient;
import com.ejetool.lib.openai.OpenaiSettings;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableConfigurationProperties(OpenaiSettings.class)
public class OpenaiConfig {
    
    @Bean
    OpenaiClient openAIClient(OpenaiSettings settings){
        log.info("settings.host={}", settings.getHost());
        log.info("settings.apiKey={}", settings.getMaskApiKey());
        return new OpenaiClient(settings);
    }
}
