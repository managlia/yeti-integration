package com.yeti.core.campaign.controller;

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
import com.yeti.core.contact.controller.ContactController;
import com.yeti.core.company.service.CompanyService;
import com.yeti.core.contact.service.ContactService;
import com.yeti.model.action.Action;
import com.yeti.model.campaign.Campaign;
import com.yeti.model.company.Company;
import com.yeti.model.contact.Contact;
import com.yeti.model.util.Batch;

@RestController
@ExposesResourceFor(Campaign.class)
@RequestMapping(value = "/Campaigns", produces = "application/hal+json")
public class CampaignController {

	@Autowired
	private CompanyService companyService;

	@Autowired
	private ContactService contactService;

	@Autowired
	private CampaignService campaignService;

	@Autowired
	private ActionService actionService;

	@GetMapping
	public ResponseEntity<List<Resource<Campaign>>> getAllCampaigns(
			@RequestParam(required=false) Integer companyId,
			@RequestParam(required=false) Integer contactId,
			@RequestParam(required=false) Integer actionId
	) {
		List<Campaign> campaigns;
		if( companyId != null ) {
			campaigns = campaignService.getCampaignsForCompany(companyId);
		} else if( contactId != null ) {
			campaigns = campaignService.getCampaignsForContact(contactId);
		} else if( actionId != null ) {
			campaigns = campaignService.getCampaignsForAction(actionId);
		} else {
			campaigns = campaignService.getAllCampaigns();
		}
		if( campaigns != null ) {
			List<Resource<Campaign>> returnCampaigns = new ArrayList<Resource<Campaign>>();
			for( Campaign campaign : campaigns ) {
				returnCampaigns.add(getCampaignResource(campaign));
			}
			return ResponseEntity.ok(returnCampaigns);
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Resource<Campaign>> getCampaign(@PathVariable Integer id) {
		Campaign campaign = campaignService.getCampaign(id);
		if( campaign == null ) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(getCampaignResource(campaign));
		}
	}
	
	@GetMapping("/{id}/Contacts")
	public ResponseEntity<List<Resource<Contact>>> getCampaignContacts(@PathVariable Integer id) {
		List<Contact> contacts = contactService.getContactsForCampaign(id);
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
	
	
	@PostMapping
	public ResponseEntity<Resource<Campaign>> addCampaign(@RequestBody Campaign campaign, HttpServletRequest request ) {
		Campaign newCampaign = campaignService.addCampaign(campaign);
		if( newCampaign != null ) {
			String requestURI = request.getRequestURI();
			try {
				return ResponseEntity.created(new URI(requestURI + "/" + newCampaign.getCampaignId())).build();		
			} catch( Exception e ) {
				return ResponseEntity.badRequest().build();
			}
		} else {
			return ResponseEntity.badRequest().build();
		}
	}

	@PutMapping("/{campaignId}/Contacts")
	public ResponseEntity<List<Resource<Contact>>> addCampaignToContact(@PathVariable Integer campaignId, @RequestBody Contact contact) {
		Campaign campaign = campaignService.getCampaign(campaignId);
		if( campaign == null ) {
			return ResponseEntity.notFound().build();
		} else {
			Contact updatedContact = contactService.addCampaignToContact(campaignId, contact.getContactId());
			if( updatedContact != null ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}	
	
	@DeleteMapping("/{campaignId}/Contacts/{contactId}")
	public ResponseEntity<Resource<Campaign>> removeCampaignToContact(@PathVariable Integer campaignId, @PathVariable Integer contactId) {
		Campaign campaign = campaignService.getCampaign(campaignId);
		Contact contact = contactService.getContact(contactId);
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
	
	@PutMapping("/{campaignId}/Companies")
	public ResponseEntity<List<Resource<Company>>> addCampaignToCompany(@PathVariable Integer campaignId, @RequestBody Company company) {
		Campaign campaign = campaignService.getCampaign(campaignId);
		if( campaign == null ) {
			return ResponseEntity.notFound().build();
		} else {
			Company updatedCompany = companyService.addCampaignToCompany(campaignId, company.getCompanyId());
			if( updatedCompany != null ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}	
	
	@DeleteMapping("/{campaignId}/Companies/{companyId}")
	public ResponseEntity<Resource<Campaign>> removeCampaignToCompany(@PathVariable Integer campaignId, @PathVariable Integer companyId) {
		Campaign campaign = campaignService.getCampaign(campaignId);
		Company company = companyService.getCompany(companyId);
		if( campaign == null || company == null ) {
			return ResponseEntity.notFound().build();
		} else {
			Company updatedCompany = companyService.removeCampaignFromCompany(campaignId, companyId);
			if( updatedCompany != null ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}
	
	@PutMapping("/{campaignId}/Actions")
	public ResponseEntity<List<Resource<Campaign>>> addActionToCampaign(@PathVariable Integer campaignId, @RequestBody Action action) {
		Campaign campaign = campaignService.getCampaign(campaignId);
		if( campaign == null ) {
			return ResponseEntity.notFound().build();
		} else {
			Campaign updatedCampaign = campaignService.addActionToCampaign(action.getActionId(), campaignId);
			if( updatedCampaign != null ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}	
	
	@DeleteMapping("/{campaignId}/Actions/{actionId}")
	public ResponseEntity<Resource<Action>> removeActionToCampaign(@PathVariable Integer campaignId, @PathVariable Integer actionId) {
		Campaign campaign = campaignService.getCampaign(campaignId);
		Action action = actionService.getAction(actionId);
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
	public ResponseEntity<Resource<Campaign>> updateCampaign(@RequestBody Campaign campaign, @PathVariable Integer id) {
		if( !campaignService.exists(id) ) {
			return ResponseEntity.notFound().build();
		} else {
			campaignService.updateCampaign(id, campaign);
			Campaign updatedCampaign = campaignService.updateCampaign(id, campaign);
			if( updatedCampaign != null ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Resource<Campaign>> deleteCampaign(@PathVariable Integer id) {
		if( !campaignService.exists(id) ) {
			return ResponseEntity.notFound().build();
		} else {
			campaignService.deleteCampaign(id);
			if( !campaignService.exists(id) ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}
	
	@PatchMapping
	public void processBatchAction(@RequestBody Batch batch) {
		campaignService.processBatchAction(batch);
	}
	
	private Resource<Campaign> getCampaignResource(Campaign a) {
	    Resource<Campaign> resource = new Resource<Campaign>(a);
	    resource.add(linkTo(methodOn(CampaignController.class).getCampaign(a.getCampaignId())).withSelfRel());
	    return resource;
	}

	private Resource<Contact> getContactResource(Contact a) {
	    Resource<Contact> resource = new Resource<Contact>(a);
	    resource.add(linkTo(methodOn(ContactController.class).getContact(a.getContactId())).withSelfRel());
	    return resource;
	}
	
}