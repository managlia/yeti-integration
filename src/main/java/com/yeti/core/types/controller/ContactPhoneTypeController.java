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

import com.yeti.core.types.service.ContactPhoneTypeService;
import com.yeti.model.contact.ContactPhoneType;
import com.yeti.model.util.Batch;

@RestController
@ExposesResourceFor(ContactPhoneType.class)
@RequestMapping(value = "/ContactPhoneTypes", produces = "application/hal+json")
public class ContactPhoneTypeController {

	@Autowired
	private ContactPhoneTypeService contactPhoneTypeService;

	@GetMapping
	public ResponseEntity<List<Resource<ContactPhoneType>>> getAllContactPhoneTypes() {
		List<ContactPhoneType> contactPhoneTypes = contactPhoneTypeService.getAllContactPhoneTypes();
		if( contactPhoneTypes != null ) {
			List<Resource<ContactPhoneType>> returnContactPhoneTypes = new ArrayList<Resource<ContactPhoneType>>();
			for( ContactPhoneType contactPhoneType : contactPhoneTypes ) {
				returnContactPhoneTypes.add(getContactPhoneTypeResource(contactPhoneType));
			}
			return ResponseEntity.ok(returnContactPhoneTypes);
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Resource<ContactPhoneType>> getContactPhoneType(@PathVariable String id) {
		ContactPhoneType contactPhoneType = contactPhoneTypeService.getContactPhoneType(id);
		if( contactPhoneType == null ) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(getContactPhoneTypeResource(contactPhoneType));
		}
	}
	
	@PostMapping
	public ResponseEntity<Resource<ContactPhoneType>> addContactPhoneType(@RequestBody ContactPhoneType contactPhoneType, HttpServletRequest request ) {
		ContactPhoneType newContactPhoneType = contactPhoneTypeService.addContactPhoneType(contactPhoneType);
		if( newContactPhoneType != null ) {
			String requestURI = request.getRequestURI();
			try {
				return ResponseEntity.created(new URI(requestURI + "/" + newContactPhoneType.getPhoneTypeId())).build();		
			} catch( Exception e ) {
				return ResponseEntity.badRequest().build();
			}
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Resource<ContactPhoneType>> updateContactPhoneType(@RequestBody ContactPhoneType contactPhoneType, @PathVariable String id) {
		if( !contactPhoneTypeService.exists(id) ) {
			return ResponseEntity.notFound().build();
		} else {
			contactPhoneTypeService.updateContactPhoneType(id, contactPhoneType);
			ContactPhoneType updatedContactPhoneType = contactPhoneTypeService.updateContactPhoneType(id, contactPhoneType);
			if( updatedContactPhoneType != null ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Resource<ContactPhoneType>> deleteContactPhoneType(@PathVariable String id) {
		if( !contactPhoneTypeService.exists(id) ) {
			return ResponseEntity.notFound().build();
		} else {
			contactPhoneTypeService.deleteContactPhoneType(id);
			if( !contactPhoneTypeService.exists(id) ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}
	
	@PatchMapping
	public void processBatchAction(@RequestBody Batch batch) {
		contactPhoneTypeService.processBatchAction(batch);
	}
	
	private Resource<ContactPhoneType> getContactPhoneTypeResource(ContactPhoneType a) {
	    Resource<ContactPhoneType> resource = new Resource<ContactPhoneType>(a);
	    resource.add(linkTo(methodOn(ContactPhoneTypeController.class).getContactPhoneType(a.getPhoneTypeId())).withSelfRel());
	    return resource;
	}

}








