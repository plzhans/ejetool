package com.ejetool.lib.telegram.helper;

import java.util.function.Supplier;

import org.springframework.util.StringUtils;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TelegramChatNotify {

    static final String DEFAULT_PARSEMODE = ParseMode.MARKDOWN;

    @Getter
    private final DefaultAbsSender telegramSender;

    @Getter
    private Long chatId;

    @Getter
    private String prefixText;

    @Getter
    private Integer messageId;

    @Getter
    private String callbackQueryId;

    @Setter
    @Getter
    private InlineKeyboardMarkup defaultReplyKeyboard;

    public static TelegramChatNotify createAndSend(DefaultAbsSender sender, Long chatId, String format, Object ...args){
        return createAndSend(sender, chatId, String.format(format, args));
    }

    public static TelegramChatNotify createAndSend(DefaultAbsSender sender, Long chatId, String text){
        var newMsg = SendMessage.builder()
            .chatId(chatId)
            .parseMode(DEFAULT_PARSEMODE)
            .text(text)
            .build();
        try {
            Message msg = sender.execute(newMsg);
            var notify = new TelegramChatNotify(sender, chatId, msg.getMessageId());
        return notify;
        } catch (TelegramApiException e) {
            log.error("createAndSend(): TelegramApiException.", e);
            var notify = new TelegramChatNotify(sender, chatId);
            return notify;
        }
        
    }

    public static TelegramChatNotify createAndSend(DefaultAbsSender sender, SendMessage sendMessage){
        try {
            Message msg = sender.execute(sendMessage);
            var notify = new TelegramChatNotify(sender, Long.parseLong(sendMessage.getChatId()), msg.getMessageId());
            return notify;
        } catch (TelegramApiException e) {
            log.error("createAndSend(): TelegramApiException.", e);
            var notify = new TelegramChatNotify(sender, Long.parseLong(sendMessage.getChatId()));
            return notify;
        }
        
    }

    public TelegramChatNotify(DefaultAbsSender telegramBot, Long chatId){
        this.telegramSender = telegramBot;
        this.chatId = chatId;
    }

    @Builder
    public TelegramChatNotify(DefaultAbsSender telegramBot, Long chatId, Integer messageId){
        this.telegramSender = telegramBot;
        this.chatId = chatId;
        this.messageId = messageId;
    }

    public boolean isMessage(){
        return this.messageId != null;
    }

    public void init(Long chatId, Integer messageId) {
        if(chatId != null){
            this.chatId = chatId;
            this.messageId = messageId;
        }
    }

    public boolean isDefaultReplyKeyboard(){
        return this.defaultReplyKeyboard != null;
    }

    public void setPrefixText(String text, Object ...args){
        this.prefixText = String.format(text, args);
    }

    public void setPrefixText(String prefix){
        this.prefixText = prefix;
    }

    public TelegramChatNotify setCallbackQuery(String callbackQueryId){
        this.callbackQueryId = callbackQueryId;
        return this;
    }

    public void send(String text) {
        this.send(text, this.defaultReplyKeyboard);
    }

    public void send(String text, InlineKeyboardMarkup inlineKeyboardMarkup) {
        if(this.chatId == null){
            return;
        }
        
        String finalText = StringUtils.hasText(this.prefixText) ? String.join("\n", this.prefixText, text) : text;

        if(this.messageId != null){
            var editMessage = EditMessageText.builder()
                .chatId(this.chatId)
                .messageId(this.messageId)
                .replyMarkup(inlineKeyboardMarkup)
                .parseMode(DEFAULT_PARSEMODE)
                .text(finalText)
                .build();
            try {
                this.telegramSender.execute(editMessage);
            } catch (TelegramApiException e) {
                log.error("send(): TelegramApiException.", e);
            }
        } else {
            var sendMessage = SendMessage.builder()
                .chatId(this.chatId)
                .parseMode(DEFAULT_PARSEMODE)
                .text(finalText)
                .build();
            try {
                var newMsg = this.telegramSender.execute(sendMessage);
                this.messageId = newMsg.getMessageId();
            } catch (TelegramApiException e) {
                log.error("send(): TelegramApiException.", e);
            }
        }
    }

    public void sendReply(String text, InlineKeyboardMarkup inlineKeyboardMarkup) {
        if(this.chatId == null){
            return;
        }
        if(this.messageId == null){
            return;
        }
        
        var sendMessage = SendMessage.builder()
            .chatId(this.chatId)
            .replyToMessageId(messageId)
            .parseMode(DEFAULT_PARSEMODE)
            .text(text)
            .replyMarkup(inlineKeyboardMarkup)
            .build();
        try {
            var newMsg = this.telegramSender.execute(sendMessage);
            this.messageId = newMsg.getMessageId();
        } catch (TelegramApiException e) {
            log.error("sendReply(): TelegramApiException.", e);
        }
    }

    public void emptyThenSend(Supplier<String> supplier) {
        if(this.isMessage()){
            return;
        }
        if(this.chatId == null){
            return;
        }

        var text = supplier.get();
        SendMessage sendMessage = SendMessage.builder()
            .chatId(this.chatId)
            .parseMode(DEFAULT_PARSEMODE)
            .text(text)
            .build();
        try {
            var sended = this.telegramSender.execute(sendMessage);
            this.messageId = sended.getMessageId();
        } catch (TelegramApiException e) {
            log.error("emptyThenSend(): TelegramApiException.", e);
        }
    }

    public void delete() {
        if(this.chatId == null || this.messageId == null){
            return;
        }

        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(this.chatId);
        deleteMessage.setMessageId(this.messageId);
        try {
            this.telegramSender.execute(deleteMessage);
            this.messageId = null;
        } catch (TelegramApiException e) {
            log.error("delete(): TelegramApiException.", e);
        }
    }

    public boolean sendAnswerCallbackQuery(String text){
        return this.sendAnswerCallbackQuery(text, false, 5);
    }

    public boolean sendAnswerCallbackQuery(String text, boolean showAlert){
        return this.sendAnswerCallbackQuery(text, showAlert, 5);
    }

    public boolean sendAnswerCallbackQuery(String text, boolean showAlert, int cacheTime){
        if(this.callbackQueryId != null){
            AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
            answerCallbackQuery.setCallbackQueryId(this.callbackQueryId);
            answerCallbackQuery.setText(text);
            answerCallbackQuery.setShowAlert(showAlert);
            answerCallbackQuery.setCacheTime(cacheTime);
            boolean executed;
            try {
                executed = this.telegramSender.execute(answerCallbackQuery);
                if(executed){
                    this.delete();
                }
                return executed;
            } catch (TelegramApiException e) {
                log.error("sendAnswerCallbackQuery(): TelegramApiException.", e);
                return false;
            }
        }
        this.send(text);
        return false;
    }

}
