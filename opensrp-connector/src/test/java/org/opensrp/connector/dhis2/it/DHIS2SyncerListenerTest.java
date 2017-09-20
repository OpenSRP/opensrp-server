package org.opensrp.connector.dhis2.it;

import static junit.framework.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.scheduler.domain.MotechEvent;
import org.opensrp.common.AllConstants.DHIS2Constants;
import org.opensrp.connector.dhis2.DHIS2SyncerListener;
import org.opensrp.connector.dhis2.Dhis2HttpUtils;
import org.opensrp.connector.openmrs.service.TestResourceLoader;
import org.opensrp.domain.Client;
import org.opensrp.repository.AllClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-applicationContext-opensrp-connector.xml")
public class DHIS2SyncerListenerTest extends TestResourceLoader {
	
	@Autowired
	private AllClients allClients;
	
	@Autowired
	private DHIS2SyncerListener dhis2SyncerListener;
	
	@Autowired
	private Dhis2HttpUtils dhis2HttpUtils;
	
	@Before
	public void setup() {
		allClients.removeAll();
	}
	
	public DHIS2SyncerListenerTest() throws IOException {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Test
	public void testPushToDHIS2() throws JSONException {
		String baseEntityId = "29";
		String identifierTypeForChild = "ZEIR_ID";
		String identifierTypeValue = "159451-37_child";
		Client client = new Client(baseEntityId).withFirstName("Jared").withGender("male").withLastName("Omwenga")
		        .withBirthdate(new DateTime(), false);
		Map<String, String> identifiers = new HashMap<>();
		identifiers.put(identifierTypeForChild, identifierTypeValue);
		client.setIdentifiers(identifiers);
		
		Map<String, Object> attributes = new HashMap<>();
		attributes.put("Father_NRC_Number", "34");
		attributes.put("Child_Register_Card_Number", "24");
		attributes.put("CHW_Phone_Number", "01284556788");
		attributes.put("CHW_Name", "Jakly");
		attributes.put("Child_Birth_Certificate", "344");
		client.setAttributes(attributes);
		allClients.add(client);
		
		MotechEvent event = new MotechEvent(DHIS2Constants.DHIS2_TRACK_DATA_SYNCER_SUBJECT);
		JSONObject returns = dhis2SyncerListener.pushToDHIS2(event);
		
		JSONObject response = returns.getJSONObject("response");
		String expectedImport = "1";
		String actualImport = response.getString("imported");
		String expectedHttpStatusCode = "200";
		String actualHttpStatusCode = returns.getString("httpStatusCode");
		assertEquals(expectedImport, actualImport);
		assertEquals(expectedHttpStatusCode, actualHttpStatusCode);
		String trackReference = returns.getString("track");
		
		JSONArray importSummariesArray = response.getJSONArray("importSummaries");
		JSONObject importSummariesJsonObject = importSummariesArray.getJSONObject(0);
		String refId = importSummariesJsonObject.getString("reference");
		
		/*Clening data*/
		deleteEnrollment(refId);
		deleteTrackInstances(trackReference);
	}
	
	public void deleteEnrollment(String id) {
		String url = "enrollments/" + id;
		dhis2HttpUtils.delete(url, "", "");
	}
	
	public void deleteTrackInstances(String id) {
		String url = "trackedEntityInstances/" + id;
		dhis2HttpUtils.delete(url, "", "");
	}
}
