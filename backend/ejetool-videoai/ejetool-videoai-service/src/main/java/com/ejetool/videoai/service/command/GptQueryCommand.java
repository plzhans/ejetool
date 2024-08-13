package com.ejetool.videoai.service.command;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ejetool.common.util.JsonUtils;
import com.ejetool.lib.openai.OpenaiClient;
import com.ejetool.lib.openai.OpenaiModelGPT;
import com.ejetool.lib.openai.dto.chat.ChatCompletionRequest;
import com.ejetool.lib.openai.dto.chat.ChatMessage;
import com.ejetool.lib.openai.exceptions.CompletionException;
import com.ejetool.videoai.domain.entity.OpenaiUseHistory;
import com.ejetool.videoai.domain.repository.OpenaiHistoryRepository;
import com.ejetool.videoai.service.dto.prompt.PromptContentResult;
import com.ejetool.videoai.service.exception.GptQueryException;
import com.fasterxml.jackson.core.type.TypeReference;

import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class GptQueryCommand {

    @UtilityClass
    private static class Const {
        static final String CACHE_KEY_CREATE_QUOTES = "gqc:createQuotes";
    }

    private final OpenaiClient openAIClient;
    private final OpenaiHistoryRepository openaiHistoryRepository;

    //public static 

    public static final String PROMPT_CREATE_QUOTES = """
        # 필수 사항
        명언의 주제는 '{{SUBJECT}}'. 
        명언은 갯수는 {{ITEM_COUNT}}개. 
        결과에 따옴표(“), 작은따옴표(‘)는 절대 사용하지마.
        명언, 이름, 출처에는 bar(|) 문자 사용하지마.
        명언에 이름이 같이 있는 경우가 있더라 삭제해줘.
        출처 정보는 연설, 인터뷰 등 자세하게 알려줘.
        한국어 명언 키워드는 한국어 명언에서 단어를 추출하고 콤마(,)로 구분해줘.
        영어 명언 키워드는 영어 명언에서 단어를 추출하고 콤마(,)로 구분해줘.
        영어, 한국어 명언 키워드에는 주제도 포함해줘.
        이미지 묘사 프롬프트는 이미지를 생성할껀데 영어로 작성해야하고 주제, 직업, 명언, 성별이 포함될 수 있게 자세히 묘사해줘.
        이미지에는 텍스트를 쓸수는 없어.
        
        문장은 40자내의 한문장으로 구성되어야해.
        같은 사람이 말한건 중복으로 나오지 않게 해줘.
        명언, 이름, 성별, 출처, 직업은 반드시 있어야 해.
        직업중에 '럭비 선수', '축구 선수' 헷갈리지 마라
        잘못 알려주면 벌점 줄꺼야.
        is[] 갯수는 {{ITEM_COUNT}}개.
        결과물은 plain text.

        # 결과물 예시
        {
            "s", "{한국어 주제}",
            "se", "{영어 주제}",
            "is": [{
                "t": { "q": "{한국어 명언}", "n": "{한국어 이름}", "g":"{한국어 성별}", "r":{한국어 출처}", "j":"{한국어 직업}" },
                "te": { "q": "{영어 명언}", "n": "{영어 이름}", "g":"{영어 성별}", "r":{영어 출처}", "j":"{영어 직업}" },
                "tk": "{한국어 명언 키워드}",
                "tke": "{영어 명언 키워드}",
                "ip": "{이미지 묘사 프롬프트}"
            }]
        """;

    /*  */
    @Cacheable(
        value = Const.CACHE_KEY_CREATE_QUOTES,
        key = "T(com.ejetool.common.crypto.CryptoUtil).toSHA256String(#model + ':' + #subject)",
        condition = "#cacheDisabled == false"
    )
    @CachePut(
        value = Const.CACHE_KEY_CREATE_QUOTES,
        key = "T(com.ejetool.common.crypto.CryptoUtil).toSHA256String(#model + ':' + #subject)",
        condition = "#cacheDisabled == true"
    )
    public PromptContentResult createQuotes(String requestId, String subject, String model, boolean cacheDisabled){
        int itemCount = 7;
        var finalPrompt = PROMPT_CREATE_QUOTES;
        finalPrompt = finalPrompt.replace("{{SUBJECT}}", subject)
            .replace("{{ITEM_COUNT}}", String.valueOf(itemCount));

        if(!StringUtils.hasText(model)){
            model = OpenaiModelGPT.GPT_3_5_TURBO;
        }

        final String SYSTEM_PROMPT = "너는 현대인의 심리를 치료해주는 심리박사이고 마음을 울리게하고 감동을 주는 유명한 연설가.";
        var request = new ChatCompletionRequest(model, new ChatMessage[]{
            ChatMessage.createBySystem(SYSTEM_PROMPT),
            ChatMessage.createByUser(finalPrompt)
        });
        request.setRequestId(requestId);
    
        var chatGptRes = this.openAIClient.chat().sendCompletion(request);
        if(!chatGptRes.isOk()){
            log.error("openAIClient.chat().sendCompletion(): Error. Status={}, Message={}", chatGptRes.getStatus(), chatGptRes.getMessage());
            throw new CompletionException("openai.chat.sendCompletion()");
        }
        var content = chatGptRes.getResult().getFirstContent();

        OpenaiUseHistory useHistory = OpenaiUseHistory.builder()
            .requestId(request.getRequestId())
            .requestModel(request.getModel())
            .usageModel(chatGptRes.getResult().getModel())
            .promptTokens(chatGptRes.getResult().getUsage().getPromptTokens())
            .completionTokens(chatGptRes.getResult().getUsage().getCompletionTokens())
            .totalTokens(chatGptRes.getResult().getUsage().getTotalTokens())
            .build();
        useHistory.setRequestAndResponse(request.getMessages(), chatGptRes.getResult().getChoices());
        this.openaiHistoryRepository.save(useHistory);

        var result = JsonUtils.convertToObjectOrNull(content, new TypeReference<PromptContentResult>(){});
        if(result == null){
            throw new GptQueryException("result empty.");
        }
        if(result.getItems().size() != itemCount){
            throw new GptQueryException("result.items size error. size="+itemCount);
        }
        result.getItems().stream().forEach(x->{
            if(!x.getText().isValid()){
                throw new GptQueryException("split count is wrong. item.text");
            }
            if(!x.getTextEn().isValid()){
                throw new GptQueryException("split count is wrong. item.text_en");
            }
        });
        return result;
    }
}