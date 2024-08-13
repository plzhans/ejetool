package com.ejetool.videoai.service.dto.quote;

import java.util.HashMap;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateQuotesParam {
     /**
     * 프롬프트
     */
    private String prompt;

    /**
     * 입력
     */
    private HashMap<String, String> variables;

    /**
     * 최종 프롬프트
     * @return
     */
    public String getFinalPrompt(){
        return this.prompt;
    }
}
