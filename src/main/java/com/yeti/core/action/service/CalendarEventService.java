package com.yeti.core.action.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeti.core.repository.action.CalendarEventRepository;
import com.yeti.model.action.CalendarEvent;
import com.yeti.model.util.Batch;

@Service
public class CalendarEventService {
	
	@Autowired
	private CalendarEventRepository calendarEventRepository;
	
	public List<CalendarEvent> getAllCalendarEvents() {
		List<CalendarEvent> calendarEvents = new ArrayList<CalendarEvent>();
		calendarEventRepository.findAll().forEach(calendarEvents::add);
		return calendarEvents;
	}
	
	public CalendarEvent getCalendarEvent(Integer id) {
		return calendarEventRepository.findOne(id);
	}
	
	public CalendarEvent addCalendarEvent(CalendarEvent calendarEvent) {
		return calendarEventRepository.save(calendarEvent);
	}

	public CalendarEvent updateCalendarEvent(Integer id, CalendarEvent calendarEvent) {
		return calendarEventRepository.save(calendarEvent);
	}

	public void deleteCalendarEvent(Integer id) {
		calendarEventRepository.delete(id);
	}
	
	public void processBatch(Batch batch) {
		// TODO: Write logic for batch processing
	}
	
	public boolean exists(Integer id) {
		return calendarEventRepository.exists(id);
	}
	
}
