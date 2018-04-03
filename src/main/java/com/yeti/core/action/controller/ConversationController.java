package com.yeti.core.action.controller;

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
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import com.yeti.core.action.service.ConversationService;
import com.yeti.model.action.Conversation;
import com.yeti.model.util.Batch;

@RestController
@ExposesResourceFor(Conversation.class)
@RequestMapping(value = "/Conversations", produces = "application/hal+json")
public class ConversationController {

	@Autowired
	private ConversationService conversationService;

	@GetMapping
	public ResponseEntity<List<Resource<Conversation>>> getAllConversations() {
		List<Conversation> conversations = conversationService.getAllConversations();
		if( conversations != null ) {
			List<Resource<Conversation>> returnConversations = new ArrayList<Resource<Conversation>>();
			for( Conversation conversation : conversations ) {
				returnConversations.add(getConversationResource(conversation));
			}
			return ResponseEntity.ok(returnConversations);
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Resource<Conversation>> getConversation(@PathVariable Integer id) {
		Conversation conversation = conversationService.getConversation(id);
		if( conversation == null ) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(getConversationResource(conversation));
		}
	}
	
	@PostMapping
	public ResponseEntity<Resource<Conversation>> addConversation(@RequestBody Conversation conversation, HttpServletRequest request ) {
		Conversation newConversation = conversationService.addConversation(conversation);
		if( newConversation != null ) {
			String requestURI = request.getRequestURI();
			try {
				return ResponseEntity.created(new URI(requestURI + "/" + newConversation.getActionId())).build();		
			} catch( Exception e ) {
				return ResponseEntity.badRequest().build();
			}
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Resource<Conversation>> updateConversation(@RequestBody Conversation conversation, @PathVariable Integer id) {
		if( !conversationService.exists(id) ) {
			return ResponseEntity.notFound().build();
		} else {
			conversationService.updateConversation(id, conversation);
			Conversation updatedConversation = conversationService.updateConversation(id, conversation);
			if( updatedConversation != null ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Resource<Conversation>> deleteConversation(@PathVariable Integer id) {
		if( !conversationService.exists(id) ) {
			return ResponseEntity.notFound().build();
		} else {
			conversationService.deleteConversation(id);
			if( !conversationService.exists(id) ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}
	
	@PatchMapping
	public void processBatch(@RequestBody Batch batch) {
		conversationService.processBatch(batch);
	}

	private Resource<Conversation> getConversationResource(Conversation a) {
	    Resource<Conversation> resource = new Resource<Conversation>(a);
	    resource.add(linkTo(methodOn(ConversationController.class).getConversation(a.getActionId())).withSelfRel());
	    return resource;
	}

}








