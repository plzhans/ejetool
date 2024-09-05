package com.ejetool.videoai.event.consumer.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ejetool.common.util.StringMakerUtils;
import com.ejetool.lib.creatomate.CreatomateApi;
import com.ejetool.lib.creatomate.CreatomateSettings;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableConfigurationProperties(CreatomateSettings.class)
public class CreatomateConfig {
    
    @Bean
    CreatomateApi creatomateClient(CreatomateSettings settings){
        log.info("settings.host={}", settings.getHost());
		log.info("settings.apiKey={}", StringMakerUtils.mask(settings.getApiKey()));
        
        CreatomateApi client = new CreatomateApi(settings);
        return client;
    }
}
