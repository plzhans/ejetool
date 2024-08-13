package com.ejetool.videoai.chatbot.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ejetool.lib.telegram.TelegramBotSettings;
import com.ejetool.lib.telegram.TelegramPollinBot;
import com.ejetool.lib.telegram.handler.CommandDispatcher;
import com.ejetool.lib.telegram.handler.CommandDispatcherWithContextImpl;
import com.ejetool.lib.telegram.handler.UserRoleManger;
import com.ejetool.videoai.chatbot.runner.TelegramContextBotRunner;
import com.ejetool.videoai.chatbot.service.UserRoleMangerService;

@Configuration
@EnableConfigurationProperties(TelegramBotSettings.class)
public class TelegramConfig {

	@Bean
	TelegramPollinBot telegramBot(TelegramBotSettings settings){
		return new TelegramPollinBot(settings);
	}

	@Bean
	UserRoleManger userRoleManger(TelegramBotSettings settings){
		return new UserRoleMangerService(settings.getBotAdminIds());
	}

	@Bean
	TelegramContextBotRunner telegramContextBotRunner(TelegramPollinBot bot, UserRoleManger userRoleManger, ApplicationContext context){
		CommandDispatcher commandDispatcher = new CommandDispatcherWithContextImpl(userRoleManger, context);
		return new TelegramContextBotRunner(bot, commandDispatcher);
	}
}
