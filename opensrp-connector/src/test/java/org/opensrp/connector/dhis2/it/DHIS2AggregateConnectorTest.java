package org.opensrp.connector.dhis2.it;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opensrp.connector.dhis2.DHIS2AggregateConnector;
import org.opensrp.connector.dhis2.Dhis2HttpUtils;
import org.opensrp.connector.openmrs.service.TestResourceLoader;
import org.opensrp.domain.Event;
import org.opensrp.domain.Obs;
import org.opensrp.repository.AllEvents;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-applicationContext-opensrp-connector.xml")
public class DHIS2AggregateConnectorTest extends TestResourceLoader {
	
	@Autowired
	private DHIS2AggregateConnector dhis2AggregateConnector;
	
	@Autowired
	private AllEvents allEvents;
	
	@Autowired
	private DHIS2AggregateConnector dHIS2AggregateConnector;
	
	@Autowired
	private Dhis2HttpUtils dhis2HttpUtils;
	
	String orgUnit = "IDc0HEyjhvL";
	
	String conceptKey = "concept";
	
	String dateKey = "date";
	
	String entityType = "child";
	
	String eventType = "Birth Registration";
	
	String formSubmissionId = "formSubmissionId";
	
	String provider = "provider";
	
	String locationId = "5bf3b4ca-9482-4e85-ab7a-0c44e4edb329";
	
	String place_Birth = "Place_Birth";
	
	String birth_Weight = "Birth_Weight";
	
	String health_Facility = "Health_Facility";
	
