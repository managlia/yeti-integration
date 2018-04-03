package com.yeti.core.types.service;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeti.core.repository.types.CompanyAddressTypeRepository;
import com.yeti.model.company.CompanyAddressType;
import com.yeti.model.util.Batch;

@Service
public class CompanyAddressTypeService {

	private void sleeper() {
		try {
			Thread.sleep( ((int)(Math.random()*10000)) );
			
		} catch( Exception e ) {
			e.printStackTrace();
		}
	}
	
	@Autowired
	private CompanyAddressTypeRepository companyAddressTypeRepository;
	
	public List<CompanyAddressType> getAllCompanyAddressTypes() {
		this.sleeper();
		List<CompanyAddressType> companyAddressTypes = new ArrayList<CompanyAddressType>();
		companyAddressTypeRepository.findAll().forEach(companyAddressTypes::add);
		return companyAddressTypes;
	}
	
	public CompanyAddressType getCompanyAddressType(String id) {
		this.sleeper();
		return companyAddressTypeRepository.findOne(id);
	}
	
	public CompanyAddressType addCompanyAddressType(CompanyAddressType companyAddressType) {
		return companyAddressTypeRepository.save(companyAddressType);
	}

	public CompanyAddressType updateCompanyAddressType(String id, CompanyAddressType companyAddressType) {
		return companyAddressTypeRepository.save(companyAddressType);
	}

	public void deleteCompanyAddressType(String id) {
		companyAddressTypeRepository.delete(id);
	}
	
	public void processBatchAction(Batch batch) {
		// TODO: Write logic for batch processing
	}
	
	public boolean exists(String id) {
		return companyAddressTypeRepository.exists(id);
	}
	
}
