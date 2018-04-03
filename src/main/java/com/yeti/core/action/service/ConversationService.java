package com.yeti.core.action.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeti.core.repository.action.ConversationRepository;
import com.yeti.model.action.Conversation;
import com.yeti.model.util.Batch;

@Service
public class ConversationService {
	
	@Autowired
	private ConversationRepository conversationRepository;
	
	public List<Conversation> getAllConversations() {
		List<Conversation> conversations = new ArrayList<Conversation>();
		conversationRepository.findAll().forEach(conversations::add);
		return conversations;
	}
	
	public Conversation getConversation(Integer id) {
		return conversationRepository.findOne(id);
	}
	
	public Conversation addConversation(Conversation conversation) {
		return conversationRepository.save(conversation);
	}

	public Conversation updateConversation(Integer id, Conversation conversation) {
		return conversationRepository.save(conversation);
	}

	public void deleteConversation(Integer id) {
		conversationRepository.delete(id);
	}
	
	public void processBatch(Batch batch) {
		// TODO: Write logic for batch processing
	}
	
	public boolean exists(Integer id) {
		return conversationRepository.exists(id);
	}
	
}
