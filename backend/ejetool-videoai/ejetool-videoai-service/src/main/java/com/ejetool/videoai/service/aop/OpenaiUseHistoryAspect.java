package com.ejetool.videoai.service.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.ejetool.lib.openai.dto.OpenaiResponse;
import com.ejetool.lib.openai.dto.chat.ChatCompletionRequest;
import com.ejetool.lib.openai.dto.chat.ChatCompletionResponse;
import com.ejetool.videoai.domain.entity.OpenaiUseHistory;
import com.ejetool.videoai.domain.repository.OpenaiHistoryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class OpenaiUseHistoryAspect {

    private final OpenaiHistoryRepository repository;
    
    @Around("execution(* com.ejetool.lib.openai.OpenaiChatAPI.sendCompletion(..))")
    public Object repositorySave(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        Object[] args = joinPoint.getArgs();
        if (args.length > 0 
            && args[0] instanceof ChatCompletionRequest request 
            && result instanceof OpenaiResponse<?> openaiResponse 
        ){
            if(openaiResponse.isOk()){
                var openaiResult = openaiResponse.getResult();
                if (openaiResult instanceof ChatCompletionResponse response) {
                    var usage = response.getUsage();
                    OpenaiUseHistory useHistory = OpenaiUseHistory.builder()
                        .requestId(request.getRequestId())
                        .requestModel(request.getModel())
                        .usageModel(response.getModel())
                        .promptTokens(usage.getPromptTokens())
                        .completionTokens(usage.getCompletionTokens())
                        .totalTokens(usage.getTotalTokens())
                        .build();
                    useHistory.setRequestAndResponse(request.getMessages(), response.getChoices());
                    this.repository.save(useHistory);
                }
            } else {
                String method = joinPoint.getSignature().toShortString();
                log.error("{}(): fail. requestId={}, model={}", method, request.getRequestId(), request.getModel());
            }
        }
        
        return result;
    }
}
