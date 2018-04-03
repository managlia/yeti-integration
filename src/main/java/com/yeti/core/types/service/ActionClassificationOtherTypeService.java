package com.yeti.core.types.service;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeti.core.repository.types.ActionClassificationOtherTypeRepository;
import com.yeti.model.action.ActionClassificationOtherType;
import com.yeti.model.util.Batch;

@Service
public class ActionClassificationOtherTypeService {
	
	@Autowired
	private ActionClassificationOtherTypeRepository actionClassificationOtherTypeRepository;
	
	public List<ActionClassificationOtherType> getAllActionClassificationOtherTypes() {
		List<ActionClassificationOtherType> actionClassificationOtherTypes = new ArrayList<ActionClassificationOtherType>();
		actionClassificationOtherTypeRepository.findAll().forEach(actionClassificationOtherTypes::add);
		return actionClassificationOtherTypes;
	}
	
	public ActionClassificationOtherType getActionClassificationOtherType(Integer id) {
		return actionClassificationOtherTypeRepository.findOne(id);
	}
	
	public ActionClassificationOtherType addActionClassificationOtherType(ActionClassificationOtherType actionClassificationOtherType) {
		return actionClassificationOtherTypeRepository.save(actionClassificationOtherType);
	}

	public ActionClassificationOtherType updateActionClassificationOtherType(Integer id, ActionClassificationOtherType actionClassificationOtherType) {
		return actionClassificationOtherTypeRepository.save(actionClassificationOtherType);
	}

	public void deleteActionClassificationOtherType(Integer id) {
		actionClassificationOtherTypeRepository.delete(id);
	}
	
	public void processBatchAction(Batch batch) {
		// TODO: Write logic for batch processing
	}
	
	public boolean exists(Integer id) {
		return actionClassificationOtherTypeRepository.exists(id);
	}
	
}
