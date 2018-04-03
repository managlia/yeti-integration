package com.yeti.core.types.service;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeti.core.repository.types.ContactUrlTypeRepository;
import com.yeti.model.contact.ContactUrlType;
import com.yeti.model.util.Batch;

@Service
public class ContactUrlTypeService {
	
	@Autowired
	private ContactUrlTypeRepository contactUrlTypeRepository;
	
	public List<ContactUrlType> getAllContactUrlTypes() {
		List<ContactUrlType> contactUrlTypes = new ArrayList<ContactUrlType>();
		contactUrlTypeRepository.findAll().forEach(contactUrlTypes::add);
		return contactUrlTypes;
	}
	
	public ContactUrlType getContactUrlType(String id) {
		return contactUrlTypeRepository.findOne(id);
	}
	
	public ContactUrlType addContactUrlType(ContactUrlType contactUrlType) {
		return contactUrlTypeRepository.save(contactUrlType);
	}

	public ContactUrlType updateContactUrlType(String id, ContactUrlType contactUrlType) {
		return contactUrlTypeRepository.save(contactUrlType);
	}

	public void deleteContactUrlType(String id) {
		contactUrlTypeRepository.delete(id);
	}
	
	public void processBatchAction(Batch batch) {
		// TODO: Write logic for batch processing
	}

	public boolean exists(String id) {
		return contactUrlTypeRepository.exists(id);
	}
	
}
