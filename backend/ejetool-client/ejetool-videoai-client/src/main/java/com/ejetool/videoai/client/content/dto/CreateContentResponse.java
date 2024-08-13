package com.ejetool.videoai.client.content.dto;

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
