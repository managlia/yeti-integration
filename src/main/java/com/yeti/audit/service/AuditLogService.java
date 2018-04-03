package com.yeti.audit.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeti.model.util.AuditLog;
import com.yeti.audit.repository.AuditLogRepository;

@Service
public class AuditLogService {
	
	@Autowired
	private AuditLogRepository auditLogRepository;
	
	public List<AuditLog> getAllAuditLogs() {
		List<AuditLog> records = new ArrayList<AuditLog>();
		auditLogRepository.findAll().forEach(records::add);
		return records;
	}
	
	public AuditLog getAuditLog(Integer id) {
		return auditLogRepository.findOne(id);
	}
	
	public void addAuditLog(AuditLog auditLog) {
		auditLogRepository.save(auditLog);
	}

	public void updateAuditLog(Integer id, AuditLog auditLog) {
		auditLogRepository.save(auditLog);
	}

	public void deleteAuditLog(Integer id, AuditLog auditLog) {
		auditLogRepository.delete(id);
	}
	
}
