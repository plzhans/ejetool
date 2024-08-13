package com.ejetool.videoai.service.application;

import org.springframework.stereotype.Service;

import com.ejetool.videoai.service.dto.quote.CreateQuotesParam;
import com.ejetool.videoai.service.dto.quote.CreateQuotesResult;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class GptServiceImpl implements GptService{

    @Override
    public CreateQuotesResult createQuotes(CreateQuotesParam param) {

        //openAIClient.prompt(finalPrompt);

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createQuotes'");
    }

    
} 