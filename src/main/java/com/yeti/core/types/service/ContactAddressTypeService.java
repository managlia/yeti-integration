package com.yeti.core.types.service;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeti.core.repository.types.ContactAddressTypeRepository;
import com.yeti.model.contact.ContactAddressType;
import com.yeti.model.util.Batch;

@Service
public class ContactAddressTypeService {
	
	@Autowired
	private ContactAddressTypeRepository contactAddressTypeRepository;
	
	public List<ContactAddressType> getAllContactAddressTypes() {
		List<ContactAddressType> contactAddressTypes = new ArrayList<ContactAddressType>();
		contactAddressTypeRepository.findAll().forEach(contactAddressTypes::add);
		return contactAddressTypes;
	}
	
	public ContactAddressType getContactAddressType(String id) {
		return contactAddressTypeRepository.findOne(id);
	}
	
	public ContactAddressType addContactAddressType(ContactAddressType contactAddressType) {
		return contactAddressTypeRepository.save(contactAddressType);
	}

	public ContactAddressType updateContactAddressType(String id, ContactAddressType contactAddressType) {
		return contactAddressTypeRepository.save(contactAddressType);
	}

	public void deleteContactAddressType(String id) {
		contactAddressTypeRepository.delete(id);
	}
	
	public void processBatchAction(Batch batch) {
		// TODO: Write logic for batch processing
	}
	
	public boolean exists(String id) {
		return contactAddressTypeRepository.exists(id);
	}
	
}
