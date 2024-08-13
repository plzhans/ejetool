package com.ejetool.videoai.api.dto.content;

import com.ejetool.videoai.common.content.ContentStatus;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateContentResponse {
    /** 
     * 컨텐츠 id
     */
    private long contentId;

    /**
     * 상태
     */
    private ContentStatus status;
}
