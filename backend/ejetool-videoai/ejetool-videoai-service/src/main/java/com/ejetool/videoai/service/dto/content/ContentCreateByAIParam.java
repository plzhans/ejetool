package com.ejetool.videoai.service.dto.content;

import org.springframework.util.StringUtils;

import com.ejetool.videoai.common.content.ContentType;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;

import lombok.Getter;
import lombok.Setter;

public class ContentCreateByAIParam {
    @Getter @Setter
    private String requestId;
    
    @Getter @Setter
    @JsonProperty("content_type")
    private ContentType contentType;

    @Getter @Setter
    private String subject;

    @Getter @Setter
    private String gptModel;

    @Getter @Setter
    private HashMap<String, String> gptVariables;

    @Getter @Setter
    private boolean cacheDisabled = false;

    @Getter @Setter
    private Long telegramChatId;

    @Getter @Setter
    private Integer telegramMessageId;

    public boolean hasGptVariables(){
        return this.gptVariables != null && this.gptVariables.size() > 0;
    }

    public boolean isGptVariable(String name){
        if(this.gptVariables != null && this.gptVariables.size() > 0){
            return this.gptVariables.containsKey(name);    
        }
        return false;
    }

    public int getGptVariableAsInt(String name){
        var value = this.gptVariables.get(name);
        if(value == null){
            return 0;
        }

        try {
            return Integer.parseInt(value);
        } catch(NumberFormatException e){
            return 0;
        }
    }

    public void addGptVariable(String name, String value){
        if(this.gptVariables == null){
            this.gptVariables = new HashMap<>();
        }
        this.gptVariables.put(name, value);
    }

    public boolean isTelegramMessage(){
        return this.telegramChatId != 0 && this.telegramMessageId != 0;
    }
}
