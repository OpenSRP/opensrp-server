package org.opensrp.connector.openmrs.service.it;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotSame;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opensrp.connector.openmrs.constants.OpenmrsHouseHold;
import org.opensrp.connector.openmrs.service.EncounterService;
import org.opensrp.connector.openmrs.service.HouseholdService;
import org.opensrp.connector.openmrs.service.OpenmrsUserService;
import org.opensrp.connector.openmrs.service.PatientService;
import org.opensrp.domain.Client;
import org.opensrp.domain.Event;
import org.opensrp.form.domain.FormSubmission;
import org.opensrp.service.formSubmission.FormEntityConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.gson.JsonIOException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-applicationContext-opensrp-connector.xml")
public class HouseHoldServiceTest extends OpenmrsApiService {
	
	@Autowired
	private EncounterService es;
	
	@Autowired
	private FormEntityConverter oc;
	
	@Autowired
	private PatientService ps;
	
	@Autowired
	private OpenmrsUserService us;
	
	@Autowired
	private HouseholdService hhs;
	
	int firstIndex = 0;
	
	int secondIndex = 1;
	
	public HouseHoldServiceTest() throws IOException {
		super();
	}
	
	@Before
	public void setup() throws IOException {
		
	}
	
	@Test
	public void testCreateRelationshipTypeAndGetRelationshipType() throws JSONException {
		String AIsToB = "Mother";
		String BIsToA = "GrandMother";
		
		JSONObject returnRelationshipType = hhs.createRelationshipType(AIsToB, BIsToA, "test relationship");
		String expectedAIsToB = AIsToB;
		String expectedBIsToA = BIsToA;
		String actualAIsToB = returnRelationshipType.getString(aIsToBKey);
		String actualBIsToA = returnRelationshipType.getString(bIsToAKey);
		String uuid = returnRelationshipType.getString(uuidKey);
		assertEquals(expectedAIsToB, actualAIsToB);
		assertEquals(expectedBIsToA, actualBIsToA);
		String notSameRelation = "notSameRelation";
		assertNotSame(notSameRelation, actualBIsToA);
		JSONObject findRelationshipType = hhs.findRelationshipTypeMatching(AIsToB);
		
		String actualForFindRelationshipTypeBIsToA = findRelationshipType.getString(bIsToAKey);
		String actualForFindRelationshipTypeAIsToB = findRelationshipType.getString(aIsToBKey);
		assertEquals(expectedAIsToB, actualForFindRelationshipTypeAIsToB);
		assertEquals(expectedBIsToA, actualForFindRelationshipTypeBIsToA);
		JSONObject getRelationshipType = hhs.getRelationshipType(AIsToB);
		String actualForGetRelationshipTypeBIsToA = getRelationshipType.getString(bIsToAKey);
		String actualForGetRelationshipTypeAIsToB = getRelationshipType.getString(aIsToBKey);
		assertEquals(expectedAIsToB, actualForGetRelationshipTypeAIsToB);
		assertEquals(expectedBIsToA, actualForGetRelationshipTypeBIsToA);
		
		assertNotSame(notSameRelation, actualForGetRelationshipTypeBIsToA);
		deleteRelationshipType(uuid);
		JSONObject getRelationshipTypeWhichNotExists = hhs.getRelationshipType(notSameRelation);
		if (getRelationshipTypeWhichNotExists.has("error")) {
			System.out.println("Not Found");
		}
		
	}
	
	@Test
	public void testConvertRelationshipToOpenmrsJson() throws JSONException {
		String expectedPersonA = "personA";
		String expectedPersonB = "personB";
		
		JSONObject convertRelationshipToOpenmrsJson = hhs.convertRelationshipToOpenmrsJson(expectedPersonA,
		    "isARelationship", expectedPersonB);
		String actualPersonA = convertRelationshipToOpenmrsJson.getString("personA");
		String actualPersonB = convertRelationshipToOpenmrsJson.getString("personB");
		assertEquals(expectedPersonA, actualPersonA);
		assertEquals(expectedPersonB, actualPersonB);
		String notSameRelation = "notSameRelation";
		assertNotSame(notSameRelation, actualPersonA);
		assertNotSame(notSameRelation, expectedPersonB);
		
	}
	
