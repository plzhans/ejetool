package com.ejetool.lib.telegram;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.telegram.telegrambots.bots.DefaultBotOptions;

import lombok.Getter;

@ConfigurationProperties
public class TelegramBotSettings{
    @Getter @Value("${telegram.bot.username:ejetool-bot}")
    private String botUsername;
    
    @Getter @Value("${telegram.bot.token}")
    private String botToken;

    @Getter @Value("${telegram.bot.default_chat_id}")
    private Long botDefaultChatId;

    @Getter @Value("${telegram.bot.admin_ids:}")
    private long[] botAdminIds;

    private DefaultBotOptions options;

    public DefaultBotOptions getOptions(){
        if(this.options == null){
            this.options = new DefaultBotOptions();
        }
        return this.options;
    }
}
