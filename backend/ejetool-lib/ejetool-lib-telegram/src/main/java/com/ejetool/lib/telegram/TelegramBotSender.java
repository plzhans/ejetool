package com.ejetool.lib.telegram;

import org.telegram.telegrambots.bots.DefaultAbsSender;

import lombok.Getter;

public class TelegramBotSender extends DefaultAbsSender{
	@Getter
	private TelegramBotSettings settings;

	public TelegramBotSender(TelegramBotSettings settings){
		super(settings.getOptions(), settings.getBotToken());
		this.settings = settings;
	}

	public Long getChatIdOrDefault(Long chatId){
		return chatId == null ? this.settings.getBotDefaultChatId() : chatId;
	}
}
