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

import com.yeti.core.types.service.CompanyClassificationTypeService;
import com.yeti.model.company.CompanyClassificationType;
import com.yeti.model.util.Batch;

@RestController
@ExposesResourceFor(CompanyClassificationType.class)
@RequestMapping(value = "/CompanyClassificationTypes", produces = "application/hal+json")
public class CompanyClassificationTypeController {

	@Autowired
	private CompanyClassificationTypeService companyClassificationTypeService;

	@GetMapping
	public ResponseEntity<List<Resource<CompanyClassificationType>>> getAllCompanyClassificationTypes() {
		List<CompanyClassificationType> companyClassificationTypes = companyClassificationTypeService.getAllCompanyClassificationTypes();
		if( companyClassificationTypes != null ) {
			List<Resource<CompanyClassificationType>> returnCompanyClassificationTypes = new ArrayList<Resource<CompanyClassificationType>>();
			for( CompanyClassificationType companyClassificationType : companyClassificationTypes ) {
				returnCompanyClassificationTypes.add(getCompanyClassificationTypeResource(companyClassificationType));
			}
			return ResponseEntity.ok(returnCompanyClassificationTypes);
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Resource<CompanyClassificationType>> getCompanyClassificationType(@PathVariable String id) {
		CompanyClassificationType companyClassificationType = companyClassificationTypeService.getCompanyClassificationType(id);
		if( companyClassificationType == null ) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(getCompanyClassificationTypeResource(companyClassificationType));
		}
	}
	
	@PostMapping
	public ResponseEntity<Resource<CompanyClassificationType>> addCompanyClassificationType(@RequestBody CompanyClassificationType companyClassificationType, HttpServletRequest request ) {
		CompanyClassificationType newCompanyClassificationType = companyClassificationTypeService.addCompanyClassificationType(companyClassificationType);
		if( newCompanyClassificationType != null ) {
			String requestURI = request.getRequestURI();
			try {
				return ResponseEntity.created(new URI(requestURI + "/" + newCompanyClassificationType.getClassificationTypeId())).build();		
			} catch( Exception e ) {
				return ResponseEntity.badRequest().build();
			}
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Resource<CompanyClassificationType>> updateCompanyClassificationType(@RequestBody CompanyClassificationType companyClassificationType, @PathVariable String id) {
		if( !companyClassificationTypeService.exists(id) ) {
			return ResponseEntity.notFound().build();
		} else {
			companyClassificationTypeService.updateCompanyClassificationType(id, companyClassificationType);
			CompanyClassificationType updatedCompanyClassificationType = companyClassificationTypeService.updateCompanyClassificationType(id, companyClassificationType);
			if( updatedCompanyClassificationType != null ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Resource<CompanyClassificationType>> deleteCompanyClassificationType(@PathVariable String id) {
		if( !companyClassificationTypeService.exists(id) ) {
			return ResponseEntity.notFound().build();
		} else {
			companyClassificationTypeService.deleteCompanyClassificationType(id);
			if( !companyClassificationTypeService.exists(id) ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}
	
	@PatchMapping
	public void processBatchAction(@RequestBody Batch batch) {
		companyClassificationTypeService.processBatchAction(batch);
	}
	
	private Resource<CompanyClassificationType> getCompanyClassificationTypeResource(CompanyClassificationType a) {
	    Resource<CompanyClassificationType> resource = new Resource<CompanyClassificationType>(a);
	    resource.add(linkTo(methodOn(CompanyClassificationTypeController.class).getCompanyClassificationType(a.getClassificationTypeId())).withSelfRel());
	    return resource;
	}

}








