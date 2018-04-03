package com.yeti.core.search.controller;

import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.yeti.core.search.service.CompanyOrContactService;
import com.yeti.model.search.CompanyOrContact;

@RestController
@ExposesResourceFor(CompanyOrContact.class)
@RequestMapping(value = "/CompanyOrContacts", produces = "application/hal+json")
public class CompanyOrContactController {

	@Autowired
	private CompanyOrContactService companyOrContactService;

	@GetMapping
	public ResponseEntity<TreeSet<CompanyOrContact>> searchCompanyOrContact(
			@RequestParam(required=false) String term,
			@RequestParam(required=false) String company,
			@RequestParam(required=false) String contact
		) {
		TreeSet<CompanyOrContact> results = new TreeSet<CompanyOrContact>();
		if(term != null) {
			results = companyOrContactService.searchCompanyOrContact(term);
		} else if(company != null) {
			results = companyOrContactService.searchCompany(company);
			
		} else if(contact != null) {
			results = companyOrContactService.searchContact(contact);
			
		}
		if( results != null ) {
			return ResponseEntity.ok(results);
		} else {
			return ResponseEntity.badRequest().build();
		}
	}

}








