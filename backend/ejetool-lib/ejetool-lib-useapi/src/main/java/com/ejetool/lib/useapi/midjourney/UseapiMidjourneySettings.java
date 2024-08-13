package com.ejetool.lib.useapi.midjourney;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ConfigurationProperties(prefix = "midjourney")
public class UseapiMidjourneySettings{
    @Getter
    @Value("${discord_token}")
    private final String discordToken;

    @Getter
    @Value("${discord_server}")
    private final String discordServer;

    @Getter
    @Value("${discord_channel}")
    private final String discordChannel;
}
