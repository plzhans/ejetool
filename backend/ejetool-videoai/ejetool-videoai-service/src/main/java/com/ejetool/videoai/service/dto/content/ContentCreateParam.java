package com.ejetool.videoai.service.dto.content;

import com.ejetool.videoai.common.content.ContentType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContentCreateParam {
    private ContentType contentType;
    private String title;
    private ContentItemCreateParam[] items;
    private boolean autoGenerateImage;
    private boolean autoGenerateVoice;
}

