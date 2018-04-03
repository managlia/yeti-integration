package com.yeti.core.types.controller;

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

import com.yeti.core.types.service.OrderStateTypeService;
import com.yeti.model.action.OrderStateType;
import com.yeti.model.util.Batch;

@RestController
@ExposesResourceFor(OrderStateType.class)
@RequestMapping(value = "/OrderStateTypes", produces = "application/hal+json")
public class OrderStateTypeController {

	@Autowired
	private OrderStateTypeService orderStateTypeService;

	@GetMapping
	public ResponseEntity<List<Resource<OrderStateType>>> getAllOrderStateTypes() {
		List<OrderStateType> orderStateTypes = orderStateTypeService.getAllOrderStateTypes();
		if( orderStateTypes != null ) {
			List<Resource<OrderStateType>> returnOrderStateTypes = new ArrayList<Resource<OrderStateType>>();
			for( OrderStateType orderStateType : orderStateTypes ) {
				returnOrderStateTypes.add(getOrderStateTypeResource(orderStateType));
			}
			return ResponseEntity.ok(returnOrderStateTypes);
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Resource<OrderStateType>> getOrderStateType(@PathVariable String id) {
		OrderStateType orderStateType = orderStateTypeService.getOrderStateType(id);
		if( orderStateType == null ) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(getOrderStateTypeResource(orderStateType));
		}
	}
	
	@PostMapping
	public ResponseEntity<Resource<OrderStateType>> addOrderStateType(@RequestBody OrderStateType orderStateType, HttpServletRequest request ) {
		OrderStateType newOrderStateType = orderStateTypeService.addOrderStateType(orderStateType);
		if( newOrderStateType != null ) {
			String requestURI = request.getRequestURI();
			try {
				return ResponseEntity.created(new URI(requestURI + "/" + newOrderStateType.getOrderStateTypeId())).build();		
			} catch( Exception e ) {
				return ResponseEntity.badRequest().build();
			}
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Resource<OrderStateType>> updateOrderStateType(@RequestBody OrderStateType orderStateType, @PathVariable String id) {
		if( !orderStateTypeService.exists(id) ) {
			return ResponseEntity.notFound().build();
		} else {
			orderStateTypeService.updateOrderStateType(id, orderStateType);
			OrderStateType updatedOrderStateType = orderStateTypeService.updateOrderStateType(id, orderStateType);
			if( updatedOrderStateType != null ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Resource<OrderStateType>> deleteOrderStateType(@PathVariable String id) {
		if( !orderStateTypeService.exists(id) ) {
			return ResponseEntity.notFound().build();
		} else {
			orderStateTypeService.deleteOrderStateType(id);
			if( !orderStateTypeService.exists(id) ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}
	
	@PatchMapping
	public void processBatchAction(@RequestBody Batch batch) {
		orderStateTypeService.processBatchAction(batch);
	}
	
	private Resource<OrderStateType> getOrderStateTypeResource(OrderStateType a) {
	    Resource<OrderStateType> resource = new Resource<OrderStateType>(a);
	    resource.add(linkTo(methodOn(OrderStateTypeController.class).getOrderStateType(a.getOrderStateTypeId())).withSelfRel());
	    return resource;
	}

}








