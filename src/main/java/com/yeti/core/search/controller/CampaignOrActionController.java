package com.yeti.core.search.controller;

import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.yeti.core.search.service.CampaignOrActionService;
import com.yeti.model.search.CampaignOrAction;

@RestController
@ExposesResourceFor(CampaignOrAction.class)
@RequestMapping(value = "/CampaignOrActions", produces = "application/hal+json")
public class CampaignOrActionController {

	@Autowired
	private CampaignOrActionService campaignOrActionService;

	@GetMapping
	public ResponseEntity<TreeSet<CampaignOrAction>> searchCampaignOrAction(
			@RequestParam(required=false) String term,
			@RequestParam(required=false) String campaign,
			@RequestParam(required=false) String action
		) {
		TreeSet<CampaignOrAction> results = new TreeSet<CampaignOrAction>();
		if(term != null) {
			results = campaignOrActionService.searchCampaignOrAction(term);
		} else if(campaign != null) {
			results = campaignOrActionService.searchCampaign(campaign);
			
		} else if(action != null) {
			results = campaignOrActionService.searchAction(action);
			
		}
		if( results != null ) {
			return ResponseEntity.ok(results);
		} else {
			return ResponseEntity.badRequest().build();
		}
	}

}








