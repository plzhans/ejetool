package com.ejetool.mq.event;

public class EmptyEvent extends BaseEvent {

    public EmptyEvent(){
        super("");
    }

    public EmptyEvent(String requestId){
        super(requestId);
    }
}
