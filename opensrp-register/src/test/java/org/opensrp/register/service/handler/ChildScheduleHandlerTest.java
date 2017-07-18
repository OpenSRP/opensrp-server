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
import org.opensrp.service.ClientService;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({ "org.apache.log4j.*", "org.apache.commons.logging.*" })
public class ChildScheduleHandlerTest extends TestResourceLoader {	
    @Mock
    private ENCCSchedulesService  enccSchedulesService;    
    private ChildScheduleHandler childScheduleHandler;
    @Mock
    private ClientService clientService;
    private static final String JSON_KEY_TYPES = "types";	
    private static final String JSON_KEY_EVENTS = "events";
    protected static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
    @Before
    public void setUp() throws Exception {
        initMocks(this);
        childScheduleHandler = new ChildScheduleHandler(enccSchedulesService,clientService);
    }   
    
    @Test
    public void shouldTestChilsScheduleHandler() throws Exception {
        Event event = geteventOfVaccination();
        JSONArray schedulesJsonObject = new JSONArray("[" + getFile() + "]");
        String scheduleName =null;
        Date dateCreated = event.getDateCreated().toDate();		
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateCreated);
        cal.add(Calendar.DATE, 1);
        String dateTo = dateFormat.format(cal.getTime());
        cal = Calendar.getInstance();
        cal.setTime(dateCreated);
        cal.add(Calendar.DATE, -1);
        String dateFrom = dateFormat.format(cal.getTime());
        when(clientService, method(ClientService.class, "findByRelationshipIdAndDateCreated", String.class,String.class,String.class))
            .withArguments("ooo-yyy-yyy",dateFrom,dateTo)
            .thenReturn(getClients());    
        for (int i = 0; i < schedulesJsonObject.length(); i++) {
            JSONObject scheduleJsonObject = schedulesJsonObject.getJSONObject(i);            
            JSONArray eventsJsonArray = scheduleJsonObject.getJSONArray(JSON_KEY_EVENTS);                      
            for (int j = 0; j < eventsJsonArray.length(); j++) {
                JSONObject scheduleConfigEvent = eventsJsonArray.getJSONObject(j);
                JSONArray eventTypesJsonArray = scheduleConfigEvent.getJSONArray(JSON_KEY_TYPES);
                List<String> eventsList = jsonArrayToList(eventTypesJsonArray);                
                if (eventsList.contains(event.getEventType())) {  
                	String action = childScheduleHandler.getAction(scheduleConfigEvent);                	
                	String milestone = childScheduleHandler.getMilestone(scheduleConfigEvent);                    
                	if (milestone.equalsIgnoreCase("opv2") && action.equalsIgnoreCase(ActionType.enroll.toString())) {
                        childScheduleHandler.handle(event,scheduleConfigEvent, scheduleName);                		
                        InOrder inOrder = inOrder(enccSchedulesService);                        
                        inOrder.verify(enccSchedulesService).enrollIntoCorrectMilestoneOfENCCCare("ooo-yyy-yyy", "BirthNotificationPregnancyStatusFollowUp", LocalDate.parse("2016-07-10"), event.getId());                        
                    } else if (milestone.equalsIgnoreCase("opv2") && action.equalsIgnoreCase(ActionType.fulfill.toString())) {
                    	childScheduleHandler.handle(event,scheduleConfigEvent, scheduleName);                		
                        InOrder inOrder = inOrder(enccSchedulesService);
                        inOrder.verify(enccSchedulesService).fullfillMilestone(event.getBaseEntityId(), event.getProviderId(), "BirthNotificationPregnancyStatusFollowUp", LocalDate.parse("2016-07-10"), event.getId());
                    }
                }				
            }			
        }		
    }    
   
}
