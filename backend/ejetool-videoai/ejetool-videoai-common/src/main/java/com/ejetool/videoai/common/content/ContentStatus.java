package com.ejetool.videoai.common.content;

import lombok.Getter;

public enum ContentStatus {
    /** 없음 */
    NONE(0),
    /** 준비 */
    READY(1),
    /** 요청 */
    REQUEST(10),
    /** 처리중 */
    PROCESS(20),
    /** 생성됨 */
    CREATED(30),
    /** 실패 */
    FAILED(40);

    @Getter
    private final int value;

    ContentStatus(int value) {
        this.value = value;
    }
}
