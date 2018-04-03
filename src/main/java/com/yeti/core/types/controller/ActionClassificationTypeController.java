package com.yeti.core.types.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import com.yeti.core.types.service.ActionClassificationTypeService;
import com.yeti.core.repository.action.validators.BeforeCreateActionClassificationTypeValidator;
import com.yeti.model.action.ActionClassificationType;
import com.yeti.model.util.Batch;

@RestController
@ExposesResourceFor(ActionClassificationType.class)
@RequestMapping(value = "/ActionClassificationTypes", produces = "application/hal+json")
public class ActionClassificationTypeController {

	@Autowired
	private ActionClassificationTypeService actionClassificationTypeService;
	
	@Autowired
	private BeforeCreateActionClassificationTypeValidator beforeCreateActionClassificationTypeValidator;
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    binder.setValidator(beforeCreateActionClassificationTypeValidator);
	}
	
	@GetMapping
	public ResponseEntity<List<Resource<ActionClassificationType>>> getAllActionClassificationTypes() {
		List<ActionClassificationType> actionClassificationTypes = actionClassificationTypeService.getAllActionClassificationTypes();
		if( actionClassificationTypes != null ) {
			List<Resource<ActionClassificationType>> returnActionClassificationTypes = new ArrayList<Resource<ActionClassificationType>>();
			for( ActionClassificationType actionClassificationType : actionClassificationTypes ) {
				returnActionClassificationTypes.add(getActionClassificationTypeResource(actionClassificationType));
			}
			return ResponseEntity.ok(returnActionClassificationTypes);
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Resource<ActionClassificationType>> getActionClassificationType(@PathVariable String id) {
		ActionClassificationType actionClassificationType = actionClassificationTypeService.getActionClassificationType(id);
		if( actionClassificationType == null ) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(getActionClassificationTypeResource(actionClassificationType));
		}
	}
	
	@PostMapping
	public ResponseEntity<Resource<ActionClassificationType>> addActionClassificationType(@Valid @RequestBody ActionClassificationType actionClassificationType, HttpServletRequest request ) {
		ActionClassificationType newActionClassificationType = actionClassificationTypeService.addActionClassificationType(actionClassificationType);
		if( newActionClassificationType != null ) {
			String requestURI = request.getRequestURI();
			try {
				return ResponseEntity.created(new URI(requestURI + "/" + newActionClassificationType.getActionClassificationTypeId())).build();		
			} catch( Exception e ) {
				return ResponseEntity.badRequest().build();
			}
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Resource<ActionClassificationType>> updateActionClassificationType(@Valid @RequestBody ActionClassificationType actionClassificationType, @PathVariable String id) {
		if( !actionClassificationTypeService.exists(id) ) {
			return ResponseEntity.notFound().build();
		} else {
			actionClassificationTypeService.updateActionClassificationType(id, actionClassificationType);
			ActionClassificationType updatedActionClassificationType = actionClassificationTypeService.updateActionClassificationType(id, actionClassificationType);
			if( updatedActionClassificationType != null ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Resource<ActionClassificationType>> deleteActionClassificationType(@PathVariable String id) {
		if( !actionClassificationTypeService.exists(id) ) {
			return ResponseEntity.notFound().build();
		} else {
			actionClassificationTypeService.deleteActionClassificationType(id);
			if( !actionClassificationTypeService.exists(id) ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}
	
	@PatchMapping
	public void processBatchAction(@RequestBody Batch batch) {
		actionClassificationTypeService.processBatchAction(batch);
	}
	
	private Resource<ActionClassificationType> getActionClassificationTypeResource(ActionClassificationType a) {
	    Resource<ActionClassificationType> resource = new Resource<ActionClassificationType>(a);
	    resource.add(linkTo(methodOn(ActionClassificationTypeController.class).getActionClassificationType(a.getActionClassificationTypeId())).withSelfRel());
	    return resource;
	}

}








