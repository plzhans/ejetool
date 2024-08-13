package com.ejetool.videoai.chatbot.runner;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import com.ejetool.lib.telegram.TelegramPollinBot;
import com.ejetool.lib.telegram.handler.CommandDispatcher;
import com.ejetool.lib.telegram.util.TelegramUtils;

import lombok.extern.slf4j.Slf4j;

import com.ejetool.lib.telegram.TelegramHandler;

@Slf4j
public class TelegramContextBotRunner implements CommandLineRunner{

    private final TelegramPollinBot telegramPollinBot;
    private final TelegramHandler telegramHandler;

    @Value("${spring.application.name:app}")
    private String appName;


    public TelegramContextBotRunner(TelegramPollinBot telegramPollinBot, CommandDispatcher dispatcher){
        this.telegramPollinBot = telegramPollinBot;
        this.telegramHandler = new TelegramHandler(this.telegramPollinBot, dispatcher);
    }

    @Override
    public void run(String... args) throws Exception {
        this.telegramHandler.initialize();
        this.telegramPollinBot.setCallbackUpdateReceived((bot, update) -> 
            telegramHandler.foward(update)
        );

        this.telegramPollinBot.sendMessage(
            String.format("[%s] LongPollingBot start.", TelegramUtils.toSafeText(this.appName)),
            builder->builder.disableNotification(true)
        );

        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(telegramPollinBot);
        log.info("telegramPollinBot start.");
    }
     
}
