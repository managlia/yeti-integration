package com.yeti.action.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.yeti.action.event.BasicTransactionEvent;

@Component
public class BasicTransactionListener implements ApplicationListener<BasicTransactionEvent> {
    @Override
    public void onApplicationEvent(BasicTransactionEvent event) {
        System.out.println("Received spring custom event - " + event.getMessage());
    }
}