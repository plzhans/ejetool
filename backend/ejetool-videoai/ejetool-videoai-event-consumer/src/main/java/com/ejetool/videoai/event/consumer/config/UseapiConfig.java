package com.ejetool.videoai.event.consumer.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ejetool.common.util.StringMakerUtils;
import com.ejetool.lib.useapi.UseapiApi;
import com.ejetool.lib.useapi.UseapiSettings;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableConfigurationProperties({UseapiSettings.class})
public class UseapiConfig {

    @Bean
    UseapiApi useApiClient(UseapiSettings settings){
        log.info("settings.token={}", StringMakerUtils.mask(settings.getToken()));
        if(settings.getMidjourney() != null){
            log.info("settings.midjourney.discordServer={}", settings.getMidjourney().getDiscordServer());
            log.info("settings.midjourney.discordChannel={}", settings.getMidjourney().getDiscordChannel());
            log.info("settings.midjourney.discordToken={}", StringMakerUtils.mask(settings.getMidjourney().getDiscordToken()));
        }
        return new UseapiApi(settings);
    }
}
