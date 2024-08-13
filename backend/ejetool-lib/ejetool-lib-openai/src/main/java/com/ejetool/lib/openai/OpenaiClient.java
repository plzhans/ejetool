package com.ejetool.lib.openai;

import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Getter
public class OpenaiClient {

    @Getter
    private final OpenaiSettings settings;

    private OpenaIChatApi chat;

    public OpenaiClient(OpenaiSettings settings){
        this.settings = settings;
    }

    public synchronized OpenaIChatApi chat(){
        if(this.chat == null){
            synchronized (this) {
                if (this.chat == null) {
                    this.chat = new OpenaIChatApi(this.settings);
                }
            }
        }
        return this.chat;
    }
}
