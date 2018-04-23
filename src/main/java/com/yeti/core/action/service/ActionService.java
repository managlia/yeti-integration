package com.yeti.core.action.service;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

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
import com.yeti.core.types.service.TagService;
import com.yeti.model.action.Action;
import com.yeti.model.action.Email;
import com.yeti.model.campaign.Campaign;
import com.yeti.model.company.Company;
import com.yeti.model.contact.Contact;
import com.yeti.model.util.Batch;

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
	
	
	public List<Action> getAllActions() {
		List<Action> actions = new ArrayList<Action>();
		actionRepository.findAll().forEach(actions::add);
		return actions;
	}

	public List<Action> getActions(Integer[] id) {
		List<Action> actions = new ArrayList<Action>();
		actionRepository.findAll(Arrays.asList(id)).forEach(actions::add);
		return actions;
	}
	
	public List<Action> getActionsForCompany(Integer companyId) {
		List<Action> actions = new ArrayList<Action>();
		Company queryAction = companyService.getCompany(companyId);
		if( queryAction != null ) {
			HashSet<Company> ts = new HashSet<Company>();
			ts.add(queryAction);
			actionRepository.findDistinctByCompaniesIn(ts).forEach(actions::add);
		}
		return actions;
	}

	public List<Action> getActionsForContact(Integer contactId) {
		List<Action> actions = new ArrayList<Action>();
		Contact queryAction = contactService.getContact(contactId);
		if( queryAction != null ) {
			HashSet<Contact> ts = new HashSet<Contact>();
			ts.add(queryAction);
			actionRepository.findDistinctByContactsIn(ts).forEach(actions::add);
		}
		return actions;
	}

	public List<Action> getActionsForCampaign(Integer campaignId) {
		List<Action> actions = new ArrayList<Action>();
		Campaign queryAction = campaignService.getCampaign(campaignId);
		if( queryAction != null ) {
			HashSet<Campaign> ts = new HashSet<Campaign>();
			ts.add(queryAction);
			actionRepository.findDistinctByCampaignsIn(ts).forEach(actions::add);
		}
		return actions;
	}
	
	public List<Action> getAllActionsByDescription(String actionDescription) {
		List<Action> actions = new ArrayList<Action>();
		actionRepository.findByDescriptionIgnoreCaseContaining(actionDescription).forEach(actions::add);
		return actions;
	}

	public List<Action> getAllActionsByName(String actionName) {
		List<Action> actions = new ArrayList<Action>();
		actionRepository.findByNameIgnoreCaseContaining(actionName).forEach(actions::add);
		return actions;
	}
	
	public List<Action> findUsingActiveFlag(Boolean activeFlag) {
		List<Action> actions = new ArrayList<Action>();
		actionRepository.findUsingActiveFlag(activeFlag).forEach(actions::add);
		return actions;
	} 
	
	public Action getAction(Integer id) {
		return actionRepository.findOne(id);
	}
	
	public Action addAction(Action action) {
		action.setTags( action.getTags().stream()
				.map( tag -> tag.getTagId() == null ? tagService.addTag(tag) : tag )
				.collect(Collectors.toSet())
			);
		return actionRepository.save(action);
	}

	public Action updateAction(Integer id, Action action) {

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
				email.copyActionForSubclass(action);
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
		if( action.isDeleteable() ) {
			actionRepository.delete(id);
		}
	}
	
	public void processBatch(Batch batch) {
		// TODO: Write logic for batch processing
	}
	
	public boolean exists(Integer id) {
		return actionRepository.exists(id);
	}
}
