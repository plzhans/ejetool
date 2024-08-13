package com.ejetool.mq.event;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class ApplicationEventMixin {
    @JsonIgnore
    private long timestamp;
}
