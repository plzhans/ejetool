package com.ejetool.videoai.domain.entity;

import com.ejetool.common.db.entity.BaseEntity;
import com.ejetool.common.util.JsonUtils;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "openai_use_history")
public class OpenaiUseHistory extends BaseEntity {
    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private long id;

    /**
     * openai request id
     */
    @Column(name = "request_id")
    private String requestId;

    /**
     * openai request model
     */
    @Column(name = "request_model")
    private String requestModel;

    /**
     * openai 사용 model
     */
    @Column(name = "usage_model")
    private String usageModel;

    /**
     * openai 토큰 사용량 Prompt
     */
    @Column(name = "usage_prompt_tokens")
    private int usagePromptTokens;

    /**
     * openai 토큰 사용량 Completion
     */
    @Column(name = "usage_completion_tokens")
    private int usageCompletionTokens;

    /**
     * openai 토큰 사용량 Total
     */
    @Column(name = "usage_total_tokens")
    private int usageTotalTokens;

    /**
     * prompt request
     */
    @Column(name = "prompt_request", columnDefinition = "MEDIUMTEXT")
    private String promptRequest;

    /**
     * prompt response
     */
    @Column(name = "prompt_response", columnDefinition = "MEDIUMTEXT")
    private String promptResponse;

    @Builder
    public OpenaiUseHistory(String requestId, String requestModel, String usageModel, int promptTokens, int completionTokens, int totalTokens) {
        this.requestId = requestId;
        this.requestModel = requestModel;
        this.usageModel = usageModel;
        this.usagePromptTokens = promptTokens;
        this.usageCompletionTokens = completionTokens;
        this.usageTotalTokens = totalTokens;
    }

    public void setRequestAndResponse(String promptRequest, String promptResponse){
        this.promptRequest = promptRequest;
        this.promptResponse = promptResponse;
    }

    public void setRequestAndResponse(Object promptRequest, Object promptResponse){
        this.promptRequest = JsonUtils.convertToString(promptRequest);
        this.promptResponse = JsonUtils.convertToString(promptResponse);
    }
}
