package com.ejetool.lib.useapi.dto.midjourney;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class JobImage {
    private String url;
    @JsonProperty("proxy_url")
    private String proxyUrl;
    private int width;
    private int height;
}
