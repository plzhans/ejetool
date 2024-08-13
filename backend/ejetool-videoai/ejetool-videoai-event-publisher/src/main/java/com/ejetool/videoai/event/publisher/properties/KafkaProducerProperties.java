package com.ejetool.videoai.event.publisher.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties("kafka")
public class KafkaProducerProperties {
    @Value("${bootstrap-servers}")
    private String bootStrapServers;
}
