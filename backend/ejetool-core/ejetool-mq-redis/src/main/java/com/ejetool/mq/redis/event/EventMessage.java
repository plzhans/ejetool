package com.ejetool.mq.redis.event;

import io.micrometer.common.lang.Nullable;
import lombok.Getter;

@Getter
public class EventMessage {
    
    @Getter
    @Nullable
    private Object data;

    public EventMessage(){
        this.data = null;
    }

    public EventMessage(@Nullable Object data){
        this.data = data;
    }

    public boolean isData() {
        return this.data != null;
    }
}
