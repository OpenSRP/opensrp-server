package org.opensrp.register.service.handler;

import org.joda.time.LocalDate;
import org.json.JSONException;
import org.json.JSONObject;
import org.opensrp.domain.Event;
import org.opensrp.register.service.scheduling.AnteNatalCareSchedulesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ANCScheduleHandler extends BaseScheduleHandler {
	
	private AnteNatalCareSchedulesService ancScheduleService;
    @Autowired
    public ANCScheduleHandler(AnteNatalCareSchedulesService ancScheduleService) {
        this.ancScheduleService = ancScheduleService;
    }
    @Override
    public void handle(Event event, JSONObject scheduleConfigEvent,String scheduleName) {
        try {
            if(scheduleName==null){
                scheduleName="Ante Natal Care Reminder Visit";
            }
            if (evaluateEvent(event, scheduleConfigEvent)) {
                String action = getAction(scheduleConfigEvent);
                if (action.equalsIgnoreCase(ActionType.enroll.toString())) {
                    String refDate=getReferenceDateForSchedule(event, scheduleConfigEvent, action);
                    if(!refDate.isEmpty())
                        ancScheduleService.enrollMother(event.getBaseEntityId(),scheduleName, LocalDate.parse(refDate),
                            event.getId());
                } else if (action.equalsIgnoreCase(ActionType.fulfill.toString())) {
                    ancScheduleService.fullfillMilestone(event.getBaseEntityId(), event.getProviderId(), scheduleName, LocalDate.parse(getReferenceDateForSchedule(event, scheduleConfigEvent, action)), event.getId());
                }
            }
        }
        catch (JSONException e) {
            logger.error("", e);
        }
    }
}
