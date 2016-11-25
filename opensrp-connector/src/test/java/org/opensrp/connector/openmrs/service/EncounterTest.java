
package org.opensrp.connector.openmrs.service;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.opensrp.domain.RelationShip;
import org.hamcrest.Matchers;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.opensrp.domain.Client;
import org.opensrp.form.domain.*;
import org.opensrp.domain.Event;
import org.opensrp.domain.Obs;
import org.opensrp.repository.AllDrugs;
import org.opensrp.connector.openmrs.constants.OpenmrsHouseHold;
import org.opensrp.form.domain.FormSubmission;
import org.opensrp.form.service.FormAttributeParser;
import org.opensrp.service.formSubmission.FormEntityConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.Log4jNestedDiagnosticContextFilter;

import com.google.gson.JsonIOException;
import com.mysql.jdbc.log.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.opensrp.repository.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-applicationContext-opensrp.xml")
public class EncounterTest extends TestResourceLoader{
	public EncounterTest() throws IOException {
		super();
	}
	private final Logger log = Logger.getLogger(getClass().getSimpleName());
	EncounterService s;
	FormEntityConverter oc;
	PatientService ps;
	OpenmrsUserService us;
	HouseholdService hhs;

	SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
	
	@Before
	public void setup() throws IOException{
		ps = new PatientService(openmrsOpenmrsUrl, openmrsUsername, openmrsPassword);
		us = new OpenmrsUserService(openmrsOpenmrsUrl, openmrsUsername, openmrsPassword);
		s = new EncounterService(openmrsOpenmrsUrl, openmrsUsername, openmrsPassword);
		s.setPatientService(ps);
		s.setUserService(us);
		hhs = new HouseholdService(openmrsOpenmrsUrl, openmrsUsername, openmrsPassword);
		hhs.setPatientService(ps);
		hhs.setEncounterService(s);
		final FormAttributeParser fam = new FormAttributeParser(formDirPath);
		oc = new FormEntityConverter(fam);
	}
	
