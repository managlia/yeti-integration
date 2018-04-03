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

import com.yeti.core.types.service.ContactTitleTypeService;
import com.yeti.model.contact.ContactTitleType;
import com.yeti.model.util.Batch;

@RestController
@ExposesResourceFor(ContactTitleType.class)
@RequestMapping(value = "/ContactTitleTypes", produces = "application/hal+json")
public class ContactTitleTypeController {

	@Autowired
	private ContactTitleTypeService contactTitleTypeService;

	@GetMapping
	public ResponseEntity<List<Resource<ContactTitleType>>> getAllContactTitleTypes() {
		List<ContactTitleType> contactTitleTypes = contactTitleTypeService.getAllContactTitleTypes();
		if( contactTitleTypes != null ) {
			List<Resource<ContactTitleType>> returnContactTitleTypes = new ArrayList<Resource<ContactTitleType>>();
			for( ContactTitleType contactTitleType : contactTitleTypes ) {
				returnContactTitleTypes.add(getContactTitleTypeResource(contactTitleType));
			}
			return ResponseEntity.ok(returnContactTitleTypes);
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Resource<ContactTitleType>> getContactTitleType(@PathVariable Integer id) {
		ContactTitleType contactTitleType = contactTitleTypeService.getContactTitleType(id);
		if( contactTitleType == null ) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(getContactTitleTypeResource(contactTitleType));
		}
	}
	
	@PostMapping
	public ResponseEntity<Resource<ContactTitleType>> addContactTitleType(@RequestBody ContactTitleType contactTitleType, HttpServletRequest request ) {
		ContactTitleType newContactTitleType = contactTitleTypeService.addContactTitleType(contactTitleType);
		if( newContactTitleType != null ) {
			String requestURI = request.getRequestURI();
			try {
				return ResponseEntity.created(new URI(requestURI + "/" + newContactTitleType.getContactTitleTypeId())).build();		
			} catch( Exception e ) {
				return ResponseEntity.badRequest().build();
			}
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Resource<ContactTitleType>> updateContactTitleType(@RequestBody ContactTitleType contactTitleType, @PathVariable Integer id) {
		if( !contactTitleTypeService.exists(id) ) {
			return ResponseEntity.notFound().build();
		} else {
			contactTitleTypeService.updateContactTitleType(id, contactTitleType);
			ContactTitleType updatedContactTitleType = contactTitleTypeService.updateContactTitleType(id, contactTitleType);
			if( updatedContactTitleType != null ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Resource<ContactTitleType>> deleteContactTitleType(@PathVariable Integer id) {
		if( !contactTitleTypeService.exists(id) ) {
			return ResponseEntity.notFound().build();
		} else {
			contactTitleTypeService.deleteContactTitleType(id);
			if( !contactTitleTypeService.exists(id) ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}
	
	@PatchMapping
	public void processBatchAction(@RequestBody Batch batch) {
		contactTitleTypeService.processBatchAction(batch);
	}
	
	private Resource<ContactTitleType> getContactTitleTypeResource(ContactTitleType a) {
	    Resource<ContactTitleType> resource = new Resource<ContactTitleType>(a);
	    resource.add(linkTo(methodOn(ContactTitleTypeController.class).getContactTitleType(a.getContactTitleTypeId())).withSelfRel());
	    return resource;
	}

}








