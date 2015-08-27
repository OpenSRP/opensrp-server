
package org.opensrp.connector.openmrs.service;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.opensrp.api.domain.Client;
import org.opensrp.api.domain.Event;
import org.opensrp.connector.FormAttributeMapper;
import org.opensrp.connector.OpenmrsConnector;
import org.opensrp.form.domain.FormSubmission;


public class EncounterTest extends TestResourceLoader{
	public EncounterTest() throws IOException {
		super();
	}

	EncounterService s;
	OpenmrsConnector oc;
	PatientService ps;
	OpenmrsUserService us;

	SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
	
	@Before
	public void setup() throws IOException{
		ps = new PatientService(openmrsOpenmrsUrl, openmrsUsername, openmrsPassword);
		us = new OpenmrsUserService(openmrsOpenmrsUrl, openmrsUsername, openmrsPassword);
		s = new EncounterService(openmrsOpenmrsUrl, openmrsUsername, openmrsPassword);
		s.setPatientService(ps);
		s.setUserService(us);
		FormAttributeMapper fam = new FormAttributeMapper(formDirPath);
		oc = new OpenmrsConnector(fam);
	}
	
	@Test
	public void testEncounter() throws JSONException, ParseException, IOException {
		FormSubmission fs = getFormSubmissionFor("basic_reg");
		
		Client c = oc.getClientFromFormSubmission(fs);
		assertEquals(c.getBaseEntityId(), "b716d938-1aea-40ae-a081-9ddddddcccc9");
		assertEquals(c.getBaseEntity().getFirstName(), "test woman_name");
		assertEquals(c.getBaseEntity().getGender(), "FEMALE");
		assertEquals(c.getBaseEntity().getAddresses().get(0).getAddressType(), "birthplace");
		assertEquals(c.getBaseEntity().getAddresses().get(1).getAddressType(), "usual_residence");
		assertEquals(c.getBaseEntity().getAddresses().get(2).getAddressType(), "previous_residence");
		assertEquals(c.getBaseEntity().getAddresses().get(3).getAddressType(), "deathplace");
		assertTrue(c.getBaseEntity().getAttributes().isEmpty());
		
		Event e = oc.getEventFromFormSubmission(fs);
		assertEquals(e.getEventType(), "patient_register");
		assertEquals(e.getEventDate(), sd.parse("2015-02-01"));
		assertEquals(e.getLocationId(), "unknown location");
	}
	
	@Test
	public void shouldHandleSubform() throws IOException, ParseException, JSONException{
		FormSubmission fs = getFormSubmissionFor("new_household_registration", 1);

		assertTrue(oc.isOpenmrsForm(fs));
		
		Client c = oc.getClientFromFormSubmission(fs);
		assertEquals(c.getBaseEntityId(), "a3f2abf4-2699-4761-819a-cea739224164");
		assertEquals(c.getBaseEntity().getFirstName(), "test");
		assertEquals(c.getBaseEntity().getGender(), "male");
		assertEquals(c.getBaseEntity().getBirthdate(), sd.parse("1900-01-01"));
		assertEquals(c.getBaseEntity().getAddresses().get(0).getAddressField("landmark"), "nothing");
		assertEquals(c.getBaseEntity().getAddresses().get(0).getAddressType(), "usual_residence");
		assertEquals(c.getIdentifiers().get("GOB HHID"), "1234");
		assertEquals(c.getIdentifiers().get("JiVitA HHID"), "1234");
		
		Event e = oc.getEventFromFormSubmission(fs);
		assertEquals(e.getBaseEntityId(), "a3f2abf4-2699-4761-819a-cea739224164");
		assertEquals(e.getEventDate(), sd.parse("2015-05-07"));
		assertEquals(e.getLocationId(), "KUPTALA");
		assertEquals(e.getFormSubmissionId(), "88c0e824-10b4-44c2-9429-754b8d823776");

		assertEquals(e.getObs().get(0).getFieldCode(), "160753AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		assertEquals(e.getObs().get(0).getFormSubmissionField(), "FWNHREGDATE");
		assertEquals(e.getObs().get(0).getValue(), "2015-05-07");

		assertEquals(e.getObs().get(1).getFieldCode(), "5611AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		assertEquals(e.getObs().get(1).getFormSubmissionField(), "FWNHHMBRNUM");
		assertEquals(e.getObs().get(1).getValue(), "2");
				
		Map<String, Map<String, Object>> dc = oc.getDependentClientsFromFormSubmission(fs);
		for (String id : dc.keySet()) {
			Client cl = (Client) dc.get(id).get("client");
			Event ev = (Event) dc.get(id).get("event");
			assertEquals(cl.getBaseEntityId(), id);
			assertEquals(ev.getBaseEntityId(), id);
		}
	}	
	
	@Test
	public void shouldHandleEmptyRepeatGroup() throws IOException, ParseException, JSONException{
		FormSubmission fs = getFormSubmissionFor("new_household_registration", 5);

		assertTrue(oc.isOpenmrsForm(fs));
		
		Client c = oc.getClientFromFormSubmission(fs);
		assertEquals(c.getBaseEntityId(), "a3f2abf4-2699-4761-819a-cea739224164");
		assertEquals(c.getBaseEntity().getFirstName(), "test");
		assertEquals(c.getBaseEntity().getGender(), "male");
		assertEquals(c.getBaseEntity().getBirthdate(), sd.parse("1900-01-01"));
		assertEquals(c.getBaseEntity().getAddresses().get(0).getAddressField("landmark"), "nothing");
		assertEquals(c.getBaseEntity().getAddresses().get(0).getAddressType(), "usual_residence");
		assertEquals(c.getIdentifiers().get("GOB HHID"), "1234");
		assertEquals(c.getIdentifiers().get("JiVitA HHID"), "1234");
		
		Event e = oc.getEventFromFormSubmission(fs);
		assertEquals(e.getBaseEntityId(), "a3f2abf4-2699-4761-819a-cea739224164");
		assertEquals(e.getEventDate(), sd.parse("2015-05-07"));
		assertEquals(e.getLocationId(), "KUPTALA");
		assertEquals(e.getFormSubmissionId(), "88c0e824-10b4-44c2-9429-754b8d823776");

		assertEquals(e.getObs().get(0).getFieldCode(), "160753AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		assertEquals(e.getObs().get(0).getFormSubmissionField(), "FWNHREGDATE");
		assertEquals(e.getObs().get(0).getValue(), "2015-05-07");

		assertEquals(e.getObs().get(1).getFieldCode(), "5611AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		assertEquals(e.getObs().get(1).getFormSubmissionField(), "FWNHHMBRNUM");
		assertEquals(e.getObs().get(1).getValue(), "2");
				
		Map<String, Map<String, Object>> dc = oc.getDependentClientsFromFormSubmission(fs);
		assertTrue(dc.isEmpty());
	}	
}