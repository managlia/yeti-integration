package com.yeti.core.action.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeti.core.repository.action.CarrierRepository;
import com.yeti.model.general.Carrier;
import com.yeti.model.util.Batch;

@Service
public class CarrierService {
	
	@Autowired
	private CarrierRepository carrierRepository;
	
	public List<Carrier> getAllCarriers() {
		List<Carrier> carriers = new ArrayList<Carrier>();
		carrierRepository.findAll().forEach(carriers::add);
		return carriers;
	}
	
	public Carrier getCarrier(String id) {
		return carrierRepository.findOne(id);
	}
	
	public Carrier addCarrier(Carrier carrier) {
		return carrierRepository.save(carrier);
	}

	public Carrier updateCarrier(String id, Carrier carrier) {
		return carrierRepository.save(carrier);
	}

	public void deleteCarrier(String id) {
		carrierRepository.delete(id);
	}
	
	public void processBatch(Batch batch) {
		// TODO: Write logic for batch processing
	}
	
	public boolean exists(String id) {
		return carrierRepository.exists(id);
	}
	
}
