package com.ejetool.videoai.service.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ejetool.lib.telegram.TelegramPollinBot;
import com.ejetool.lib.telegram.TelegramBotSettings;

@Configuration
@EnableConfigurationProperties(TelegramBotSettings.class)
public class TelegramConfig {
	@Bean
	TelegramPollinBot telegramBot(TelegramBotSettings settings){
		return new TelegramPollinBot(settings);
	}
}