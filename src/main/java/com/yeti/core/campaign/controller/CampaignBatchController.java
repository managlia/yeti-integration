package com.yeti.core.campaign.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import com.yeti.core.campaign.service.CampaignService;
import com.yeti.model.campaign.Campaign;

@RestController
@ExposesResourceFor(Campaign.class)
@RequestMapping(value = "/CampaignBatch", produces = "application/hal+json")
public class CampaignBatchController {


	@Autowired
	private CampaignService campaignService;

	@GetMapping
	public ResponseEntity<List<Resource<Campaign>>> getCampaigns(
			@RequestParam(required=true) Integer[] id
	) {
		List<Campaign> campaigns = campaignService.getCampaigns(id);
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
	
	private Resource<Campaign> getCampaignResource(Campaign a) {
	    Resource<Campaign> resource = new Resource<Campaign>(a);
	    resource.add(linkTo(methodOn(CampaignBatchController.class).getCampaign(a.getCampaignId())).withSelfRel());
	    return resource;
	}

}