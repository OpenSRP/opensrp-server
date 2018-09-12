package org.opensrp.connector.openmrs.service;

import static org.opensrp.dto.AlertStatus.normal;
import static org.opensrp.dto.BeneficiaryType.mother;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.model.Time;
import org.motechproject.scheduletracking.api.domain.Enrollment;
import org.motechproject.scheduletracking.api.domain.EnrollmentStatus;
import org.motechproject.scheduletracking.api.domain.Schedule;
import org.opensrp.domain.Client;
import org.opensrp.domain.Event;
import org.opensrp.connector.openmrs.constants.OpenmrsConstants;
import org.opensrp.connector.openmrs.constants.OpenmrsHouseHold;
import org.opensrp.dto.ActionData;
import org.opensrp.form.domain.FormSubmission;
import org.opensrp.form.service.FormAttributeParser;
import org.opensrp.scheduler.Action;
import org.opensrp.service.formSubmission.FormEntityConverter;

import com.google.gson.JsonIOException;

public class OpenmrsSchedulerServiceTest extends TestResourceLoader {
	
	public OpenmrsSchedulerServiceTest() throws IOException {
		super();
	}
	
	OpenmrsSchedulerService ss;
	
	OpenmrsUserService us;
	
	EncounterService es;
	
	FormEntityConverter oc;
	
	PatientService ps;
	
	HouseholdService hhs;
	
	SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
	
	@Before
	public void setup() throws IOException {
		ps = new PatientService(openmrsOpenmrsUrl, openmrsUsername, openmrsPassword);
		us = new OpenmrsUserService(openmrsOpenmrsUrl, openmrsUsername, openmrsPassword);
		ss = new OpenmrsSchedulerService(openmrsOpenmrsUrl, openmrsUsername, openmrsPassword);
		ss.setUserService(us);
		ss.setPatientService(ps);
		
		es = new EncounterService(openmrsOpenmrsUrl, openmrsUsername, openmrsPassword);
		es.setPatientService(ps);
		es.setUserService(us);
		hhs = new HouseholdService(openmrsOpenmrsUrl, openmrsUsername, openmrsPassword);
		hhs.setPatientService(ps);
		hhs.setEncounterService(es);
		FormAttributeParser fam = new FormAttributeParser(formDirPath);
		oc = new FormEntityConverter(fam);
	}
	
	@Test
	public void testTrack() throws JSONException, ParseException {
		String id = UUID.randomUUID().toString();
		
		Enrollment e = new Enrollment(id, new Schedule("Boosters"), "REMINDER", new DateTime(2012, 1, 1, 0, 0),
		        new DateTime(2012, 1, 1, 0, 0), new Time(23, 8), EnrollmentStatus.ACTIVE, null);
		List<Action> alertActions = new ArrayList<Action>();
		alertActions.add(new Action(id, "admin", alert("Boosters", "REMINDER")));
		alertActions.add(new Action(id, "admin", ActionData.markAlertAsClosed("REMINDER", "12-12-2015")));
		if (pushToOpenmrsForTest) {
			String puuid = ps.getPatientByIdentifierUUID(id);
			if (puuid == null) {
				ps.createPatient(
				    new Client(id, "TEST", null, "Name", new DateTime().minusYears(20), null, false, false, "MALE"));
			}
			JSONObject t = ss.createTrack(e, alertActions);
			e.setStatus(EnrollmentStatus.COMPLETED);
			Map<String, String> metadata = new HashMap<>();
			metadata.put(OpenmrsConstants.ENROLLMENT_TRACK_UUID, t.getString("uuid"));
			e.setMetadata(metadata);
			ss.updateTrack(e, alertActions);
		}
	}
	
	@Test
	public void testTrackWithoutAnyMilestoneOrAction() throws JSONException, ParseException {
		String id = UUID.randomUUID().toString();
		
		Enrollment e = new Enrollment(id, new Schedule("Boosters"), "REMINDER", new DateTime(2012, 1, 1, 0, 0),
		        new DateTime(2012, 1, 1, 0, 0), new Time(23, 8), EnrollmentStatus.ACTIVE, null);
		List<Action> alertActions = new ArrayList<Action>();
		if (pushToOpenmrsForTest) {
			String puuid = ps.getPatientByIdentifierUUID(id);
			if (puuid == null) {
				ps.createPatient(
				    new Client(id, "TEST", null, "Name", new DateTime().minusYears(20), null, false, false, "MALE"));
			}
			JSONObject t = ss.createTrack(e, alertActions);
			e.setStatus(EnrollmentStatus.COMPLETED);
			Map<String, String> metadata = new HashMap<>();
			metadata.put(OpenmrsConstants.ENROLLMENT_TRACK_UUID, t.getString("uuid"));
			e.setMetadata(metadata);
			ss.updateTrack(e, alertActions);
		}
	}
	
