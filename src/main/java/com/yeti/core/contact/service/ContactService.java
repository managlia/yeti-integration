package com.yeti.core.contact.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.BeanUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yeti.core.action.service.ActionService;
import com.yeti.core.campaign.service.CampaignService;
import com.yeti.core.repository.contact.ContactRepository;
import com.yeti.core.repository.types.ContactClassificationTypeRepository;
import com.yeti.core.repository.user.UserRepository;
import com.yeti.core.types.service.TagService;
import com.yeti.model.action.Action;
import com.yeti.model.campaign.Campaign;
import com.yeti.model.company.Company;
import com.yeti.model.contact.Contact;
import com.yeti.model.contact.Team;
import com.yeti.model.host.User;
import com.yeti.model.util.Batch;

@Service
public class ContactService {
		
	private static final Logger log = LoggerFactory.getLogger(ContactService.class);

	@Autowired
	private ContactRepository contactRepository;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TeamService teamService;

	@Autowired
	private CampaignService campaignService;

	@Autowired
	private ActionService actionService;

	@Autowired
	private TagService tagService;
	
	@Autowired
	ContactClassificationTypeRepository contactClassificationTypeRepository;
	
	public List<Contact> getAllContacts(Integer companyId) {
		List<Contact> contacts = new ArrayList<Contact>();
		if( companyId == null ) {
			contactRepository.findAll().forEach(contacts::add);
		} else {
			contactRepository.findByCompanyId(companyId).forEach(contacts::add);
		}
		return contacts;
	}

	public List<Contact> getContacts(Integer[] id) {
		List<Contact> contacts = new ArrayList<Contact>();
		contactRepository.findAll(Arrays.asList(id)).forEach(contacts::add);
		return contacts;
	}
	
	public List<Contact> getContactsForCampaign(Integer campaignId) {
		List<Contact> contacts = new ArrayList<Contact>();
		Campaign queryCampaign = campaignService.getCampaign(campaignId);
		if( queryCampaign != null ) {
			HashSet<Campaign> ts = new HashSet<Campaign>();
			ts.add(queryCampaign);
			contactRepository.findDistinctByCampaignsIn(ts).forEach(contacts::add);
		}
		return contacts;
	}

	public List<Contact> getContactsForAction(Integer actionId) {
		List<Contact> contacts = new ArrayList<Contact>();
		Action queryAction = actionService.getAction(actionId);
		if( queryAction != null ) {
			HashSet<Action> ts = new HashSet<Action>();
			ts.add(queryAction);
			contactRepository.findDistinctByActionsIn(ts).forEach(contacts::add);
		}
		return contacts;
	}

	public List<Contact> getContactsForTeam(Integer teamId) {
		List<Contact> contacts = new ArrayList<Contact>();
		Team queryTeam = teamService.getTeam(teamId);
		if( queryTeam != null ) {
			HashSet<Team> ts = new HashSet<Team>();
			ts.add(queryTeam);
			contactRepository.findDistinctByTeamsIn(ts).forEach(contacts::add);
		}
		return contacts;
	}

	public Contact getContact(Integer id) {
		return contactRepository.findOne(id);
	}
	
