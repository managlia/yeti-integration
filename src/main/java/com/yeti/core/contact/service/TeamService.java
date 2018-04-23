package com.yeti.core.contact.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yeti.core.contact.service.ContactService;
import com.yeti.core.repository.contact.TeamRepository;
import com.yeti.model.contact.Contact;
import com.yeti.model.contact.Team;
import com.yeti.model.util.Batch;

@Service
public class TeamService {

	private static final Logger log = LoggerFactory.getLogger(TeamService.class);
	
	@Autowired
	private TeamRepository teamRepository;

	@Autowired
	private ContactService contactService;

	public List<Team> getAllTeams(String filter) {
		List<Team> teams = new ArrayList<Team>();
		if( filter != null && filter.length() > 0) {
			/*
				INTENTIONALLY COMMENTED BECAUSE WE DON'T WANT TEAMS TO BE SEARCHABLE (WE DON'T THINK)
				
			String term = filter.toLowerCase();
			LinkedList<String> terms = new LinkedList<String>();
			StringTokenizer st = new StringTokenizer(term);
			while( st.hasMoreTokens() ) {
				terms.add(st.nextToken());
			}
			
			Future<List<Team>> futureTeamList = teamRepository.searchTeamsByTerm(terms.getFirst());
			teams.addAll(this.filterTeams(terms, futureTeamList));
			*/
		} else {
			teamRepository.findAll().forEach(teams::add);
		}
		return teams;
	}
	
	public TreeSet<Team> filterTeams(LinkedList<String> terms, Future<List<Team>> future) {
		TreeSet<Team> teams = new TreeSet<Team>();
		try {
			List<Team> actions = new ArrayList<Team>();
			while(!future.isDone()) {
				Thread.sleep(50);
			}
			actions = future.get();
			here: for( Team action : actions ) {
				String concatString = action.getName() + " " + action.getDescription();
				for( String term: terms ) {
					if( concatString.toLowerCase().indexOf(term) == -1 ) {
						continue here;
					}
				}
				Team team = new Team();
				team.setMatchTerm(concatString);
				team.setEntiyType("team");
				team.setTeamId(action.getTeamId());
				teams.add(team);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return teams;
	}

	public List<Team> getTeams(Integer[] id) {
		List<Team> teams = new ArrayList<Team>();
		teamRepository.findAll(Arrays.asList(id)).forEach(teams::add);
		return teams;
	}
	
	public List<Team> getTeamsForContact(Integer contactId) {
		List<Team> teams = new ArrayList<Team>();
		Contact queryContact = contactService.getContact(contactId);
		if( queryContact != null ) {
			HashSet<Contact> ts = new HashSet<Contact>();
			ts.add(queryContact);
			teamRepository.findDistinctByContactsIn(ts).forEach(teams::add);
		}
		return teams;
	}

	public Team getTeam(Integer id) {
		return teamRepository.findOne(id);
	}
	
	private Team addCompletedTeam(Team team) {
		return teamRepository.save(team);
	}
	
	@Transactional
	public Team addNewTeam(Team team) {
		//team.setCreateDate(new Date());
		Team newTeam = addCompletedTeam(team);
		log.debug("dfm added team");
		log.debug("dfm team id " +  newTeam.getTeamId());
		log.debug("dfm creatorId id " + newTeam.getCreatorId());
		
		return addContactToTeam( newTeam.getCreatorId(), newTeam.getTeamId() );
	}

	@Transactional
	public Team addContactToTeam(Integer contactId, Integer teamId) {
		Team team = getTeam(teamId);
		Contact linkingContact = contactService.getContact(contactId);
		log.debug("dfm ready to add contact with contactId == " + linkingContact.getContactId() 
					+ " to team with teamId " + team.getTeamId() );
		team.getContacts().add(linkingContact);
		return updateTeam(team.getTeamId(), team);
	}

	@Transactional
	public Team removeContactFromTeam(Integer contactId, Integer teamId) {
		boolean removeOne = false;
		Team team = getTeam(teamId);
		List<Contact> remainingContacts = new ArrayList<Contact>();
		if( team.getContacts() == null ) {
			return null;
		} else {
			for( Contact existingContact : team.getContacts() ) {
				if( existingContact.getContactId() != contactId ) {
					remainingContacts.add(existingContact);
				} else {
					removeOne = true;
					System.out.println( "Want to remove " + contactId + " and " + existingContact.getContactId() );
				}
			}
		}
		if( removeOne ) {
			team.setContacts(remainingContacts);
			return updateTeam(team.getTeamId(), team);
		} else {
			return null;
		}
	}
	
	public Team updateTeam(Integer id, Team team) {
		team.setContacts(contactService.getContactsForTeam(team.getTeamId()));
		return teamRepository.save(team);
	}

	public void deleteTeam(Integer id) {
		teamRepository.delete(id);
	}
	
	public void processBatchAction(Batch batch) {
		// TODO: Write logic for batch processing
	}
	
	public boolean exists(Integer id) {
		return teamRepository.exists(id);
	}


	
}
