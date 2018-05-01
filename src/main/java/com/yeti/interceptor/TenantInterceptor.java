package com.yeti.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.yeti.TenantContext;

@Component
public class TenantInterceptor extends HandlerInterceptorAdapter {

	private static final Logger log = LoggerFactory.getLogger(TenantInterceptor.class);
    
	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
		
		String checkId = request.getHeader("X-CHECK-ID");
		String tenantId = request.getHeader("X-HOST-ID");
		log.debug("x-check-id -> " + checkId);
		log.debug("x-tenantId-id -> " + tenantId);
		if( checkId != null && checkId.length() > 0 ) {
		    TenantContext.setCurrentUser(new Integer(checkId));
		}
		if( tenantId != null && tenantId.length() > 0 ) {
			TenantContext.setCurrentTenant(new Integer(tenantId));
		}
		return true;
    }
    
	@Override
    public void postHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
            throws Exception {
		log.debug("dfm in tenant context clear");
        TenantContext.clear();
    }
	
}