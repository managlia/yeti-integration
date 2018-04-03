package com.yeti.core.company.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import java.util.HashSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yeti.core.action.service.ActionService;
import com.yeti.core.campaign.service.CampaignService;
import com.yeti.core.contact.service.ContactService;
import com.yeti.core.repository.action.ActionRepository;
import com.yeti.core.repository.campaign.CampaignRepository;
import com.yeti.core.repository.company.CompanyRepository;
import com.yeti.core.repository.contact.ContactRepository;
import com.yeti.core.repository.types.CompanyClassificationTypeRepository;
import com.yeti.model.action.Action;
import com.yeti.model.campaign.Campaign;
import com.yeti.model.company.Company;
import com.yeti.model.company.CompanyAddress;
import com.yeti.model.contact.Contact;
import com.yeti.model.general.Tag;
import com.yeti.model.util.Batch;

@Service
public class CompanyService {
	
	@Autowired
	private EntityManager em;
	
	@Autowired
	private CompanyRepository companyRepository;
	
	@Autowired
	private ContactService contactService;

	@Autowired
	private CampaignService campaignService;

	@Autowired
	private ActionService actionService;

	@Autowired
	CompanyClassificationTypeRepository companyClassificationTypeRepository;
	
	public List<Company> getAllCompanies() {
		List<Company> companies = new ArrayList<Company>();
		companyRepository.findAll().forEach(companies::add);
		return companies;
	}
	
	public List<Company> getCompaniesForCampaign(Integer campaignId) {
		List<Company> companies = new ArrayList<Company>();
		Campaign queryCampaign = campaignService.getCampaign(campaignId);
		if( queryCampaign != null ) {
			HashSet<Campaign> ts = new HashSet<Campaign>();
			ts.add(queryCampaign);
			companyRepository.findDistinctByCampaignsIn(ts).forEach(companies::add);
		}
		return companies;
	}

	public List<Company> getCompaniesForAction(Integer actionId) {
		List<Company> companies = new ArrayList<Company>();
		Action queryAction = actionService.getAction(actionId);
		if( queryAction != null ) {
			HashSet<Action> ts = new HashSet<Action>();
			ts.add(queryAction);
			companyRepository.findDistinctByActionsIn(ts).forEach(companies::add);
		}
		return companies;
	}

	public List<Company> getCompaniesForContact(Integer contactId) {
		List<Company> companies = new ArrayList<Company>();
		Contact queryAction = contactService.getContact(contactId);
		if( queryAction != null ) {
			HashSet<Contact> ts = new HashSet<Contact>();
			ts.add(queryAction);
			companyRepository.findDistinctByContactsIn(ts).forEach(companies::add);
		}
		return companies;
	}

	public Company getCompany(Integer id) {
		return companyRepository.findOne(id);
	}
	
	private Company addCompletedCompany(Company company) {
		return companyRepository.save(company);
	}

	public Company addNewCompany(Company company) {
		String companyClassificationTypeId = "UN";
		if( company.getClassificationType() != null && 
				company.getClassificationType().getClassificationTypeId() != null ) {
				companyClassificationTypeId = 
						company.getClassificationType().getClassificationTypeId();
			}
		company.setClassificationType(companyClassificationTypeRepository.getOne(companyClassificationTypeId));
		//company.setCreateDate(new Date());
		return addCompletedCompany(company);
	}
	
	@Transactional
	public Company addCampaignToCompany(Integer campaignId, Integer companyId) {
		Company company = getCompany(companyId);
		Campaign linkingCampaign = campaignService.getCampaign(campaignId);
		company.getCampaigns().add(linkingCampaign);
		return updateCompany(company.getCompanyId(), company);
	}

	@Transactional
	public Company removeCampaignFromCompany(Integer campaignId, Integer companyId) {
		boolean removeOne = false;
		Company company = getCompany(companyId);
		List<Campaign> remainingCampaigns = new ArrayList<Campaign>();
		if( company.getCampaigns() == null ) {
			return null;
		} else {
			for( Campaign existingCampaign : company.getCampaigns() ) {
				if( existingCampaign.getCampaignId() != campaignId ) {
					remainingCampaigns.add(existingCampaign);
				} else {
					removeOne = true;
					System.out.println( "Want to remove " + campaignId + " and " + existingCampaign.getCampaignId() );
				}
			}
		}
		if( removeOne ) {
			company.setCampaigns(remainingCampaigns);
			return updateCompany(company.getCompanyId(), company);
		} else {
			return null;
		}
	}
	
	@Transactional
	public Company addActionToCompany(Integer actionId, Integer companyId) {
		Company company = getCompany(companyId);
		Action linkingAction = actionService.getAction(actionId);
		company.getActions().add(linkingAction);
		return updateCompany(company.getCompanyId(), company);
	}

	@Transactional
	public Company removeActionFromCompany(Integer actionId, Integer companyId) {
		boolean removeOne = false;
		Company company = getCompany(companyId);
		List<Action> remainingActions = new ArrayList<Action>();
		if( company.getActions() == null ) {
			return null;
		} else {
			for( Action existingAction : company.getActions() ) {
				if( existingAction.getActionId() != actionId ) {
					remainingActions.add(existingAction);
				} else {
					removeOne = true;
					System.out.println( "Want to remove " + actionId + " and " + existingAction.getActionId() );
				}
			}
		}
		if( removeOne ) {
			company.setActions(remainingActions);
			return updateCompany(company.getCompanyId(), company);
		} else {
			return null;
		}
	}
	
	public Company updateCompany(Integer id, Company company) {
		company.setActions(actionService.getActionsForCompany(company.getCompanyId()));
		company.setCampaigns(campaignService.getCampaignsForCompany(company.getCompanyId()));
		return companyRepository.save(company);
	}

	public void deleteCompany(Integer id) {
		companyRepository.delete(id);
	}
	
	public void processBatchAction(Batch batch) {
		// TODO: Write logic for batch processing
	}
	
	public boolean exists(Integer id) {
		return companyRepository.exists(id);
	}

	
}
