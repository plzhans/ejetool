package com.ejetool.lib.telegram;

import java.util.stream.Collectors;

import org.springframework.util.StringUtils;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.ejetool.common.exception.BaseException;
import com.ejetool.lib.telegram.exception.BotInitException;
import com.ejetool.lib.telegram.handler.CommandDispatcher;
import com.ejetool.lib.telegram.handler.CommandRequest;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TelegramHandler{

	static final String DEFAULT_PARSEMODE = ParseMode.MARKDOWN;

	@Getter
	private final TelegramPollinBot bot;
	
	@Getter
	private final CommandDispatcher dispatcher;

	@Getter
	private User botUser;


	public TelegramHandler(TelegramPollinBot bot, CommandDispatcher dispatcher){
		this.bot = bot;
		this.dispatcher = dispatcher;
	}

	@PostConstruct
	public void initialize(){
		this.dispatcher.initialize();
		try {
			this.botUser = this.bot.getMe();
		} catch (TelegramApiException e) {
			log.error("initialize(): TelegramApiException", e);
			throw new BotInitException(e);
		}
		this.setMyCommandsByCommandMappings();
	}

	public void foward(Update update) {
		String command = null;
		if(update.hasMessage()){
			var message = update.getMessage();
			var text = message.getText();
			var firstIndex = text.indexOf(" ");
			command = firstIndex < 0 ? text : text.substring(0, text.indexOf(" "));
		} else if(update.hasCallbackQuery()){
			var callbackQuery = update.getCallbackQuery();
			var callbackData = callbackQuery.getData();
			var firstIndex = callbackData.indexOf(" ");
			command = firstIndex < 0 ? callbackData : callbackData.substring(0, callbackData.indexOf(" "));
		}
		if(command != null && command.startsWith("/")){
			int at = command.indexOf('@');
			if(at > -1){
				String commandUserName = command.substring(at+1, command.length());
				command = command.substring(0, at);
				if(!commandUserName.equals(this.botUser.getUserName())){
					log.debug("command pass. command=\"{}\"", command);
					return;
				}
			}
			
			CommandRequest request = new CommandRequest(this.bot, update, command);
			try{
				log.debug("dispatcher.foward(command=\"{}\")", request.getCommandName());
				boolean forwarded = dispatcher.foward(request);
				if(!forwarded && update.hasCallbackQuery()){
					log.error("Callback query handler not found. data={}", request.getUpdate().getCallbackQuery().getData());
					this.sendErrorMessage(request, "Callback query handler not found.");
				}
			} catch(BaseException e){
				log.info("{}. Message={}", e.getSimpleName(), e.getMessage());
				this.sendErrorMessage(request, e);
			} catch(Exception e){
				log.error(String.format("%s. Message=%s", this.getClass().getSimpleName(), e.getMessage()), e);
				this.sendErrorMessage(request, "Interal server error.");
			}
		}
	}

	public void sendErrorMessage(CommandRequest request, BaseException errorException){
		var msg = request.getMessage();
		if(msg == null){
			return;
		}

		var sendMsg = SendMessage.builder()
			.chatId(msg.getChatId())
			.replyToMessageId(msg.getMessageId())
			.parseMode(DEFAULT_PARSEMODE)
			.text(String.format("Error!%n>> message: [%s] %s", errorException.getSimpleName(), errorException.getMessage()))
			.build();
		try {
			this.bot.execute(sendMsg);
		} catch (TelegramApiException e) {
			log.error("sendErrorMessage(): TelegramApiException.", e);
		}
	}

	public void sendErrorMessage(CommandRequest request, String message){
		if(request.getUpdate().hasMessage()){
			var msg = request.getMessage();
			var sendMsg = SendMessage.builder()
				.chatId(msg.getChatId())
				.replyToMessageId(msg.getMessageId())
				.parseMode(DEFAULT_PARSEMODE)
				.text(String.format("Error!%n>> message: %s", message))
				.build();
			try {
				this.bot.execute(sendMsg);
			} catch (TelegramApiException e) {
				log.error("TelegramApiException.", e);
			}
		} else if(request.getUpdate().hasCallbackQuery()){
			var msg = request.getUpdate().getCallbackQuery().getMessage();
			var sendMsg = SendMessage.builder()
				.chatId(msg.getChatId())
				.replyToMessageId(msg.getMessageId())
				.parseMode(DEFAULT_PARSEMODE)
				.text(String.format("Error!%n>> message: %s", message))
				.build();
			try {
				this.bot.execute(sendMsg);
			} catch (TelegramApiException e) {
				log.error("sendErrorMessage(): TelegramApiException.", e);
			}
		}
	}

    private void setMyCommandsByCommandMappings() {
        try{
			var botCommands = this.dispatcher.getCommandMappingList()
				.stream()
				.filter(x->StringUtils.hasText(x.description()))
				.map(x->new BotCommand(x.value(), x.description()))
				.collect(Collectors.toList());

			SetMyCommands setMyCommands = SetMyCommands.builder()
				.commands(botCommands)
				.build();
			this.bot.execute(setMyCommands);
		}catch(Exception e){
			log.error(String.format("setMyCommands(): %s", e.getClass().getName()), e);
		}
    }

}
