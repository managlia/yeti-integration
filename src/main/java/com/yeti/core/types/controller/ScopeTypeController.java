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

import com.yeti.core.types.service.ScopeTypeService;
import com.yeti.model.general.ScopeType;
import com.yeti.model.util.Batch;

@RestController
@ExposesResourceFor(ScopeType.class)
@RequestMapping(value = "/ScopeTypes", produces = "application/hal+json")
public class ScopeTypeController {

	@Autowired
	private ScopeTypeService scopeTypeService;

	@GetMapping
	public ResponseEntity<List<Resource<ScopeType>>> getAllScopeTypes() {
		List<ScopeType> scopeTypes = scopeTypeService.getAllScopeTypes();
		if( scopeTypes != null ) {
			List<Resource<ScopeType>> returnScopeTypes = new ArrayList<Resource<ScopeType>>();
			for( ScopeType scopeType : scopeTypes ) {
				returnScopeTypes.add(getScopeTypeResource(scopeType));
			}
			return ResponseEntity.ok(returnScopeTypes);
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Resource<ScopeType>> getScopeType(@PathVariable String id) {
		ScopeType scopeType = scopeTypeService.getScopeType(id);
		if( scopeType == null ) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(getScopeTypeResource(scopeType));
		}
	}
	
	@PostMapping
	public ResponseEntity<Resource<ScopeType>> addScopeType(@RequestBody ScopeType scopeType, HttpServletRequest request ) {
		ScopeType newScopeType = scopeTypeService.addScopeType(scopeType);
		if( newScopeType != null ) {
			String requestURI = request.getRequestURI();
			try {
				return ResponseEntity.created(new URI(requestURI + "/" + newScopeType.getScopeTypeId())).build();		
			} catch( Exception e ) {
				return ResponseEntity.badRequest().build();
			}
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Resource<ScopeType>> updateScopeType(@RequestBody ScopeType scopeType, @PathVariable String id) {
		if( !scopeTypeService.exists(id) ) {
			return ResponseEntity.notFound().build();
		} else {
			scopeTypeService.updateScopeType(id, scopeType);
			ScopeType updatedScopeType = scopeTypeService.updateScopeType(id, scopeType);
			if( updatedScopeType != null ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Resource<ScopeType>> deleteScopeType(@PathVariable String id) {
		if( !scopeTypeService.exists(id) ) {
			return ResponseEntity.notFound().build();
		} else {
			scopeTypeService.deleteScopeType(id);
			if( !scopeTypeService.exists(id) ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}
	
	@PatchMapping
	public void processBatchAction(@RequestBody Batch batch) {
		scopeTypeService.processBatchAction(batch);
	}
	
	private Resource<ScopeType> getScopeTypeResource(ScopeType a) {
	    Resource<ScopeType> resource = new Resource<ScopeType>(a);
	    resource.add(linkTo(methodOn(ScopeTypeController.class).getScopeType(a.getScopeTypeId())).withSelfRel());
	    return resource;
	}

}








