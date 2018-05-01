package com.yeti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.yeti.interceptor.BasicRequestInterceptor;
import com.yeti.interceptor.TenantInterceptor;


@Configuration
public class AppConfig extends WebMvcConfigurerAdapter {

	@Autowired
	BasicRequestInterceptor basicRequestInterceptor;

	@Autowired
	TenantInterceptor tenantInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(basicRequestInterceptor);
		registry.addInterceptor(tenantInterceptor);
	}
}