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

import com.yeti.core.types.service.ContactUrlTypeService;
import com.yeti.model.contact.ContactUrlType;
import com.yeti.model.util.Batch;

@RestController
@ExposesResourceFor(ContactUrlType.class)
@RequestMapping(value = "/ContactUrlTypes", produces = "application/hal+json")
public class ContactUrlTypeController {

	@Autowired
	private ContactUrlTypeService contactUrlTypeService;

	@GetMapping
	public ResponseEntity<List<Resource<ContactUrlType>>> getAllContactUrlTypes() {
		List<ContactUrlType> contactUrlTypes = contactUrlTypeService.getAllContactUrlTypes();
		if( contactUrlTypes != null ) {
			List<Resource<ContactUrlType>> returnContactUrlTypes = new ArrayList<Resource<ContactUrlType>>();
			for( ContactUrlType contactUrlType : contactUrlTypes ) {
				returnContactUrlTypes.add(getContactUrlTypeResource(contactUrlType));
			}
			return ResponseEntity.ok(returnContactUrlTypes);
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Resource<ContactUrlType>> getContactUrlType(@PathVariable String id) {
		ContactUrlType contactUrlType = contactUrlTypeService.getContactUrlType(id);
		if( contactUrlType == null ) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(getContactUrlTypeResource(contactUrlType));
		}
	}
	
	@PostMapping
	public ResponseEntity<Resource<ContactUrlType>> addContactUrlType(@RequestBody ContactUrlType contactUrlType, HttpServletRequest request ) {
		ContactUrlType newContactUrlType = contactUrlTypeService.addContactUrlType(contactUrlType);
		if( newContactUrlType != null ) {
			String requestURI = request.getRequestURI();
			try {
				return ResponseEntity.created(new URI(requestURI + "/" + newContactUrlType.getUrlTypeId())).build();		
			} catch( Exception e ) {
				return ResponseEntity.badRequest().build();
			}
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Resource<ContactUrlType>> updateContactUrlType(@RequestBody ContactUrlType contactUrlType, @PathVariable String id) {
		if( !contactUrlTypeService.exists(id) ) {
			return ResponseEntity.notFound().build();
		} else {
			contactUrlTypeService.updateContactUrlType(id, contactUrlType);
			ContactUrlType updatedContactUrlType = contactUrlTypeService.updateContactUrlType(id, contactUrlType);
			if( updatedContactUrlType != null ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Resource<ContactUrlType>> deleteContactUrlType(@PathVariable String id) {
		if( !contactUrlTypeService.exists(id) ) {
			return ResponseEntity.notFound().build();
		} else {
			contactUrlTypeService.deleteContactUrlType(id);
			if( !contactUrlTypeService.exists(id) ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}
	
	@PatchMapping
	public void processBatchAction(@RequestBody Batch batch) {
		contactUrlTypeService.processBatchAction(batch);
	}
	
	private Resource<ContactUrlType> getContactUrlTypeResource(ContactUrlType a) {
	    Resource<ContactUrlType> resource = new Resource<ContactUrlType>(a);
	    resource.add(linkTo(methodOn(ContactUrlTypeController.class).getContactUrlType(a.getUrlTypeId())).withSelfRel());
	    return resource;
	}

}








