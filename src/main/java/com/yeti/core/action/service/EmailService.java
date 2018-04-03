package com.yeti.core.action.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeti.core.repository.action.EmailRepository;
import com.yeti.model.action.Email;
import com.yeti.model.util.Batch;

@Service
public class EmailService {
	
	@Autowired
	private EmailRepository emailRepository;
	
	public List<Email> getAllEmails() {
		List<Email> emails = new ArrayList<Email>();
		emailRepository.findAll().forEach(emails::add);
		return emails;
	}
	
	public Email getEmail(Integer id) {
		return emailRepository.findOne(id);
	}
	
	public Email addEmail(Email email) {
		return emailRepository.save(email);
	}

	public Email updateEmail(Integer id, Email email) {
		return emailRepository.save(email);
	}

	public void deleteEmail(Integer id) {
		emailRepository.delete(id);
	}
	
	public void processBatch(Batch batch) {
		// TODO: Write logic for batch processing
	}
	
	public boolean exists(Integer id) {
		return emailRepository.exists(id);
	}
	
}
