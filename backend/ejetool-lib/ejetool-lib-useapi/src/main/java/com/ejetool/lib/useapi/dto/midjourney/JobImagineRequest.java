package com.ejetool.lib.useapi.dto.midjourney;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JobImagineRequest {
    @JsonProperty("prompt")
    private String prompt;
    
    @JsonProperty("discord")
    private String discordToken;

    @JsonProperty("server")
    private String discordServer;

    @JsonProperty("channel")
    private String discordChannel;

    @JsonProperty("maxJobs")
    private int maxJobs;

    @JsonProperty("replyUrl")
    private String replyUrl;

    @JsonProperty("replyRef")
    private String replyRef;
}
