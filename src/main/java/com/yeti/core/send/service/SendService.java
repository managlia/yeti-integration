package com.yeti.core.send.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import net.fortuna.ical4j.model.Date;

import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Dur;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.util.RandomUidGenerator;
import net.fortuna.ical4j.util.UidGenerator;
import net.fortuna.ical4j.validate.ValidationException;
import net.fortuna.ical4j.data.CalendarOutputter;

import com.yetix.core.repository.send.SendQueueRepository;
import com.yeti.TenantContext;
import com.yeti.core.contact.service.ContactService;
import com.yeti.core.repository.contact.ContactTeamRepository;
import com.yeti.model.action.Action;
import com.yeti.model.contact.Contact;
import com.yeti.model.util.Batch;
import com.yetix.model.send.SendQueue;

@Service
public class SendService {

	private static final Logger log = LoggerFactory.getLogger(SendService.class);

	@Autowired
	private SendQueueRepository sendQueueRepository;

	@Autowired
	private ContactTeamRepository contactTeamRepository;
	
	@Autowired
	private ContactService contactService;

	private List<Integer> getTeamList(Integer userId) {
		List<Integer> teamIds = contactTeamRepository.getTeamIds( userId );
		if( teamIds == null ) teamIds = new ArrayList<Integer>();
		teamIds.add(new Integer(999999999));
		return teamIds;
	}

	public List<SendQueue> getAllSendQueues() {
		List<SendQueue> sendQueues = new ArrayList<SendQueue>();
		sendQueueRepository.findAll().forEach(sendQueues::add);
		return sendQueues;
	}

	public SendQueue getFromSendQueue(Integer sendQueueId) {
		Integer userId = TenantContext.getCurrentUser();
		List<Integer> teamIds = getTeamList( userId );
		System.out.println("Team Ids: " + teamIds);
		//return sendQueueRepository.findOne(userId, teamIds, sendQueueId);
		return null;
	}

	public SendQueue addToSendQueueFromAction(Action action) {
		Integer userId = TenantContext.getCurrentUser();
		try {
			List<Contact> contacts = contactService.getContactsForAction(action.getActionId());
			
			String[] recips = contacts.stream().map( a -> a.getContactId().toString()).toArray(String[]::new);
			log.debug("recipis:: " + java.util.Arrays.toString(recips) );

			String[] recipEmails = contacts.stream().map( a -> a.getContactEmailAddress()).toArray(String[]::new);
			log.debug("recipiEmails:: " + java.util.Arrays.toString(recipEmails) );
			
			SendQueue sq = new SendQueue();
			
			sq.setSendQueueCreatorId(userId.toString());
			sq.setSendQueueItemType("ical");
			sq.setSendQueueStatus("send_pending");

			File f = getIcsPayloadFromAction(action);
			sq.setSendQueuePayload(f);
			
			sq.setLinkedToEntityType("action");
			sq.setLinkedToEntityId(action.getActionId().toString());
	
			sq.setRecipients(recips);
	
			java.util.Date updateDate = new java.util.Date();
			sq.setSendQueueCreateDate(updateDate);
			return sendQueueRepository.save(sq);
		} catch( Exception e ) {
			log.error("create failed", e);
			e.printStackTrace();
			return null;
		}
	}

	private File getIcsPayloadFromAction(Action action) throws ValidationException, IOException {
		Date startDate = new Date(action.getTargetCompletionDate());
		Date endDate = new Date(action.getTargetCompletionDateEnd());
				
				
		VEvent theCalendarEvent = new VEvent( startDate, endDate, action.getDescription() );
		
		
		// Generate a UID for the event..
		UidGenerator ug = new RandomUidGenerator();
		theCalendarEvent.getProperties().add(ug.generateUid());
		
		Calendar calendar = new Calendar();
		calendar.getProperties().add(new ProdId("-//YETI//iCal4j 1.0//EN"));
		calendar.getProperties().add(Version.VERSION_2_0);
		calendar.getProperties().add(CalScale.GREGORIAN);
		calendar.getComponents().add(theCalendarEvent);
		
		
		File f = new File("yetical.ics");
		FileOutputStream fout = new FileOutputStream(f);
		CalendarOutputter outputter = new CalendarOutputter();
		outputter.output(calendar, fout);
		return f;
	}

	public SendQueue updateSendQueueFromAction(Integer sendQueueId, Action action) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean exists(Integer sendQueueId) {
		// TODO Auto-generated method stub
		return false;
	}

	public void deleteFromSendQueue(Integer sendQueueId) {
		// TODO Auto-generated method stub
		
	}

	public void processBatchAction(Batch batch) {
		// TODO Auto-generated method stub
		
	}


}
