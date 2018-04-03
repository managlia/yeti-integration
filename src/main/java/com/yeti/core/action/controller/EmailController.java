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

import com.yeti.core.action.service.EmailService;
import com.yeti.model.action.Email;
import com.yeti.model.util.Batch;

@RestController
@ExposesResourceFor(Email.class)
@RequestMapping(value = "/Emails", produces = "application/hal+json")
public class EmailController {

	@Autowired
	private EmailService emailService;

	@GetMapping
	public ResponseEntity<List<Resource<Email>>> getAllEmails() {
		List<Email> emails = emailService.getAllEmails();
		if( emails != null ) {
			List<Resource<Email>> returnEmails = new ArrayList<Resource<Email>>();
			for( Email email : emails ) {
				returnEmails.add(getEmailResource(email));
			}
			return ResponseEntity.ok(returnEmails);
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Resource<Email>> getEmail(@PathVariable Integer id) {
		Email email = emailService.getEmail(id);
		if( email == null ) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(getEmailResource(email));
		}
	}
	
	@PostMapping
	public ResponseEntity<Resource<Email>> addEmail(@RequestBody Email email, HttpServletRequest request ) {
		Email newEmail = emailService.addEmail(email);
		if( newEmail != null ) {
			String requestURI = request.getRequestURI();
			try {
				return ResponseEntity.created(new URI(requestURI + "/" + newEmail.getActionId())).build();		
			} catch( Exception e ) {
				return ResponseEntity.badRequest().build();
			}
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Resource<Email>> updateEmail(@RequestBody Email email, @PathVariable Integer id) {
		if( !emailService.exists(id) ) {
			return ResponseEntity.notFound().build();
		} else {
			emailService.updateEmail(id, email);
			Email updatedEmail = emailService.updateEmail(id, email);
			if( updatedEmail != null ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Resource<Email>> deleteEmail(@PathVariable Integer id) {
		if( !emailService.exists(id) ) {
			return ResponseEntity.notFound().build();
		} else {
			emailService.deleteEmail(id);
			if( !emailService.exists(id) ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}
	
	@PatchMapping
	public void processBatch(@RequestBody Batch batch) {
		emailService.processBatch(batch);
	}

	private Resource<Email> getEmailResource(Email a) {
	    Resource<Email> resource = new Resource<Email>(a);
	    resource.add(linkTo(methodOn(EmailController.class).getEmail(a.getActionId())).withSelfRel());
	    return resource;
	}

}








