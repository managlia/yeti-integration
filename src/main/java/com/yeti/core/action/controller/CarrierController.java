package com.yeti.core.action.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import com.yeti.core.action.service.CarrierService;
import com.yeti.model.general.Carrier;
import com.yeti.model.util.Batch;

@RestController
@ExposesResourceFor(Carrier.class)
@RequestMapping(value = "/Carriers", produces = "application/hal+json")
public class CarrierController {

	@Autowired
	private CarrierService carrierService;

	@GetMapping
	public ResponseEntity<List<Resource<Carrier>>> getAllCarriers() {
		List<Carrier> carriers = carrierService.getAllCarriers();
		if( carriers != null ) {
			List<Resource<Carrier>> returnCarriers = new ArrayList<Resource<Carrier>>();
			for( Carrier carrier : carriers ) {
				returnCarriers.add(getCarrierResource(carrier));
			}
			return ResponseEntity.ok(returnCarriers);
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Resource<Carrier>> getCarrier(@PathVariable String id) {
		Carrier carrier = carrierService.getCarrier(id);
		if( carrier == null ) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(getCarrierResource(carrier));
		}
	}
	
	@PostMapping
	public ResponseEntity<Resource<Carrier>> addCarrier(@RequestBody Carrier carrier, HttpServletRequest request ) {
		Carrier newCarrier = carrierService.addCarrier(carrier);
		if( newCarrier != null ) {
			String requestURI = request.getRequestURI();
			try {
				return ResponseEntity.created(new URI(requestURI + "/" + newCarrier.getCarrierId())).build();		
			} catch( Exception e ) {
				return ResponseEntity.badRequest().build();
			}
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Resource<Carrier>> updateCarrier(@RequestBody Carrier carrier, @PathVariable String id) {
		if( !carrierService.exists(id) ) {
			return ResponseEntity.notFound().build();
		} else {
			carrierService.updateCarrier(id, carrier);
			Carrier updatedCarrier = carrierService.updateCarrier(id, carrier);
			if( updatedCarrier != null ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Resource<Carrier>> deleteCarrier(@PathVariable String id) {
		if( !carrierService.exists(id) ) {
			return ResponseEntity.notFound().build();
		} else {
			carrierService.deleteCarrier(id);
			if( !carrierService.exists(id) ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}
	
	@PatchMapping
	public void processBatch(@RequestBody Batch batch) {
		carrierService.processBatch(batch);
	}

	private Resource<Carrier> getCarrierResource(Carrier a) {
	    Resource<Carrier> resource = new Resource<Carrier>(a);
	    resource.add(linkTo(methodOn(CarrierController.class).getCarrier(a.getCarrierId())).withSelfRel());
	    return resource;
	}

}








