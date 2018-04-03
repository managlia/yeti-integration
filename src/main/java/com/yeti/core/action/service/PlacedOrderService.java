package com.yeti.core.action.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeti.core.repository.action.PlacedOrderRepository;
import com.yeti.model.action.PlacedOrder;
import com.yeti.model.util.Batch;

@Service
public class PlacedOrderService {
	
	@Autowired
	private PlacedOrderRepository placedOrderRepository;
	
	public List<PlacedOrder> getAllPlacedOrders() {
		List<PlacedOrder> placedOrders = new ArrayList<PlacedOrder>();
		placedOrderRepository.findAll().forEach(placedOrders::add);
		return placedOrders;
	}
	
	public PlacedOrder getPlacedOrder(Integer id) {
		return placedOrderRepository.findOne(id);
	}
	
	public PlacedOrder addPlacedOrder(PlacedOrder placedOrder) {
		return placedOrderRepository.save(placedOrder);
	}

	public PlacedOrder updatePlacedOrder(Integer id, PlacedOrder placedOrder) {
		return placedOrderRepository.save(placedOrder);
	}

	public void deletePlacedOrder(Integer id) {
		placedOrderRepository.delete(id);
	}
	
	public void processBatch(Batch batch) {
		// TODO: Write logic for batch processing
	}
	
	public boolean exists(Integer id) {
		return placedOrderRepository.exists(id);
	}
	
}
