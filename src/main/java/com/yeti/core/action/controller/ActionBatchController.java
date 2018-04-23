package com.yeti.core.action.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import com.yeti.core.action.service.ActionService;
import com.yeti.model.action.Action;

@RestController
@ExposesResourceFor(Action.class)
@RequestMapping(value = "/ActionBatch", produces = "application/hal+json")
public class ActionBatchController {

   private static final Logger log = LoggerFactory.getLogger(ActionBatchController.class);
	
	@Autowired
	private ActionService actionService;

	@GetMapping
	public ResponseEntity<List<Resource<Action>>> getActions(
			@RequestParam(required=true) Integer[] id
	) {
		List<Action> actions = actionService.getActions(id);
		if( actions != null ) {
			List<Resource<Action>> returnActions = new ArrayList<Resource<Action>>();
			for( Action action : actions ) {
				returnActions.add(getActionResource(action));
			}
			return ResponseEntity.ok(returnActions);
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Resource<Action>> getAction(@PathVariable Integer id) {
		Action action = actionService.getAction(id);
		if( action == null ) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(getActionResource(action));
		}
	}

	private Resource<Action> getActionResource(Action a) {
	    Resource<Action> resource = new Resource<Action>(a);
	    resource.add(linkTo(methodOn(ActionBatchController.class).getAction(a.getActionId())).withSelfRel());
	    return resource;
	}

}