package com.yeti.audit.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.yeti.audit.service.AuditLogDetailService;
import com.yeti.model.util.AuditLogDetail;

@RestController
public class AuditLogDetailController {

	@Autowired
	private AuditLogDetailService auditLogDetailService;
	
	@RequestMapping(method=RequestMethod.GET, value="/AuditLogDetails")
	public List<AuditLogDetail> getAllAuditLogDetails() {
		return auditLogDetailService.getAllAuditLogDetails();
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/AuditLogDetails/{id}")
	public AuditLogDetail getAuditLogDetail(@PathVariable Integer id) {
		return auditLogDetailService.getAuditLogDetail(id);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/AuditLogDetails")
	public void addAuditLogDetail(@RequestBody AuditLogDetail auditLogDetail) {
		auditLogDetailService.addAuditLogDetail(auditLogDetail);
	}
	
	@RequestMapping(method=RequestMethod.PUT, value="/AuditLogDetails/{id}")
	public void updateAuditLogDetail(@RequestBody AuditLogDetail auditLogDetail, @PathVariable Integer id) {
		auditLogDetailService.updateAuditLogDetail(id, auditLogDetail);
	}
	
	@RequestMapping(method=RequestMethod.DELETE, value="/AuditLogDetails/{id}")
	public void deleteAuditLogDetail(@RequestBody AuditLogDetail auditLogDetail, @PathVariable Integer id) {
		auditLogDetailService.deleteAuditLogDetail(id, auditLogDetail);
	}
	
	
}








