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

import com.yeti.core.types.service.CompanyAddressTypeService;
import com.yeti.model.company.CompanyAddressType;
import com.yeti.model.util.Batch;

@RestController
@ExposesResourceFor(CompanyAddressType.class)
@RequestMapping(value = "/CompanyAddressTypes", produces = "application/hal+json")
public class CompanyAddressTypeController {

	@Autowired
	private CompanyAddressTypeService companyAddressTypeService;

	@GetMapping
	public ResponseEntity<List<Resource<CompanyAddressType>>> getAllCompanyAddressTypes() {
		List<CompanyAddressType> companyAddressTypes = companyAddressTypeService.getAllCompanyAddressTypes();
		if( companyAddressTypes != null ) {
			List<Resource<CompanyAddressType>> returnCompanyAddressTypes = new ArrayList<Resource<CompanyAddressType>>();
			for( CompanyAddressType companyAddressType : companyAddressTypes ) {
				returnCompanyAddressTypes.add(getCompanyAddressTypeResource(companyAddressType));
			}
			return ResponseEntity.ok(returnCompanyAddressTypes);
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Resource<CompanyAddressType>> getCompanyAddressType(@PathVariable String id) {
		CompanyAddressType companyAddressType = companyAddressTypeService.getCompanyAddressType(id);
		if( companyAddressType == null ) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(getCompanyAddressTypeResource(companyAddressType));
		}
	}
	
	@PostMapping
	public ResponseEntity<Resource<CompanyAddressType>> addCompanyAddressType(@RequestBody CompanyAddressType companyAddressType, HttpServletRequest request ) {
		CompanyAddressType newCompanyAddressType = companyAddressTypeService.addCompanyAddressType(companyAddressType);
		if( newCompanyAddressType != null ) {
			String requestURI = request.getRequestURI();
			try {
				return ResponseEntity.created(new URI(requestURI + "/" + newCompanyAddressType.getAddressTypeId())).build();		
			} catch( Exception e ) {
				return ResponseEntity.badRequest().build();
			}
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Resource<CompanyAddressType>> updateCompanyAddressType(@RequestBody CompanyAddressType companyAddressType, @PathVariable String id) {
		if( !companyAddressTypeService.exists(id) ) {
			return ResponseEntity.notFound().build();
		} else {
			companyAddressTypeService.updateCompanyAddressType(id, companyAddressType);
			CompanyAddressType updatedCompanyAddressType = companyAddressTypeService.updateCompanyAddressType(id, companyAddressType);
			if( updatedCompanyAddressType != null ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Resource<CompanyAddressType>> deleteCompanyAddressType(@PathVariable String id) {
		if( !companyAddressTypeService.exists(id) ) {
			return ResponseEntity.notFound().build();
		} else {
			companyAddressTypeService.deleteCompanyAddressType(id);
			if( !companyAddressTypeService.exists(id) ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}
	
	@PatchMapping
	public void processBatchAction(@RequestBody Batch batch) {
		companyAddressTypeService.processBatchAction(batch);
	}
	
	private Resource<CompanyAddressType> getCompanyAddressTypeResource(CompanyAddressType a) {
	    Resource<CompanyAddressType> resource = new Resource<CompanyAddressType>(a);
	    resource.add(linkTo(methodOn(CompanyAddressTypeController.class).getCompanyAddressType(a.getAddressTypeId())).withSelfRel());
	    return resource;
	}

}








