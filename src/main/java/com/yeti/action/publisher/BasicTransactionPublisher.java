package com.yeti.action.publisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

import com.yeti.action.event.BasicTransactionEvent;

public class BasicTransactionPublisher {

	@Autowired
    private ApplicationEventPublisher applicationEventPublisher;
 
    public void doStuffAndPublishAnEvent(final String message) {
    	/* ONLY AN EXAMPLE OF HOW TO PUBLISH AN EVENT */
    	System.out.println("Publishing custom event. ");
        BasicTransactionEvent transactoinEvent = new BasicTransactionEvent(this, message);
        applicationEventPublisher.publishEvent(transactoinEvent);
    }
}