	public DHIS2AggregateConnectorTest() throws IOException {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Before
	public void setup() throws JSONException, IOException {
		allEvents.removeAll();
		testDeleteDHIS2Data();
	}
	
	public void testDeleteDHIS2Data() throws JSONException, IOException {
		delete(orgUnit, "xMlVHstzOgC");
		delete(orgUnit, "yNWOJ0OOOQD");
		delete(orgUnit, "ii7lOGQqEq5");
		delete(orgUnit, "Wtf7iSiQdUJ");
		
		delete(orgUnit, "XYqYdPiapTB");
		delete(orgUnit, "ghHOqHNST3Z");
		delete(orgUnit, "jY9SUZVxPHZ");
		delete(orgUnit, "MNe2NbiMPi4");
		
		delete(orgUnit, "DF4I78hJCyE");
		delete(orgUnit, "IMh3lVLICJM");
		delete(orgUnit, "belqjUALCbL");
		delete(orgUnit, "dYqIehgysyx");
		delete(orgUnit, "MR1zrXS829u");
		delete(orgUnit, "dxH32jHc21V");
	}
	
	@Test
	public void testGetAggregatedDataCount() throws JSONException, IOException {
		Event expectedEvent = new Event("049e6b44-a9b5-4553-b463-004fa6743dc2", eventType,
		        new DateTime(0l, DateTimeZone.UTC), entityType, provider, locationId, formSubmissionId);
		expectedEvent.addIdentifier("BirthRegistrationKey", "BirthRegistrationVlue");
		List<Obs> firstObservations = new ArrayList<>();
		
		firstObservations.add(getObsWithValue(birth_Weight, "4"));
		firstObservations.add(getObsWithValue(place_Birth, health_Facility));
		expectedEvent.setObs(firstObservations);
		allEvents.add(expectedEvent);
		
		Event anotherExpectedEvent = new Event("049e6b44-a9b5-4553-b463-004fa6743d34", eventType, new DateTime(0l,
		        DateTimeZone.UTC), entityType, provider, locationId, formSubmissionId);
		anotherExpectedEvent.addIdentifier("vaccineIdentifierKeyTest", "vaccineIdentifierValueTest");
		List<Obs> secondObservations = new ArrayList<>();
		
		secondObservations.add(getObsWithValue(place_Birth, health_Facility));
		
		secondObservations.add(getObsWithValue(birth_Weight, "3"));
		anotherExpectedEvent.setObs(secondObservations);
		
		allEvents.add(anotherExpectedEvent);
		
		Event expectedVaccineEvent = new Event("049e6b4r-a9b5-4553-b463-004fa6743d34", "Vaccination", new DateTime(0l,
		        DateTimeZone.UTC), entityType, provider, locationId, formSubmissionId);
		anotherExpectedEvent.addIdentifier("vaccineIdentifierKey", "vaccineIdentifierValue");
		List<Obs> vaccineObservations = new ArrayList<>();
		
		vaccineObservations.add(getObsWithVaccine("opv_1"));
		
		vaccineObservations.add(getObsWithVaccine("opv_2"));
		
		vaccineObservations.add(getObsWithVaccine("bcg"));
		
		vaccineObservations.add(getObsWithVaccine("pcv_1"));
		
		vaccineObservations.add(getObsWithVaccine("pcv_2"));
		
		vaccineObservations.add(getObsWithVaccine("penta_1"));
		
		vaccineObservations.add(getObsWithVaccine("penta_2"));
		
		vaccineObservations.add(getObsWithVaccine("rota_1"));
		
		vaccineObservations.add(getObsWithVaccine("rota_2"));
		
		expectedVaccineEvent.setObs(vaccineObservations);
		
		allEvents.add(expectedVaccineEvent);
		
		JSONObject aggregatedDataSet = null;
		
		aggregatedDataSet = dHIS2AggregateConnector.getAggregatedDataCount();
		JSONObject response = dHIS2AggregateConnector.aggredateDataSendToDHIS2(aggregatedDataSet);
		String expectedImportedCount = "14";
		JSONObject importCount = response.getJSONObject("importCount");
		String actualImportedCount = importCount.getString("imported");
		String expectedStatus = "SUCCESS";
		String actualdStatus = response.getString("status");
		assertEquals(expectedImportedCount, actualImportedCount);
		assertEquals(expectedStatus, actualdStatus);
		
		testDeleteDHIS2Data();
		
	}
	
	@Test(expected = Exception.class)
	public void testException() throws JSONException {
		JSONObject aggregatedDataSet = null;
		dHIS2AggregateConnector.aggredateDataSendToDHIS2(aggregatedDataSet);
	}
	
	public Obs getObsWithValue(String formSubmissionField, String facility) {
		Obs obs = new Obs();
		obs.setFieldCode("1572AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		obs.setFieldDataType("select one");
		obs.setFieldType(conceptKey);
		obs.setParentCode("");
		obs.setFormSubmissionField(formSubmissionField);
		List<Object> values = new ArrayList<Object>();
		values.add("1536AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		obs.setValues(values);
		List<Object> humanReadableValues = new ArrayList<Object>();
		humanReadableValues.add(facility);
		obs.setHumanReadableValues(humanReadableValues);
		return obs;
		
	}
	
	public Obs getObsWithVaccine(String vaccine) {
		Obs obs = new Obs();
		obs.setFieldCode("1410AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		obs.setFieldDataType(dateKey);
		obs.setFieldType(conceptKey);
		obs.setParentCode("783AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		obs.setFormSubmissionField(vaccine);
		List<Object> obs_values = new ArrayList<Object>();
		obs_values.add("2016-12-07");
		obs.setValues(obs_values);
		return obs;
		
	}
	
	public void delete(String ou, String de) throws JSONException, IOException {
		Calendar now = Calendar.getInstance();
		now.add(Calendar.MONTH, -1);
		int year = now.get(Calendar.YEAR);
		int month = now.get(Calendar.MONTH) + 1;
		int length = (int) (Math.log10(month) + 1);
		String formatted;
		
		if (length < 2) {
			formatted = String.format("%02d", month);
		} else {
			formatted = Integer.toString(month);
		}
		String periodTime = Integer.toString(year) + formatted;
		String url = "dataValues?pe=" + periodTime + "&ou=" + ou + "&de=" + de;
		dhis2HttpUtils.delete(url, "", "");
		
	}
	
}
