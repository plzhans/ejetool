package com.ejetool.lib.useapi.dto.midjourney;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class JobAttachment {
    private String id;
    @JsonProperty("content_type")
    private String contentType;
    private String filename;
    private String url;
    @JsonProperty("proxy_url")
    private String proxyUrl;
    private int size;
    private int width;
    private int height;
}
