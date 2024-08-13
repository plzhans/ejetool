package com.ejetool.videoai.service.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.cache.annotation.EnableCaching;
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
@EnableCaching
public class CacheConfig {

    @Bean
    ParametersAllHashKeyGenerator parametersAllHashKeyGenerator() {
        return new ParametersAllHashKeyGenerator();
    }
   
    @Bean
    RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        // RedisCacheConfigurations 은 캐시 데이터를 저장하는 RedisCache 를 설정하는 기능 제공
        RedisCacheConfiguration cacheDefaults = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofDays(1))
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
            //.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new Jackson2JsonRedisSerializer<>(Object.class))); // json 직렬화
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
            .prefixCacheNameWith("cache:");

        Map<String, RedisCacheConfiguration> configurations = new HashMap<>();
        //configurations.put("xxxCache", cacheDefaults.entryTtl(Duration.ofMinutes(30)));

        return RedisCacheManager.builder(redisConnectionFactory)
            //.fromConnectionFactory()
            .cacheDefaults(cacheDefaults)
            .withInitialCacheConfigurations(configurations)
            .build();
    }

    // @Bean("customKeyGenerator")
    // public KeyGenerator keyGenerator() {
    //     return new KeyGenerator() {
    //         @Override
    //         public Object generate(Object target, Method method, Object... params) {
    //             Custom key generation logic here
    //             String requestId = (String) params[0];
    //             String model = (String) params[1];
    //             String prompt = StringUtils.truncateString((String) params[2], 50);
    //             return requestId + "_" + model + "_" + prompt.hashCode();
    //         }
    //     };
    // }
}
