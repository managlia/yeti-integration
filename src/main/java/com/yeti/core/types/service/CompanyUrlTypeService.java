package com.yeti.core.types.service;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeti.core.repository.types.CompanyUrlTypeRepository;
import com.yeti.model.company.CompanyUrlType;
import com.yeti.model.util.Batch;

@Service
public class CompanyUrlTypeService {
	
	@Autowired
	private CompanyUrlTypeRepository companyUrlTypeRepository;
	
	public List<CompanyUrlType> getAllCompanyUrlTypes() {
		List<CompanyUrlType> companyUrlTypes = new ArrayList<CompanyUrlType>();
		companyUrlTypeRepository.findAll().forEach(companyUrlTypes::add);
		return companyUrlTypes;
	}
	
	public CompanyUrlType getCompanyUrlType(String id) {
		return companyUrlTypeRepository.findOne(id);
	}
	
	public CompanyUrlType addCompanyUrlType(CompanyUrlType companyUrlType) {
		return companyUrlTypeRepository.save(companyUrlType);
	}

	public CompanyUrlType updateCompanyUrlType(String id, CompanyUrlType companyUrlType) {
		return companyUrlTypeRepository.save(companyUrlType);
	}

	public void deleteCompanyUrlType(String id) {
		companyUrlTypeRepository.delete(id);
	}
	
	public void processBatchAction(Batch batch) {
		// TODO: Write logic for batch processing
	}
	
	public boolean exists(String id) {
		return companyUrlTypeRepository.exists(id);
	}
	
}
