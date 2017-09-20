package org.opensrp.connector.openmrs.service.it;

import static junit.framework.Assert.assertEquals;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.scheduler.domain.MotechEvent;
import org.opensrp.connector.openmrs.schedule.OpenmrsSyncerListener;
import org.opensrp.connector.openmrs.service.EncounterService;
import org.opensrp.connector.openmrs.service.OpenmrsUserService;
import org.opensrp.connector.openmrs.service.PatientService;
import org.opensrp.domain.Client;
import org.opensrp.domain.Event;
import org.opensrp.repository.AllClients;
import org.opensrp.repository.AllEvents;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-applicationContext-opensrp-connector.xml")
public class OpenmrsSyncerListenerTest extends OpenmrsApiService {
	
	@Autowired
	private AllClients allClients;
	
	@Autowired
	private AllEvents allEvents;
	
	@Autowired
	private PatientService patientService;
	
	@Autowired
	private OpenmrsUserService openmrsUserService;
	
	@Autowired
	private OpenmrsSyncerListener openmrsSyncerListener;
	
	@Autowired
	private EncounterService encounterService;
	
	MotechEvent event = new MotechEvent("subject");
	
	public OpenmrsSyncerListenerTest() throws IOException {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Before
	public void setup() {
		allEvents.removeAll();
		allClients.removeAll();
		
	}
	
	@After
	public void tearDown() {
		//allEvents.removeAll();
		//allClients.removeAll();
	}
	
	@Test
	public void testPushClient() throws JSONException {
		
		Client expectedChildClient = EventClient.getChildClient();
		allClients.add(expectedChildClient);
		Client expectedMotherClient = EventClient.getMotherClient();
		allClients.add(expectedMotherClient);
		
		String identifierType = "OPENMRS_UUID";
		JSONObject expectedIdentifier = patientService.getIdentifierType(identifierType);
		
		if (!identifierType.equals(expectedIdentifier.get(displayKey))) {
			patientService.createIdentifierType(identifierType, identifierType);
		}
		
		JSONObject createdPatientJsonObject = openmrsSyncerListener.pushClient(indexKey);
		System.out.println("createdPatientJsonObject:" + createdPatientJsonObject);
		JSONObject updatedPatient = openmrsSyncerListener.pushClient(indexKey);
		
		JSONArray createdPatientJsonArray = createdPatientJsonObject.getJSONArray("patient");
		JSONArray createdRelationArray = createdPatientJsonObject.getJSONArray(relationKey);
		JSONArray updatedRelationArray = updatedPatient.getJSONArray(relationKey);
		String actualChildNname = "";
		String actualMotherName = "";
		
		JSONObject craetedRelationObject = createdRelationArray.getJSONObject(indexKey);
		String createdRelationId = craetedRelationObject.getString(uuidKey);
		
		JSONObject updatedRelationObject = updatedRelationArray.getJSONObject(indexKey);
		String updatedRelationId = updatedRelationObject.getString(uuidKey);
		
		deleteRelation(createdRelationId);
		deleteRelation(updatedRelationId);
		
		for (int i = 0; i < createdPatientJsonArray.length(); i++) {
			JSONObject patient = createdPatientJsonArray.getJSONObject(i);
			JSONObject person = patient.getJSONObject(personKey);
			deletePerson(person.getString(uuidKey));// client person
			if (person.getString(genderKey).equalsIgnoreCase("male")) {
				actualChildNname = person.getString("display");
			} else if (person.getString(genderKey).equalsIgnoreCase("Female")) {
				actualMotherName = person.getString(displayKey);
			}
		}
		
		JSONObject childFromRelation = craetedRelationObject.getJSONObject("personB");
		JSONObject motherFromRelation = craetedRelationObject.getJSONObject("personA");
		String actualMotherNameForRelation = motherFromRelation.getString(displayKey);
		String actualChildNameForRelation = childFromRelation.getString(displayKey);
		
		assertEquals(expectedChildClient.fullName() + " -", actualChildNameForRelation);
		assertEquals(expectedMotherClient.fullName() + " -", actualMotherNameForRelation);
		
		assertEquals(expectedChildClient.fullName() + " -", actualChildNname);
		assertEquals(expectedMotherClient.fullName() + " -", actualMotherName);
		
	}
	
	@Test
	public void testPushEvent() throws JSONException {
		
		Event creatingEvent = EventClient.getEvent();
		
		allEvents.add(creatingEvent);
		
		String IdentifierType = "TestIdentifierType";
		JSONObject identifier = patientService.createIdentifierType(IdentifierType, "description");
		String identifierUuid = identifier.getString(uuidKey);
		String fn = "jack";
		String mn = "bgu";
		String ln = "nil";
		String userName = "providerId";
		String password = "Dotel@1234";
		JSONObject person = createPerson(fn, mn, ln);
		JSONObject usr = createUser(userName, password, fn, mn, ln);
		
		openmrsUserService.createProvider(userName, IdentifierType);
		
		JSONObject provider = openmrsUserService.getProvider(IdentifierType);
		JSONObject personObject = provider.getJSONObject(personKey);
		String actualEncounterType = "TestEncounterType";
		JSONObject returnEncounterType = encounterService.createEncounterType(actualEncounterType, "Test desc");
		JSONObject expectedEvent = openmrsSyncerListener.pushEvent(0);
		System.err.println("expectedEvent:" + expectedEvent);
		openmrsSyncerListener.pushEvent(0);
		
		/** Data cleaning ***/
		deletePerson(person.getString(uuidKey));
		deleteUser(usr.getString(uuidKey));
		deleteIdentifierType(identifierUuid);
		deleteProvider(provider.getString(uuidKey));
		deletePerson(personObject.getString(uuidKey).trim());
		deleteEncounter(expectedEvent.getString(uuidKey));
		deleteEncounterType(returnEncounterType.getString(uuidKey));
		JSONArray obsArray = expectedEvent.getJSONArray("obs");
		JSONObject obs = obsArray.getJSONObject(0);
		JSONArray encounterProviders = expectedEvent.getJSONArray("encounterProviders");
		JSONObject encounterProvider = encounterProviders.getJSONObject(0);
		JSONObject encounterType = expectedEvent.getJSONObject(encounterTypeKey);
		String expectedEncounterProvider = fn + " " + mn + " " + ln + ":" + " Unknown";
		String actualEncounterProvider = encounterProvider.getString(displayKey);
		
		String expectedConceptOfObservation = "WHITE BLOOD CELLS: ";
		String actualConceptOfObservation = obs.getString(displayKey);
		String expectedEncounterType = encounterType.getString(displayKey);
		
		assertEquals(expectedConceptOfObservation, actualConceptOfObservation);
		assertEquals(expectedEncounterProvider, actualEncounterProvider);
		assertEquals(expectedEncounterType, actualEncounterType);
	}
	
}
