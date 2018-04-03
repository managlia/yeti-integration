package com.yeti.core.types.service;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeti.core.repository.types.ActionClassificationTypeRepository;
import com.yeti.model.action.ActionClassificationType;
import com.yeti.model.util.Batch;

@Service
public class ActionClassificationTypeService {
	
	@Autowired
	private ActionClassificationTypeRepository actionClassificationTypeRepository;
	
	public List<ActionClassificationType> getAllActionClassificationTypes() {
		List<ActionClassificationType> actionClassificationTypes = new ArrayList<ActionClassificationType>();
		actionClassificationTypeRepository.findAll().forEach(actionClassificationTypes::add);
		return actionClassificationTypes;
	}
	
	public ActionClassificationType getActionClassificationType(String id) {
		return actionClassificationTypeRepository.findOne(id);
	}
	
	public ActionClassificationType addActionClassificationType(ActionClassificationType actionClassificationType) {
		return actionClassificationTypeRepository.save(actionClassificationType);
	}

	public ActionClassificationType updateActionClassificationType(String id, ActionClassificationType actionClassificationType) {
		return actionClassificationTypeRepository.save(actionClassificationType);
	}

	public void deleteActionClassificationType(String id) {
		actionClassificationTypeRepository.delete(id);
	}
	
	public void processBatchAction(Batch batch) {
		// TODO: Write logic for batch processing
	}
	
	public boolean exists(String id) {
		return actionClassificationTypeRepository.exists(id);
	}
	
}
