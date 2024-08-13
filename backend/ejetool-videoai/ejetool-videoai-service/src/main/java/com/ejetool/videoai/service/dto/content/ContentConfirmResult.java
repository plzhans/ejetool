package com.ejetool.videoai.service.dto.content;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContentConfirmResult {
    /** 
     * 컨텐츠 id
     */
    private long contentId;
    
    /** 
     * 수락 여부
     */
    private boolean agree;

    @Builder
    public ContentConfirmResult(long contentId, boolean agree){
        this.contentId = contentId;
        this.agree = agree;
    }
}

