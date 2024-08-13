package com.ejetool.lib.telegram;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage.SendMessageBuilder;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TelegramPollinBot extends TelegramLongPollingBot{

	static final String DEFAULT_PARSEMODE = ParseMode.MARKDOWN;
	
	@Getter
	private TelegramBotSettings settings;

    @Setter
    protected BiConsumer<TelegramPollinBot, Update> callbackUpdateReceived;

	public TelegramPollinBot(TelegramBotSettings settings){
		super(settings.getOptions(), settings.getBotToken());
		this.settings = settings;
	}

	@Override
	public void onUpdateReceived(Update update) {
		if(this.callbackUpdateReceived != null){
			this.callbackUpdateReceived.accept(this, update);
		}
	}

	@Override
	public String getBotUsername() {
		return this.settings.getBotUsername();
	}

	public Long getChatIdOrDefault(Long chatId){
		return chatId == null ? this.settings.getBotDefaultChatId() : chatId;
	}

	public Long getDefaultChatId(){
		return this.settings.getBotDefaultChatId();
	}

    public void sendMessage(String text, Consumer<SendMessageBuilder> builderFunc) {
		var chatId = this.settings.getBotDefaultChatId();
		if(chatId == null){
			return;
		}
		
        SendMessageBuilder builder = SendMessage.builder()
            .chatId(chatId)
			.parseMode(DEFAULT_PARSEMODE)
            .text(text);
		builderFunc.accept(builder);

		SendMessage method = builder.build();
        try {
            this.execute(method);
        } catch (TelegramApiException e) {
            log.error("sendMessage(): TelegramApiException.", e);
        }
    }

	public void sendMessage(String text) {
		var chatId = this.settings.getBotDefaultChatId();
		if(chatId == null){
			return;
		}
		
        SendMessage sendMessage = SendMessage.builder()
            .chatId(chatId)
			.parseMode(DEFAULT_PARSEMODE)
            .text(text)
            .build();
        try {
            this.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("sendMessage(): TelegramApiException.", e);
        }
    }
}
