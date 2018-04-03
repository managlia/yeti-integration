package com.yeti.core.types.controller;

import org.joda.time.LocalTime;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LocalTimeController {
	
	@RequestMapping("/localtime")
	public String getCurrentTime() {
        LocalTime localTime = new LocalTime();
        return "Local time is " + localTime;
	}

}
