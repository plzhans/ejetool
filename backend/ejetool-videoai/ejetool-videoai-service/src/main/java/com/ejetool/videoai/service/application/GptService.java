package com.ejetool.videoai.service.application;

import com.ejetool.videoai.service.dto.quote.*;

public interface GptService {

    CreateQuotesResult createQuotes(CreateQuotesParam param);

} 