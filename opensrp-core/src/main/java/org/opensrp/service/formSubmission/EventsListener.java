package org.opensrp.service.formSubmission;

import static java.text.MessageFormat.format;
import static java.util.Collections.sort;
import static org.apache.commons.lang.exception.ExceptionUtils.getFullStackTrace;

import java.text.MessageFormat;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import org.joda.time.DateTime;
import org.motechproject.scheduler.domain.MotechEvent;
import org.motechproject.server.event.annotations.MotechListener;
import org.opensrp.common.AllConstants;
import org.opensrp.domain.AppStateToken;
import org.opensrp.domain.Client;
import org.opensrp.domain.ErrorTrace;
import org.opensrp.domain.Event;
import org.opensrp.form.domain.FormSubmission;
import org.opensrp.repository.ClientsRepository;
import org.opensrp.repository.EventsRepository;
import org.opensrp.service.ConfigService;
import org.opensrp.service.ErrorTraceService;
import org.opensrp.service.EventService;
import org.opensrp.service.formSubmission.handler.EventsRouter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventsListener {
	
	private static Logger logger = LoggerFactory.getLogger(EventsListener.class.toString());
	
	private static final ReentrantLock lock = new ReentrantLock();
	
	private ConfigService configService;
	
	private EventsRepository allEvents;
	
	@Autowired
	private ClientsRepository allClients;
	
	@Autowired
	EventService eventService;
	
	private EventsRouter eventsRouter;
	
	private ErrorTraceService errorTraceService;
	
	@Autowired
	public EventsListener(EventsRouter eventsRouter, ConfigService configService, EventsRepository allEvents,
	    ErrorTraceService errorTraceService) {
		this.configService = configService;
		this.errorTraceService = errorTraceService;
		this.eventsRouter = eventsRouter;
		this.allEvents = allEvents;
		this.configService.registerAppStateToken(AllConstants.Config.EVENTS_PARSER_LAST_PROCESSED_EVENT, 0,
		    "Token to keep track of events processed for client n event parsing and schedule handling", true);
	}

	
	public EventsListener(EventsRouter eventsRouter, ConfigService configService, EventsRepository allEvents,
	    EventService eventService, ErrorTraceService errorTraceService, ClientsRepository allClients) {
		this.configService = configService;
		this.errorTraceService = errorTraceService;
		this.eventsRouter = eventsRouter;
		this.allEvents = allEvents;
		this.eventService = eventService;
		this.allClients = allClients;
		this.configService.registerAppStateToken(AllConstants.Config.EVENTS_PARSER_LAST_PROCESSED_EVENT, 0,
				"Token to keep track of events processed for client n event parsing and schedule handling", true);
	}
	
	@MotechListener(subjects = AllConstants.EVENTS_SCHEDULE_SUBJECT)
	public void processEvent(MotechEvent motechEvent) {
		if (!lock.tryLock()) {
			logger.warn("Not fetching events from Message Queue. It is already in progress.");
			return;
		}
		try {
			//update server version first
			//addServerVersion();
			logger.info("Fetching Events");
			long version = getVersion();
			
			List<Event> events = allEvents.findByServerVersion(version);
			
			if (events.isEmpty()) {
				logger.info("No new events found. Export token: " + version);
				return;
			}
			
			logger.info(format("Fetched {0} new events found. Export token: {1}", events.size(), version));
			
			sort(events, serverVersionComparator());
			
			for (Event event : events) {
				try {
					event=eventService.processOutOfArea(event);
					eventsRouter.route(event);
					configService.updateAppStateToken(AllConstants.Config.EVENTS_PARSER_LAST_PROCESSED_EVENT,
					    event.getServerVersion());
				}
				catch (Exception e) {
					e.printStackTrace();
					errorTraceService
					        .addError(new ErrorTrace(new DateTime(), "FormSubmissionProcessor", this.getClass().getName(),
					                e.getStackTrace().toString(), "unsolved", FormSubmission.class.getName()));
				}
			}
		}
		catch (Exception e) {
			logger.error(MessageFormat.format("{0} occurred while trying to fetch forms. Message: {1} with stack trace {2}",
			    e.toString(), e.getMessage(), getFullStackTrace(e)));
		}
		finally {
			lock.unlock();
		}
	}
	
	private synchronized void addServerVersion() {
		try {
			List<Client> clients = allClients.findByEmptyServerVersion();
			long currentTimeMillis = getCurrentMilliseconds();
			while (clients != null && !clients.isEmpty()) {
				for (Client client : clients) {
					try {
						Thread.sleep(1);
						client.setServerVersion(currentTimeMillis);
						allClients.update(client);
						logger.debug("Add server_version: found new client " + client.getBaseEntityId());
					}
					catch (InterruptedException e) {
						logger.error("", e);
					}
					currentTimeMillis += 1;
				}
				clients = allClients.findByEmptyServerVersion();
			}
			
			List<Event> events = allEvents.findByEmptyServerVersion();
			while (events != null && !events.isEmpty()) {
				for (Event event : events) {
					try {
						Thread.sleep(1);
						event = eventService.processOutOfArea(event);
						event.setServerVersion(currentTimeMillis);
						allEvents.update(event);
						
						logger.debug("Add server_version: found new event " + event.getBaseEntityId());
					}
					catch (InterruptedException e) {
						logger.error("", e);
					}
					currentTimeMillis += 1;
				}
				
				events = allEvents.findByEmptyServerVersion();
			}
		}
		catch (Exception e) {
			logger.error("", e);
		}
		
	}

	public long getCurrentMilliseconds() {
		return System.currentTimeMillis();
	}

	private long getVersion() {
		AppStateToken token = configService.getAppStateTokenByName(AllConstants.Config.EVENTS_PARSER_LAST_PROCESSED_EVENT);
		return token == null ? 0L : token.longValue();
	}
	
	private Comparator<Event> serverVersionComparator() {
		return new Comparator<Event>() {
			
			public int compare(Event firstEvent, Event secondEvent) {
				long firstTimestamp = firstEvent.getVersion();
				long secondTimestamp = secondEvent.getVersion();
				return firstTimestamp == secondTimestamp ? 0 : firstTimestamp < secondTimestamp ? -1 : 1;
			}
		};
	}
}
