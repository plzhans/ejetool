package com.ejetool.videoai.common.content;

import lombok.Getter;

public enum ContentItemStatus {
    /** 없음 */
    NONE(0),
    /** 준비 */
    READY(1),
    /** 요청 */
    REQUEST(10),
    /** 처리중 */
    PROCESS(20),
    /** 확인중 */
    CONFIRM(30),
    /** 가잘 */
    REJECT(40),
    /** 완료 */
    COMPLETED(50),
    /** 실패 */
    FAILED(60),
    /** 조정됨 */
    MODERATED(61);

    @Getter
    private final int value;

    ContentItemStatus(int value) {
        this.value = value;
    }
}

