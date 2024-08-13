package com.ejetool.videoai.service.dto.content;

import lombok.Getter;
import lombok.Setter;

public class UpdateContentItemParam {
    @Getter @Setter
    private long itemId;

    @Getter 
    private Long contentId;

    @Getter @Setter
    private Long telegramChatId;

    @Getter @Setter
    private Integer telegramMessageId;

    @Getter @Setter
    private String imagePrompt;

    public void setContentId(Long contentId){
        this.contentId = contentId;
    }

    public void setContentId(String contentId){
        try{
            this.contentId = Long.valueOf(contentId);
        } catch(Exception e){
            // Nothing
        }
    }
}
