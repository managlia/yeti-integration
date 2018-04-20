package com.yeti.core.types.service;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeti.core.repository.types.CompanyPhoneTypeRepository;
import com.yeti.model.company.CompanyPhoneType;
import com.yeti.model.util.Batch;

@Service
public class CompanyPhoneTypeService {
	
	@Autowired
	private CompanyPhoneTypeRepository companyPhoneTypeRepository;
	
	public List<CompanyPhoneType> getAllCompanyPhoneTypes() {
		List<CompanyPhoneType> companyPhoneTypes = new ArrayList<CompanyPhoneType>();
		companyPhoneTypeRepository.findAll().forEach(companyPhoneTypes::add);
		return companyPhoneTypes;
	}
	
	public CompanyPhoneType getCompanyPhoneType(String id) {
		return companyPhoneTypeRepository.findOne(id);
	}
	
	public CompanyPhoneType addCompanyPhoneType(CompanyPhoneType companyPhoneType) {
		return companyPhoneTypeRepository.save(companyPhoneType);
	}

	public CompanyPhoneType updateCompanyPhoneType(String id, CompanyPhoneType companyPhoneType) {
		return companyPhoneTypeRepository.save(companyPhoneType);
	}

	public void deleteCompanyPhoneType(String id) {
		companyPhoneTypeRepository.delete(id);
	}
	
	public void processBatchAction(Batch batch) {
		// TODO: Write logic for batch processing
	}
	
	public boolean exists(String id) {
		return companyPhoneTypeRepository.exists(id);
	}
	
}
