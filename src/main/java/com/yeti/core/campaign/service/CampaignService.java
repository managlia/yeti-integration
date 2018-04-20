package com.yeti.core.campaign.service;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yeti.core.action.service.ActionService;
import com.yeti.core.company.service.CompanyService;
import com.yeti.core.contact.service.ContactService;
import com.yeti.core.repository.action.ActionRepository;
import com.yeti.core.repository.campaign.CampaignRepository;
import com.yeti.core.repository.company.CompanyRepository;
import com.yeti.core.repository.contact.ContactRepository;
import com.yeti.core.types.service.TagService;
import com.yeti.model.action.Action;
import com.yeti.model.campaign.Campaign;
import com.yeti.model.company.Company;
import com.yeti.model.contact.Contact;
import com.yeti.model.util.Batch;

@Service
public class CampaignService {
	
	@Autowired
	private CampaignRepository campaignRepository;

	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private ContactService contactService;

	@Autowired
	private ActionService actionService;
	
	@Autowired
	private TagService tagService;
	
	
	public List<Campaign> getAllCampaigns() {
		List<Campaign> campaigns = new ArrayList<Campaign>();
		campaignRepository.findAll().forEach(campaigns::add);
		return campaigns;
	}

	public List<Campaign> getCampaignsForCompany(Integer companyId) {
		List<Campaign> campaigns = new ArrayList<Campaign>();
		Company queryAction = companyService.getCompany(companyId);
		if( queryAction != null ) {
			HashSet<Company> ts = new HashSet<Company>();
			ts.add(queryAction);
			campaignRepository.findDistinctByCompaniesIn(ts).forEach(campaigns::add);
		}
		return campaigns;
	}

	public List<Campaign> getCampaignsForContact(Integer contactId) {
		List<Campaign> campaigns = new ArrayList<Campaign>();
		Contact queryAction = contactService.getContact(contactId);
		if( queryAction != null ) {
			HashSet<Contact> ts = new HashSet<Contact>();
			ts.add(queryAction);
			campaignRepository.findDistinctByContactsIn(ts).forEach(campaigns::add);
		}
		return campaigns;
	}

	public List<Campaign> getCampaignsForAction(Integer actionId) {
		List<Campaign> campaigns = new ArrayList<Campaign>();
		Action queryAction = actionService.getAction(actionId);
		if( queryAction != null ) {
			HashSet<Action> ts = new HashSet<Action>();
			ts.add(queryAction);
			campaignRepository.findDistinctByActionsIn(ts).forEach(campaigns::add);
		}
		return campaigns;
	}
	
	public Campaign getCampaign(Integer id) {
		return campaignRepository.findOne(id);
	}
	
	public Campaign addCampaign(Campaign campaign) {
		campaign.setTags( campaign.getTags().stream()
				.map( tag -> tag.getTagId() == null ? tagService.addTag(tag) : tag )
				.collect(Collectors.toSet())
			);
		return campaignRepository.save(campaign);
	}

	public Campaign updateCampaign(Integer id, Campaign campaign) {
		campaign.setContacts(contactService.getContactsForCampaign(campaign.getCampaignId()));
		campaign.setActions(actionService.getActionsForCampaign(campaign.getCampaignId()));
		campaign.setCompanies(companyService.getCompaniesForCampaign(campaign.getCampaignId()));
		campaign.setTags( campaign.getTags().stream()
				.map( tag -> tag.getTagId() == null ? tagService.addTag(tag) : tag )
				.collect(Collectors.toSet())
			);
		return campaignRepository.save(campaign);
	}
	
	@Transactional
	public Campaign addActionToCampaign(Integer actionId, Integer campaignId) {
		Campaign campaign = getCampaign(campaignId);
		Action linkingAction = actionService.getAction(actionId);
		campaign.getActions().add(linkingAction);
		return updateCampaign(campaign.getCampaignId(), campaign);
	}

	@Transactional
	public Campaign removeActionFromCampaign(Integer actionId, Integer campaignId) {
		boolean removeOne = false;
		Campaign campaign = getCampaign(campaignId);
		List<Action> remainingActions = new ArrayList<Action>();
		if( campaign.getActions() == null ) {
			return null;
		} else {
			for( Action existingAction : campaign.getActions() ) {
				if( existingAction.getActionId() != actionId ) {
					remainingActions.add(existingAction);
				} else {
					removeOne = true;
					System.out.println( "Want to remove " + actionId + " and " + existingAction.getActionId() );
				}
			}
		}
		if( removeOne ) {
			campaign.setActions(remainingActions);
			return updateCampaign(campaign.getCampaignId(), campaign);
		} else {
			return null;
		}
	}

	public Campaign addCompanyToCampaign(Company company, Campaign campaign) {
		List<Company> companies = campaign.getCompanies();
		if( companies == null ) {
			companies = new ArrayList<Company>();
		}
		companies.add(company);
		campaign.setCompanies(companies);
		return campaignRepository.save(campaign);
	}

	public Campaign removeCompanyFromCampaign(Company company, Campaign campaign) {
		List<Company> companies = campaign.getCompanies();
		if( companies == null ) {
			return null;
		} else {
			for( Company existingCompany : companies) {
				if( existingCompany.getCompanyId() == company.getCompanyId() ) {
					companies.remove(existingCompany);
				}
			}
		}
		campaign.setCompanies(companies);
		return campaignRepository.save(campaign);
	}
	
	public void deleteCampaign(Integer id) {
		campaignRepository.delete(id);
	}
	
	public void processBatchAction(Batch batch) {
		// TODO: Write logic for batch processing
	}
	
	public boolean exists(Integer id) {
		return campaignRepository.exists(id);
	}

	
}
