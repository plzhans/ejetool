package com.ejetool.videoai.chatbot.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ejetool.videoai.client.EjetoolVideoAIClient;
import com.ejetool.videoai.client.setting.EjetoolVideoAIClientSetting;

@Configuration
@EnableConfigurationProperties(EjetoolVideoAIClientSetting.class)
public class EjetoolConfig {
    @Bean
    EjetoolVideoAIClient ejetoolVideoAIClient(EjetoolVideoAIClientSetting settings){
        return EjetoolVideoAIClient.build(settings);
    }
}
