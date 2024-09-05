package com.ejetool.videoai.event.consumer.config;

import java.util.Arrays;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ejetool.common.util.StringMakerUtils;
import com.ejetool.lib.telegram.TelegramBotSender;
import com.ejetool.lib.telegram.TelegramBotSettings;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableConfigurationProperties(TelegramBotSettings.class)
public class TelegramConfig {
	@Bean
	TelegramBotSender telegramBot(TelegramBotSettings settings){
		log.info("settings.botUsername={}", settings.getBotUsername());
		log.info("settings.botToken={}", StringMakerUtils.mask(settings.getBotToken()));
		log.info("settings.botAdminIds={}", Arrays.toString(settings.getBotAdminIds()));
		log.info("settings.botDefaultChatId={}", settings.getBotDefaultChatId());
		return new TelegramBotSender(settings);
	}
}