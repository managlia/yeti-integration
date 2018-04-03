package com.yeti.core.action.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@ExposesResourceFor(Action.class)
@RequestMapping(value = "/Actions", produces = "application/hal+json")
public class ActionController {

   private static final Logger log = LoggerFactory.getLogger(ActionController.class);
	
	@Autowired
	private CompanyService companyService;

	@Autowired
	private ContactService contactService;
	
	@Autowired
	private CampaignService campaignService;

	@Autowired
	private ActionService actionService;

	@GetMapping
	public ResponseEntity<List<Resource<Action>>> getAllActions(
			@RequestParam(required=false) Integer companyId,
			@RequestParam(required=false) Integer contactId,
			@RequestParam(required=false) Integer campaignId
	) {
		List<Action> actions;
		if( companyId != null ) {
			actions = actionService.getActionsForCompany(companyId);
		} else if( contactId != null ) {
			actions = actionService.getActionsForContact(contactId);
		} else if( campaignId != null ) {
			actions = actionService.getActionsForCampaign(campaignId);
		} else {
			actions = actionService.getAllActions();
		}
		if( actions != null ) {
			List<Resource<Action>> returnActions = new ArrayList<Resource<Action>>();
			for( Action action : actions ) {
				returnActions.add(getActionResource(action));
			}
			return ResponseEntity.ok(returnActions);
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Resource<Action>> getAction(@PathVariable Integer id) {
		Action action = actionService.getAction(id);
		if( action == null ) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(getActionResource(action));
		}
	}
	
	@PostMapping
	public ResponseEntity<Resource<Action>> addAction(@RequestBody Action action, HttpServletRequest request ) {
		Action newAction = actionService.addAction(action);
		if( newAction != null ) {
			String requestURI = request.getRequestURI();
			try {
				return ResponseEntity.created(new URI(requestURI + "/" + newAction.getActionId())).build();		
			} catch( Exception e ) {
				return ResponseEntity.badRequest().build();
			}
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PutMapping("/{actionId}/Companies")
	public ResponseEntity<List<Resource<Company>>> addActionToCompany(@PathVariable Integer actionId, @RequestBody Company company) {
		Action action = actionService.getAction(actionId);
		if( action == null ) {
			return ResponseEntity.notFound().build();
		} else {
			Company updatedCompany = companyService.addActionToCompany(actionId, company.getCompanyId());
			if( updatedCompany != null ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}	
	
	@DeleteMapping("/{actionId}/Companies/{companyId}")
	public ResponseEntity<Resource<Action>> removeActionToCompany(@PathVariable Integer actionId, @PathVariable Integer companyId) {
		Action action = actionService.getAction(actionId);
		Company company = companyService.getCompany(companyId);
		if( action == null || company == null ) {
			return ResponseEntity.notFound().build();
		} else {
			Company updatedCompany = companyService.removeActionFromCompany(actionId, companyId);
			if( updatedCompany != null ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}

	@PutMapping("/{actionId}/Contacts")
	public ResponseEntity<List<Resource<Contact>>> addActionToContact(@PathVariable Integer actionId, @RequestBody Contact contact) {
		Action action = actionService.getAction(actionId);
		if( action == null ) {
			return ResponseEntity.notFound().build();
		} else {
			Contact updatedContact = contactService.addActionToContact(actionId, contact.getContactId());
			if( updatedContact != null ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}	
	
	@DeleteMapping("/{actionId}/Contacts/{contactId}")
	public ResponseEntity<Resource<Action>> removeActionToContact(@PathVariable Integer actionId, @PathVariable Integer contactId) {
		Action action = actionService.getAction(actionId);
		Contact contact = contactService.getContact(contactId);
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
	
	@PutMapping("/{actionId}/Campaigns")
	public ResponseEntity<List<Resource<Campaign>>> addActionToCampaign(@PathVariable Integer actionId, @RequestBody Campaign campaign) {
		Action action = actionService.getAction(actionId);
		if( action == null ) {
			return ResponseEntity.notFound().build();
		} else {
			Campaign updatedCampaign = campaignService.addActionToCampaign(actionId, campaign.getCampaignId());
			if( updatedCampaign != null ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}	
	
	@DeleteMapping("/{actionId}/Campaigns/{campaignId}")
	public ResponseEntity<Resource<Action>> removeActionToCampaign(@PathVariable Integer actionId, @PathVariable Integer campaignId) {
		Action action = actionService.getAction(actionId);
		Campaign campaign = campaignService.getCampaign(campaignId);
		if( action == null || campaign == null ) {
			return ResponseEntity.notFound().build();
		} else {
			Campaign updatedCampaign = campaignService.removeActionFromCampaign(actionId, campaignId);
			if( updatedCampaign != null ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<Resource<Action>> updateAction(@RequestBody Action action, @PathVariable Integer id) {
		log.debug( "In the UpdateAction " + action.getActionId() );
		log.debug( "In the UpdateAction exits " + actionService.exists(id) );
		if( !actionService.exists(id) ) {
			return ResponseEntity.notFound().build();
		} else {
			Action updatedAction = actionService.updateAction(id, action);
			if( updatedAction != null ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Resource<Action>> deleteAction(@PathVariable Integer id) {
		if( !actionService.exists(id) ) {
			return ResponseEntity.notFound().build();
		} else {
			actionService.deleteAction(id);
			if( !actionService.exists(id) ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}
	
	@PatchMapping
	public void processBatch(@RequestBody Batch batch) {
		actionService.processBatch(batch);
	}

	private Resource<Action> getActionResource(Action a) {
	    Resource<Action> resource = new Resource<Action>(a);
	    resource.add(linkTo(methodOn(ActionController.class).getAction(a.getActionId())).withSelfRel());
	    return resource;
	}

}