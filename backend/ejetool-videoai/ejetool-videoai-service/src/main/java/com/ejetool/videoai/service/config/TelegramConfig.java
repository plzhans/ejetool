package com.ejetool.videoai.service.config;

import java.util.Arrays;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ejetool.lib.telegram.TelegramPollinBot;

import lombok.extern.slf4j.Slf4j;

import com.ejetool.common.util.StringMakerUtils;
import com.ejetool.lib.telegram.TelegramBotSettings;

@Slf4j
@Configuration
@EnableConfigurationProperties(TelegramBotSettings.class)
public class TelegramConfig {
	@Bean
	TelegramPollinBot telegramBot(TelegramBotSettings settings){
		log.info("settings.botUsername={}", settings.getBotUsername());
		log.info("settings.botToken={}", StringMakerUtils.mask(settings.getBotToken()));
		log.info("settings.botAdminIds={}", Arrays.toString(settings.getBotAdminIds()));
		log.info("settings.botDefaultChatId={}", settings.getBotDefaultChatId());
		return new TelegramPollinBot(settings);
	}
}