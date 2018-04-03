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

import com.yeti.core.types.service.ActionClassificationOtherTypeService;
import com.yeti.model.action.ActionClassificationOtherType;
import com.yeti.model.util.Batch;

@RestController
@ExposesResourceFor(ActionClassificationOtherType.class)
@RequestMapping(value = "/ActionClassificationOtherTypes", produces = "application/hal+json")
public class ActionClassificationOtherTypeController {

	@Autowired
	private ActionClassificationOtherTypeService actionClassificationOtherTypeService;

	@GetMapping
	public ResponseEntity<List<Resource<ActionClassificationOtherType>>> getAllActionClassificationOtherTypes() {
		List<ActionClassificationOtherType> actionClassificationOtherTypes = actionClassificationOtherTypeService.getAllActionClassificationOtherTypes();
		if( actionClassificationOtherTypes != null ) {
			List<Resource<ActionClassificationOtherType>> returnActionClassificationOtherTypes = new ArrayList<Resource<ActionClassificationOtherType>>();
			for( ActionClassificationOtherType actionClassificationOtherType : actionClassificationOtherTypes ) {
				returnActionClassificationOtherTypes.add(getActionClassificationOtherTypeResource(actionClassificationOtherType));
			}
			return ResponseEntity.ok(returnActionClassificationOtherTypes);
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Resource<ActionClassificationOtherType>> getActionClassificationOtherType(@PathVariable Integer id) {
		ActionClassificationOtherType actionClassificationOtherType = actionClassificationOtherTypeService.getActionClassificationOtherType(id);
		if( actionClassificationOtherType == null ) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(getActionClassificationOtherTypeResource(actionClassificationOtherType));
		}
	}
	
	@PostMapping
	public ResponseEntity<Resource<ActionClassificationOtherType>> addActionClassificationOtherType(@RequestBody ActionClassificationOtherType actionClassificationOtherType, HttpServletRequest request ) {
		ActionClassificationOtherType newActionClassificationOtherType = actionClassificationOtherTypeService.addActionClassificationOtherType(actionClassificationOtherType);
		if( newActionClassificationOtherType != null ) {
			String requestURI = request.getRequestURI();
			try {
				return ResponseEntity.created(new URI(requestURI + "/" + newActionClassificationOtherType.getActionClassificationOtherTypeId())).build();		
			} catch( Exception e ) {
				return ResponseEntity.badRequest().build();
			}
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Resource<ActionClassificationOtherType>> updateActionClassificationOtherType(@RequestBody ActionClassificationOtherType actionClassificationOtherType, @PathVariable Integer id) {
		if( !actionClassificationOtherTypeService.exists(id) ) {
			return ResponseEntity.notFound().build();
		} else {
			actionClassificationOtherTypeService.updateActionClassificationOtherType(id, actionClassificationOtherType);
			ActionClassificationOtherType updatedActionClassificationOtherType = actionClassificationOtherTypeService.updateActionClassificationOtherType(id, actionClassificationOtherType);
			if( updatedActionClassificationOtherType != null ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Resource<ActionClassificationOtherType>> deleteActionClassificationOtherType(@PathVariable Integer id) {
		if( !actionClassificationOtherTypeService.exists(id) ) {
			return ResponseEntity.notFound().build();
		} else {
			actionClassificationOtherTypeService.deleteActionClassificationOtherType(id);
			if( !actionClassificationOtherTypeService.exists(id) ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}
	
	@PatchMapping
	public void processBatch(@RequestBody Batch batch) {
		actionClassificationOtherTypeService.processBatchAction(batch);
	}
	
	private Resource<ActionClassificationOtherType> getActionClassificationOtherTypeResource(ActionClassificationOtherType a) {
	    Resource<ActionClassificationOtherType> resource = new Resource<ActionClassificationOtherType>(a);
	    resource.add(linkTo(methodOn(ActionClassificationOtherTypeController.class).getActionClassificationOtherType(a.getActionClassificationOtherTypeId())).withSelfRel());
	    return resource;
	}

}