	@Ignore
	@Test
	public void testEncounter() throws JSONException, ParseException, IOException {
		final FormSubmission fs = getFormSubmissionFor("basic_reg");
		
		final Client c = oc.getClientFromFormSubmission(fs);
		assertEquals(c.getBaseEntityId(), "b716d938-1aea-40ae-a081-9ddddddcccc9");
		assertEquals(c.getFirstName(), "test woman_name");
		assertEquals(c.getGender(), "FEMALE");
		assertEquals(c.getAddresses().get(0).getAddressType(), "birthplace");
		assertEquals(c.getAddresses().get(1).getAddressType(), "usual_residence");
		assertEquals(c.getAddresses().get(2).getAddressType(), "previous_residence");
		assertEquals(c.getAddresses().get(3).getAddressType(), "deathplace");
		assertTrue(c.getAttributes().isEmpty());
		
		final Event e = oc.getEventFromFormSubmission(fs);
		assertEquals(e.getEventType(), "patient_register");
		assertEquals(e.getEventDate(), new DateTime(new DateTime("2015-02-01")));
		assertEquals(e.getLocationId(), "unknown location");
		
		if(pushToOpenmrsForTest){
			
			System.out.println(ps.getPatientByIdentifier(c.getBaseEntityId()));
			JSONObject p = ps.getPatientByIdentifier(c.getBaseEntityId());
			if(p == null){
				p = ps.createPatient(c);
			}
			final JSONObject en = s.createEncounter(e);
			System.out.println(en);
		}
	}
	
	
	@Test
	public void relationshipTest() throws JSONException, ParseException, IOException {
		final FormSubmission fs = getFormSubmissionFor("new_member_registration");
		final Client c = oc.getClientFromFormSubmission(fs);
		System.out.print(c);
		if(pushToOpenmrsForTest){
			System.out.println(ps.getPatientByIdentifier(c.getBaseEntityId()));
			JSONObject p = ps.getPatientByIdentifier(c.getBaseEntityId());
			if(p == null){
				p = ps.createPatient(c);
			}
		final JSONObject relationShipJSON=hhs.getRelationshipType(c.getRelationships().get(0).getRelationship());
		final String relationShipUUID=relationShipJSON.getString("uuid");
		JSONObject o=hhs.getRelationship(p.getString("uuid"),c.getRelationships().get(0).getRelationship(),c.getRelationships().get(0).getPerson_b());
		System.out.println("Before If: "+ o);
		if(o==null)
		{
			o= hhs.createRelationship(p.getString("uuid"), relationShipUUID,c.getRelationships().get(0).getPerson_b());
			System.out.println("JsonObject: "+ o);	
			return;
		}
		System.out.println("RelationShip UUID: "+ o.getString("uuid"));
		}
	}
	
	
	@Ignore
	@Test
	public void testGroupedEncounter() throws JSONException, ParseException, IOException {
		final FormSubmission fs = getFormSubmissionFor("repeatform");
		
		final Client c = oc.getClientFromFormSubmission(fs);
//TODO		
		final Event e = oc.getEventFromFormSubmission(fs);
//TODO
		/*if(true){
			JSONObject p = ps.getPatientByIdentifier(c.getBaseEntityId());
			if(p == null){
				p = ps.createPatient(c);
			}
			JSONObject en = s.createEncounter(e);
			System.out.println(en);
		}*/
	}
	@Ignore
	@Test
	public void shouldHandleSubform() throws IOException, ParseException, JSONException{
		final FormSubmission fs = getFormSubmissionFor("new_household_registration", 1);

		final Client c = oc.getClientFromFormSubmission(fs);
		assertEquals(c.getBaseEntityId(), "a3f2abf4-2699-4761-819a-cea739224164");
		assertEquals(c.getFirstName(), "test");
		assertEquals(c.getGender(), "male");
		assertEquals(c.getBirthdate(), new DateTime("1900-01-01"));
		assertEquals(c.getAddresses().get(0).getAddressField("landmark"), "nothing");
		assertEquals(c.getAddresses().get(0).getAddressType(), "usual_residence");
		assertEquals(c.getIdentifiers().get("GOB HHID"), "1234");
		assertEquals(c.getIdentifiers().get("JiVitA HHID"), "1234");
		
		final Event e = oc.getEventFromFormSubmission(fs);
		assertEquals(e.getBaseEntityId(), "a3f2abf4-2699-4761-819a-cea739224164");
		assertEquals(e.getEventDate(), new DateTime(new DateTime("2015-05-07")));
		assertEquals(e.getLocationId(), "KUPTALA");
		assertEquals(e.getFormSubmissionId(), "88c0e824-10b4-44c2-9429-754b8d823776");

		assertEquals(e.getObs().get(0).getFieldCode(), "160753AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		assertEquals(e.getObs().get(0).getFormSubmissionField(), "FWNHREGDATE");
		assertEquals(e.getObs().get(0).getValue(), "2015-05-07");

		assertEquals(e.getObs().get(1).getFieldCode(), "5611AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		assertEquals(e.getObs().get(1).getFormSubmissionField(), "FWNHHMBRNUM");
		assertEquals(e.getObs().get(1).getValue(), "2");
		
		final Map<String, Map<String, Object>> dc = oc.getDependentClientsFromFormSubmission(fs);
		final int i=0;
		for (final String id : dc.keySet()) {
			final Client cl = (Client) dc.get(id).get("client");
			final Event ev = (Event) dc.get(id).get("event");
			assertEquals(cl.getBaseEntityId(), id);
			assertEquals(ev.getBaseEntityId(), id);
		}
	}	
	@Ignore
	@Test
	public void shouldHandleEmptyRepeatGroup() throws IOException, ParseException, JSONException{
		final FormSubmission fs = getFormSubmissionFor("new_household_registration", 5);

		final Client c = oc.getClientFromFormSubmission(fs);
		assertEquals(c.getBaseEntityId(), "a3f2abf4-2699-4761-819a-cea739224164");
		assertEquals(c.getFirstName(), "test");
		assertEquals(c.getGender(), "male");
		assertEquals(c.getBirthdate(), new DateTime("1900-01-01"));
		assertEquals(c.getAddresses().get(0).getAddressField("landmark"), "nothing");
		assertEquals(c.getAddresses().get(0).getAddressType(), "usual_residence");
		assertEquals(c.getIdentifiers().get("GOB HHID"), "1234");
		assertEquals(c.getIdentifiers().get("JiVitA HHID"), "1234");
		
		final Event e = oc.getEventFromFormSubmission(fs);
		assertEquals(e.getBaseEntityId(), "a3f2abf4-2699-4761-819a-cea739224164");
		assertEquals(e.getEventDate(), new DateTime(new DateTime("2015-05-07")));
		assertEquals(e.getLocationId(), "KUPTALA");
		assertEquals(e.getFormSubmissionId(), "88c0e824-10b4-44c2-9429-754b8d823776");

		assertEquals(e.getObs().get(0).getFieldCode(), "160753AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		assertEquals(e.getObs().get(0).getFormSubmissionField(), "FWNHREGDATE");
		assertEquals(e.getObs().get(0).getValue(), "2015-05-07");

		assertEquals(e.getObs().get(1).getFieldCode(), "5611AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		assertEquals(e.getObs().get(1).getFormSubmissionField(), "FWNHHMBRNUM");
		assertEquals(e.getObs().get(1).getValue(), "2");
				
		final Map<String, Map<String, Object>> dc = oc.getDependentClientsFromFormSubmission(fs);
		assertTrue(dc.isEmpty());
	}	
	@Ignore
	@Test
	public void shouldGetBirthdateNotEstimatedForMainAndApproxForRepeatGroup() throws IOException, ParseException, JSONException{
		final FormSubmission fs = getFormSubmissionFor("new_household_registration", 7);

		final Client c = oc.getClientFromFormSubmission(fs);
		assertEquals(c.getBirthdate(), new DateTime("1900-01-01"));
		assertTrue(c.getBirthdateApprox());
		
		final Map<String, Map<String, Object>> dc = oc.getDependentClientsFromFormSubmission(fs);
		for (final String id : dc.keySet()) {
			final Client cl = (Client) dc.get(id).get("client");
			assertEquals(cl.getBirthdate(), new DateTime("2000-05-07"));
			assertFalse(cl.getBirthdateApprox());
		}
	}	
	@Ignore
	@Test
	public void shouldGetBirthdateNotEstimatedForMainAndRepeatGroupIfNotSpecified() throws IOException, ParseException, JSONException{
		final FormSubmission fs = getFormSubmissionFor("new_household_registration", 8);

		final Client c = oc.getClientFromFormSubmission(fs);
		assertEquals(c.getBirthdate(), new DateTime("1900-01-01"));
		assertFalse(c.getBirthdateApprox());
		
		final Map<String, Map<String, Object>> dc = oc.getDependentClientsFromFormSubmission(fs);
		for (final String id : dc.keySet()) {
			final Client cl = (Client) dc.get(id).get("client");
			assertEquals(cl.getBirthdate(), new DateTime("2000-05-07"));
			assertFalse(cl.getBirthdateApprox());
		}
	}	
	
	@Ignore
	@SuppressWarnings("unchecked")
	@Test
	public void shouldGetDataSpecifiedInGroupInsideSubform() throws IOException, ParseException, JSONException{
		final FormSubmission fs = getFormSubmissionFor("new_household_registration_with_grouped_subform_data", 1);

		final Client c = oc.getClientFromFormSubmission(fs);
		assertEquals(c.getBirthdate(), new DateTime("1900-01-01"));
		assertFalse(c.getBirthdateApprox());
		assertThat(c.getAttributes(), Matchers.<String, Object>hasEntry(equalTo("GoB_HHID"), equalTo((Object)"2322")));
		assertThat(c.getAttributes(), Matchers.<String, Object>hasEntry(equalTo("JiVitA_HHID"), equalTo((Object)"9889")));
		
		final Event e = oc.getEventFromFormSubmission(fs);
		assertEquals(e.getBaseEntityId(), c.getBaseEntityId());
		assertEquals(e.getEventType(), "New Household Registration");
		assertEquals(e.getEventDate(), new DateTime(new SimpleDateFormat("yyyy-M-dd").parse("2015-10-11")));
		assertEquals(e.getLocationId(), "2fc43738-ace5-g961-8e8f-ab7dg0e5bc63");
		
		assertThat(e.getObs(), Matchers.<Obs>hasItem(Matchers.<Obs>allOf(
				Matchers.<Obs>hasProperty("fieldCode",equalTo("5611AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")),
				Matchers.<Obs>hasProperty("value",equalTo("23")),
				Matchers.<Obs>hasProperty("formSubmissionField",equalTo("FWNHHMBRNUM"))
				)));
		
		final Map<String, Map<String, Object>> dc = oc.getDependentClientsFromFormSubmission(fs);
		for (final String id : dc.keySet()) {
			final Client cl = (Client) dc.get(id).get("client");
			assertEquals(cl.getBirthdate(), new DateTime("1988-10-08"));
			assertFalse(cl.getBirthdateApprox());
			assertEquals(cl.getFirstName(), "jackfruit");
			assertEquals(cl.getAddresses().get(0).getCountry(), "Bangladesh");
			assertEquals(cl.getAddresses().get(0).getAddressType(), "usual_residence");
			assertEquals(cl.getAddresses().get(0).getStateProvince(), "RANGPUR");
			assertThat(cl.getIdentifiers(), Matchers.<String, String>hasEntry(equalTo("NID"), equalTo("7675788777775")));
			assertThat(cl.getIdentifiers(), Matchers.<String, String>hasEntry(equalTo("Birth Registration ID"), equalTo("98899998888888888")));
			assertThat(cl.getAttributes(), Matchers.<String, Object>hasEntry(equalTo("GoB_HHID"), equalTo((Object)"2322")));
			assertThat(cl.getAttributes(), Matchers.<String, Object>hasEntry(equalTo("JiVitA_HHID"), equalTo((Object)"9889")));
		
			final Event ev = (Event) dc.get(id).get("event");
			assertEquals(ev.getBaseEntityId(), cl.getBaseEntityId());
			assertEquals(ev.getEventType(), "New Woman Registration");
			assertEquals(ev.getEventDate(), new DateTime(new SimpleDateFormat("yyyy-M-dd").parse("2015-10-11")));
			assertEquals(ev.getLocationId(), "2fc43738-ace5-g961-8e8f-ab7dg0e5bc63");
			
			assertThat(ev.getObs(), Matchers.<Obs>hasItem(Matchers.<Obs>allOf(
					Matchers.<Obs>hasProperty("fieldCode",equalTo("161135AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")),
					Matchers.<Obs>hasProperty("value",equalTo("zoom")),
					Matchers.<Obs>hasProperty("formSubmissionField",equalTo("FWHUSNAME"))
					)));
			assertThat(ev.getObs(), Matchers.<Obs>hasItem(Matchers.<Obs>allOf(
					Matchers.<Obs>hasProperty("fieldCode",equalTo("163087AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")),
					Matchers.<Obs>hasProperty("values",hasItems(equalTo("163084AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"),equalTo("163083AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"))),
					Matchers.<Obs>hasProperty("formSubmissionField",equalTo("FWWOMANYID"))
					)));
		}
		
		if(pushToOpenmrsForTest){
			final OpenmrsHouseHold hh = new OpenmrsHouseHold(c, e);
			for (final Map<String, Object> cm : dc.values()) {
				hh.addHHMember((Client)cm.get("client"), (Event)cm.get("event"));
			}
			
			hhs.saveHH(hh, true);
		}
	}
	
	@Ignore
	@SuppressWarnings("unchecked")
	@Test
	public void shouldGetDataSpecifiedInMultiselect() throws IOException, ParseException, JSONException{
		final FormSubmission fs = getFormSubmissionFor("new_household_registration_with_grouped_subform_data", 1);

		final Client c = oc.getClientFromFormSubmission(fs);
		final Event e = oc.getEventFromFormSubmission(fs);
		
		final Map<String, Map<String, Object>> dc = oc.getDependentClientsFromFormSubmission(fs);
		for (final String id : dc.keySet()) {
			final Client cl = (Client) dc.get(id).get("client");
			final Event ev = (Event) dc.get(id).get("event");
			
			assertThat(ev.getObs(), Matchers.<Obs>hasItem(Matchers.<Obs>allOf(
					Matchers.<Obs>hasProperty("fieldCode",equalTo("163087AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")),
					Matchers.<Obs>hasProperty("values",hasItems(equalTo("163084AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"),equalTo("163083AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"))),
					Matchers.<Obs>hasProperty("formSubmissionField",equalTo("FWWOMANYID")),
					Matchers.<Obs>hasProperty("fieldType",equalTo("concept")),
					Matchers.<Obs>hasProperty("fieldDataType",startsWith("select all"))
					)));
		}
		
		if(pushToOpenmrsForTest){
			final OpenmrsHouseHold hh = new OpenmrsHouseHold(c, e);
			for (final Map<String, Object> cm : dc.values()) {
				hh.addHHMember((Client)cm.get("client"), (Event)cm.get("event"));
			}
			
			hhs.saveHH(hh, true);
		}
	}
	
	@Ignore
	@Test
	public void parentChildObsTest() throws JsonIOException, IOException, JSONException {
		final FormSubmission fs = getFormSubmissionFor("psrf_form");
		
		final Client c = oc.getClientFromFormSubmission(fs);
		final Event e = (Event) oc.getEventFromFormSubmission(fs);
		
		if(pushToOpenmrsForTest){
			
			JSONObject p = ps.getPatientByIdentifier(c.getBaseEntityId());
			if(p == null){
				p = ps.createPatient(c);
			}
			s.createEncounter(e);
		}

	}
	
	@Ignore
	@SuppressWarnings("unchecked")
	@Test
	public void shouldHandleTTEnrollmentform() throws IOException, ParseException, JSONException{
		final FormSubmission fs = getFormSubmissionFor("woman_enrollment");

		final Client c = oc.getClientFromFormSubmission(fs);
		assertEquals(c.getBaseEntityId(), "69995674-bb29-4985-967a-fec8d372a475");
		assertEquals(c.getFirstName(), "barsaat");
		assertEquals(c.getGender(), "female");
		assertEquals(c.getBirthdate(), new DateTime("1979-04-05"));
		assertEquals(c.getAddresses().get(0).getAddressField("landmark"), "nishaani");
		assertEquals(c.getAddresses().get(0).getStateProvince(), "sindh");
		assertEquals(c.getAddresses().get(0).getCityVillage(), "karachi");
		assertEquals(c.getAddresses().get(0).getTown(), "liaquatabad");
		assertEquals(c.getAddresses().get(0).getSubTown(), "sharifabad");
		assertEquals(c.getAddresses().get(0).getAddressField("house"), "6h");
		assertEquals(c.getIdentifiers().get("Program Client ID"), "14608844");
		assertEquals(c.getAttributes().get("EPI Card Number"), "20160003");

		final Event e = oc.getEventFromFormSubmission(fs);
		assertEquals(e.getBaseEntityId(), "69995674-bb29-4985-967a-fec8d372a475");
		assertEquals(e.getEventDate(), new DateTime(new DateTime("2016-04-05")));
		assertEquals(e.getLocationId(), "Homeopathic Center");
		assertEquals(e.getFormSubmissionId(), "de408c93-2ec5-40bc-a957-eaf375583e27");
		assertEquals(e.getEntityType(), "pkwoman");
		assertEquals(e.getEventType(), "Woman TT enrollment");
		assertEquals(e.getProviderId(), "demotest");

		assertThat(e.getObs(), Matchers.<Obs>hasItem(Matchers.<Obs>allOf(
				Matchers.<Obs>hasProperty("fieldCode",equalTo("154384AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")),
				Matchers.<Obs>hasProperty("values",hasItems(equalTo("37"))),
				Matchers.<Obs>hasProperty("formSubmissionField",equalTo("calc_age_confirm")),
				Matchers.<Obs>hasProperty("fieldType",equalTo("concept")),
				Matchers.<Obs>hasProperty("fieldDataType",startsWith("calculate")),
				Matchers.<Obs>hasProperty("effectiveDatetime", equalTo(e.getEventDate()))
				)));
		
		assertThat(e.getObs(), Matchers.<Obs>hasItem(Matchers.<Obs>allOf(
				Matchers.<Obs>hasProperty("fieldCode",equalTo("163137AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")),
				Matchers.<Obs>hasProperty("values",hasItems(equalTo("2016-04-05 16:21:32"))),
				Matchers.<Obs>hasProperty("formSubmissionField",equalTo("start")),
				Matchers.<Obs>hasProperty("fieldType",equalTo("concept")),
				Matchers.<Obs>hasProperty("fieldDataType",startsWith("start")),
				Matchers.<Obs>hasProperty("effectiveDatetime", equalTo(e.getEventDate()))
				)));
		
		assertThat(e.getObs(), Matchers.<Obs>hasItem(Matchers.<Obs>allOf(
				Matchers.<Obs>hasProperty("fieldCode",equalTo("163138AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")),
				Matchers.<Obs>hasProperty("values",hasItems(equalTo("2016-04-05 16:23:59"))),
				Matchers.<Obs>hasProperty("formSubmissionField",equalTo("end")),
				Matchers.<Obs>hasProperty("fieldType",equalTo("concept")),
				Matchers.<Obs>hasProperty("fieldDataType",startsWith("end")),
				Matchers.<Obs>hasProperty("effectiveDatetime", equalTo(e.getEventDate()))
				)));
		
		if(pushToOpenmrsForTest){
			
			JSONObject p = ps.getPatientByIdentifier(c.getBaseEntityId());
			if(p == null){
				p = ps.createPatient(c);
			}
			s.createEncounter(e);
		}

	}
	
	@Ignore
	@SuppressWarnings("unchecked")
	@Test
	public void shouldHandleChildVaccinationEnrollmentform() throws IOException, ParseException, JSONException{
		final FormSubmission fs = getFormSubmissionFor("child_enrollment");

		final Client c = oc.getClientFromFormSubmission(fs);
		assertEquals(c.getBaseEntityId(), "ad653225-6bed-48d3-8e5d-741d3d50d61a");
		assertEquals(c.getFirstName(), "aase");
		assertEquals(c.getLastName(), "zeest");
		assertEquals(c.getGender(), "male");
		assertEquals(c.getBirthdate(), new DateTime("2016-01-03"));
		assertEquals(c.getAddresses().get(0).getAddressField("landmark"), "nishaani");
		assertEquals(c.getAddresses().get(0).getStateProvince(), "sindh");
		assertEquals(c.getAddresses().get(0).getCityVillage(), "karachi");
		assertEquals(c.getAddresses().get(0).getTown(), "liaquatabad");
		assertEquals(c.getAddresses().get(0).getSubTown(), "mujahid_colony");
		assertEquals(c.getAddresses().get(0).getAddressField("house"), "hi65");
		assertEquals(c.getIdentifiers().get("Program Client ID"), "98120722");
		assertEquals(c.getAttributes().get("EPI Card Number"), "20160009");

		final Event e = oc.getEventFromFormSubmission(fs);
		assertEquals(e.getBaseEntityId(), "ad653225-6bed-48d3-8e5d-741d3d50d61a");
		assertEquals(e.getEventDate(), new DateTime(new DateTime("2016-03-05")));
		assertEquals(e.getLocationId(), "Homeopathic Center");
		assertEquals(e.getFormSubmissionId(), "8524f6b8-441a-4769-aa74-03e1dde0901a");
		assertEquals(e.getEntityType(), "pkchild");
		assertEquals(e.getEventType(), "Child Vaccination Enrollment");
		assertEquals(e.getProviderId(), "demotest");

		assertThat(e.getObs(), Matchers.<Obs>hasItem(Matchers.<Obs>allOf(
				Matchers.<Obs>hasProperty("fieldCode",equalTo("154384AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")),
				Matchers.<Obs>hasProperty("values",hasItems(equalTo("2"))),
				Matchers.<Obs>hasProperty("formSubmissionField",equalTo("calc_age_confirm")),
				Matchers.<Obs>hasProperty("fieldType",equalTo("concept")),
				Matchers.<Obs>hasProperty("fieldDataType",startsWith("calculate")),
				Matchers.<Obs>hasProperty("effectiveDatetime", equalTo(e.getEventDate()))
				)));
		
		assertThat(e.getObs(), Matchers.<Obs>hasItem(Matchers.<Obs>allOf(
				Matchers.<Obs>hasProperty("fieldCode",equalTo("163137AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")),
				Matchers.<Obs>hasProperty("values",hasItems(equalTo("2016-03-05 23:01:13"))),
				Matchers.<Obs>hasProperty("formSubmissionField",equalTo("start")),
				Matchers.<Obs>hasProperty("fieldType",equalTo("concept")),
				Matchers.<Obs>hasProperty("fieldDataType",startsWith("start")),
				Matchers.<Obs>hasProperty("effectiveDatetime", equalTo(e.getEventDate()))
				)));
		
		assertThat(e.getObs(), Matchers.<Obs>hasItem(Matchers.<Obs>allOf(
				Matchers.<Obs>hasProperty("fieldCode",equalTo("163138AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")),
				Matchers.<Obs>hasProperty("values",hasItems(equalTo("2016-03-05 23:03:51"))),
				Matchers.<Obs>hasProperty("formSubmissionField",equalTo("end")),
				Matchers.<Obs>hasProperty("fieldType",equalTo("concept")),
				Matchers.<Obs>hasProperty("fieldDataType",startsWith("end")),
				Matchers.<Obs>hasProperty("effectiveDatetime", equalTo(e.getEventDate()))
				)));
		
		if(pushToOpenmrsForTest){
			
			JSONObject p = ps.getPatientByIdentifier(c.getBaseEntityId());
			if(p == null){
				p = ps.createPatient(c);
			}
			s.createEncounter(e);
		}

	}
}