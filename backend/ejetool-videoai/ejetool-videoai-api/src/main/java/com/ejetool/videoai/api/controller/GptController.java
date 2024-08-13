
package com.ejetool.videoai.api.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ejetool.videoai.api.dto.quotes.*;
import com.ejetool.videoai.api.mapper.GptMapper;
import com.ejetool.videoai.service.application.GptService;

import lombok.RequiredArgsConstructor;

/**
 * 컨텐츠 컨트롤러
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/gpt")
public class GptController {

    private final GptService service;
    private final GptMapper mapper;

    /**
     * 컨텐츠 자동 생성
     * @param request 요청
     * @return 응답
     */
    @PostMapping("/quotes")
    public CreateQuotesResponse createQuotes(@RequestBody CreateQuotesRequest request) {
        var param = mapper.from(request);

        var result = service.createQuotes(param);

        return mapper.from(result);
    }

    
}
