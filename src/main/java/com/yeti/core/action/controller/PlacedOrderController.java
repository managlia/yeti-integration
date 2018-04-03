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

import com.yeti.core.action.service.PlacedOrderService;
import com.yeti.model.action.PlacedOrder;
import com.yeti.model.util.Batch;

@RestController
@ExposesResourceFor(PlacedOrder.class)
@RequestMapping(value = "/PlacedOrders", produces = "application/hal+json")
public class PlacedOrderController {

	@Autowired
	private PlacedOrderService placedOrderService;

	@GetMapping
	public ResponseEntity<List<Resource<PlacedOrder>>> getAllPlacedOrders() {
		List<PlacedOrder> placedOrders = placedOrderService.getAllPlacedOrders();
		if( placedOrders != null ) {
			List<Resource<PlacedOrder>> returnPlacedOrders = new ArrayList<Resource<PlacedOrder>>();
			for( PlacedOrder placedOrder : placedOrders ) {
				returnPlacedOrders.add(getPlacedOrderResource(placedOrder));
			}
			return ResponseEntity.ok(returnPlacedOrders);
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Resource<PlacedOrder>> getPlacedOrder(@PathVariable Integer id) {
		PlacedOrder placedOrder = placedOrderService.getPlacedOrder(id);
		if( placedOrder == null ) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(getPlacedOrderResource(placedOrder));
		}
	}
	
	@PostMapping
	public ResponseEntity<Resource<PlacedOrder>> addPlacedOrder(@RequestBody PlacedOrder placedOrder, HttpServletRequest request ) {
		PlacedOrder newPlacedOrder = placedOrderService.addPlacedOrder(placedOrder);
		if( newPlacedOrder != null ) {
			String requestURI = request.getRequestURI();
			try {
				return ResponseEntity.created(new URI(requestURI + "/" + newPlacedOrder.getActionId())).build();		
			} catch( Exception e ) {
				return ResponseEntity.badRequest().build();
			}
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Resource<PlacedOrder>> updatePlacedOrder(@RequestBody PlacedOrder placedOrder, @PathVariable Integer id) {
		if( !placedOrderService.exists(id) ) {
			return ResponseEntity.notFound().build();
		} else {
			placedOrderService.updatePlacedOrder(id, placedOrder);
			PlacedOrder updatedPlacedOrder = placedOrderService.updatePlacedOrder(id, placedOrder);
			if( updatedPlacedOrder != null ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Resource<PlacedOrder>> deletePlacedOrder(@PathVariable Integer id) {
		if( !placedOrderService.exists(id) ) {
			return ResponseEntity.notFound().build();
		} else {
			placedOrderService.deletePlacedOrder(id);
			if( !placedOrderService.exists(id) ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}
	
	@PatchMapping
	public void processBatch(@RequestBody Batch batch) {
		placedOrderService.processBatch(batch);
	}

	private Resource<PlacedOrder> getPlacedOrderResource(PlacedOrder a) {
	    Resource<PlacedOrder> resource = new Resource<PlacedOrder>(a);
	    resource.add(linkTo(methodOn(PlacedOrderController.class).getPlacedOrder(a.getActionId())).withSelfRel());
	    return resource;
	}

}








