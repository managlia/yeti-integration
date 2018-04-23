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
@RequestMapping(value = "/CompanyBatch", produces = "application/hal+json")
public class CompanyBatchController {


	@Autowired
	private CompanyService companyService;


	@GetMapping
	public ResponseEntity<List<Resource<Company>>> getCompanies(
			@RequestParam(required=true) Integer[] id
		) {

		List<Company> companies = companyService.getCompanies(id);
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
	
	private Resource<Company> getCompanyResource(Company a) {
	    Resource<Company> resource = new Resource<Company>(a);
	    resource.add(linkTo(methodOn(CompanyBatchController.class).getCompany(a.getCompanyId())).withSelfRel());
	    return resource;
	}

}








