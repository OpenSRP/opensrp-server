package org.opensrp.connector.openmrs.schedule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.motechproject.scheduler.domain.MotechEvent;
import org.motechproject.server.event.annotations.MotechListener;
import org.opensrp.common.util.DateUtil;
import org.opensrp.connector.dhis2.Dhis2TrackCaptureConnector;
import org.opensrp.connector.openmrs.constants.OpenmrsConstants;
import org.opensrp.connector.openmrs.constants.OpenmrsConstants.SchedulerConfig;
import org.opensrp.connector.openmrs.service.EncounterService;
import org.opensrp.connector.openmrs.service.OpenmrsSchedulerService;
import org.opensrp.connector.openmrs.service.PatientService;
import org.opensrp.domain.AppStateToken;
import org.opensrp.domain.Client;
import org.opensrp.domain.Event;
import org.opensrp.scheduler.service.ActionService;
import org.opensrp.scheduler.service.ScheduleService;
import org.opensrp.service.ClientService;
import org.opensrp.service.ConfigService;
import org.opensrp.service.ErrorTraceService;
import org.opensrp.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OpenmrsSyncerListener {
	
	private static final ReentrantLock lock = new ReentrantLock();
	
	private static Logger logger = LoggerFactory.getLogger(OpenmrsSyncerListener.class.toString());
	
	private OpenmrsSchedulerService openmrsSchedulerService;
	
	private ScheduleService opensrpScheduleService;
	
	private ActionService actionService;
	
	private ConfigService config;
	
	private ErrorTraceService errorTraceService;
	
	private PatientService patientService;
	
	private EncounterService encounterService;
	
	private EventService eventService;
	
	private ClientService clientService;
	// private RelationShipService relationShipService;
	
	@Autowired
	private Dhis2TrackCaptureConnector dhis2TrackCaptureConnector;
	
	@Autowired
	public OpenmrsSyncerListener(OpenmrsSchedulerService openmrsSchedulerService, ScheduleService opensrpScheduleService,
	    ActionService actionService, ConfigService config, ErrorTraceService errorTraceService,
	    PatientService patientService, EncounterService encounterService, ClientService clientService,
	    EventService eventService) {
		this.openmrsSchedulerService = openmrsSchedulerService;
		this.opensrpScheduleService = opensrpScheduleService;
		this.actionService = actionService;
		this.config = config;
		this.errorTraceService = errorTraceService;
		this.patientService = patientService;
		this.encounterService = encounterService;
		this.eventService = eventService;
		this.clientService = clientService;
		
		this.config.registerAppStateToken(SchedulerConfig.openmrs_syncer_sync_schedule_tracker_by_last_update_enrollment, 0,
		    "ScheduleTracker token to keep track of enrollment synced with OpenMRS", true);
		
		this.config.registerAppStateToken(SchedulerConfig.openmrs_syncer_sync_client_by_date_updated, 0,
		    "OpenMRS data pusher token to keep track of new / updated clients synced with OpenMRS", true);
		
		this.config.registerAppStateToken(SchedulerConfig.openmrs_syncer_sync_client_by_date_voided, 0,
		    "OpenMRS data pusher token to keep track of voided clients synced with OpenMRS", true);
		
		this.config.registerAppStateToken(SchedulerConfig.openmrs_syncer_sync_event_by_date_updated, 0,
		    "OpenMRS data pusher token to keep track of new / updated events synced with OpenMRS", true);
		
		this.config.registerAppStateToken(SchedulerConfig.openmrs_syncer_sync_event_by_date_voided, 0,
		    "OpenMRS data pusher token to keep track of voided events synced with OpenMRS", true);
	}
	
	// @MotechListener(subjects =
	// OpenmrsConstants.SCHEDULER_TRACKER_SYNCER_SUBJECT)
	// public void scheduletrackerSyncer(MotechEvent event) {
	// try {
	// logger.info("RUNNING " + event.getSubject());
	// AppStateToken lastsync =
	// config.getAppStateTokenByName(SchedulerConfig.openmrs_syncer_sync_schedule_tracker_by_last_update_enrollment);
	// DateTime start = lastsync == null || lastsync.getValue() == null ? new
	// DateTime().minusYears(33) : new DateTime(lastsync.stringValue());
	// DateTime end = new DateTime();
	// List<Enrollment> el =
	// opensrpScheduleService.findEnrollmentByLastUpDate(start, end);
	// for (Enrollment e : el) {
	// DateTime alertstart = e.getStartOfSchedule();
	// DateTime alertend = e.getLastFulfilledDate();
	// if (alertend == null) {
	// alertend = e.getCurrentMilestoneStartDate();
	// }
	// try {
	// if (e.getMetadata().get(OpenmrsConstants.ENROLLMENT_TRACK_UUID) != null)
	// {
	// openmrsSchedulerService.updateTrack(e,
	// actionService.findByCaseIdScheduleAndTimeStamp(e.getExternalId(),
	// e.getScheduleName(), alertstart, alertend));
	// } else {
	// JSONObject tr = openmrsSchedulerService.createTrack(e,
	// actionService.findByCaseIdScheduleAndTimeStamp(e.getExternalId(),
	// e.getScheduleName(), alertstart, alertend));
	// opensrpScheduleService.updateEnrollmentWithMetadata(e.getId(),
	// OpenmrsConstants.ENROLLMENT_TRACK_UUID, tr.getString("uuid"));
	// }
	// } catch (Exception e1) {
	// e1.printStackTrace();
	// errorTraceService.log("ScheduleTracker Syncer Inactive Schedule",
	// Enrollment.class.getName(), e.getId(), e1.getStackTrace().toString(),
	// "");
	// }
	// }
	// config.updateAppStateToken(SchedulerConfig.openmrs_syncer_sync_schedule_tracker_by_last_update_enrollment,
	// end);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	
	@MotechListener(subjects = OpenmrsConstants.SCHEDULER_OPENMRS_DATA_PUSH_SUBJECT)
	public void pushToOpenMRS(MotechEvent event) {
		
		if (!lock.tryLock()) {
			logger.warn("Not fetching forms from Message Queue. It is already in progress.");
			return;
		}
		try {
			logger.info("RUNNING " + event.getSubject() + " at " + DateTime.now());
			
			AppStateToken lastsync = config
			        .getAppStateTokenByName(SchedulerConfig.openmrs_syncer_sync_client_by_date_updated);
			Long start = lastsync == null || lastsync.getValue() == null ? 0 : lastsync.longValue();
			
			List<Client> cl = clientService.findByServerVersion(start);
			logger.info("Clients list size " + cl.size());
			for (Client c : cl) {
				// try {
				// //sentTrackCaptureDataToDHIS2(c);
				// } catch (Exception e) {
				// logger.error("DHIS2 Message:" + e.getMessage());
				// }
				try {
					// FIXME This is to deal with existing records and should be
					// removed later
					if (c.getIdentifiers().containsKey("M_ZEIR_ID")) {
						if (c.getBirthdate() == null) {
							c.setBirthdate(new DateTime("01-01-1960"));
						}
						c.setGender("Female");
					}
					String uuid = c.getIdentifier(PatientService.OPENMRS_UUID_IDENTIFIER_TYPE);
					
					if (uuid == null) {
						JSONObject p = patientService.getPatientByIdentifier(c.getBaseEntityId());
						for (Entry<String, String> id : c.getIdentifiers().entrySet()) {
							p = patientService.getPatientByIdentifier(id.getValue());
							if (p != null) {
								break;
							}
						}
						if (p != null) {
							uuid = p.getString("uuid");
						}
					}
					if (uuid != null) {
						logger.info("Updating patient " + uuid);
						patientService.updatePatient(c, uuid);
						
						config.updateAppStateToken(SchedulerConfig.openmrs_syncer_sync_client_by_date_updated,
						    c.getServerVersion());
						
					} else {
						JSONObject patientJson = patientService.createPatient(c);
						if (patientJson != null && patientJson.has("uuid")) {
							c.addIdentifier(PatientService.OPENMRS_UUID_IDENTIFIER_TYPE, patientJson.getString("uuid"));
							clientService.addorUpdate(c, false);
							
							config.updateAppStateToken(SchedulerConfig.openmrs_syncer_sync_client_by_date_updated,
							    c.getServerVersion());
							
						}
						
					}
				}
				catch (Exception ex1) {
					ex1.printStackTrace();
					errorTraceService.log("OPENMRS FAILED CLIENT PUSH", Client.class.getName(), c.getBaseEntityId(),
					    ExceptionUtils.getStackTrace(ex1), "");
				}
			}
			for (Client c : cl) {
				
				JSONObject motherJson = patientService
				        .getPatientByIdentifier(c.getRelationships().get("mother").get(0).toString());
				JSONObject person = motherJson.getJSONObject("person");
				
				if (person.getString("uuid") != null) {
					patientService.createPatientRelationShip(c.getIdentifier("OPENMRS_UUID"), person.getString("uuid"),
					    "8d91a210-c2cc-11de-8d13-0010c6dffd0f");
					logger.info("RelationshipsCreated check openrs" + c.getIdentifier("OPENMRS_UUID"));
				}
				
				List<Client> siblings = clientService
				        .findByRelationship(c.getRelationships().get("mother").get(0).toString());
				if (!siblings.isEmpty() || siblings != null) {
					JSONObject siblingJson;
					JSONObject sibling;
					for (Client client : siblings) {
						if (!c.getBaseEntityId().equals(client.getBaseEntityId())) {
							siblingJson = patientService.getPatientByIdentifier(client.getBaseEntityId());
							sibling = siblingJson.getJSONObject("person");
							patientService.createPatientRelationShip(c.getIdentifier("OPENMRS_UUID"),
							    sibling.getString("uuid"), "8d91a01c-c2cc-11de-8d13-0010c6dffd0f");
						}
						
					}
					
				}
				logger.info("RelationshipsCreated sibling1 ");
				
			}
			
			logger.info("RUNNING FOR EVENTS");
			
			lastsync = config.getAppStateTokenByName(SchedulerConfig.openmrs_syncer_sync_event_by_date_updated);
			start = lastsync == null || lastsync.getValue() == null ? 0 : lastsync.longValue();
			
			List<Event> el = eventService.findByServerVersion(start);
			logger.info("Event list size " + el.size() + " [start]" + start);
			
			for (Event e : el) {
				try {
					String uuid = e.getIdentifier(EncounterService.OPENMRS_UUID_IDENTIFIER_TYPE);
					if (uuid != null) {
						encounterService.updateEncounter(e);
						config.updateAppStateToken(SchedulerConfig.openmrs_syncer_sync_event_by_date_updated,
						    e.getServerVersion());
					} else {
						JSONObject eventJson = encounterService.createEncounter(e);
						if (eventJson != null && eventJson.has("uuid")) {
							e.addIdentifier(EncounterService.OPENMRS_UUID_IDENTIFIER_TYPE, eventJson.getString("uuid"));
							eventService.updateEvent(e);
							config.updateAppStateToken(SchedulerConfig.openmrs_syncer_sync_event_by_date_updated,
							    e.getServerVersion());
						}
					}
				}
				catch (Exception ex2) {
					logger.error("", ex2);
					errorTraceService.log("OPENMRS FAILED EVENT PUSH", Event.class.getName(), e.getId(),
					    ExceptionUtils.getStackTrace(ex2), "");
				}
			}
			
			logger.info("PUSH TO OPENMRS FINISHED AT " + DateTime.now());
		}
		catch (Exception ex) {
			logger.error("", ex);
		}
		finally {
			lock.unlock();
		}
	}
	
	private void sentTrackCaptureDataToDHIS2(Client client) throws JSONException {
		
		JSONObject clientData = new JSONObject();
		JSONArray clientAttribute = new JSONArray();
		
		JSONObject fullName = new JSONObject();
		fullName.put("attribute", "pzuh7zrs9Xx");
		fullName.put("value", client.fullName());
		clientAttribute.put(fullName);
		
		JSONObject gender = new JSONObject();
		gender.put("attribute", "xDvyz0ezL4e");
		gender.put("value", client.getGender());
		clientAttribute.put(gender);
		System.err.println(client.toString());
		if (client.getAttributes().containsKey("Father_NRC_Number")) {
			JSONObject Father_NRC_Number = new JSONObject();
			Father_NRC_Number.put("attribute", "UpMkVyXSk4b");
			Father_NRC_Number.put("value", client.getAttributes().get("Father_NRC_Number"));
			clientAttribute.put(Father_NRC_Number);
		}
		
		if (client.getAttributes().containsKey("Child_Register_Card_Number")) {
			JSONObject Child_Register_Card_Number = new JSONObject();
			Child_Register_Card_Number.put("attribute", "P5Ew7lka7GR");
			Child_Register_Card_Number.put("value", client.getAttributes().get("Child_Register_Card_Number"));
			clientAttribute.put(Child_Register_Card_Number);
		}
		if (client.getAttributes().containsKey("CHW_Phone_Number")) {
			JSONObject CHW_Phone_Number = new JSONObject();
			CHW_Phone_Number.put("attribute", "wCom53wUTKf");
			CHW_Phone_Number.put("value", client.getAttributes().get("CHW_Phone_Number"));
			clientAttribute.put(CHW_Phone_Number);
		}
		
		if (client.getAttributes().containsKey("CHW_Name")) {
			JSONObject CHW_Name = new JSONObject();
			CHW_Name.put("attribute", "t2C80PnQfJH");
			CHW_Name.put("value", client.getAttributes().get("CHW_Name"));
			clientAttribute.put(CHW_Name);
		}
		
		if (client.getAttributes().containsKey("Child_Birth_Certificate")) {
			JSONObject Child_Birth_Certificate = new JSONObject();
			Child_Birth_Certificate.put("attribute", "ZDWzVhjlgWK");
			Child_Birth_Certificate.put("value", client.getAttributes().get("Child_Birth_Certificate"));
			clientAttribute.put(Child_Birth_Certificate);
		}
		
		/////////////////////
		JSONArray enrollments = new JSONArray();
		JSONObject enrollmentsObj = new JSONObject();
		enrollmentsObj.put("orgUnit", "IDc0HEyjhvL");
		enrollmentsObj.put("program", "OprRhyWVIM6");
		enrollmentsObj.put("enrollmentDate", DateUtil.getTodayAsString());
		enrollmentsObj.put("incidentDate", DateUtil.getTodayAsString());
		enrollments.put(enrollmentsObj);
		
		clientData.put("attributes", clientAttribute);
		clientData.put("trackedEntity", "MCPQUTHX1Ze");
		clientData.put("orgUnit", "IDc0HEyjhvL");
		dhis2TrackCaptureConnector.trackCaptureDataSendToDHIS2(clientData);
	}
}
