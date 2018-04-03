package com.yeti.audit.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeti.model.util.AuditLogDetail;
import com.yeti.audit.repository.AuditLogDetailRepository;

@Service
public class AuditLogDetailService {
	
	@Autowired
	private AuditLogDetailRepository auditLogDetailRepository;
	
	public List<AuditLogDetail> getAllAuditLogDetails() {
		List<AuditLogDetail> records = new ArrayList<AuditLogDetail>();
		auditLogDetailRepository.findAll().forEach(records::add);
		return records;
	}
	
	public AuditLogDetail getAuditLogDetail(Integer id) {
		return auditLogDetailRepository.findOne(id);
	}
	
	public void addAuditLogDetail(AuditLogDetail auditLogDetail) {
		auditLogDetailRepository.save(auditLogDetail);
	}

	public void updateAuditLogDetail(Integer id, AuditLogDetail auditLogDetail) {
		auditLogDetailRepository.save(auditLogDetail);
	}

	public void deleteAuditLogDetail(Integer id, AuditLogDetail auditLogDetail) {
		auditLogDetailRepository.delete(id);
	}
	
}
