package com.ejetool.videoai.event.consumer.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.ejetool.common.cache.generator.ParametersAllHashKeyGenerator;

@Configuration
public class CacheConfig {
    @Bean
    ParametersAllHashKeyGenerator parametersAllHashKeyGenerator() {
        return new ParametersAllHashKeyGenerator();
    }
   
    @Bean
    RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration cacheDefaults = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofDays(1))
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
            //.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new Jackson2JsonRedisSerializer<>(Object.class))); // json 직렬화
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
            .prefixCacheNameWith("cache:");

        Map<String, RedisCacheConfiguration> configurations = new HashMap<>();

        return RedisCacheManager.builder(redisConnectionFactory)
            //.fromConnectionFactory()
            .cacheDefaults(cacheDefaults)
            .withInitialCacheConfigurations(configurations)
            .build();
    }

}
