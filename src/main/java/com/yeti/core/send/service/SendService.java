package com.yeti.core.send.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yetix.core.repository.send.SendQueueRepository;
import com.yeti.model.util.Batch;
import com.yetix.model.send.SendQueue;

@Service
public class SendService {

	@Autowired
	private SendQueueRepository sendQueueRepository;

	public List<SendQueue> getAllSendQueues() {
		List<SendQueue> sendQueues = new ArrayList<SendQueue>();
		sendQueueRepository.findAll().forEach(sendQueues::add);
		return sendQueues;
	}

	public SendQueue getSendQueue(Integer integer) {
		// TODO Auto-generated method stub
		return null;
	}

	public SendQueue addSendQueue(SendQueue send) {
		// TODO Auto-generated method stub
		return null;
	}

	public SendQueue updateSendQueue(String id, SendQueue send) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean exists(String id) {
		// TODO Auto-generated method stub
		return false;
	}

	public void deleteSendQueue(String id) {
		// TODO Auto-generated method stub
		
	}

	public void processBatchAction(Batch batch) {
		// TODO Auto-generated method stub
		
	}

}
