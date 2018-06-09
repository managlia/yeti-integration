package com.yeti.core.send.controller;

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

import com.yeti.core.send.service.SendService;
import com.yetix.model.send.SendQueue;
import com.yeti.model.action.Action;
import com.yeti.model.util.Batch;

@RestController
@ExposesResourceFor(SendQueue.class)
@RequestMapping(value = "/Sends", produces = "application/hal+json")
public class SendController {

	@Autowired
	private SendService sendService;

	@GetMapping
	public ResponseEntity<List<Resource<SendQueue>>> getAllSendQueues() {
		List<SendQueue> sends = sendService.getAllSendQueues();
		if( sends != null ) {
			List<Resource<SendQueue>> returnSendQueues = new ArrayList<Resource<SendQueue>>();
			for( SendQueue send : sends ) {
				returnSendQueues.add(getSendQueueResource(send));
			}
			return ResponseEntity.ok(returnSendQueues);
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Resource<SendQueue>> getSendQueue(@PathVariable Integer id) {
		SendQueue send = sendService.getFromSendQueue(id);
		if( send == null ) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(getSendQueueResource(send));
		}
	}
	
	@PostMapping
	public ResponseEntity<Resource<SendQueue>> addToSendQueueFromAction(@RequestBody Action action, HttpServletRequest request ) {
		SendQueue newSendQueue = sendService.addToSendQueueFromAction(action);
		if( newSendQueue != null ) {
			String requestURI = request.getRequestURI();
			try {
				return ResponseEntity.created(new URI(requestURI + "/" + newSendQueue.getSendQueueId())).build();		
			} catch( Exception e ) {
				return ResponseEntity.badRequest().build();
			}
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Resource<SendQueue>> updateSendQueue(@RequestBody Action action, @PathVariable Integer id) {
		if( !sendService.exists(id) ) {
			return ResponseEntity.notFound().build();
		} else {
			SendQueue updatedSendQueue = sendService.updateSendQueueFromAction(id, action);
			if( updatedSendQueue != null ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Resource<SendQueue>> deleteFromSendQueue(@PathVariable Integer id) {
		if( !sendService.exists(id) ) {
			return ResponseEntity.notFound().build();
		} else {
			sendService.deleteFromSendQueue(id);
			if( !sendService.exists(id) ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}
	
	@PatchMapping
	public void processBatchAction(@RequestBody Batch batch) {
		sendService.processBatchAction(batch);
	}
	
	private Resource<SendQueue> getSendQueueResource(SendQueue a) {
	    Resource<SendQueue> resource = new Resource<SendQueue>(a);
	    resource.add(linkTo(methodOn(SendController.class).getSendQueue(a.getSendQueueId())).withSelfRel());
	    return resource;
	}

}








