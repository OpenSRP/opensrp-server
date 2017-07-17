
package org.opensrp.connector.openmrs.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.motechproject.scheduletracking.api.domain.Enrollment;
import org.motechproject.scheduletracking.api.domain.MilestoneFulfillment;
import org.motechproject.scheduletracking.api.domain.json.ScheduleRecord;
import org.opensrp.common.util.HttpUtil;
import org.opensrp.connector.openmrs.constants.OpenmrsConstants;
import org.opensrp.scheduler.Action;
import org.opensrp.scheduler.service.AllScheduleWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OpenmrsSchedulerService extends OpenmrsService{
	
	private static final String TRACK_URL = "ws/rest/v1/scheduletracker/track";
	private static final String TRACK_MILESTONE_URL = "ws/rest/v1/scheduletracker/trackmilestone";
	private static final String SCHEDULE_URL = "ws/rest/v1/scheduletracker/schedule";
	private static final String SCHEDULE_SAVE_URL = "module/scheduletracker/jsonschedule/saveJsonSchedule.form";
	private static final String MILESTONE_URL = "ws/rest/v1/scheduletracker/milestone";
	
	private OpenmrsUserService userService;
	private PatientService patientService;
	private AllScheduleWrapper allSchedules;


	public OpenmrsUserService getUserService() {
		return userService;
	}

	public void setUserService(OpenmrsUserService userService) {
		this.userService = userService;
	}

	public PatientService getPatientService() {
		return patientService;
	}

	public void setPatientService(PatientService patientService) {
		this.patientService = patientService;
	}

	@Autowired
	public OpenmrsSchedulerService(AllScheduleWrapper allSchedules,
			OpenmrsUserService userService, PatientService patientService) throws JSONException {
		this.userService = userService;
		this.patientService = patientService;
		this.allSchedules = allSchedules;
	}

    public OpenmrsSchedulerService(String openmrsUrl, String user, String password) {
    	super(openmrsUrl, user, password);
    }
	
    public JSONObject getSchedule(String schedule) throws JSONException {
		JSONObject to = new JSONObject(HttpUtil.get(getURL()+"/"+SCHEDULE_URL+"/"+schedule, "", OPENMRS_USER, OPENMRS_PWD).body());
		return to;
	}
    
    public JSONObject createOrUpdateSchedule(ScheduleRecord schedule) throws JSONException {
		return new JSONObject(HttpUtil.post(getURL()+"/"+SCHEDULE_URL, "", schedule.toString(), OPENMRS_USER, OPENMRS_PWD).body());
	}
    
	public JSONObject createTrack(Enrollment e, List<Action> alertActions) throws JSONException, ParseException
	{
		if(getSchedule(e.getScheduleName()) == null){
			createOrUpdateSchedule(allSchedules.getRecordByName(e.getScheduleName()));
		}
		JSONObject po = patientService.getPatientByIdentifier(e.getExternalId());
		JSONObject t = new JSONObject();
		t.put("beneficiary", po.getJSONObject("person").getString("uuid"));
		t.put("beneficiaryRole", alertActions!=null&&alertActions.size()>0?alertActions.get(0).data().get("beneficiaryType"):null);
		t.put("schedule", e.getScheduleName());
		String hr = StringUtils.leftPad(e.getPreferredAlertTime().getHour().toString(),2,"0");
		String mn = StringUtils.leftPad(e.getPreferredAlertTime().getMinute().toString(),2,"0");
		t.put("preferredAlertTime", hr+":"+mn+":00");
		t.put("referenceDate", OPENMRS_DATE.format(e.getStartOfSchedule().toDate()));
		t.put("referenceDateType", "MANUAL");
		t.put("dateEnrolled", OPENMRS_DATE.format(e.getEnrolledOn().toDate()));
		
		/*DateTime earliestStart = e.getStartOfWindowForCurrentMilestone(WindowName.earliest);
        DateTime dueStart = e.getStartOfWindowForCurrentMilestone(WindowName.due);
        DateTime lateStart = e.getStartOfWindowForCurrentMilestone(WindowName.late);
        DateTime maxStart = e.getStartOfWindowForCurrentMilestone(WindowName.max);
		t.put("earlyStartDate", OPENMRS_DATE.format(earliestStart.toDate()));
		t.put("dueStartDate", OPENMRS_DATE.format(dueStart.toDate()));
		t.put("lateStartDate", OPENMRS_DATE.format(lateStart.toDate()));
		t.put("maxStartDate", OPENMRS_DATE.format(maxStart.toDate()));*/
		t.put("currentMilestone", e.getCurrentMilestoneName());
		t.put("status", e.getStatus().name());
		
		
		JSONObject to = new JSONObject(HttpUtil.post(getURL()+"/"+TRACK_URL, "", t.toString(), OPENMRS_USER, OPENMRS_PWD).body());
		String trackuuid = to.getString("uuid");
		
		JSONArray tmarr = new JSONArray();
		for (Action ac : alertActions) {
			if(ac.getActionType().equalsIgnoreCase("createAlert")){
				JSONObject tm = fromActionToTrackMilestone(false, ac, trackuuid, e, alertActions);
				
				JSONObject tmo = new JSONObject(HttpUtil.post(getURL()+"/"+TRACK_MILESTONE_URL, "", tm.toString(), OPENMRS_USER, OPENMRS_PWD).body());
			
				tmarr.put(tmo);
			}
		}
		for (MilestoneFulfillment f : e.getFulfillments()) {
			if(getCreatedAction(f.getMilestoneName(), alertActions) == null){
				JSONObject tm = fromMilestoneToTrackMilestone(false, f, trackuuid, e, alertActions);
				
				JSONObject tmo = new JSONObject(HttpUtil.post(getURL()+"/"+TRACK_MILESTONE_URL, "", tm.toString(), OPENMRS_USER, OPENMRS_PWD).body());
			
				tmarr.put(tmo);
			}
		}
		
		to.put("trackMilestones", tmarr);
		return to;
	}
	
	public JSONObject updateTrack(Enrollment e, List<Action> alertActions) throws JSONException, ParseException
	{
		JSONObject t = new JSONObject();
		t.put("beneficiaryRole", alertActions!=null&&alertActions.size()>0?alertActions.get(0).data().get("beneficiaryType"):null);
		String hr = StringUtils.leftPad(e.getPreferredAlertTime().getHour().toString(),2,"0");
		String mn = StringUtils.leftPad(e.getPreferredAlertTime().getMinute().toString(),2,"0");
		t.put("preferredAlertTime", hr+":"+mn+":00");
		t.put("currentMilestone", e.getCurrentMilestoneName());
		t.put("status", e.getStatus().name());
		
		
		JSONObject to = new JSONObject(HttpUtil.post(getURL()+"/"+TRACK_URL+"/"+e.getMetadata().get(OpenmrsConstants.ENROLLMENT_TRACK_UUID), "", t.toString(), OPENMRS_USER, OPENMRS_PWD).body());
		String trackuuid = to.getString("uuid");
		
		JSONArray tmarr = new JSONArray();
		for (Action ac : alertActions) {
			if(ac.getActionType().equalsIgnoreCase("createAlert")){
				String milestone = ac.data().get("visitCode");
				JSONArray j = new JSONObject(HttpUtil.get(getURL()+"/"+TRACK_MILESTONE_URL, "v=full&track="+trackuuid+"&milestone="+milestone, OPENMRS_USER, OPENMRS_PWD).body()).getJSONArray("results");
				JSONObject tm = fromActionToTrackMilestone(j.length()>0, ac, trackuuid, e, alertActions);
	
				String uuid = j.length()>0?j.getJSONObject(0).getString("uuid"):"";
				JSONObject tmo = new JSONObject(HttpUtil.post(getURL()+"/"+TRACK_MILESTONE_URL+"/"+uuid, "", tm.toString(), OPENMRS_USER, OPENMRS_PWD).body());
			
				tmarr.put(tmo);
			}
		}
		for (MilestoneFulfillment f : e.getFulfillments()) {
			if(getCreatedAction(f.getMilestoneName(), alertActions) == null){
				String milestone = f.getMilestoneName();
				JSONArray j = new JSONObject(HttpUtil.get(getURL()+"/"+TRACK_MILESTONE_URL, "v=full&track="+trackuuid+"&milestone="+milestone, OPENMRS_USER, OPENMRS_PWD).body()).getJSONArray("results");
				JSONObject tm = fromMilestoneToTrackMilestone(j.length()>0, f, trackuuid, e, alertActions);
				
				String uuid = j.length()>0?j.getJSONObject(0).getString("uuid"):"";
				JSONObject tmo = new JSONObject(HttpUtil.post(getURL()+"/"+TRACK_MILESTONE_URL+"/"+uuid, "", tm.toString(), OPENMRS_USER, OPENMRS_PWD).body());
			
				tmarr.put(tmo);
			}
		}
		
		t.put("trackMilestones", tmarr);
		return t;
	}
	
	private JSONObject fromActionToTrackMilestone(boolean isUpdate, Action ac, String trackUuid, Enrollment e, List<Action> alertActions) throws JSONException, ParseException {
		JSONObject tm = new JSONObject();
		String milestone = ac.data().get("visitCode");
		if(!isUpdate){
			JSONObject pr = userService.getPersonByUser(ac.providerId());
			tm.put("track", trackUuid);
			tm.put("milestone", milestone );
			tm.put("alertRecipient", pr.getString("uuid"));
			tm.put("alertRecipientRole", "PROVIDER");
		}
		Action close = getClosedAction(milestone, alertActions);
		MilestoneFulfillment m = getMilestone(milestone, e);
		String fdate = m == null?null:OPENMRS_DATE.format(m.getFulfillmentDateTime().toDate());
		if(fdate == null){
			fdate = close==null?null:OPENMRS_DATE.format(new SimpleDateFormat("dd-MM-yyyy").parse(close.data().get("completionDate")));
		}
		tm.put("fulfillmentDate", fdate);
		tm.put("status", ac.data().get("alertStatus")+(close==null?"":"-completed"));
		//TODO tm.put("reasonClosed", ac.data().get(""));
		tm.put("alertStartDate", ac.data().get("startDate"));
		tm.put("alertExpiryDate", ac.data().get("expiryDate"));
		tm.put("isActive", ac.getIsActionActive());
		tm.put("actionType", "PROVIDER ALERT");
		return tm;
	}
	
	private JSONObject fromMilestoneToTrackMilestone(boolean isUpdate, MilestoneFulfillment m, String trackUuid, Enrollment e, List<Action> alertActions) throws JSONException, ParseException {
		JSONObject tm = new JSONObject();
		if(!isUpdate){
			JSONObject pr = userService.getPersonByUser("daemon");
			tm.put("track", trackUuid);
			tm.put("milestone", m.getMilestoneName());
			tm.put("alertRecipient", pr.getString("uuid"));
			tm.put("alertRecipientRole", "PROVIDER");
		}

		Action close = getClosedAction(m.getMilestoneName(), alertActions);
		String fdate = m == null?null:OPENMRS_DATE.format(m.getFulfillmentDateTime().toDate());
		if(fdate == null){
			fdate = close==null?null:OPENMRS_DATE.format(new SimpleDateFormat("dd-MM-yyyy").parse(close.data().get("completionDate")));
		}
		tm.put("fulfillmentDate", fdate);
		tm.put("status", "FULFILLED");
		//TODO tm.put("reasonClosed", ac.data().get(""));
		tm.put("alertStartDate", OPENMRS_DATE.format(new Date(0L)));
		tm.put("alertExpiryDate", OPENMRS_DATE.format(new Date(0L)));
		tm.put("isActive", false);
		tm.put("actionType", "PROVIDER ALERT MISSING");
		return tm;
	}
	
	private Action getClosedAction(String milestone, List<Action> actions){
		for (Action a : actions) {
			if(a.data().get("visitCode") != null && a.data().get("visitCode").equalsIgnoreCase(milestone)
					&& a.getActionType().equalsIgnoreCase("closeAlert")){
				return a;
			}
		}
		return null;
	}
	
	private Action getCreatedAction(String milestone, List<Action> actions){
		for (Action a : actions) {
			if(a.data().get("visitCode") != null && a.data().get("visitCode").equalsIgnoreCase(milestone)
					&& a.getActionType().equalsIgnoreCase("createAlert")){
				return a;
			}
		}
		return null;
	}
	private MilestoneFulfillment getMilestone(String milestone, Enrollment e) {
		for (MilestoneFulfillment m : e.getFulfillments()) {
			if(m.getMilestoneName().equalsIgnoreCase(milestone)){
				return m;
			}
		}
		return null;
	}
}
