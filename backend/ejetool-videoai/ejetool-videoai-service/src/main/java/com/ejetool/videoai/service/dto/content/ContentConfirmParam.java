package com.ejetool.videoai.service.dto.content;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContentConfirmParam {
    /** 컨텐츠 id */
    private long contentId;
    
    /** 수락 여부 */
    private boolean agree;

    /** 발행 */
    private boolean publish;
}

