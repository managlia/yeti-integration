package com.yeti.core.company.controller;

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
import com.yeti.model.campaign.Campaign;
import com.yeti.model.company.Company;
import com.yeti.model.contact.Contact;
import com.yeti.model.action.Action;
import com.yeti.model.util.Batch;

@RestController
@ExposesResourceFor(Company.class)
@RequestMapping(value = "/Companies", produces = "application/hal+json")
public class CompanyController {


	@Autowired
	private CompanyService companyService;

	@Autowired
	private ContactService contactService;

	@Autowired
	private CampaignService campaignService;

	@Autowired
	private ActionService actionService;

	@GetMapping
	public ResponseEntity<List<Resource<Company>>> getAllCompanies(
			@RequestParam(required=false) Integer contactId,
			@RequestParam(required=false) Integer campaignId,
			@RequestParam(required=false) Integer actionId
			) {
		List<Company> companies;
		if( campaignId != null ) {
			companies = companyService.getCompaniesForCampaign(campaignId);
		} else if( actionId != null ) {
			companies = companyService.getCompaniesForAction(actionId);
		} else if( contactId != null ) {
			companies = companyService.getCompaniesForContact(contactId);
		} else {
			companies = companyService.getAllCompanies();
		}
		if( companies != null ) {
			List<Resource<Company>> returnCompanys = new ArrayList<Resource<Company>>();
			for( Company company : companies ) {
				returnCompanys.add(getCompanyResource(company));
			}
			return ResponseEntity.ok(returnCompanys);
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Resource<Company>> getCompany(@PathVariable Integer id) {
		Company company = companyService.getCompany(id);
		if( company == null ) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(getCompanyResource(company));
		}
	}
	
	@PostMapping
	public ResponseEntity<Resource<Company>> addCompany(@RequestBody Company company, HttpServletRequest request ) {
		Company newCompany = companyService.addNewCompany(company);
		if( newCompany != null ) {
			String requestURI = request.getRequestURI();
			try {
				return ResponseEntity.created(new URI(requestURI + "/" + newCompany.getCompanyId())).build();		
			} catch( Exception e ) {
				return ResponseEntity.badRequest().build();
			}
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PutMapping("/{companyId}/Contacts")
	public ResponseEntity<List<Resource<Contact>>> addCompanyToContact(@PathVariable Integer companyId, @RequestBody Contact contact) {
		Company company = companyService.getCompany(companyId);
		if( company == null ) {
			return ResponseEntity.notFound().build();
		} else {
			Contact updatedContact = contactService.addCompanyToContact(company, contact);
			if( updatedContact != null ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}	
	
	@DeleteMapping("/{companyId}/Contacts/{contactId}")
	public ResponseEntity<Resource<Company>> removeCompanyToContact(@PathVariable Integer companyId, @PathVariable Integer contactId) {
		Company company = companyService.getCompany(companyId);
		Contact contact = contactService.getContact(contactId);
		if( company == null || contact == null ) {
			return ResponseEntity.notFound().build();
		} else {
			Contact updatedContact = contactService.removeCompanyFromContact(company, contact);
			if( updatedContact != null ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}
	
	@PutMapping("/{companyId}/Campaigns")
	public ResponseEntity<List<Resource<Company>>> addCampaignToCompany(@PathVariable Integer companyId, @RequestBody Campaign campaign) {
		Company company = companyService.getCompany(companyId);
		if( company == null ) {
			return ResponseEntity.notFound().build();
		} else {
			Company updatedCompany = companyService.addCampaignToCompany(campaign.getCampaignId(), companyId);
			if( updatedCompany != null ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}	
	
	@DeleteMapping("/{companyId}/Campaigns/{campaignId}")
	public ResponseEntity<Resource<Campaign>> removeCampaignToCompany(@PathVariable Integer companyId, @PathVariable Integer campaignId) {
		Company company = companyService.getCompany(companyId);
		Campaign campaign = campaignService.getCampaign(campaignId);
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

	
	@PutMapping("/{companyId}/Actions")
	public ResponseEntity<List<Resource<Company>>> addActionToCompany(@PathVariable Integer companyId, @RequestBody Action action) {
		Company company = companyService.getCompany(companyId);
		if( company == null ) {
			return ResponseEntity.notFound().build();
		} else {
			Company updatedCompany = companyService.addActionToCompany(action.getActionId(), companyId);
			if( updatedCompany != null ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}	
	
	@DeleteMapping("/{companyId}/Actions/{actionId}")
	public ResponseEntity<Resource<Action>> removeActionToCompany(@PathVariable Integer companyId, @PathVariable Integer actionId) {
		Company company = companyService.getCompany(companyId);
		Action action = actionService.getAction(actionId);
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
		
	
	@PutMapping("/{id}")
	public ResponseEntity<Resource<Company>> updateCompany(@RequestBody Company company, @PathVariable Integer id) {
		if( !companyService.exists(id) ) {
			return ResponseEntity.notFound().build();
		} else {
			companyService.updateCompany(id, company);
			Company updatedCompany = companyService.updateCompany(id, company);
			if( updatedCompany != null ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Resource<Company>> deleteCompany(@PathVariable Integer id) {
		if( !companyService.exists(id) ) {
			return ResponseEntity.notFound().build();
		} else {
			companyService.deleteCompany(id);
			if( !companyService.exists(id) ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}
	
	@PatchMapping
	public void processBatchAction(@RequestBody Batch batch) {
		companyService.processBatchAction(batch);
	}
	
	private Resource<Company> getCompanyResource(Company a) {
	    Resource<Company> resource = new Resource<Company>(a);
	    resource.add(linkTo(methodOn(CompanyController.class).getCompany(a.getCompanyId())).withSelfRel());
	    return resource;
	}

}








