package com.yeti.core.campaign.service;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yeti.TenantContext;
import com.yeti.core.action.service.ActionService;
import com.yeti.core.company.service.CompanyService;
import com.yeti.core.contact.service.ContactService;
import com.yeti.core.repository.campaign.CampaignRepository;
import com.yeti.core.repository.contact.ContactTeamRepository;
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
	
	@Autowired
	private ContactTeamRepository contactTeamRepository;	
	
	private List<Integer> getTeamList(Integer userId) {
		List<Integer> teamIds = contactTeamRepository.getTeamIds( userId );
		if( teamIds == null ) teamIds = new ArrayList<Integer>();
		teamIds.add(new Integer(999999999));
		return teamIds;
	}
	
	public List<Campaign> getAllCampaigns() {
		Integer userId = TenantContext.getCurrentUser();
		List<Integer> teamIds = getTeamList( userId );
		List<Campaign> campaigns = new ArrayList<Campaign>();
		campaignRepository.findAll(userId, teamIds).forEach(campaigns::add);
		return campaigns;
	}

	public List<Campaign> getCampaigns(Integer[] id) {
		Integer userId = TenantContext.getCurrentUser();
		List<Integer> teamIds = getTeamList( userId );
		List<Campaign> campaigns = new ArrayList<Campaign>();
		campaignRepository.findAll(userId, teamIds, Arrays.asList(id)).forEach(campaigns::add);
		return campaigns;
	}

	public List<Campaign> getCampaignsForCompany(Integer companyId) {
		Integer userId = TenantContext.getCurrentUser();
		List<Integer> teamIds = getTeamList( userId );
		List<Campaign> campaigns = new ArrayList<Campaign>();
		campaignRepository.retrieveCampaignsForCompany(userId, teamIds, companyId).forEach(campaigns::add);
		return campaigns;
	}

	public List<Campaign> getCampaignsForContact(Integer contactId) {
		Integer userId = TenantContext.getCurrentUser();
		List<Integer> teamIds = getTeamList( userId );
		List<Campaign> campaigns = new ArrayList<Campaign>();
		campaignRepository.retrieveCampaignsForContact(userId, teamIds, contactId).forEach(campaigns::add);
		return campaigns;
	}

	public List<Campaign> getCampaignsForAction(Integer actionId) {
		Integer userId = TenantContext.getCurrentUser();
		List<Integer> teamIds = getTeamList( userId );
		List<Campaign> campaigns = new ArrayList<Campaign>();
		campaignRepository.retrieveCampaignsForAction(userId, teamIds, actionId).forEach(campaigns::add);
		return campaigns;
	}
	
	public Campaign getCampaign(Integer id) {
		Integer userId = TenantContext.getCurrentUser();
		List<Integer> teamIds = getTeamList( userId );
		return campaignRepository.findOne(userId, teamIds, id);
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
				if( existingAction.getActionId().intValue() != actionId.intValue() ) {
					remainingActions.add(existingAction);
				} else {
					removeOne = true;
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
				if( existingCompany.getCompanyId().intValue() == company.getCompanyId().intValue() ) {
					companies.remove(existingCompany);
				}
			}
		}
		campaign.setCompanies(companies);
		return campaignRepository.save(campaign);
	}
	
	public void deleteCampaign(Integer id) {
		if( exists(id) ) {
			campaignRepository.delete(id);
		}
	}
	
	public void processBatchAction(Batch batch) {
		// TODO: Write logic for batch processing
	}
	
	public boolean exists(Integer id) {
		Integer userId = TenantContext.getCurrentUser();
		List<Integer> teamIds = getTeamList( userId );
		return campaignRepository.exists(userId, teamIds, id);
	}
}
