package com.ejetool.videoai.event.consumer.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ejetool.lib.useapi.UseapiApi;
import com.ejetool.lib.useapi.UseapiSettings;

@Configuration
@EnableConfigurationProperties({UseapiSettings.class})
public class UseapiConfig {

    @Bean
    UseapiApi useApiClient(UseapiSettings settings){
        return new UseapiApi(settings);
    }
}
