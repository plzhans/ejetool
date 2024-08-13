package com.ejetool.videoai.common.content;

import lombok.Getter;

public enum ContentPublishStatus {
    /** 없음 */
    NONE(0),
    /** 준비 */
    READY(1),
    /** 요청 */
    REQUEST(10),
    /** 처리중 */
    PROCESS(20),
    /** 완료 */
    COMPLETED(30),
    /** 실패 */
    FAILED(40);

    @Getter
    private final int value;

    ContentPublishStatus(int value) {
        this.value = value;
    }
}
