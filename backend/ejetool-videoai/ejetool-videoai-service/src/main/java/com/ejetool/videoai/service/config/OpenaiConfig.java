package com.ejetool.videoai.service.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ejetool.lib.openai.OpenaiClient;
import com.ejetool.lib.openai.OpenaiSettings;

@Configuration
@EnableConfigurationProperties(OpenaiSettings.class)
public class OpenaiConfig {
    @Bean
    OpenaiClient openAIClient(OpenaiSettings settings){
        return new OpenaiClient(settings);
    }
}
