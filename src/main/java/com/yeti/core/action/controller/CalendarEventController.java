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

import com.yeti.core.action.service.CalendarEventService;
import com.yeti.model.action.CalendarEvent;
import com.yeti.model.util.Batch;

@RestController
@ExposesResourceFor(CalendarEvent.class)
@RequestMapping(value = "/CalendarEvents", produces = "application/hal+json")
public class CalendarEventController {

	@Autowired
	private CalendarEventService calendarEventService;

	@GetMapping
	public ResponseEntity<List<Resource<CalendarEvent>>> getAllCalendarEvents() {
		List<CalendarEvent> calendarEvents = calendarEventService.getAllCalendarEvents();
		if( calendarEvents != null ) {
			List<Resource<CalendarEvent>> returnCalendarEvents = new ArrayList<Resource<CalendarEvent>>();
			for( CalendarEvent calendarEvent : calendarEvents ) {
				returnCalendarEvents.add(getCalendarEventResource(calendarEvent));
			}
			return ResponseEntity.ok(returnCalendarEvents);
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Resource<CalendarEvent>> getCalendarEvent(@PathVariable Integer id) {
		CalendarEvent calendarEvent = calendarEventService.getCalendarEvent(id);
		if( calendarEvent == null ) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(getCalendarEventResource(calendarEvent));
		}
	}
	
	@PostMapping
	public ResponseEntity<Resource<CalendarEvent>> addCalendarEvent(@RequestBody CalendarEvent calendarEvent, HttpServletRequest request ) {
		CalendarEvent newCalendarEvent = calendarEventService.addCalendarEvent(calendarEvent);
		if( newCalendarEvent != null ) {
			String requestURI = request.getRequestURI();
			try {
				return ResponseEntity.created(new URI(requestURI + "/" + newCalendarEvent.getActionId())).build();		
			} catch( Exception e ) {
				return ResponseEntity.badRequest().build();
			}
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Resource<CalendarEvent>> updateCalendarEvent(@RequestBody CalendarEvent calendarEvent, @PathVariable Integer id) {
		if( !calendarEventService.exists(id) ) {
			return ResponseEntity.notFound().build();
		} else {
			calendarEventService.updateCalendarEvent(id, calendarEvent);
			CalendarEvent updatedCalendarEvent = calendarEventService.updateCalendarEvent(id, calendarEvent);
			if( updatedCalendarEvent != null ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Resource<CalendarEvent>> deleteCalendarEvent(@PathVariable Integer id) {
		if( !calendarEventService.exists(id) ) {
			return ResponseEntity.notFound().build();
		} else {
			calendarEventService.deleteCalendarEvent(id);
			if( !calendarEventService.exists(id) ) {
				return ResponseEntity.accepted().build();		
			} else {
				return ResponseEntity.badRequest().build();
			}
		}
	}
	
	@PatchMapping
	public void processBatch(@RequestBody Batch batch) {
		calendarEventService.processBatch(batch);
	}

	private Resource<CalendarEvent> getCalendarEventResource(CalendarEvent a) {
	    Resource<CalendarEvent> resource = new Resource<CalendarEvent>(a);
	    resource.add(linkTo(methodOn(CalendarEventController.class).getCalendarEvent(a.getActionId())).withSelfRel());
	    return resource;
	}

}








