package com.yeti.core.types.service;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeti.core.repository.types.CompanyClassificationTypeRepository;
import com.yeti.model.company.CompanyClassificationType;
import com.yeti.model.util.Batch;

@Service
public class CompanyClassificationTypeService {
	
	@Autowired
	private CompanyClassificationTypeRepository companyClassificationTypeRepository;
	
	public List<CompanyClassificationType> getAllCompanyClassificationTypes() {
		List<CompanyClassificationType> companyClassificationTypes = new ArrayList<CompanyClassificationType>();
		companyClassificationTypeRepository.findAll().forEach(companyClassificationTypes::add);
		return companyClassificationTypes;
	}
	
	public CompanyClassificationType getCompanyClassificationType(String id) {
		return companyClassificationTypeRepository.findOne(id);
	}
	
	public CompanyClassificationType addCompanyClassificationType(CompanyClassificationType companyClassificationType) {
		return companyClassificationTypeRepository.save(companyClassificationType);
	}

	public CompanyClassificationType updateCompanyClassificationType(String id, CompanyClassificationType companyClassificationType) {
		return companyClassificationTypeRepository.save(companyClassificationType);
	}

	public void deleteCompanyClassificationType(String id) {
		companyClassificationTypeRepository.delete(id);
	}
	
	public void processBatchAction(Batch batch) {
		// TODO: Write logic for batch processing
	}
	
	public boolean exists(String id) {
		return companyClassificationTypeRepository.exists(id);
	}
	
}
