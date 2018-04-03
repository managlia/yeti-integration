package com.yeti.audit.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.yeti.audit.service.AuditLogService;
import com.yeti.model.util.AuditLog;

@RestController
public class AuditLogController {

	@Autowired
	private AuditLogService auditLogService;
	
	@RequestMapping(method=RequestMethod.GET, value="/AuditLogs")
	public List<AuditLog> getAllAuditLogs() {
		return auditLogService.getAllAuditLogs();
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/AuditLogs/{id}")
	public AuditLog getAuditLog(@PathVariable Integer id) {
		return auditLogService.getAuditLog(id);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/AuditLogs")
	public void addAuditLog(@RequestBody AuditLog auditLog) {
		auditLogService.addAuditLog(auditLog);
	}
	
	@RequestMapping(method=RequestMethod.PUT, value="/AuditLogs/{id}")
	public void updateAuditLog(@RequestBody AuditLog auditLog, @PathVariable Integer id) {
		auditLogService.updateAuditLog(id, auditLog);
	}
	
	@RequestMapping(method=RequestMethod.DELETE, value="/AuditLogs/{id}")
	public void deleteAuditLog(@RequestBody AuditLog auditLog, @PathVariable Integer id) {
		auditLogService.deleteAuditLog(id, auditLog);
	}
	
	
}








