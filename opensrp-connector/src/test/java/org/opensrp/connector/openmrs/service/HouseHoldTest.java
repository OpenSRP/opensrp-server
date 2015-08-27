
package org.opensrp.connector.openmrs.service;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.isOneOf;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.hamcrest.Matchers;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.opensrp.api.domain.Client;
import org.opensrp.api.domain.Event;
import org.opensrp.connector.FormAttributeMapper;
import org.opensrp.connector.OpenmrsConnector;
import org.opensrp.connector.openmrs.constants.OpenmrsHouseHold;
import org.opensrp.form.domain.FormSubmission;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class HouseHoldTest extends TestResourceLoader{
	public HouseHoldTest() throws IOException {
		super();
	}

	EncounterService es;
	OpenmrsConnector oc;
	PatientService ps;
	OpenmrsUserService us;
	HouseholdService hhs;
	
	Map<Integer, String[]> datamap = new HashMap<Integer, String[]>(){
		private static final long serialVersionUID = 3137471662816047127L;
	{
		put(1, new String[]{"a3f2abf4-2699-4761-819a-cea739224164", "test"});
		put(2, new String[]{"0aac6d81-b51f-4096-b354-5a5786e406c8", "karim mia"});
		put(3, new String[]{"baf59aa4-64e9-46fc-99e6-8cd8f01618ff", "hasan ferox"});
		put(4, new String[]{"f92ee1b5-c3ce-42fb-bbc8-e01f474acc5a", "jashim mia"});
	}};
	
	Map<Integer, String[]> childdatamap = new HashMap<Integer, String[]>(){
		private static final long serialVersionUID = 3137471662816047127L;
	{
		put(1, new String[]{"babcd9d2-b3e9-4f6d-8a06-2df8f5fbf01f", "74eebb60-a1b9-4691-81a4-5c04ecce7ae9"});
		put(2, new String[]{"b19db74f-6e96-4652-a765-5078beb12434"});
		put(3, new String[]{"409b44c4-262a-40b8-ad7d-748c480c7c13"});
		put(4, new String[]{"0036b7ca-36ec-4242-9885-a0a03a666cda"});
	}};

	private String hhRegistrationformName = "new_household_registration";

	@Before
	public void setup() throws IOException{
		ps = new PatientService(openmrsOpenmrsUrl, openmrsUsername, openmrsPassword);
		us = new OpenmrsUserService(openmrsOpenmrsUrl, openmrsUsername, openmrsPassword);
		es = new EncounterService(openmrsOpenmrsUrl, openmrsUsername, openmrsPassword);
		es.setPatientService(ps);
		es.setUserService(us);
		hhs = new HouseholdService(openmrsOpenmrsUrl, openmrsUsername, openmrsPassword);
		hhs.setPatientService(ps);
		hhs.setEncounterService(es);
		FormAttributeMapper fam = new FormAttributeMapper(formDirPath);
		oc = new OpenmrsConnector(fam);
	}
	
	@Test
	public void shouldGetHHHeadAsClientAndEventFromHHRegistationForm() throws JsonSyntaxException, JsonIOException, IOException, ParseException {
		for (int i = 1; i <= 4; i++) {
			FormSubmission fs = getFormSubmissionFor(hhRegistrationformName, i);
			Client hhhead = oc.getClientFromFormSubmission(fs);
			testClient(hhhead, datamap.get(i)[0], datamap.get(i)[1]);
			
			Event e = oc.getEventFromFormSubmission(fs);
			
			assertNotNull(e.getObs());
			assertFalse(e.getObs().isEmpty());
			assertEquals(2, e.getObs().size());
			
			Map<String, Map<String, Object>> dep = oc.getDependentClientsFromFormSubmission(fs);
			
			assertNotNull(dep);
			assertFalse(dep.isEmpty());
			assertThat(dep, Matchers.<String,Map<String, Object>>hasKey(isOneOf(childdatamap.get(i))));
		
			for (Map<String, Object> cm : dep.values()) {
				assertThat(cm, Matchers.<String, Object>hasKey(equalTo("client")));
				assertThat(cm, Matchers.<String, Object>hasKey(equalTo("event")));

				testClient((Client) cm.get("client"), null, null);
				
				assertNotNull(cm.get("event"));
				assertTrue(cm.get("event") instanceof Event);
				
				Event depe = (Event)cm.get("event");
				assertFalse(depe.getObs().isEmpty());
				assertEquals(2, depe.getObs().size());
			}
		}
	}
	
	@Test
	public void shouldTestHHRegistrationIntegration() throws JsonSyntaxException, JsonIOException, IOException, ParseException, JSONException {
		for (int i = 1; i <= 4; i++) {
			FormSubmission fs = getFormSubmissionFor(hhRegistrationformName, i);
			Client hhhClient = oc.getClientFromFormSubmission(fs);
			
			Event hhhEvent = oc.getEventFromFormSubmission(fs);
			
			OpenmrsHouseHold hh = new OpenmrsHouseHold(hhhClient, hhhEvent);
			
			Map<String, Map<String, Object>> dep = oc.getDependentClientsFromFormSubmission(fs);
			
			for (Map<String, Object> cm : dep.values()) {
				hh.addHHMember((Client)cm.get("client"), (Event)cm.get("event"));
			}
			
			assertEquals(hh.getHouseholdHead().getClient().getBaseEntityId(), datamap.get(i)[0]);
		}
	}
	
	private void testClient(Client client, String baseEntityId, String firstName) {
		assertNotNull(client);
		assertNotNull(client.getBaseEntityId());
		assertNotNull(client.getBaseEntity());
		assertNotNull(client.getBaseEntity().getId());
		assertThat(client.getBaseEntity(), hasProperty("firstName", notNullValue()));
		assertThat(client.getBaseEntity(), hasProperty("gender", notNullValue()));
		assertThat(client.getBaseEntity(), hasProperty("birthdate", notNullValue()));
		assertNotNull(client.getIdentifiers());
		assertFalse(client.getIdentifiers().isEmpty());
		
	
		if(baseEntityId != null){
			assertNotNull(client.getBaseEntity().getAddresses());
			assertFalse(client.getBaseEntity().getAddresses().isEmpty());
			assertTrue(client.getBaseEntity().getAddresses().get(0).getAddressType().equalsIgnoreCase("usual_residence"));
			assertThat(client.getBaseEntity().getAddresses().get(0).getAddressFields(), Matchers.<String,String>hasKey("landmark"));
			assertThat(client.getBaseEntity().getAddresses().get(0).getAddressFields(), Matchers.<String,String>hasEntry(equalTo("geopoint"), notNullValue(String.class)));
			
			assertThat(client.getBaseEntity().getAddresses().get(0), hasProperty("latitude", notNullValue()));
			assertThat(client.getBaseEntity().getAddresses().get(0), hasProperty("longitute", notNullValue()));

			assertTrue(client.getIdentifiers().containsKey("GOB HHID"));
			assertTrue(client.getIdentifiers().containsKey("JiVitA HHID"));
			assertEquals(client.getBaseEntityId(), baseEntityId);
			assertEquals(client.getBaseEntity().getId(), baseEntityId);
			assertThat(client.getBaseEntity(), hasProperty("firstName", equalToIgnoringCase(firstName)));
		}
	}
}