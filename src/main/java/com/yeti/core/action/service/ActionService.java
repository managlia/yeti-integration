package com.yeti.core.action.service;

import java.util.Arrays;

import java.util.Date;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.yeti.core.campaign.service.CampaignService;
import com.yeti.core.company.service.CompanyService;
import com.yeti.core.contact.service.ContactService;
import com.yeti.core.repository.action.ActionRepository;
import com.yeti.core.repository.action.EmailRepository;
import com.yeti.core.repository.contact.ContactTeamRepository;
import com.yeti.core.types.service.TagService;
import com.yeti.model.action.Action;
import com.yeti.model.action.Email;
import com.yeti.model.campaign.Campaign;
import com.yeti.model.company.Company;
import com.yeti.model.contact.Contact;
import com.yeti.model.util.Batch;
import com.yeti.TenantContext;

@Service
public class ActionService {
	
	private static final Logger log = LoggerFactory.getLogger(ActionService.class);

	@Autowired
	private ActionRepository actionRepository;

	@Autowired
	private EmailRepository emailRepository;

	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private ContactService contactService;

	@Autowired
	private CampaignService campaignService;

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
	
	public List<Action> getAllActions() {
		Integer userId = TenantContext.getCurrentUser();
		List<Integer> teamIds = getTeamList( userId );
		
		List<Action> actions = new ArrayList<Action>();
		actionRepository.findAll(userId, teamIds).forEach(actions::add);
		return actions;
	}

	public List<Action> getActions(Integer[] id) {
		Integer userId = TenantContext.getCurrentUser();
		List<Integer> teamIds = getTeamList( userId );

		List<Action> actions = new ArrayList<Action>();
		actionRepository.findAll(userId, teamIds, Arrays.asList(id)).forEach(actions::add);
		return actions;
	}
	
	public List<Action> getActionsForCompany(Integer companyId) {
		Integer userId = TenantContext.getCurrentUser();
		List<Integer> teamIds = getTeamList( userId );
		
		List<Action> actions = new ArrayList<Action>();
		actionRepository.retrieveActionsForCompany(userId, teamIds, companyId).forEach(actions::add);
		return actions;
	}

	public List<Action> getActionsForContact(Integer contactId) {
		Integer userId = TenantContext.getCurrentUser();
		List<Integer> teamIds = getTeamList( userId );

		List<Action> actions = new ArrayList<Action>();
		actionRepository.retrieveActionsForContact(userId, teamIds, contactId).forEach(actions::add);
		return actions;
	}

	public List<Action> getActionsForCampaign(Integer campaignId) {
		Integer userId = TenantContext.getCurrentUser();
		List<Integer> teamIds = getTeamList( userId );

		List<Action> actions = new ArrayList<Action>();
		actionRepository.retrieveActionsForCampaign(userId, teamIds, campaignId).forEach(actions::add);
		return actions;
	}
	
	public Action getAction(Integer id) {
		Integer userId = TenantContext.getCurrentUser();
		List<Integer> teamIds = getTeamList( userId );
		return actionRepository.findOne(userId, teamIds, id);
	}
	
	public Action addAction(Action action) {
		action.setTags( action.getTags().stream()
				.map( tag -> tag.getTagId() == null ? tagService.addTag(tag) : tag )
				.collect(Collectors.toSet())
			);
		Date updateDate = new Date();
		action.setCreateDate(updateDate);
		action.setLastModifiedDate(updateDate);
		return actionRepository.save(action);
	}

	public Action updateAction(Integer id, Action action) {

		Date updateDate = new Date();
		action.setLastModifiedDate(updateDate);
		
		
		
		List<Contact> contacts = contactService.getContactsForAction(action.getActionId());
		if( ! CollectionUtils.isEmpty(contacts) ) {
			action.setContacts(contacts);
		}
		List<Campaign> campaigns = campaignService.getCampaignsForAction(action.getActionId());
		if( ! CollectionUtils.isEmpty(contacts) ) {
			action.setCampaigns(campaigns);
		}
		List<Company> companies = companyService.getCompaniesForAction(action.getActionId());
		if( ! CollectionUtils.isEmpty(contacts) ) {
			action.setCompanies(companies);
		}
		log.debug("dfm2 Updating action with id " + action.getActionId() + "-" + id);
		log.debug("dfm3 Updating action to string: " + action.toString());

		action.setTags( action.getTags().stream()
				.map( tag -> tag.getTagId() == null ? tagService.addTag(tag) : tag )
				.collect(Collectors.toSet())
			);
		
		if( action.getClassificationType() != null && 
				action.getClassificationType().getActionClassificationTypeId() != null ) {
			switch (action.getClassificationType().getActionClassificationTypeId()) {
			case "EM":
				log.debug("Treating action as an Email");
				Email email = emailRepository.findOne(id);

				try {
					BeanUtils.copyProperties(email, action);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return (Action) emailRepository.save(email);
			default:
				log.debug("Treating action as an Action");
				return actionRepository.save(action);
			}
		} else {
			log.debug("Treating action as an Action");
			return actionRepository.save(action);
		}
	}

	public void deleteAction(Integer id) {
		Action action = getAction(id);
		if( action !=null && action.isDeleteable() ) {
			actionRepository.delete(id);
		}
	}
	
	public void processBatch(Batch batch) {
		// TODO: Write logic for batch processing
	}
	
	public boolean exists(Integer id) {
		Integer userId = TenantContext.getCurrentUser();
		List<Integer> teamIds = getTeamList( userId );
		return actionRepository.exists(userId, teamIds, id);
	}
}
