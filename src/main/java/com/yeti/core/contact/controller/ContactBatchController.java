package com.yeti.core.contact.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import com.yeti.core.action.service.ActionService;
import com.yeti.core.campaign.service.CampaignService;
import com.yeti.core.company.service.CompanyService;
import com.yeti.core.contact.service.ContactService;
import com.yeti.model.action.Action;
import com.yeti.model.campaign.Campaign;
import com.yeti.model.company.Company;
import com.yeti.model.contact.Contact;
import com.yeti.model.util.Batch;

@RestController
@ExposesResourceFor(Contact.class)
@RequestMapping(value = "/ContactBatch", produces = "application/hal+json")
public class ContactBatchController {

	@Autowired
	private ContactService contactService;

	
	@GetMapping
	public ResponseEntity<List<Resource<Contact>>> getAllContacts(
			@RequestParam(required=true) Integer[] id
	) {
		List<Contact> contacts = contactService.getContacts(id);
		if( contacts != null ) {
			List<Resource<Contact>> returnContacts = new ArrayList<Resource<Contact>>();
			for( Contact contact : contacts ) {
				returnContacts.add(getContactResource(contact));
			}
			return ResponseEntity.ok(returnContacts);
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Resource<Contact>> getContact(@PathVariable Integer id ) {
		Contact contact = contactService.getContact(id);
		if( contact == null ) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(getContactResource(contact));
		}
	}
	
	private Resource<Contact> getContactResource(Contact a) {
	    Resource<Contact> resource = new Resource<Contact>(a);
	    resource.add(linkTo(methodOn(ContactBatchController.class).getContact(a.getContactId())).withSelfRel());
	    return resource;
	}

}








