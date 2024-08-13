package com.ejetool.lib.telegram.handler;

import org.springframework.util.StringUtils;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage.SendMessageBuilder;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText.EditMessageTextBuilder;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.objects.User;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommandRequest {

    static final String DEFAULT_PARSEMODE = ParseMode.MARKDOWN;

    @Getter
    private final DefaultAbsSender sender;

    @Getter
    private final Update update;
    
    @Getter
    private final String commandName;

    private String commandArg;

    public CommandRequest(DefaultAbsSender sender, Update update, String command){
        this.sender = sender;
        this.update = update;
        this.commandName = command;
    }

    public boolean isCommandArg(){
        return StringUtils.hasText(this.getCommandArg());
    }

    public String[] getCommandArgs(){
        return this.getCommandArg().split(" ");
    }

    public String[] getCommandArgs(int limit){
        return this.getCommandArg().split(" ", limit);
    }

    public String getCommandArg(){
        if(this.commandArg == null){
            String text = null;
            if(update.hasMessage()){
                text = update.getMessage().getText();
            } else if(update.hasCallbackQuery()){
                text = update.getCallbackQuery().getData();
            }
            if(text != null){
                var first = text.indexOf(' ');
                if(first > -1){
                    var argText = text.substring(first + 1);
                    this.commandArg = argText;
                } else {
                    this.commandArg = "";
                }
            } else {
                this.commandArg = "";
            }
        }
        return this.commandArg;
    }

    public User getFrom(){
        if(this.update.hasMessage()){
            return this.update.getMessage().getFrom();
        }
        else if(this.update.hasCallbackQuery()){
            return this.update.getCallbackQuery().getFrom();
        }
        return null;
    }

    @Override
    public String toString() {
        return String.format("CommandRequest[command=\"%s\"]", commandName);
    }

    public Message getMessage(){
        return this.update.getMessage();
    }

    public Long getChatId(){
        if(this.update.hasMessage()){
            return this.update.getMessage().getChatId();
        } else if(this.update.hasCallbackQuery()){
            return this.update.getCallbackQuery().getMessage().getChatId();
        } 
        return null;
    }

    public Integer getMessageId(){
        if(this.update.hasMessage()){
            return this.update.getMessage().getMessageId();
        } else if(this.update.hasCallbackQuery()){
            return this.update.getCallbackQuery().getMessage().getMessageId();
        } 
        return null;
    }

    public String getMessageText() {
        if(this.update.hasMessage()){
            return this.update.getMessage().getText();
        } else if(this.update.hasCallbackQuery()){
            return null;
        } 
        return null;
    }

    public SendMessageBuilder createSendMessage(String format, Object... args) {
        return this.createSendMessage(String.format(format, args));
    }

    public SendMessageBuilder createSendMessage(String text) {
        return SendMessage.builder()
            .chatId(this.getChatId())
            .parseMode(DEFAULT_PARSEMODE)
            .text(text);
    }

    public SendMessageBuilder createReplyMessage(String format, Object... args) {
        return this.createReplyMessage(String.format(format, args));
    }

    public SendMessageBuilder createReplyMessage(String text) {
        return SendMessage.builder()
            .chatId(this.getChatId())
            .replyToMessageId(this.getMessageId())
            .parseMode(DEFAULT_PARSEMODE)
            .text(text);
    }

    public EditMessageTextBuilder createEditMessage(Integer messageId, String format, Object... args) {
        return this.createEditMessage(messageId, String.format(format, args));
    }

    public EditMessageTextBuilder createEditMessage(Integer messageId, String text) {
        return EditMessageText.builder()
            .chatId(this.getChatId())
            .messageId(messageId)
            .parseMode(DEFAULT_PARSEMODE)
            .text(text);
    }

    public EditMessageTextBuilder createEditMessage(String format, Object... args) {
        return this.createEditMessage(String.format(format, args));
    }

    public EditMessageTextBuilder createEditMessage(String text) {
        return EditMessageText.builder()
            .chatId(this.getChatId())
            .messageId(this.getMessageId())
            .parseMode(DEFAULT_PARSEMODE)
            .text(text);
    }

    public boolean sendMessage(String format, Object ...args){
        return this.sendMessage(String.format(format, args));
    }

    public boolean sendMessage(String text){
        var newMsg = SendMessage.builder()
            .chatId(this.getChatId())
            .parseMode(DEFAULT_PARSEMODE)
            .text(text)
            .build();
        try {
            this.sender.execute(newMsg);
            return true;
        } catch (TelegramApiException e) {
            log.error("sendMessage(): TelegramApiException.", e);
        }
        return false;
    }

    public boolean sendReplyMessage(String format, Object ...args){
        return this.sendReplyMessage(String.format(format, args));
    }

    public boolean sendReplyMessage(String text){
        var replyMsg = SendMessage.builder()
            .chatId(this.getChatId())
            .replyToMessageId(this.getMessageId())
            .parseMode(DEFAULT_PARSEMODE)
            .text(text)
            .build();
        try {
            this.sender.execute(replyMsg);
            return true;
        } catch (TelegramApiException e) {
            log.error("sendReplyMessage(): TelegramApiException.", e);
        }
        return false;
    }

    public boolean sendEditMessage(String format, Object... args){
        return this.sendEditMessage(String.format(format, args));
    }

    public boolean sendEditMessage(String text){
        var editMsg = EditMessageText.builder()
            .chatId(this.getChatId())
            .messageId(this.getMessageId())
            .parseMode(DEFAULT_PARSEMODE)
            .text(text)
            .build();
        try {
            this.sender.execute(editMsg);
            return true;
        } catch (TelegramApiException e) {
            log.error("sendEditMessage(): Teleg8ramApiException.", e);
        }
        return false;
    }

    public boolean sendAnswerCallbackQuery(String text, boolean showAlert, int cacheTime){
        AnswerCallbackQuery answer = new AnswerCallbackQuery();
        answer.setCallbackQueryId(this.update.getCallbackQuery().getId());
        answer.setText(text);
        answer.setShowAlert(showAlert);
        answer.setCacheTime(cacheTime);
        try {
            return this.sender.execute(answer);
        } catch (TelegramApiException e) {
            log.error("sendAnswerCallbackQuery(): TelegramApiException.", e);
        }
        return false;
    }

    public boolean hasMessage(){
        return this.update.hasMessage();
    }

    public boolean hasCallbackQuery(){
        return this.update.hasCallbackQuery();
    }

    public String getCallbackQueryId(){
        if(this.update.hasCallbackQuery()){
            return this.update.getCallbackQuery().getId();
        }
        return null;
    }
}
