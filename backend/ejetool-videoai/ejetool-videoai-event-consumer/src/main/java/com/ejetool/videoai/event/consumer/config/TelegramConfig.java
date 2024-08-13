package com.ejetool.videoai.event.consumer.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ejetool.lib.telegram.TelegramBotSender;
import com.ejetool.lib.telegram.TelegramBotSettings;

@Configuration
@EnableConfigurationProperties(TelegramBotSettings.class)
public class TelegramConfig {
	@Bean
	TelegramBotSender telegramBot(TelegramBotSettings settings){
		return new TelegramBotSender(settings);
	}
}