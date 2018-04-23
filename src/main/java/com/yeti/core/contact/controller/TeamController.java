package com.yeti.core.contact.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import com.yeti.core.contact.service.ContactService;
import com.yeti.core.contact.service.TeamService;
import com.yeti.model.contact.Contact;
import com.yeti.model.contact.Team;
import com.yeti.model.util.Batch;

@RestController
@ExposesResourceFor(Team.class)
@RequestMapping(value = "/Teams", produces = "application/hal+json")
public class TeamController {

	@Autowired
	private TeamService teamService;

	@Autowired
	private ContactService contactService;

	@GetMapping
	public ResponseEntity<List<Resource<Team>>> getAllTeams(
			@RequestParam(required=false) Integer contactId,
			@RequestParam(required=false) String filter
	) {
		List<Team> teams;
		if( contactId != null ) {
			teams = teamService.getTeamsForContact(contactId);
		} else {
			teams = teamService.getAllTeams(filter);
		}
		if( teams != null ) {
			List<Resource<Team>> returnTeams = new ArrayList<Resource<Team>>();
			for( Team team : teams ) {
				returnTeams.add(getTeamResource(team));
			}
			return ResponseEntity.ok(returnTeams);
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Resource<Team>> getTeam(@PathVariable Integer id ) {
		Team team = teamService.getTeam(id);
		if( team == null ) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(getTeamResource(team));
		}
	}
	
	@PostMapping
	public ResponseEntity<Resource<Team>> addTeam(@RequestBody Team team, HttpServletRequest request ) {
		Team newTeam = teamService.addNewTeam(team);
		if( newTeam != null ) {
			String requestURI = request.getRequestURI();
			try {
				return ResponseEntity.created(new URI(requestURI + "/" + newTeam.getTeamId())).build();		
			} catch( Exception e ) {
				return ResponseEntity.badRequest().build();
			}
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Resource<Team>> updateTeam(@RequestBody Team team, @PathVariable Integer id) {
		if( !teamService.exists(id) ) {
			return ResponseEntity.notFound().build();
		} else {
			teamService.updateTeam(id, team);
			Team updatedTeam = teamService.updateTeam(id, team);
			if( updatedTeam != null ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Resource<Team>> deleteTeam(@PathVariable Integer id) {
		if( !teamService.exists(id) ) {
			return ResponseEntity.notFound().build();
		} else {
			teamService.deleteTeam(id);
			if( !teamService.exists(id) ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}
	
	@PutMapping("/{teamId}/Contacts")
	public ResponseEntity<List<Resource<Team>>> addContactToTeam(@PathVariable Integer teamId, @RequestBody Contact contact) {
		Team team = teamService.getTeam(teamId);
		if( team == null ) {
			return ResponseEntity.notFound().build();
		} else {
			Team updatedTeam = teamService.addContactToTeam(contact.getContactId(), teamId);
			if( updatedTeam != null ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}	
	
	@DeleteMapping("/{teamId}/Contacts/{contactId}")
	public ResponseEntity<Resource<Contact>> removeContactToTeam(@PathVariable Integer teamId, @PathVariable Integer contactId) {
		Team team = teamService.getTeam(teamId);
		Contact contact = contactService.getContact(contactId);
		if( contact == null || team == null ) {
			return ResponseEntity.notFound().build();
		} else {
			Team updatedTeam = teamService.removeContactFromTeam(contactId, teamId);
			if( updatedTeam != null ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}
	
	@PatchMapping
	public void processBatchAction(@RequestBody Batch batch) {
		teamService.processBatchAction(batch);
	}
	
	private Resource<Team> getTeamResource(Team a) {
	    Resource<Team> resource = new Resource<Team>(a);
	    resource.add(linkTo(methodOn(TeamController.class).getTeam(a.getTeamId())).withSelfRel());
	    return resource;
	}

}








