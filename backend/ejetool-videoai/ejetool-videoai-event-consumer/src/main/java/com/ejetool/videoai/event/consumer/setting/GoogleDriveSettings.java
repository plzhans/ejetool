package com.ejetool.videoai.event.consumer.setting;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties("google.drive")
public class GoogleDriveSettings {
    @Getter @Setter
    private String rootId;
    @Getter @Setter
    private String path;
}
