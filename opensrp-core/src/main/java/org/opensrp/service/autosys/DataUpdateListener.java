package org.opensrp.service.autosys;

import static java.text.MessageFormat.format;
import static java.util.Collections.sort;
import static org.apache.commons.lang.exception.ExceptionUtils.getFullStackTrace;

import java.text.MessageFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import org.joda.time.DateTime;
import org.motechproject.scheduler.domain.MotechEvent;
import org.motechproject.server.event.annotations.MotechListener;
import org.opensrp.common.AllConstants;
import org.opensrp.domain.AppStateToken;
import org.opensrp.domain.ErrorTrace;
import org.opensrp.domain.Event;
import org.opensrp.dto.form.FormSubmissionDTO;
import org.opensrp.form.domain.FormSubmission;
import org.opensrp.form.service.FormSubmissionService;
import org.opensrp.service.ClientService;
import org.opensrp.service.ConfigService;
import org.opensrp.service.ErrorTraceService;
import org.opensrp.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Component
public class DataUpdateListener {
    private static Logger logger = LoggerFactory.getLogger(DataUpdateListener.class.toString());
    private static final ReentrantLock lock = new ReentrantLock();
    private ClientService clientService;
    private EventService eventService;
    private ConfigService configService;
    private FormSubmissionProcessor fsp;
    private ErrorTraceService errorTraceService;

    @Autowired
    public DataUpdateListener(ClientService clientService, EventService eventService, 
    		FormSubmissionProcessor fsp,
    		ConfigService configService, ErrorTraceService errorTraceService) {
        this.clientService = clientService;
        this.eventService = eventService;
        this.configService = configService;
        this.errorTraceService = errorTraceService;
        this.fsp = fsp;
		this.configService.registerAppStateToken(AllConstants.Config.FORM_ENTITY_PARSER_LAST_SYNCED_FORM_SUBMISSION, 
			0, "Token to keep track of forms processed for client n event parsing and schedule handling", true);
    }

    @MotechListener(subjects = AllConstants.FORM_SCHEDULE_SUBJECT)
    public void parseForms(MotechEvent event) {
        if (!lock.tryLock()) {
            logger.warn("Not fetching forms from Message Queue. It is already in progress.");
            return;
        }
        try {
            logger.info("Fetching Forms");
            long version = getVersion();

            List<Event> events = eventService.findEventsBy(null, null, null, null, null, null, null, new DateTime(version) , DateTime.now());
            if (events.isEmpty()) {
                logger.info("No new events found. Export token: " + version);
                return;
            }

            logger.info(format("Fetched {0} new forms found. Export token: {1}", events.size(), version));
            
            sort(events, serverVersionComparator());

            for (Event e : events) {
            	try{
	            	logger.info(format("Invoking save for form with instance Id: {0} and for entity Id: {1}", submission.instanceId(), submission.entityId()));
	
	            	if(submission.getField("no_client_event") == null || submission.getField("no_client_event").contains("false")){
		            	fsp.processFormSubmission(submission);
	            	}
	            	
	            	configService.updateAppStateToken(AllConstants.Config.FORM_ENTITY_PARSER_LAST_SYNCED_FORM_SUBMISSION, submission.serverVersion());
            	}
            	catch(Exception e){
            		e.printStackTrace();
            		errorTraceService.addError(new ErrorTrace(new Date(), "FormSubmissionProcessor", this.getClass().getName(), e.getStackTrace().toString(), "unsolved", FormSubmission.class.getName()));
            	}
            }
        } catch (Exception e) {
            logger.error(MessageFormat.format("{0} occurred while trying to fetch forms. Message: {1} with stack trace {2}",
                    e.toString(), e.getMessage(), getFullStackTrace(e)));
        } finally {
            lock.unlock();
        }
    }

    private long getVersion() {
        AppStateToken token = configService.getAppStateTokenByName(AllConstants.Config.FORM_ENTITY_PARSER_LAST_SYNCED_FORM_SUBMISSION);
        return token==null?0L:token.longValue();
    }
    
    private Comparator<Event> serverVersionComparator() {
        return new Comparator<Event>() {
            public int compare(Event first, Event second) {
                long firstTimestamp = first.getDateCreated().getTime();
                long secondTimestamp = second.getDateCreated().getTime();
                return firstTimestamp == secondTimestamp ? 0 : firstTimestamp < secondTimestamp ? -1 : 1;
            }
        };
    }
}
