package com.ejetool.videoai.api.dto.quotes;

import java.util.HashMap;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateQuotesRequest {
    /**
     * 프롬프트
     */
    private String prompt;

    /**
     * 입력
     */
    private HashMap<String, String> variables;
}
