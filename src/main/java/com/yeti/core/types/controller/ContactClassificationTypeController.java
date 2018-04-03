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

import com.yeti.core.types.service.ContactClassificationTypeService;
import com.yeti.model.contact.ContactClassificationType;
import com.yeti.model.util.Batch;

@RestController
@ExposesResourceFor(ContactClassificationType.class)
@RequestMapping(value = "/ContactClassificationTypes", produces = "application/hal+json")
public class ContactClassificationTypeController {

	@Autowired
	private ContactClassificationTypeService contactClassificationTypeService;

	@GetMapping
	public ResponseEntity<List<Resource<ContactClassificationType>>> getAllContactClassificationTypes() {
		List<ContactClassificationType> contactClassificationTypes = contactClassificationTypeService.getAllContactClassificationTypes();
		if( contactClassificationTypes != null ) {
			List<Resource<ContactClassificationType>> returnContactClassificationTypes = new ArrayList<Resource<ContactClassificationType>>();
			for( ContactClassificationType contactClassificationType : contactClassificationTypes ) {
				returnContactClassificationTypes.add(getContactClassificationTypeResource(contactClassificationType));
			}
			return ResponseEntity.ok(returnContactClassificationTypes);
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Resource<ContactClassificationType>> getContactClassificationType(@PathVariable String id) {
		ContactClassificationType contactClassificationType = contactClassificationTypeService.getContactClassificationType(id);
		if( contactClassificationType == null ) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(getContactClassificationTypeResource(contactClassificationType));
		}
	}
	
	@PostMapping
	public ResponseEntity<Resource<ContactClassificationType>> addContactClassificationType(@RequestBody ContactClassificationType contactClassificationType, HttpServletRequest request ) {
		ContactClassificationType newContactClassificationType = contactClassificationTypeService.addContactClassificationType(contactClassificationType);
		if( newContactClassificationType != null ) {
			String requestURI = request.getRequestURI();
			try {
				return ResponseEntity.created(new URI(requestURI + "/" + newContactClassificationType.getClassificationTypeId())).build();		
			} catch( Exception e ) {
				return ResponseEntity.badRequest().build();
			}
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Resource<ContactClassificationType>> updateContactClassificationType(@RequestBody ContactClassificationType contactClassificationType, @PathVariable String id) {
		if( !contactClassificationTypeService.exists(id) ) {
			return ResponseEntity.notFound().build();
		} else {
			contactClassificationTypeService.updateContactClassificationType(id, contactClassificationType);
			ContactClassificationType updatedContactClassificationType = contactClassificationTypeService.updateContactClassificationType(id, contactClassificationType);
			if( updatedContactClassificationType != null ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Resource<ContactClassificationType>> deleteContactClassificationType(@PathVariable String id) {
		if( !contactClassificationTypeService.exists(id) ) {
			return ResponseEntity.notFound().build();
		} else {
			contactClassificationTypeService.deleteContactClassificationType(id);
			if( !contactClassificationTypeService.exists(id) ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}
	
	@PatchMapping
	public void processBatchAction(@RequestBody Batch batch) {
		contactClassificationTypeService.processBatchAction(batch);
	}
	
	private Resource<ContactClassificationType> getContactClassificationTypeResource(ContactClassificationType a) {
	    Resource<ContactClassificationType> resource = new Resource<ContactClassificationType>(a);
	    resource.add(linkTo(methodOn(ContactClassificationTypeController.class).getContactClassificationType(a.getClassificationTypeId())).withSelfRel());
	    return resource;
	}

}








