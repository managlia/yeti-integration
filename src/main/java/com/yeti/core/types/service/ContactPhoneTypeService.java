package com.yeti.core.types.service;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeti.core.repository.types.ContactPhoneTypeRepository;
import com.yeti.model.contact.ContactPhoneType;
import com.yeti.model.util.Batch;

@Service
public class ContactPhoneTypeService {
	
	@Autowired
	private ContactPhoneTypeRepository contactPhoneTypeRepository;
	
	public List<ContactPhoneType> getAllContactPhoneTypes() {
		List<ContactPhoneType> contactPhoneTypes = new ArrayList<ContactPhoneType>();
		contactPhoneTypeRepository.findAll().forEach(contactPhoneTypes::add);
		return contactPhoneTypes;
	}
	
	public ContactPhoneType getContactPhoneType(String id) {
		return contactPhoneTypeRepository.findOne(id);
	}
	
	public ContactPhoneType addContactPhoneType(ContactPhoneType contactPhoneType) {
		return contactPhoneTypeRepository.save(contactPhoneType);
	}

	public ContactPhoneType updateContactPhoneType(String id, ContactPhoneType contactPhoneType) {
		return contactPhoneTypeRepository.save(contactPhoneType);
	}

	public void deleteContactPhoneType(String id) {
		contactPhoneTypeRepository.delete(id);
	}
	
	public void processBatchAction(Batch batch) {
		// TODO: Write logic for batch processing
	}

	public boolean exists(String id) {
		return contactPhoneTypeRepository.exists(id);
	}
	
}
