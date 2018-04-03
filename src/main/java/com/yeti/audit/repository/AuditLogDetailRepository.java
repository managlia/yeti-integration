package com.yeti.audit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.yeti.model.util.AuditLogDetail;

@CrossOrigin
@RepositoryRestResource(collectionResourceRel = "AuditLogDetail", path = "AuditLogDetails")
public interface AuditLogDetailRepository extends JpaRepository<AuditLogDetail, Integer> {  //Entity, Id



}
