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
import com.yeti.core.contact.service.TeamService;
import com.yeti.model.action.Action;
import com.yeti.model.campaign.Campaign;
import com.yeti.model.company.Company;
import com.yeti.model.contact.Contact;
import com.yeti.model.contact.Team;
import com.yeti.model.util.Batch;

@RestController
@ExposesResourceFor(Contact.class)
@RequestMapping(value = "/Contacts", produces = "application/hal+json")
public class ContactController {

	@Autowired
	private ContactService contactService;

	@Autowired
	private TeamService teamService;

	@Autowired
	private CampaignService campaignService;

	@Autowired
	private ActionService actionService;
	
	@GetMapping
	public ResponseEntity<List<Resource<Contact>>> getAllContacts(
			@RequestParam(required=false) Integer companyId,
			@RequestParam(required=false) Integer campaignId,
			@RequestParam(required=false) Integer teamId,
			@RequestParam(required=false) Integer actionId
	) {
		List<Contact> contacts;
		if( campaignId != null ) {
			contacts = contactService.getContactsForCampaign(campaignId);
		} else if( teamId != null ) {
			contacts = contactService.getContactsForTeam(teamId);
		} else if( actionId != null ) {
			contacts = contactService.getContactsForAction(actionId);
		} else {
			contacts = contactService.getAllContacts(companyId);
		}
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
	
	@PostMapping
	public ResponseEntity<Resource<Contact>> addContact(@RequestBody Contact contact, HttpServletRequest request ) {
		Contact newContact = contactService.addNewContact(contact);
		if( newContact != null ) {
			String requestURI = request.getRequestURI();
			try {
				return ResponseEntity.created(new URI(requestURI + "/" + newContact.getContactId())).build();		
			} catch( Exception e ) {
				return ResponseEntity.badRequest().build();
			}
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Resource<Contact>> updateContact(@RequestBody Contact contact, @PathVariable Integer id) {
		if( !contactService.exists(id) ) {
			return ResponseEntity.notFound().build();
		} else {
			contactService.updateContact(id, contact);
			Contact updatedContact = contactService.updateContact(id, contact);
			if( updatedContact != null ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Resource<Contact>> deleteContact(@PathVariable Integer id) {
		if( !contactService.exists(id) ) {
			return ResponseEntity.notFound().build();
		} else {
			contactService.deleteContact(id);
			if( !contactService.exists(id) ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}
	
	@PutMapping("/{contactId}/Campaigns")
	public ResponseEntity<List<Resource<Contact>>> addCampaignToContact(@PathVariable Integer contactId, @RequestBody Campaign campaign) {
		Contact contact = contactService.getContact(contactId);
		if( contact == null ) {
			return ResponseEntity.notFound().build();
		} else {
			Contact updatedContact = contactService.addCampaignToContact(campaign.getCampaignId(), contactId);
			if( updatedContact != null ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}	
	
	@DeleteMapping("/{contactId}/Campaigns/{campaignId}")
	public ResponseEntity<Resource<Campaign>> removeCampaignToContact(@PathVariable Integer contactId, @PathVariable Integer campaignId) {
		Contact contact = contactService.getContact(contactId);
		Campaign campaign = campaignService.getCampaign(campaignId);
		if( campaign == null || contact == null ) {
			return ResponseEntity.notFound().build();
		} else {
			Contact updatedContact = contactService.removeCampaignFromContact(campaignId, contactId);
			if( updatedContact != null ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}

	@PutMapping("/{contactId}/Teams")
	public ResponseEntity<List<Resource<Contact>>> addTeamToContact(@PathVariable Integer contactId, @RequestBody Team team) {
		Contact contact = contactService.getContact(contactId);
		if( contact == null ) {
			return ResponseEntity.notFound().build();
		} else {
			Contact updatedContact = contactService.addTeamToContact(team.getTeamId(), contactId);
			if( updatedContact != null ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}	
	
	@DeleteMapping("/{contactId}/Teams/{teamId}")
	public ResponseEntity<Resource<Team>> removeTeamToContact(@PathVariable Integer contactId, @PathVariable Integer teamId) {
		Contact contact = contactService.getContact(contactId);
		Team team = teamService.getTeam(teamId);
		if( team == null || contact == null ) {
			return ResponseEntity.notFound().build();
		} else {
			Contact updatedContact = contactService.removeTeamFromContact(teamId, contactId);
			if( updatedContact != null ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}

	@PutMapping("/{contactId}/Actions")
	public ResponseEntity<List<Resource<Contact>>> addActionToContact(@PathVariable Integer contactId, @RequestBody Action action) {
		Contact contact = contactService.getContact(contactId);
		if( contact == null ) {
			return ResponseEntity.notFound().build();
		} else {
			Contact updatedContact = contactService.addActionToContact(action.getActionId(), contactId);
			if( updatedContact != null ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}	
	
	@DeleteMapping("/{contactId}/Actions/{actionId}")
	public ResponseEntity<Resource<Action>> removeActionToContact(@PathVariable Integer contactId, @PathVariable Integer actionId) {
		Contact contact = contactService.getContact(contactId);
		Action action = actionService.getAction(actionId);
		if( action == null || contact == null ) {
			return ResponseEntity.notFound().build();
		} else {
			Contact updatedContact = contactService.removeActionFromContact(actionId, contactId);
			if( updatedContact != null ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}
	
	
	
	@PatchMapping
	public void processBatchAction(@RequestBody Batch batch) {
		contactService.processBatchAction(batch);
	}
	
	private Resource<Contact> getContactResource(Contact a) {
	    Resource<Contact> resource = new Resource<Contact>(a);
	    resource.add(linkTo(methodOn(ContactController.class).getContact(a.getContactId())).withSelfRel());
	    return resource;
	}

}








