package com.ejetool.videoai.chatbot.service;

import org.springframework.stereotype.Service;

import com.ejetool.videoai.chatbot.dto.quote.CreateQuoteParam;
import com.ejetool.videoai.chatbot.dto.quote.CreateQuoteResult;
import com.ejetool.videoai.client.EjetoolVideoAIClient;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class QuoteServiceImpl implements QuoteService {
	private final EjetoolVideoAIClient ejetoolVideoAIClient;

    public CreateQuoteResult createQuote(CreateQuoteParam param){
        return new CreateQuoteResult();
    }   
}
