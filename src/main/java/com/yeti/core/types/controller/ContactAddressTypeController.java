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

import com.yeti.core.types.service.ContactAddressTypeService;
import com.yeti.model.contact.ContactAddressType;
import com.yeti.model.util.Batch;

@RestController
@ExposesResourceFor(ContactAddressType.class)
@RequestMapping(value = "/ContactAddressTypes", produces = "application/hal+json")
public class ContactAddressTypeController {

	@Autowired
	private ContactAddressTypeService contactAddressTypeService;

	@GetMapping
	public ResponseEntity<List<Resource<ContactAddressType>>> getAllContactAddressTypes() {
		List<ContactAddressType> contactAddressTypes = contactAddressTypeService.getAllContactAddressTypes();
		if( contactAddressTypes != null ) {
			List<Resource<ContactAddressType>> returnContactAddressTypes = new ArrayList<Resource<ContactAddressType>>();
			for( ContactAddressType contactAddressType : contactAddressTypes ) {
				returnContactAddressTypes.add(getContactAddressTypeResource(contactAddressType));
			}
			return ResponseEntity.ok(returnContactAddressTypes);
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Resource<ContactAddressType>> getContactAddressType(@PathVariable String id) {
		ContactAddressType contactAddressType = contactAddressTypeService.getContactAddressType(id);
		if( contactAddressType == null ) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(getContactAddressTypeResource(contactAddressType));
		}
	}
	
	@PostMapping
	public ResponseEntity<Resource<ContactAddressType>> addContactAddressType(@RequestBody ContactAddressType contactAddressType, HttpServletRequest request ) {
		ContactAddressType newContactAddressType = contactAddressTypeService.addContactAddressType(contactAddressType);
		if( newContactAddressType != null ) {
			String requestURI = request.getRequestURI();
			try {
				return ResponseEntity.created(new URI(requestURI + "/" + newContactAddressType.getAddressTypeId())).build();		
			} catch( Exception e ) {
				return ResponseEntity.badRequest().build();
			}
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Resource<ContactAddressType>> updateContactAddressType(@RequestBody ContactAddressType contactAddressType, @PathVariable String id) {
		if( !contactAddressTypeService.exists(id) ) {
			return ResponseEntity.notFound().build();
		} else {
			contactAddressTypeService.updateContactAddressType(id, contactAddressType);
			ContactAddressType updatedContactAddressType = contactAddressTypeService.updateContactAddressType(id, contactAddressType);
			if( updatedContactAddressType != null ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Resource<ContactAddressType>> deleteContactAddressType(@PathVariable String id) {
		if( !contactAddressTypeService.exists(id) ) {
			return ResponseEntity.notFound().build();
		} else {
			contactAddressTypeService.deleteContactAddressType(id);
			if( !contactAddressTypeService.exists(id) ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}
	
	@PatchMapping
	public void processBatchAction(@RequestBody Batch batch) {
		contactAddressTypeService.processBatchAction(batch);
	}
	
	private Resource<ContactAddressType> getContactAddressTypeResource(ContactAddressType a) {
	    Resource<ContactAddressType> resource = new Resource<ContactAddressType>(a);
	    resource.add(linkTo(methodOn(ContactAddressTypeController.class).getContactAddressType(a.getAddressTypeId())).withSelfRel());
	    return resource;
	}

}








