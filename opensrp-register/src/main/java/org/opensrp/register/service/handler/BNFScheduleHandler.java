package org.opensrp.register.service.handler;

import org.joda.time.LocalDate;
import org.json.JSONException;
import org.json.JSONObject;
import org.opensrp.domain.Event;
import org.opensrp.register.service.scheduling.BNFSchedulesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BNFScheduleHandler extends BaseScheduleHandler { 
    private BNFSchedulesService bnfSchedulesService; 
    @Autowired
    public BNFScheduleHandler(BNFSchedulesService bnfSchedulesService) {
        this.bnfSchedulesService = bnfSchedulesService; 
    }
    @Override 
    public void handle(Event event, JSONObject scheduleConfigEvent,String scheduleName) {
        try {
            if(scheduleName==null){
                scheduleName="BirthNotificationPregnancyStatusFollowUp";
            }
            if (evaluateEvent(event, scheduleConfigEvent)) {
                String action = getAction(scheduleConfigEvent);
                if (action.equalsIgnoreCase(ActionType.enroll.toString())) {
                    String refDate = getReferenceDateForSchedule(event, scheduleConfigEvent, action);
                    if(!refDate.isEmpty())
                        bnfSchedulesService.enrollBNF(event.getBaseEntityId(), scheduleName, LocalDate.parse(refDate), event.getId());
                }
            }
        }
        catch (JSONException e) {
            logger.error("", e);
        }
    }
}