	@Test
	public void testTrackWithMilestoneAndWithoutAction() throws JSONException, ParseException {
		String id = UUID.randomUUID().toString();
		
		Enrollment e = new Enrollment(id, new Schedule("Boosters"), "REMINDER", new DateTime(2012, 1, 1, 0, 0),
		        new DateTime(2012, 1, 1, 0, 0), new Time(23, 8), EnrollmentStatus.ACTIVE, null);
		e.fulfillCurrentMilestone(new DateTime());
		List<Action> alertActions = new ArrayList<Action>();
		if (pushToOpenmrsForTest) {
			String puuid = ps.getPatientByIdentifierUUID(id);
			if (puuid == null) {
				ps.createPatient(
				    new Client(id, "TEST", null, "Name", new DateTime().minusYears(20), null, false, false, "MALE"));
			}
			JSONObject t = ss.createTrack(e, alertActions);
			e.setStatus(EnrollmentStatus.COMPLETED);
			Map<String, String> metadata = new HashMap<>();
			metadata.put(OpenmrsConstants.ENROLLMENT_TRACK_UUID, t.getString("uuid"));
			e.setMetadata(metadata);
			ss.updateTrack(e, alertActions);
		}
	}
	
	@Test
	public void testHHScheduleData() throws JSONException, ParseException, JsonIOException, IOException {
		FormSubmission fs = getFormSubmissionFor("new_household_registration", 6);
		
		Client hhhead = oc.getClientFromFormSubmission(fs);
		Event ev = oc.getEventFromFormSubmission(fs);
		Map<String, Map<String, Object>> dep = oc.getDependentClientsFromFormSubmission(fs);
		
		OpenmrsHouseHold household = new OpenmrsHouseHold(hhhead, ev);
		for (String hhmid : dep.keySet()) {
			household.addHHMember((Client) dep.get(hhmid).get("client"), (Event) dep.get(hhmid).get("event"));
		}
		if (pushToOpenmrsForTest) {
			JSONObject pr = us.getProvider(fs.anmId(),null);
			if (pr == null) {
				us.createProvider(fs.anmId(), fs.anmId());
			}
			
			JSONObject enct = es.getEncounterType(ev.getEventType());
			if (enct == null) {
				es.createEncounterType(ev.getEventType(), "Encounter type created to fullfill scheduling test pre-reqs");
			}
			
			for (String hhmid : dep.keySet()) {
				Event ein = (Event) dep.get(hhmid).get("event");
				JSONObject hmenct = es.getEncounterType(ein.getEventType());
				if (hmenct == null) {
					es.createEncounterType(ein.getEventType(),
					    "Encounter type created to fullfill scheduling test pre-reqs");
				}
			}
			
			hhs.saveHH(household, true);
		}
		
		Enrollment e = new Enrollment(hhhead.getBaseEntityId(), new Schedule("FW CENSUS"), "FW CENSUS", new DateTime(),
		        new DateTime(), new Time(23, 8), EnrollmentStatus.ACTIVE, null);
		List<Action> alertActions = new ArrayList<Action>();
		alertActions.add(new Action(hhhead.getBaseEntityId(), ev.getProviderId(), alert("FW CENSUS", "FW CENSUS")));
		if (pushToOpenmrsForTest) {
			JSONObject t = ss.createTrack(e, alertActions);
			alertActions.add(new Action(hhhead.getBaseEntityId(), ev.getProviderId(),
			        ActionData.markAlertAsClosed("FW CENSUS", "12-12-2015")));
			e.setStatus(EnrollmentStatus.COMPLETED);
			Map<String, String> metadata = new HashMap<>();
			metadata.put(OpenmrsConstants.ENROLLMENT_TRACK_UUID, t.getString("uuid"));
			e.setMetadata(metadata);
			ss.updateTrack(e, alertActions);
		}
	}
	
	private ActionData alert(String schedule, String milestone) {
		return ActionData.createAlert(mother.value(), schedule, milestone, normal, DateTime.now(),
		    DateTime.now().plusDays(3));
	}
}