	private Contact addCompletedContact(Contact contact) {
		contact.setTags( contact.getTags().stream()
				.map( tag -> tag.getTagId() == null ? tagService.addTag(tag) : tag )
				.collect(Collectors.toSet())
			);
		
		if( contact.getClassificationType().getClassificationTypeId().equals("HO") ) {
			User u = new User();
			// u.copyActionForSubclass(contact);

			try {
				BeanUtils.copyProperties(u, contact);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			u.setUsername("temp123");
			u.setEmailSystem("google");
			u.setEmailAddress("test@test.com");
			u.setExternalId("abc123");
			u.setPassword("abc123");
			return (Contact) userRepository.save(u);
		} else {
			return contactRepository.save(contact);
		}
	}
	
	public Contact addNewContact(Contact contact) {
		String contactClassificationTypeId = "UN";
		if( contact.getClassificationType() != null && 
			contact.getClassificationType().getClassificationTypeId() != null ) {
			contactClassificationTypeId = 
					contact.getClassificationType().getClassificationTypeId();
		}
		contact.setClassificationType(contactClassificationTypeRepository.getOne(contactClassificationTypeId));
		//contact.setCreateDate(new Date());
		contact.setActive(true);
		return addCompletedContact(contact);
	}

	public Contact addCompanyToContact(Company company, Contact contact) {
		contact.setCompanyId(company.getCompanyId());
		return updateContact(contact.getContactId(), contact);
	}

	public Contact removeCompanyFromContact(Company company, Contact contact) {
		contact.setCompanyId(null);
		return updateContact(contact.getContactId(), contact);
	}
	
	@Transactional
	public Contact addCampaignToContact(Integer campaignId, Integer contactId) {
		Contact contact = getContact(contactId);
		Campaign linkingCampaign = campaignService.getCampaign(campaignId);
		contact.getCampaigns().add(linkingCampaign);
		return updateContact(contact.getContactId(), contact);
	}

	@Transactional
	public Contact removeCampaignFromContact(Integer campaignId, Integer contactId) {
		boolean removeOne = false;
		Contact contact = getContact(contactId);
		List<Campaign> remainingCampaigns = new ArrayList<Campaign>();
		if( contact.getCampaigns() == null ) {
			return null;
		} else {
			for( Campaign existingCampaign : contact.getCampaigns() ) {
				if( existingCampaign.getCampaignId() != campaignId ) {
					remainingCampaigns.add(existingCampaign);
				} else {
					removeOne = true;
					System.out.println( "Want to remove " + campaignId + " and " + existingCampaign.getCampaignId() );
				}
			}
		}
		if( removeOne ) {
			contact.setCampaigns(remainingCampaigns);
			return updateContact(contact.getContactId(), contact);
		} else {
			return null;
		}
	}
	
	@Transactional
	public Contact addActionToContact(Integer actionId, Integer contactId) {
		Contact contact = getContact(contactId);
		Action linkingAction = actionService.getAction(actionId);
		contact.getActions().add(linkingAction);
		return updateContact(contact.getContactId(), contact);
	}

	@Transactional
	public Contact removeActionFromContact(Integer actionId, Integer contactId) {
		boolean removeOne = false;
		Contact contact = getContact(contactId);
		List<Action> remainingActions = new ArrayList<Action>();
		if( contact.getActions() == null ) {
			return null;
		} else {
			for( Action existingAction : contact.getActions() ) {
				if( existingAction.getActionId() != actionId ) {
					remainingActions.add(existingAction);
				} else {
					removeOne = true;
					System.out.println( "Want to remove " + actionId + " and " + existingAction.getActionId() );
				}
			}
		}
		if( removeOne ) {
			contact.setActions(remainingActions);
			return updateContact(contact.getContactId(), contact);
		} else {
			return null;
		}
	}
	
	@Transactional
	public Contact addTeamToContact(Integer teamId, Integer contactId) {
		Contact contact = getContact(contactId);
		Team linkingTeam = teamService.getTeam(teamId);
		contact.getTeams().add(linkingTeam);
		return updateContact(contact.getContactId(), contact);
	}

	@Transactional
	public Contact removeTeamFromContact(Integer teamId, Integer contactId) {
		boolean removeOne = false;
		Contact contact = getContact(contactId);
		List<Team> remainingTeams = new ArrayList<Team>();
		if( contact.getTeams() == null ) {
			return null;
		} else {
			for( Team existingTeam : contact.getTeams() ) {
				if( existingTeam.getTeamId() != teamId ) {
					remainingTeams.add(existingTeam);
				} else {
					removeOne = true;
					System.out.println( "Want to remove " + teamId + " and " + existingTeam.getTeamId() );
				}
			}
		}
		if( removeOne ) {
			contact.setTeams(remainingTeams);
			return updateContact(contact.getContactId(), contact);
		} else {
			return null;
		}
	}

	public Contact updateContact(Integer id, Contact contact) {
		contact.setActions(actionService.getActionsForContact(contact.getContactId()));
		contact.setCampaigns(campaignService.getCampaignsForContact(contact.getContactId()));
		contact.setTags( contact.getTags().stream()
				.map( tag -> tag.getTagId() == null ? tagService.addTag(tag) : tag )
				.collect(Collectors.toSet())
			);
		if( contact.getClassificationType().getClassificationTypeId().equals("HO") ) {
			User u = userRepository.findOne(contact.getContactId());

			try {
				BeanUtils.copyProperties(u, contact);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			log.debug("contact copied to user:: " + u.toString());
			return (Contact) userRepository.save(u);
		} else {
			return contactRepository.save(contact);
		}
	}

	public void deleteContact(Integer id) {
		contactRepository.delete(id);
	}
	
	public void processBatchAction(Batch batch) {
		// TODO: Write logic for batch processing
	}
	
	public boolean exists(Integer id) {
		return contactRepository.exists(id);
	}


	
}
