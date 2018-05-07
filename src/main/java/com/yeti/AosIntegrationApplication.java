package com.yeti;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.hateoas.config.EnableEntityLinks;

@SpringBootApplication
//@EnableEntityLinks
//@EnableAutoConfiguration (exclude = {  DataSourceAutoConfiguration.class })
public class AosIntegrationApplication {
//
//	@PostConstruct
//	void started() {
//		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
//	}
	
	
	public static void main(String[] args) {
		SpringApplication.run(AosIntegrationApplication.class, args);
	}
	
}

