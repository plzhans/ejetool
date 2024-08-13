package com.ejetool.videoai.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

import io.swagger.v3.core.jackson.ModelResolver;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class CustomWebMvcConfigurer implements WebMvcConfigurer {
    @Bean
    ModelResolver modelResolver(ObjectMapper objectMapper){
        log.info("objectMapper.setPropertyNamingStrategy(): SNAKE_CASE");
        return new ModelResolver(objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE));
    }
}
