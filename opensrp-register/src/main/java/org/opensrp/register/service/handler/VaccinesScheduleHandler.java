package org.opensrp.register.service.handler;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Weeks;
import org.json.JSONException;
import org.json.JSONObject;
import org.motechproject.scheduletracking.api.domain.Enrollment;
import org.opensrp.domain.Event;
import org.opensrp.domain.Obs;
import org.opensrp.repository.AllEvents;
import org.opensrp.scheduler.HealthSchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VaccinesScheduleHandler extends BaseScheduleHandler {
    private HealthSchedulerService scheduler;
    private AllEvents allEvents;
    @Autowired
    public VaccinesScheduleHandler(HealthSchedulerService scheduler,AllEvents allEvents) {
        this.scheduler = scheduler;
        this.allEvents = allEvents;
    }
    @Override
    public void handle(Event event, JSONObject scheduleConfigEvent, String scheduleName) {
        try {
            String action = getAction(scheduleConfigEvent);
            if (evaluateEvent(event, scheduleConfigEvent)) {				
                if (scheduleName.equalsIgnoreCase("OPV 4") && !evaluateOpv4(event.getBaseEntityId())) {
                    return;
                } 
                String milestone = getMilestone(scheduleConfigEvent);				
                if (action.equalsIgnoreCase(ActionType.enroll.toString())) {
                    // Vit A 1 has multiple enrollments, i.e. child dob or vit a ifc 2.
                    String vitaScheduleName = "VIT A 1";
                    if (scheduleName.equalsIgnoreCase(vitaScheduleName)){
                        Enrollment enrollment = scheduler.getActiveEnrollment(event.getBaseEntityId(), vitaScheduleName);
                        if (enrollment != null){
                            scheduler.unEnrollFromSchedule(event.getBaseEntityId(), event.getProviderId(), vitaScheduleName, event.getId());
                        }
                    }
                    if (milestone == null || milestone.isEmpty()) {						
                        scheduler.enrollIntoSchedule(event.getBaseEntityId(), scheduleName,
                            getReferenceDateForSchedule(event, scheduleConfigEvent, action), event.getId());
                    } else {                    	
                        scheduler.enrollIntoSchedule(event.getBaseEntityId(), scheduleName, milestone,
                            getReferenceDateForSchedule(event, scheduleConfigEvent, action), event.getId());						
                    }
                    logger.info("Enrolled " + event.getBaseEntityId() + "to schedule " + scheduleName);
					
                    } else if (action.equalsIgnoreCase(ActionType.fulfill.toString())) {
                        //MR or Measles can be given, but not both
                    	//If fulfilling (means vaccine given) measles1 unenroll from mr1 schedule and vice versa
                    if (scheduleName.equalsIgnoreCase("MEASLES 1") ) {
                        scheduler.unEnrollFromSchedule(event.getBaseEntityId(), event.getProviderId(), "MR 1", event.getId());
                        scheduler.unEnrollFromSchedule(event.getBaseEntityId(), event.getProviderId(), "MR 2", event.getId());
                    }else if(scheduleName.equalsIgnoreCase("MR 1")){
                        scheduler.unEnrollFromSchedule(event.getBaseEntityId(), event.getProviderId(), "MEASLES 1", event.getId());
                        scheduler.unEnrollFromSchedule(event.getBaseEntityId(), event.getProviderId(), "MEASLES 2", event.getId());
                    }				   
                    scheduler.fullfillMilestoneAndCloseAlert(event.getBaseEntityId(), event.getProviderId(),
                        scheduleName.toUpperCase(),
                        LocalDate.parse(getReferenceDateForSchedule(event, scheduleConfigEvent, action)), event.getId());
                    logger.info("Fulfilled schedule" + scheduleName + "for schedule " + event.getBaseEntityId());
                }
            }			
        }
        catch (JSONException e) {
            logger.error("", e);
        }
    }
	
    /** 
    * OPV 0 was not given and OPV 3 was given at least 4 weeks ago
    * 
    * @param baseEntityId
    * @return
    */
    private boolean evaluateOpv4(String baseEntityId) {
        String opv_parent_code = "783AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        String opv0_field_code = "1418AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        String opv_date_field_code = "1410AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        List<Event> opv0Events = allEvents.findByBaseEntityIdAndConceptParentCode(baseEntityId, opv0_field_code,
            opv_parent_code);
        boolean opv0Given = false;
        boolean opv3Given = false;
        if (opv0Events != null && !opv0Events.isEmpty()) {
            //check if opv0 was not given
            for (Event event : opv0Events) {
                Obs obs = event.getObs(opv_parent_code, opv0_field_code);
                if (obs.getValues().contains("0")) {
                    //OPV 0 was given
                    opv0Given = true;
                } else if (obs.getValues().contains("3")) {
                    //confirm opv3 was given at least 4 weeks ago
                    obs = event.getObs(opv_parent_code, opv_date_field_code);
                    String strDate = obs.getValues().get(0).toString();
					
                    try {						
                        DateTime vaccineDate = new DateTime(dateFormat.parse(strDate));
                        DateTime now = new DateTime(new Date());						
                        int weeks = Weeks.weeksBetween(vaccineDate, now).getWeeks();
                        if (weeks >= 4) {
                            opv3Given = true;
                        }
                    }
                    catch (ParseException e) {
                        logger.error(e.getMessage());
                    }					
                }
            }		
        }
        return opv0Given && opv3Given;
    }	
}
