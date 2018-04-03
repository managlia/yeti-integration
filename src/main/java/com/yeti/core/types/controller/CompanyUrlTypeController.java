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

import com.yeti.core.types.service.CompanyUrlTypeService;
import com.yeti.model.company.CompanyUrlType;
import com.yeti.model.util.Batch;

@RestController
@ExposesResourceFor(CompanyUrlType.class)
@RequestMapping(value = "/CompanyUrlTypes", produces = "application/hal+json")
public class CompanyUrlTypeController {

	@Autowired
	private CompanyUrlTypeService companyUrlTypeService;

	@GetMapping
	public ResponseEntity<List<Resource<CompanyUrlType>>> getAllCompanyUrlTypes() {
		List<CompanyUrlType> companyUrlTypes = companyUrlTypeService.getAllCompanyUrlTypes();
		if( companyUrlTypes != null ) {
			List<Resource<CompanyUrlType>> returnCompanyUrlTypes = new ArrayList<Resource<CompanyUrlType>>();
			for( CompanyUrlType companyUrlType : companyUrlTypes ) {
				returnCompanyUrlTypes.add(getCompanyUrlTypeResource(companyUrlType));
			}
			return ResponseEntity.ok(returnCompanyUrlTypes);
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Resource<CompanyUrlType>> getCompanyUrlType(@PathVariable String id) {
		CompanyUrlType companyUrlType = companyUrlTypeService.getCompanyUrlType(id);
		if( companyUrlType == null ) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(getCompanyUrlTypeResource(companyUrlType));
		}
	}
	
	@PostMapping
	public ResponseEntity<Resource<CompanyUrlType>> addCompanyUrlType(@RequestBody CompanyUrlType companyUrlType, HttpServletRequest request ) {
		CompanyUrlType newCompanyUrlType = companyUrlTypeService.addCompanyUrlType(companyUrlType);
		if( newCompanyUrlType != null ) {
			String requestURI = request.getRequestURI();
			try {
				return ResponseEntity.created(new URI(requestURI + "/" + newCompanyUrlType.getUrlTypeId())).build();		
			} catch( Exception e ) {
				return ResponseEntity.badRequest().build();
			}
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Resource<CompanyUrlType>> updateCompanyUrlType(@RequestBody CompanyUrlType companyUrlType, @PathVariable String id) {
		if( !companyUrlTypeService.exists(id) ) {
			return ResponseEntity.notFound().build();
		} else {
			companyUrlTypeService.updateCompanyUrlType(id, companyUrlType);
			CompanyUrlType updatedCompanyUrlType = companyUrlTypeService.updateCompanyUrlType(id, companyUrlType);
			if( updatedCompanyUrlType != null ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Resource<CompanyUrlType>> deleteCompanyUrlType(@PathVariable String id) {
		if( !companyUrlTypeService.exists(id) ) {
			return ResponseEntity.notFound().build();
		} else {
			companyUrlTypeService.deleteCompanyUrlType(id);
			if( !companyUrlTypeService.exists(id) ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}
	
	@PatchMapping
	public void processBatchAction(@RequestBody Batch batch) {
		companyUrlTypeService.processBatchAction(batch);
	}
	
	private Resource<CompanyUrlType> getCompanyUrlTypeResource(CompanyUrlType a) {
	    Resource<CompanyUrlType> resource = new Resource<CompanyUrlType>(a);
	    resource.add(linkTo(methodOn(CompanyUrlTypeController.class).getCompanyUrlType(a.getUrlTypeId())).withSelfRel());
	    return resource;
	}

}








