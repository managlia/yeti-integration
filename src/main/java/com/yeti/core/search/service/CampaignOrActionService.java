package com.yeti.core.search.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeti.core.repository.campaign.CampaignRepository;
import com.yeti.core.repository.contact.ContactTeamRepository;
import com.yeti.core.repository.action.ActionRepository;
import com.yeti.model.campaign.Campaign;
import com.yeti.model.action.Action;
import com.yeti.model.search.CampaignOrAction;
import com.yeti.TenantContext;

@Service
public class CampaignOrActionService {

	private static final Logger log = LoggerFactory.getLogger(CampaignOrActionService.class);

	@Autowired
	private CampaignRepository campaignRepository;
	
	@Autowired
	private ActionRepository actionRepository;
	
	@Autowired
	private ContactTeamRepository contactTeamRepository;

	private List<Integer> getTeamList(Integer userId) {
		List<Integer> teamIds = contactTeamRepository.getTeamIds( userId );
		if( teamIds == null ) teamIds = new ArrayList<Integer>();
		teamIds.add(new Integer(999999999));
		return teamIds;
	}
	
	public TreeSet<CampaignOrAction> searchCampaignOrAction(String term) {
		Integer userId = TenantContext.getCurrentUser();
		List<Integer> teamIds = getTeamList( userId );
		log.debug("dfm -------------------------> user id " + userId);
		teamIds.stream().forEach(a -> log.debug("dfm -------------------------> one team id is " + a));
		
		term = term.toLowerCase();
		LinkedList<String> terms = new LinkedList<String>();
		StringTokenizer st = new StringTokenizer(term);
		while( st.hasMoreTokens() ) {
			terms.add(st.nextToken());
		}
		TreeSet<CampaignOrAction> cocs = new TreeSet<CampaignOrAction>();
		Future<List<Campaign>> futureCampaignList = campaignRepository.searchCampaignsByTerm(
				userId, 
				teamIds, 
				terms.getFirst());
		Future<List<Action>> futureActionList = actionRepository.searchActionsByTerm(
				userId, 
				teamIds, 
				terms.getFirst());
		cocs.addAll(this.searchCampaigns(terms, futureCampaignList));
		cocs.addAll(this.searchActions(terms, futureActionList));
		return cocs;
	}

	public TreeSet<CampaignOrAction> searchCampaign(String campaign) {
		Integer userId = TenantContext.getCurrentUser();
		List<Integer> teamIds = getTeamList( userId );
		log.debug("dfm -------------------------> user id " + userId);
		teamIds.stream().forEach(a -> log.debug("dfm -------------------------> one team id is " + a));
		
		String term = campaign.toLowerCase();
		LinkedList<String> terms = new LinkedList<String>();
		StringTokenizer st = new StringTokenizer(term);
		while( st.hasMoreTokens() ) {
			terms.add(st.nextToken());
		}
		TreeSet<CampaignOrAction> cocs = new TreeSet<CampaignOrAction>();
		Future<List<Campaign>> futureCampaignList = campaignRepository.searchCampaignsByTerm(
				userId, 
				teamIds, 
				terms.getFirst());
		cocs.addAll(this.searchCampaigns(terms, futureCampaignList));
		return cocs;
	}

	public TreeSet<CampaignOrAction> searchAction(String action) {
		Integer userId = TenantContext.getCurrentUser();
		List<Integer> teamIds = getTeamList( userId );
		log.debug("dfm -------------------------> user id " + userId);
		teamIds.stream().forEach(a -> log.debug("dfm -------------------------> one team id is " + a));

		String term = action.toLowerCase();
		LinkedList<String> terms = new LinkedList<String>();
		StringTokenizer st = new StringTokenizer(term);
		while( st.hasMoreTokens() ) {
			terms.add(st.nextToken());
		}
		TreeSet<CampaignOrAction> cocs = new TreeSet<CampaignOrAction>();
		Future<List<Action>> futureActionList = actionRepository.searchActionsByTerm(
				userId, 
				teamIds, 
				terms.getFirst());
		cocs.addAll(this.searchActions(terms, futureActionList));
		return cocs;
	}
	
	public TreeSet<CampaignOrAction> searchCampaigns(LinkedList<String> terms, Future<List<Campaign>> future) {
		TreeSet<CampaignOrAction> cocs = new TreeSet<CampaignOrAction>();

		try {
			List<Campaign> campaigns = new ArrayList<Campaign>();
			while(!future.isDone()) {
				Thread.sleep(50);
			}
			campaigns = future.get();
			here: for( Campaign campaign : campaigns ) {
				String concatString = campaign.getName() + ", " + campaign.getDescription();
				for( String term: terms ) {
					if( concatString.toLowerCase().indexOf(term) == -1 ) {
						continue here;
					}
				}
				CampaignOrAction coc = new CampaignOrAction();
				coc.setMatchTerm(concatString);
				coc.setEntiyType("campaign");
				coc.setCampaignId(campaign.getCampaignId());
				cocs.add(coc);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return cocs;
	}
	
	public TreeSet<CampaignOrAction> searchActions(LinkedList<String> terms, Future<List<Action>> future) {
		TreeSet<CampaignOrAction> cocs = new TreeSet<CampaignOrAction>();
		try {
			List<Action> actions = new ArrayList<Action>();
			while(!future.isDone()) {
				Thread.sleep(50);
			}
			actions = future.get();
			here: for( Action action : actions ) {
				String concatString = action.getName() + " " + action.getDescription();
				for( String term: terms ) {
					if( concatString.toLowerCase().indexOf(term) == -1 ) {
						continue here;
					}
				}
				CampaignOrAction coc = new CampaignOrAction();
				coc.setMatchTerm(concatString);
				coc.setEntiyType("action");
				coc.setActionId(action.getActionId());
				cocs.add(coc);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return cocs;
	}
}
