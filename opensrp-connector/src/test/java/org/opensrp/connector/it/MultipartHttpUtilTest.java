/**
 * 
 */
package org.opensrp.connector.it;

import static junit.framework.Assert.assertEquals;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opensrp.common.util.HttpResponse;
import org.opensrp.connector.MultipartHttpUtil;
import org.opensrp.connector.openmrs.service.it.OpenmrsApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author proshanto
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-applicationContext-opensrp-connector.xml")
public class MultipartHttpUtilTest extends OpenmrsApiService {
	
	final String PERSON_ATTRIBUTE_TYPE = "ws/rest/v1/personattributetype";
	
	final String OPENMRS_URL = openmrsOpenmrsUrl;
	
	@Autowired
	private MultipartHttpUtil multipartHttpUtil;
	
	public MultipartHttpUtilTest() throws IOException {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Before
	public void setup() {
		
	}
	
	@Test
	public void testPostAndGet() throws JSONException {
		String attributeName = "multipartAttribute";
		JSONObject personAttributeType = new JSONObject();
		personAttributeType.put(description, "Description");
		personAttributeType.put(nameKey, attributeName);
		personAttributeType.put(formatKey, typeString);
		String responseFromPostRequest = MultipartHttpUtil.post(
		    MultipartHttpUtil.removeEndingSlash(OPENMRS_URL) + "/" + PERSON_ATTRIBUTE_TYPE, "",
		    personAttributeType.toString(), openmrsUsername, openmrsPassword).body();
		
		JSONObject personAttributeTypeCreatedResponse = new JSONObject(responseFromPostRequest);
		String uuid = personAttributeTypeCreatedResponse.getString(uuidKey);
		assertEquals(attributeName, personAttributeTypeCreatedResponse.getString(displayKey));
		
		HttpResponse responseFromGetRequest = MultipartHttpUtil.get(MultipartHttpUtil.removeEndingSlash(OPENMRS_URL) + "/"
		        + PERSON_ATTRIBUTE_TYPE + "/" + uuid + "?v=full", "", openmrsUsername, openmrsPassword);
		deletePersonAttributeType(personAttributeTypeCreatedResponse.getString(uuidKey));
		
		JSONObject personAttributeTypeGetResponse = new JSONObject(responseFromGetRequest.body());
		assertEquals(attributeName, personAttributeTypeGetResponse.getString(displayKey));
		
	}
	
	@Test(expected = Exception.class)
	public void testIOExceptionPostAndGet() throws JSONException {
		String attributeName = "ExceptionmultipartAttribute";
		JSONObject personAttributeType = new JSONObject();
		personAttributeType.put(description, "Description text");
		personAttributeType.put(nameKey, attributeName);
		personAttributeType.put(formatKey, typeString);
		MultipartHttpUtil.post(MultipartHttpUtil.removeEndingSlash(OPENMRS_URL) + "/" + PERSON_ATTRIBUTE_TYPE, "",
		    personAttributeType.toString(), openmrsUsername, openmrsPassword).body();
		MultipartHttpUtil.post(MultipartHttpUtil.removeEndingSlash(OPENMRS_URL) + "/" + PERSON_ATTRIBUTE_TYPE, "",
		    personAttributeType.toString(), openmrsUsername, openmrsPassword).body();
		
	}
}
