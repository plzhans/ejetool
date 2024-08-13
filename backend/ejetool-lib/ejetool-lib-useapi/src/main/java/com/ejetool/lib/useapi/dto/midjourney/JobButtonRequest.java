package com.ejetool.lib.useapi.dto.midjourney;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JobButtonRequest {
    @JsonProperty("jobid")
    private String jobId;

    @JsonProperty("button")
    private String button;

    @JsonProperty("prompt")
    private String prompt;

    @JsonProperty("discord")
    private String discordToken;

    @JsonProperty("maxJobs")
    private int maxJobs;

    @JsonProperty("replyUrl")
    private String replyUrl;

    @JsonProperty("replyRef")
    private String replyRef;
}
