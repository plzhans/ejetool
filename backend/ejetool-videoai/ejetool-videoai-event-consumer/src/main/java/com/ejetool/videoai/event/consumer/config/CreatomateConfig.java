package com.ejetool.videoai.event.consumer.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ejetool.lib.creatomate.CreatomateApi;
import com.ejetool.lib.creatomate.CreatomateSettings;

@Configuration
@EnableConfigurationProperties(CreatomateSettings.class)
public class CreatomateConfig {
    
    @Bean
    CreatomateApi creatomateClient(CreatomateSettings settings){
        CreatomateApi client = new CreatomateApi(settings);
        return client;
    }
}
