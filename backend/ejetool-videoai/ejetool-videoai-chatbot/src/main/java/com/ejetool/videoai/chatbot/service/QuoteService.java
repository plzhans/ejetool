package com.ejetool.videoai.chatbot.service;

import com.ejetool.videoai.chatbot.dto.quote.CreateQuoteParam;
import com.ejetool.videoai.chatbot.dto.quote.CreateQuoteResult;

public interface QuoteService {
    CreateQuoteResult createQuote(CreateQuoteParam text);
}
