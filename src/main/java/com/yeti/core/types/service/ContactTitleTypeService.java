package com.yeti.core.types.service;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeti.core.repository.types.ContactTitleTypeRepository;
import com.yeti.model.contact.ContactTitleType;
import com.yeti.model.util.Batch;

@Service
public class ContactTitleTypeService {
	
	@Autowired
	private ContactTitleTypeRepository contactTitleTypeRepository;
	
	public List<ContactTitleType> getAllContactTitleTypes() {
		List<ContactTitleType> contactTitleTypes = new ArrayList<ContactTitleType>();
		contactTitleTypeRepository.findAll().forEach(contactTitleTypes::add);
		return contactTitleTypes;
	}
	
	public ContactTitleType getContactTitleType(Integer id) {
		return contactTitleTypeRepository.findOne(id);
	}
	
	public ContactTitleType addContactTitleType(ContactTitleType contactTitleType) {
		return contactTitleTypeRepository.save(contactTitleType);
	}

	public ContactTitleType updateContactTitleType(Integer id, ContactTitleType contactTitleType) {
		return contactTitleTypeRepository.save(contactTitleType);
	}

	public void deleteContactTitleType(Integer id) {
		contactTitleTypeRepository.delete(id);
	}
	
	public void processBatchAction(Batch batch) {
		// TODO: Write logic for batch processing
	}

	public boolean exists(Integer id) {
		return contactTitleTypeRepository.exists(id);
	}
	
}
