package com.yeti.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public class BasicRequestInterceptor extends HandlerInterceptorAdapter {

	
	
	 @Override
	 public boolean preHandle(HttpServletRequest request, 
			HttpServletResponse response, Object object) throws Exception {
		System.out.println("In preHandle we are Intercepting the Request");
		System.out.println("____________________________________________");
		String requestURI = request.getRequestURI();
		String name = ServletRequestUtils.getStringParameter(request, "name");
		String description = ServletRequestUtils.getStringParameter(request, "description");
		System.out.println("RequestURI::" + requestURI + 
				" || Search for Name with value ::" + name + 
				" || Search for Description with value ::" + description);
		System.out.println("____________________________________________");
		return true;
	 
	 }
	 
		private void seeHeaders( Map<String, String> header) {
			header.forEach((key, value) -> {
			    System.out.println("Key : " + key + " Value : " + value);
			});
		}

}
