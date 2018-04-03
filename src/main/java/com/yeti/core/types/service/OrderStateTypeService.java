package com.yeti.core.types.service;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeti.core.repository.types.OrderStateTypeRepository;
import com.yeti.model.action.OrderStateType;
import com.yeti.model.util.Batch;

@Service
public class OrderStateTypeService {
	
	@Autowired
	private OrderStateTypeRepository orderStateTypeRepository;
	
	public List<OrderStateType> getAllOrderStateTypes() {
		List<OrderStateType> orderStateTypes = new ArrayList<OrderStateType>();
		orderStateTypeRepository.findAll().forEach(orderStateTypes::add);
		return orderStateTypes;
	}
	
	public OrderStateType getOrderStateType(String id) {
		return orderStateTypeRepository.findOne(id);
	}
	
	public OrderStateType addOrderStateType(OrderStateType orderStateType) {
		return orderStateTypeRepository.save(orderStateType);
	}

	public OrderStateType updateOrderStateType(String id, OrderStateType orderStateType) {
		return orderStateTypeRepository.save(orderStateType);
	}

	public void deleteOrderStateType(String id) {
		orderStateTypeRepository.delete(id);
	}
	
	public void processBatchAction(Batch batch) {
		// TODO: Write logic for batch processing
	}

	public boolean exists(String id) {
		return orderStateTypeRepository.exists(id);
	}
	
}
