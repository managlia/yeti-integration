package com.yeti;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.hateoas.config.EnableEntityLinks;

@SpringBootApplication
@EnableEntityLinks
public class AosIntegrationApplication {

	public static void main(String[] args) {
		SpringApplication.run(AosIntegrationApplication.class, args);
	}
	

}