	@Test
	public void testConvertRelationshipTypeToOpenmrsJson() throws JSONException {
		String AIsToB = "Mother";
		String BIsToA = "GrandMother";
		JSONObject convertRelationshipTypeToOpenmrsJson = hhs.convertRelationshipTypeToOpenmrsJson(AIsToB, BIsToA,
		    description);
		String expectedAIsToB = AIsToB;
		String expectedBIsToA = BIsToA;
		
		String actualAIsToB = convertRelationshipTypeToOpenmrsJson.getString(aIsToBKey);
		String actualBIsToA = convertRelationshipTypeToOpenmrsJson.getString(bIsToAKey);
		assertEquals(expectedAIsToB, actualAIsToB);
		assertEquals(expectedBIsToA, actualBIsToA);
		String notSameRelation = "notSameRelation";
		assertNotSame(notSameRelation, actualBIsToA);
	}
	
	@Test
	public void testHHScheduleData() throws JSONException, ParseException, JsonIOException, IOException {
		FormSubmission fs = getFormSubmissionFor("new_household_registration", 6);
		
		/*** create patient *******/
		
		String fn = "shumi";
		String mn = "sumaita";
		String ln = "khan";
		String baseEntity = UUID.randomUUID().toString();
		
		String attributeName = "HouseholdAttributeName";
		JSONObject attribute = createPersonAttributeType(description, attributeName);
		JSONObject firstPatient = EventClient.getCreatedPatientData(fn, mn, ln, "a3f2abf4-2699-4761-819a-cea739224164",
		    attributeName, baseEntity);
		baseEntity = UUID.randomUUID().toString();
		JSONObject secondPatient = EventClient.getCreatedPatientData(fn, mn, ln, "babcd9d2-b3e9-4f6d-8a06-2df8f5fbf01f",
		    attributeName, baseEntity);
		
		Client hhhead = oc.getClientFromFormSubmission(fs);
		Event ev = oc.getEventFromFormSubmission(fs);
		Map<String, Map<String, Object>> dep = oc.getDependentClientsFromFormSubmission(fs);
		
		OpenmrsHouseHold household = new OpenmrsHouseHold(hhhead, ev);
		for (String hhmid : dep.keySet()) {
			household.addHHMember((Client) dep.get(hhmid).get("client"), (Event) dep.get(hhmid).get("event"));
		}
		
		JSONObject pr = us.getProvider(fs.anmId());
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
				es.createEncounterType(ein.getEventType(), "Encounter type created to fullfill scheduling test pre-reqs");
			}
			
		}
		
		JSONObject response = hhs.saveHH(household, true);
		
		JSONArray encounters = response.getJSONArray(encountersKey);
		JSONArray relationships = response.getJSONArray(relationshipsKey);
		JSONObject hhEncounter = encounters.getJSONObject(firstIndex);
		String hhEncounterUUID = hhEncounter.getString(uuidKey);
		JSONObject hhEncounterType = hhEncounter.getJSONObject(encounterTypeKey);
		String actualHHEncounterTypeName = hhEncounterType.getString(displayKey);
		
		JSONObject memberEncounter = encounters.getJSONObject(secondIndex);
		String memberEncounterUUID = memberEncounter.getString(uuidKey);
		
		JSONObject meEncounterType = memberEncounter.getJSONObject(encounterTypeKey);
		String actualMEEncounterTypeName = meEncounterType.getString(displayKey);
		
		JSONObject relationship = relationships.getJSONObject(firstIndex);
		String relationshipUUID = relationship.getString(uuidKey);
		JSONObject personB = relationship.getJSONObject("personB");
		JSONObject personA = relationship.getJSONObject("personA");
		String actualPersonBName = personB.getString(displayKey);
		String actualPersonAName = personA.getString(displayKey);
		
		/* cleaning openmrs data */
		deleteEncounter(hhEncounterUUID);
		deleteEncounter(memberEncounterUUID);
		deleteRelation(relationshipUUID);
		
		String uuid = firstPatient.getString(uuidKey);
		deletePerson(uuid);
		String uuids = secondPatient.getString(uuidKey);
		deletePerson(uuids);
		deletePersonAttributeType(attribute.getString(uuidKey));
		assertEquals("New Household Registration", actualHHEncounterTypeName);
		assertEquals("Census and New Woman Registration", actualMEEncounterTypeName);
		assertEquals(fn + " " + mn + " " + ln, actualPersonBName);
		assertEquals(fn + " " + mn + " " + ln, actualPersonAName);
		
	}
	
}
