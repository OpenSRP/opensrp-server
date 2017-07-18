package org.opensrp.register.service.handler;

import org.joda.time.LocalDate;
import org.json.JSONException;
import org.json.JSONObject;
import org.opensrp.domain.Event;
import org.opensrp.register.service.scheduling.HHSchedulesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HHScheduleHandler extends BaseScheduleHandler {
    private HHSchedulesService hhSchedulesService;
    @Autowired
    public HHScheduleHandler(HHSchedulesService hhSchedulesService) {
        this.hhSchedulesService  = hhSchedulesService;
    }
    @Override
    public void handle(Event event, JSONObject scheduleConfigEvent,String scheduleName) {
       try {
            if(scheduleName==null){
                scheduleName="FW CENSUS";
            }
            if (evaluateEvent(event, scheduleConfigEvent)) {
                String milestone = getMilestone(scheduleConfigEvent);
                String action = getAction(scheduleConfigEvent);
                if (action.equalsIgnoreCase(ActionType.enroll.toString())) {
                    hhSchedulesService.enrollIntoMilestoneOfCensus(event.getBaseEntityId(),
                        getReferenceDateForSchedule(event, scheduleConfigEvent, action), event.getProviderId(), milestone,
                        event.getId());
                    } else if (action.equalsIgnoreCase(ActionType.fulfill.toString())) {
                    hhSchedulesService.fullfillMilestone(event.getBaseEntityId(), event.getProviderId(), scheduleName, LocalDate.parse(getReferenceDateForSchedule(event, scheduleConfigEvent, action)), event.getId());
                }
            }
        }
        catch (JSONException e) {
            logger.error("", e);
        }
    }
}
