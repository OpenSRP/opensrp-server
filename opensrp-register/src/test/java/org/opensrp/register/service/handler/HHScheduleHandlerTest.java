package org.opensrp.register.service.handler;

import static org.mockito.Mockito.inOrder;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.support.membermodification.MemberMatcher.method;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.opensrp.domain.Event;
import org.opensrp.register.service.handler.BaseScheduleHandler.ActionType;
import org.opensrp.register.service.scheduling.ENCCSchedulesService;
import org.opensrp.register.service.scheduling.HHSchedulesService;
import org.opensrp.service.ClientService;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({ "org.apache.log4j.*", "org.apache.commons.logging.*" })
public class HHScheduleHandlerTest extends TestResourceLoader {	
    @Mock
    private HHSchedulesService  hhSchedulesService;    
    private HHScheduleHandler hhScheduleHandler;    
    private static final String JSON_KEY_TYPES = "types";	
    private static final String JSON_KEY_EVENTS = "events";
    protected static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
    @Before
    public void setUp() throws Exception {
        initMocks(this);
        hhScheduleHandler = new HHScheduleHandler(hhSchedulesService);
    }   
    
    @Test
    public void shouldTestChilsScheduleHandler() throws Exception {
        Event event = geteventOfVaccination();
        JSONArray schedulesJsonObject = new JSONArray("[" + getFile() + "]");
        String scheduleName =null;        
        for (int i = 0; i < schedulesJsonObject.length(); i++) {
            JSONObject scheduleJsonObject = schedulesJsonObject.getJSONObject(i);            
            JSONArray eventsJsonArray = scheduleJsonObject.getJSONArray(JSON_KEY_EVENTS);                      
            for (int j = 0; j < eventsJsonArray.length(); j++) {
                JSONObject scheduleConfigEvent = eventsJsonArray.getJSONObject(j);
                JSONArray eventTypesJsonArray = scheduleConfigEvent.getJSONArray(JSON_KEY_TYPES);
                List<String> eventsList = jsonArrayToList(eventTypesJsonArray);                
                if (eventsList.contains(event.getEventType())) {  
                	String action = hhScheduleHandler.getAction(scheduleConfigEvent);                	
                	String milestone=hhScheduleHandler.getMilestone(scheduleConfigEvent);                    
                	if (milestone.equalsIgnoreCase("opv2") && action.equalsIgnoreCase(ActionType.enroll.toString())) {
                        hhScheduleHandler.handle(event,scheduleConfigEvent, scheduleName);                		
                        InOrder inOrder = inOrder(hhSchedulesService);                        
                        inOrder.verify(hhSchedulesService).enrollIntoMilestoneOfCensus(event.getBaseEntityId(),
        				    "2016-07-10", event.getProviderId(), "opv2",event.getId());                        
                    } else if(milestone.equalsIgnoreCase("opv2") && action.equalsIgnoreCase(ActionType.fulfill.toString())) {
                    	hhScheduleHandler.handle(event,scheduleConfigEvent, scheduleName);                		
                        InOrder inOrder = inOrder(hhSchedulesService);
                        inOrder.verify(hhSchedulesService).fullfillMilestone(event.getBaseEntityId(), event.getProviderId(), "FW CENSUS", LocalDate.parse("2016-07-10"), event.getId());
                    }
                }				
            }			
        }		
    }    
   
}
