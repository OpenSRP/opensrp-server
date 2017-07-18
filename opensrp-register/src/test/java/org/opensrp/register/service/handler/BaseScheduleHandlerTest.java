package org.opensrp.register.service.handler;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.support.membermodification.MemberMatcher.method;

import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.motechproject.model.Time;
import org.motechproject.scheduletracking.api.domain.Enrollment;
import org.motechproject.scheduletracking.api.domain.EnrollmentStatus;
import org.motechproject.scheduletracking.api.domain.Schedule;
import org.opensrp.common.util.TestLoggerAppender;
import org.opensrp.domain.Client;
import org.opensrp.domain.Event;
import org.opensrp.register.service.handler.BaseScheduleHandler.ActionType;
import org.opensrp.repository.AllEvents;
import org.opensrp.scheduler.HealthSchedulerService;
import org.opensrp.service.ClientService;
import org.powermock.modules.junit4.PowerMockRunner;

import com.fasterxml.jackson.core.JsonParseException;

/*@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-applicationContext-opensrp-register.xml")*/
@RunWith(PowerMockRunner.class)
//@PrepareForTest(AllEvents.class)
public class BaseScheduleHandlerTest extends TestResourceLoader {	
    @Mock
    private HealthSchedulerService scheduler;	
    @Mock
    private AllEvents allEvents;
    @Mock
    private ClientService clientService;
    private VaccinesScheduleHandler  vaccinesScheduleHandler;
    private static final String JSON_KEY_HANDLER = "handler";	
    private static final String JSON_KEY_TYPES = "types";	
    private static final String JSON_KEY_SCHEDULE_NAME = "name";	
    private static final String JSON_KEY_EVENTS = "events";	
	
    @Before
    public void setUp() throws Exception {
        initMocks(this);        
        vaccinesScheduleHandler = new VaccinesScheduleHandler(scheduler, allEvents); 
        vaccinesScheduleHandler.setClientService(clientService);
    }   
    
    @Test
    public void shouldTestgetReferenceDateForSchedule() throws Exception {
        Event event = geteventOfBirthRegistration();      
        List<Event> events = getEvents("2016-02-03");
        when(allEvents, method(AllEvents.class, "findByBaseEntityIdAndConceptParentCode", String.class, String.class,String.class))
            .withArguments(anyString(), anyString(),anyString())
            .thenReturn(events);
        
        when(clientService, method(ClientService.class, "getByBaseEntityId", String.class))
        .withArguments("ooo-yyy-yyy")
        .thenReturn(getClients().get(0));
        JSONArray schedulesJsonObject = new JSONArray("[" + getFile() + "]");        
        String scheduleName = "PENTA 1";
        for (int i = 0; i < schedulesJsonObject.length(); i++) {
            JSONObject scheduleJsonObject = schedulesJsonObject.getJSONObject(i);            
            JSONArray eventsJsonArray = scheduleJsonObject.getJSONArray(JSON_KEY_EVENTS);
            scheduleName = scheduleJsonObject.getString(JSON_KEY_SCHEDULE_NAME);            
            for (int j = 0; j < eventsJsonArray.length(); j++) {
                JSONObject scheduleConfigEvent = eventsJsonArray.getJSONObject(j);
                JSONArray eventTypesJsonArray = scheduleConfigEvent.getJSONArray(JSON_KEY_TYPES);
                List<String> eventsList = jsonArrayToList(eventTypesJsonArray);                
                if (eventsList.contains(event.getEventType())) {
                    scheduleName = scheduleJsonObject.getString(JSON_KEY_SCHEDULE_NAME);;
                    String action = vaccinesScheduleHandler.getAction(scheduleConfigEvent);                    
                    String milestone = vaccinesScheduleHandler.getMilestone(scheduleConfigEvent);
                    LocalDate  date = LocalDate.parse("2016-07-10");
                    vaccinesScheduleHandler.handle(event,scheduleConfigEvent, scheduleName); 
                    vaccinesScheduleHandler.getReferenceDateForSchedule(event, scheduleConfigEvent, action);
                }				
            }			
        }        
        
    }

}
