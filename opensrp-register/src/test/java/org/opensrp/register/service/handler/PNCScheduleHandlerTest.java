package org.opensrp.register.service.handler;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.motechproject.model.Time;
import org.motechproject.scheduletracking.api.domain.Enrollment;
import org.motechproject.scheduletracking.api.domain.EnrollmentStatus;
import org.motechproject.scheduletracking.api.repository.AllEnrollments;
import org.motechproject.scheduletracking.api.repository.AllSchedules;
import org.opensrp.domain.Client;
import org.opensrp.domain.Event;
import org.opensrp.domain.Obs;
import org.opensrp.register.service.handler.BaseScheduleHandler.ActionType;
import org.opensrp.register.service.scheduling.AnteNatalCareSchedulesService;
import org.opensrp.register.service.scheduling.PNCSchedulesService;
import org.opensrp.repository.AllClients;
import org.opensrp.scheduler.HealthSchedulerService;
import org.opensrp.scheduler.service.ScheduleService;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/*@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-applicationContext-opensrp-register.xml")
*/
@RunWith(PowerMockRunner.class)
//@PrepareForTest({PNCSchedulesService.class})
@PowerMockIgnore({ "org.apache.log4j.*", "org.apache.commons.logging.*" })
public class PNCScheduleHandlerTest extends TestResourceLoader {
	public PNCScheduleHandlerTest() {
		
	}
	private PNCScheduleHandler pncScheduleHandler;
    @Mock
    private AnteNatalCareSchedulesService anteNatalCareSchedulesService;    
    @Mock
    private AllClients allClients;
    @Mock
    private AllSchedules allSchedules;
    @Mock
    private AllEnrollments  allEnrollments;
    @Mock
    private PNCSchedulesService pncSchedulesService;
    @Mock
    private  ScheduleService scheduleService;
    @Mock
    private HealthSchedulerService scheduler;
    private static final String JSON_KEY_HANDLER = "handler";	
    private static final String JSON_KEY_TYPES = "types";   	
    private static final String JSON_KEY_EVENTS = "events";	
	
    @Before
    public void setUp() throws Exception {
        initMocks(this);       
        pncScheduleHandler = new PNCScheduleHandler(pncSchedulesService);
    }

    @Test
    public void shouldTestPNCScheduleHandler() throws Exception {
        Event event = geteventOfVaccination();
        JSONArray schedulesJsonObject = new JSONArray("[" +  getFile() + "]");        
        for (int i = 0; i < schedulesJsonObject.length(); i++) {
            JSONObject scheduleJsonObject = schedulesJsonObject.getJSONObject(i);            
            JSONArray eventsJsonArray = scheduleJsonObject.getJSONArray(JSON_KEY_EVENTS);                      
            for (int j = 0; j < eventsJsonArray.length(); j++) {
                JSONObject scheduleConfigEvent = eventsJsonArray.getJSONObject(j);
                JSONArray eventTypesJsonArray = scheduleConfigEvent.getJSONArray(JSON_KEY_TYPES);
                List<String> eventsList = jsonArrayToList(eventTypesJsonArray);                
                if (eventsList.contains(event.getEventType())) {  
                	String action = pncScheduleHandler.getAction(scheduleConfigEvent);                	
                	String milestone=pncScheduleHandler.getMilestone(scheduleConfigEvent);
                	if (milestone.equalsIgnoreCase("opv2") && action.equalsIgnoreCase(ActionType.enroll.toString())) {
                        pncScheduleHandler.handle(event,scheduleConfigEvent, null);
                        InOrder inOrder = inOrder(pncSchedulesService);
                        LocalDate  date = LocalDate.parse(pncScheduleHandler.getReferenceDateForSchedule(event, scheduleConfigEvent, action));
                        inOrder.verify(pncSchedulesService).enrollPNCRVForMother(event.getBaseEntityId(), "Post Natal Care Reminder Visit",date , milestone, event.getId());                        
                    }
                    else if (milestone.equalsIgnoreCase("opv2") && action.equalsIgnoreCase(ActionType.fulfill.toString())) {
                	    pncScheduleHandler.handle(event,scheduleConfigEvent, null);
                        InOrder inOrder = inOrder(pncSchedulesService);
                        LocalDate  date = LocalDate.parse(pncScheduleHandler.getReferenceDateForSchedule(event, scheduleConfigEvent, action));                        
                        inOrder.verify(pncSchedulesService).fullfillMilestone(event.getBaseEntityId(), event.getProviderId(), "Post Natal Care Reminder Visit", date, event.getId()); 
                    } else {
                    	
                    }
                }				
            }			
        }		
    }
    
    @Test(expected=JSONException.class)
    public void shouldReturnExceptionWhenTestPNCScheduleHandler() throws Exception {
        Event event = geteventOfVaccination();
        JSONArray schedulesJsonObject = new JSONArray("[" +  getFile() + "]");        
        for (int i = 0; i < schedulesJsonObject.length(); i++) {
            JSONObject scheduleJsonObject = schedulesJsonObject.getJSONObject(i);            
            JSONArray eventsJsonArray = scheduleJsonObject.getJSONArray(JSON_KEY_EVENTS);                      
            for (int j = 0; j < eventsJsonArray.length(); j++) {
                JSONObject scheduleConfigEvent = new JSONObject("df");
                pncScheduleHandler.handle(event,scheduleConfigEvent, scheduleName);
            }			
        }		
    }

}
