package com.yeti.audit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.yeti.model.util.AuditLog;

@CrossOrigin
@RepositoryRestResource(collectionResourceRel = "AuditLog", path = "AuditLogs")
public interface AuditLogRepository extends JpaRepository<AuditLog, Integer> {  //Entity, Id



}
