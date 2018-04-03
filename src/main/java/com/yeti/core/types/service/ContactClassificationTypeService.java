package com.yeti.core.types.service;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeti.core.repository.types.ContactClassificationTypeRepository;
import com.yeti.model.contact.ContactClassificationType;
import com.yeti.model.util.Batch;

@Service
public class ContactClassificationTypeService {
	
	@Autowired
	private ContactClassificationTypeRepository contactClassificationTypeRepository;
	
	public List<ContactClassificationType> getAllContactClassificationTypes() {
		List<ContactClassificationType> contactClassificationTypes = new ArrayList<ContactClassificationType>();
		contactClassificationTypeRepository.findAll().forEach(contactClassificationTypes::add);
		return contactClassificationTypes;
	}
	
	public ContactClassificationType getContactClassificationType(String id) {
		return contactClassificationTypeRepository.findOne(id);
	}
	
	public ContactClassificationType addContactClassificationType(ContactClassificationType contactClassificationType) {
		return contactClassificationTypeRepository.save(contactClassificationType);
	}

	public ContactClassificationType updateContactClassificationType(String id, ContactClassificationType contactClassificationType) {
		return contactClassificationTypeRepository.save(contactClassificationType);
	}

	public void deleteContactClassificationType(String id) {
		contactClassificationTypeRepository.delete(id);
	}

	public void processBatchAction(Batch batch) {
		// TODO: Write logic for batch processing
	}
	
	public boolean exists(String id) {
		return contactClassificationTypeRepository.exists(id);
	}
	
}
