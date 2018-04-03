package com.yeti.core.types.service;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeti.core.repository.types.ScopeTypeRepository;
import com.yeti.model.general.ScopeType;
import com.yeti.model.util.Batch;

@Service
public class ScopeTypeService {
	
	@Autowired
	private ScopeTypeRepository scopeTypeRepository;
	
	public List<ScopeType> getAllScopeTypes() {
		List<ScopeType> scopeTypes = new ArrayList<ScopeType>();
		scopeTypeRepository.findAll().forEach(scopeTypes::add);
		return scopeTypes;
	}
	
	public ScopeType getScopeType(String id) {
		return scopeTypeRepository.findOne(id);
	}
	
	public ScopeType addScopeType(ScopeType scopeType) {
		return scopeTypeRepository.save(scopeType);
	}

	public ScopeType updateScopeType(String id, ScopeType scopeType) {
		return scopeTypeRepository.save(scopeType);
	}

	public void deleteScopeType(String id) {
		scopeTypeRepository.delete(id);
	}
	
	public void processBatchAction(Batch batch) {
		// TODO: Write logic for batch processing
	}

	public boolean exists(String id) {
		return scopeTypeRepository.exists(id);
	}
	
}
