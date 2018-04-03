package com.yeti.action.event;

import org.springframework.context.ApplicationEvent;

public class BasicTransactionEvent extends ApplicationEvent {
    private String message;
 
    public BasicTransactionEvent(Object source, String message) {
        super(source);
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
}