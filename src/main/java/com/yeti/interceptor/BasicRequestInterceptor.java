package com.yeti.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public class BasicRequestInterceptor extends HandlerInterceptorAdapter {

	private static final Logger log = LoggerFactory.getLogger(BasicRequestInterceptor.class);
	
	 @Override
	 public boolean preHandle(HttpServletRequest request, 
			HttpServletResponse response, Object object) throws Exception {
/*
		 String checkId = request.getHeader("X-CHECK-ID");
		 log.debug("x-check-id -> " + checkId);
		 
		 
		log.debug("In preHandle we are Intercepting the Request");
		log.debug("____________________________________________");
		String requestURI = request.getRequestURI();
		String name = ServletRequestUtils.getStringParameter(request, "name");
		String description = ServletRequestUtils.getStringParameter(request, "description");
		log.debug("RequestURI::" + requestURI + 
			" || Search for Name with value ::" + name + 
			" || Search for Description with value ::" + description);
		log.debug("____________________________________________");
*/
		return true;
	 
	 }
	 
//		private void seeHeaders( Map<String, String> header) {
//			header.forEach((key, value) -> {
//			    log.debug("Key : " + key + " Value : " + value);
//			});
//		}

}
