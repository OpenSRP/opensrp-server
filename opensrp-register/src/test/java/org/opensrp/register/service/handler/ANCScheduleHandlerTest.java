package org.opensrp.register.service.handler;

import static org.mockito.Mockito.inOrder;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.motechproject.scheduletracking.api.domain.json.ScheduleRecord;
import org.motechproject.scheduletracking.api.repository.AllSchedules;
import org.opensrp.domain.Client;
import org.opensrp.domain.Event;
import org.opensrp.domain.Obs;
import org.opensrp.register.service.handler.BaseScheduleHandler.ActionType;
import org.opensrp.register.service.scheduling.AnteNatalCareSchedulesService;
import org.opensrp.repository.AllClients;
import org.opensrp.scheduler.HealthSchedulerService;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({ "org.apache.log4j.*", "org.apache.commons.logging.*" })
public class ANCScheduleHandlerTest extends TestResourceLoader {	
    @Mock
    private AnteNatalCareSchedulesService anteNatalCareSchedulesService;    
    private ANCScheduleHandler aNCScheduleHandler;
    @Mock
    private HealthSchedulerService scheduler;
    private static final String JSON_KEY_HANDLER = "handler";	
    private static final String JSON_KEY_TYPES = "types";	
    private static final String JSON_KEY_SCHEDULE_NAME = "name";	
    private static final String JSON_KEY_EVENTS = "events";	
	
    @Before
    public void setUp() throws Exception {
        initMocks(this);
        aNCScheduleHandler = new ANCScheduleHandler(anteNatalCareSchedulesService);
    }   
    
    @Test
    public void shouldTestANCScheduleHandler() throws Exception {
        Event event = geteventOfVaccination();
        JSONArray schedulesJsonObject = new JSONArray("[" + getFile() + "]");
        String scheduleName = null;
        for (int i = 0; i < schedulesJsonObject.length(); i++) {
            JSONObject scheduleJsonObject = schedulesJsonObject.getJSONObject(i);            
            JSONArray eventsJsonArray = scheduleJsonObject.getJSONArray(JSON_KEY_EVENTS);                      
            for (int j = 0; j < eventsJsonArray.length(); j++) {
                JSONObject scheduleConfigEvent = eventsJsonArray.getJSONObject(j);
                JSONArray eventTypesJsonArray = scheduleConfigEvent.getJSONArray(JSON_KEY_TYPES);
                List<String> eventsList = jsonArrayToList(eventTypesJsonArray);                
                if (eventsList.contains(event.getEventType())) {  
                	String action = aNCScheduleHandler.getAction(scheduleConfigEvent);                	
                	String milestone = aNCScheduleHandler.getMilestone(scheduleConfigEvent);
                    LocalDate  date = LocalDate.parse("2016-07-10");
                	if (milestone.equalsIgnoreCase("opv2") && action.equalsIgnoreCase(ActionType.enroll.toString())) {
                		aNCScheduleHandler.handle(event,scheduleConfigEvent, scheduleName);
                        InOrder inOrder = inOrder(anteNatalCareSchedulesService);                        
                        inOrder.verify(anteNatalCareSchedulesService).enrollMother(event.getBaseEntityId(),"Ante Natal Care Reminder Visit", LocalDate.parse("2016-07-10"),
                            event.getId());                       
                    }
                    else if (milestone.equalsIgnoreCase("opv2") && action.equalsIgnoreCase(ActionType.fulfill.toString())) {
                    	aNCScheduleHandler.handle(event,scheduleConfigEvent, scheduleName);
                        InOrder inOrder = inOrder(anteNatalCareSchedulesService);                                                
                        inOrder.verify(anteNatalCareSchedulesService).fullfillMilestone(event.getBaseEntityId(), event.getProviderId(), "Ante Natal Care Reminder Visit", date, event.getId()); 
                    } else {
                    	
                    }
                }				
            }			
        }		
    }    
   
}
