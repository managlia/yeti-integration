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

import com.yeti.core.types.service.CompanyPhoneTypeService;
import com.yeti.model.company.CompanyPhoneType;
import com.yeti.model.util.Batch;

@RestController
@ExposesResourceFor(CompanyPhoneType.class)
@RequestMapping(value = "/CompanyPhoneTypes", produces = "application/hal+json")
public class CompanyPhoneTypeController {

	@Autowired
	private CompanyPhoneTypeService companyPhoneTypeService;

	@GetMapping
	public ResponseEntity<List<Resource<CompanyPhoneType>>> getAllCompanyPhoneTypes() {
		List<CompanyPhoneType> companyPhoneTypes = companyPhoneTypeService.getAllCompanyPhoneTypes();
		if( companyPhoneTypes != null ) {
			List<Resource<CompanyPhoneType>> returnCompanyPhoneTypes = new ArrayList<Resource<CompanyPhoneType>>();
			for( CompanyPhoneType companyPhoneType : companyPhoneTypes ) {
				returnCompanyPhoneTypes.add(getCompanyPhoneTypeResource(companyPhoneType));
			}
			return ResponseEntity.ok(returnCompanyPhoneTypes);
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Resource<CompanyPhoneType>> getCompanyPhoneType(@PathVariable String id) {
		CompanyPhoneType companyPhoneType = companyPhoneTypeService.getCompanyPhoneType(id);
		if( companyPhoneType == null ) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(getCompanyPhoneTypeResource(companyPhoneType));
		}
	}
	
	@PostMapping
	public ResponseEntity<Resource<CompanyPhoneType>> addCompanyPhoneType(@RequestBody CompanyPhoneType companyPhoneType, HttpServletRequest request ) {
		CompanyPhoneType newCompanyPhoneType = companyPhoneTypeService.addCompanyPhoneType(companyPhoneType);
		if( newCompanyPhoneType != null ) {
			String requestURI = request.getRequestURI();
			try {
				return ResponseEntity.created(new URI(requestURI + "/" + newCompanyPhoneType.getPhoneTypeId())).build();		
			} catch( Exception e ) {
				return ResponseEntity.badRequest().build();
			}
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Resource<CompanyPhoneType>> updateCompanyPhoneType(@RequestBody CompanyPhoneType companyPhoneType, @PathVariable String id) {
		if( !companyPhoneTypeService.exists(id) ) {
			return ResponseEntity.notFound().build();
		} else {
			companyPhoneTypeService.updateCompanyPhoneType(id, companyPhoneType);
			CompanyPhoneType updatedCompanyPhoneType = companyPhoneTypeService.updateCompanyPhoneType(id, companyPhoneType);
			if( updatedCompanyPhoneType != null ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Resource<CompanyPhoneType>> deleteCompanyPhoneType(@PathVariable String id) {
		if( !companyPhoneTypeService.exists(id) ) {
			return ResponseEntity.notFound().build();
		} else {
			companyPhoneTypeService.deleteCompanyPhoneType(id);
			if( !companyPhoneTypeService.exists(id) ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}
	
	@PatchMapping
	public void processBatchAction(@RequestBody Batch batch) {
		companyPhoneTypeService.processBatchAction(batch);
	}
	
	private Resource<CompanyPhoneType> getCompanyPhoneTypeResource(CompanyPhoneType a) {
	    Resource<CompanyPhoneType> resource = new Resource<CompanyPhoneType>(a);
	    resource.add(linkTo(methodOn(CompanyPhoneTypeController.class).getCompanyPhoneType(a.getPhoneTypeId())).withSelfRel());
	    return resource;
	}

}








