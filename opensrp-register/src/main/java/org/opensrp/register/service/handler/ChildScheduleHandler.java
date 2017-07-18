package org.opensrp.register.service.handler;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.joda.time.LocalDate;
import org.json.JSONException;
import org.json.JSONObject;
import org.opensrp.domain.Client;
import org.opensrp.domain.Event;
import org.opensrp.register.service.scheduling.ENCCSchedulesService;
import org.opensrp.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChildScheduleHandler extends BaseScheduleHandler {
    private ENCCSchedulesService enccSchedulesService;
    private ClientService clientService;
    @Autowired
    public ChildScheduleHandler(ENCCSchedulesService enccSchedulesService,ClientService clientService) {
        this.enccSchedulesService = enccSchedulesService;
        this.clientService = clientService;
    }
    
    @Override
    public void handle(Event event, JSONObject scheduleConfigEvent,String scheduleName) {
        try {
            if(scheduleName==null){
                scheduleName="BirthNotificationPregnancyStatusFollowUp";
            }
            if (evaluateEvent(event, scheduleConfigEvent)) {
                String action = getAction(scheduleConfigEvent);
                //String milestone = getMilestone(scheduleConfigEvent);
                if (action.equalsIgnoreCase(ActionType.enroll.toString())) {
                    //if (milestone.equalsIgnoreCase("enccrv_1")) {//first milestone is initiated by the nbnf event
                    List<Client> children = getChildrenIds(event);
                    if (children != null && !children.isEmpty()) {
                        for (Client client : children) {
                            enccSchedulesService.enrollIntoCorrectMilestoneOfENCCCare(client.getBaseEntityId(), scheduleName, LocalDate.parse(getReferenceDateForSchedule(event, scheduleConfigEvent, action)), event.getId());
                        }
                    }
                }
                else if (action.equalsIgnoreCase(ActionType.fulfill.toString())) {
                    enccSchedulesService.fullfillMilestone(event.getBaseEntityId(), event.getProviderId(), scheduleName, LocalDate.parse(getReferenceDateForSchedule(event, scheduleConfigEvent, action)), event.getId());
                }
            }			
        }
        catch (JSONException e) {
            logger.error("", e);
        }
    }
	
    /**
    * Currently this handler is using the birthnotification event for the first milestone since it's the one that has the
    * date of birth/date of outcome concept. This event has the mother's baseentityid but we are
    * interested in the child's/chidlrens' baseentityid However if the dob can also be added to the
    * ENCC event, that would be great! use nbnf event baseentityid as the mother's id and the event
    * date created this method adds and substracts 1 so that the date is in a range just in case a
    * kid was born at 11:59 pm
    * 
    * @param event
    * @return
    */
    private List<Client> getChildrenIds(Event event) {
        Date dateCreated = event.getDateCreated().toDate();
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateCreated);
        cal.add(Calendar.DATE, 1);
        String dateTo = dateFormat.format(cal.getTime());
        cal = Calendar.getInstance();
        cal.setTime(dateCreated);
        cal.add(Calendar.DATE, -1);
        String dateFrom = dateFormat.format(cal.getTime());		
        List<Client> children = clientService.findByRelationshipIdAndDateCreated(event.getBaseEntityId(), dateFrom, dateTo);
        return children;
    }
}
