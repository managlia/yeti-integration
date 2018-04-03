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

import com.yeti.core.types.service.CampaignClassificationTypeService;
import com.yeti.model.campaign.CampaignClassificationType;
import com.yeti.model.util.Batch;

@RestController
@ExposesResourceFor(CampaignClassificationType.class)
@RequestMapping(value = "/CampaignClassificationTypes", produces = "application/hal+json")
public class CampaignClassificationTypeController {

	@Autowired
	private CampaignClassificationTypeService campaignClassificationTypeService;

	@GetMapping
	public ResponseEntity<List<Resource<CampaignClassificationType>>> getAllCampaignClassificationTypes() {
		List<CampaignClassificationType> campaignClassificationTypes = campaignClassificationTypeService.getAllCampaignClassificationTypes();
		if( campaignClassificationTypes != null ) {
			List<Resource<CampaignClassificationType>> returnCampaignClassificationTypes = new ArrayList<Resource<CampaignClassificationType>>();
			for( CampaignClassificationType campaignClassificationType : campaignClassificationTypes ) {
				returnCampaignClassificationTypes.add(getCampaignClassificationTypeResource(campaignClassificationType));
			}
			return ResponseEntity.ok(returnCampaignClassificationTypes);
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Resource<CampaignClassificationType>> getCampaignClassificationType(@PathVariable Integer id) {
		CampaignClassificationType campaignClassificationType = campaignClassificationTypeService.getCampaignClassificationType(id);
		if( campaignClassificationType == null ) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(getCampaignClassificationTypeResource(campaignClassificationType));
		}
	}
	
	@PostMapping
	public ResponseEntity<Resource<CampaignClassificationType>> addCampaignClassificationType(@RequestBody CampaignClassificationType campaignClassificationType, HttpServletRequest request ) {
		CampaignClassificationType newCampaignClassificationType = campaignClassificationTypeService.addCampaignClassificationType(campaignClassificationType);
		if( newCampaignClassificationType != null ) {
			String requestURI = request.getRequestURI();
			try {
				return ResponseEntity.created(new URI(requestURI + "/" + newCampaignClassificationType.getCampaignClassificationTypeId())).build();		
			} catch( Exception e ) {
				return ResponseEntity.badRequest().build();
			}
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Resource<CampaignClassificationType>> updateCampaignClassificationType(@RequestBody CampaignClassificationType campaignClassificationType, @PathVariable Integer id) {
		if( !campaignClassificationTypeService.exists(id) ) {
			return ResponseEntity.notFound().build();
		} else {
			campaignClassificationTypeService.updateCampaignClassificationType(id, campaignClassificationType);
			CampaignClassificationType updatedCampaignClassificationType = campaignClassificationTypeService.updateCampaignClassificationType(id, campaignClassificationType);
			if( updatedCampaignClassificationType != null ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Resource<CampaignClassificationType>> deleteCampaignClassificationType(@PathVariable Integer id) {
		if( !campaignClassificationTypeService.exists(id) ) {
			return ResponseEntity.notFound().build();
		} else {
			campaignClassificationTypeService.deleteCampaignClassificationType(id);
			if( !campaignClassificationTypeService.exists(id) ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}
	
	@PatchMapping
	public void processBatchAction(@RequestBody Batch batch) {
		campaignClassificationTypeService.processBatchAction(batch);
	}
	
	private Resource<CampaignClassificationType> getCampaignClassificationTypeResource(CampaignClassificationType a) {
	    Resource<CampaignClassificationType> resource = new Resource<CampaignClassificationType>(a);
	    resource.add(linkTo(methodOn(CampaignClassificationTypeController.class).getCampaignClassificationType(a.getCampaignClassificationTypeId())).withSelfRel());
	    return resource;
	}

}








