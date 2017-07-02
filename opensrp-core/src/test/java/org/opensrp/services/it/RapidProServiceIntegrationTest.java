package org.opensrp.services.it;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.opensrp.SpringApplicationContextProvider;
import org.opensrp.service.RapidProService;
import org.springframework.beans.factory.annotation.Autowired;

@Ignore
public class RapidProServiceIntegrationTest  extends SpringApplicationContextProvider{

	@Autowired
	RapidProService rapidproService;
	Map<String, Object> contact = new HashMap<String, Object>();
	List<String> urns = new ArrayList<String>();
	List<String> groups = new ArrayList<String>();

	@Before
	public void setup() {
		Map<String, Object> fields = new HashMap<String, Object>();
		fields.put("lmp", "2016-09-30");
		fields.put("anc1", "2016-10-10");
		fields.put("anc2", "2016-10-20");
		fields.put("anc3", "2016-10-30");
		fields.put("anc4", "2016-11-10");
		// set contact name
		contact.put("name", "Test Woman");
		groups.add("Pregnant Women");
		urns.add("telegram:207355745");
		contact.put("urns", urns);
		contact.put("groups", groups);
		contact.put("fields", fields);
	}

	@Test
	public void shouldCreateContactAndReceiveContactObjectResponse() throws Exception {
		String response = rapidproService.createContact(contact);
		JSONObject jsonResponse = new JSONObject(response);
		verify(jsonResponse);
		Assert.assertEquals(204, rapidproService.deleteContact(jsonResponse.getString("uuid")));

	}

	@Test
	public void shouldUpdateContactAndReceiveContactObjectResponse() throws Exception {
		String response = rapidproService.createContact(contact);
		JSONObject jsonResponse = new JSONObject(response);
		contact.put("uuid", jsonResponse.getString("uuid"));
		contact.put("name", "Test Woman Updated");
		response = rapidproService.createContact(contact);
		jsonResponse = new JSONObject(response);
		Assert.assertEquals("Test Woman Updated", jsonResponse.getString("name"));
		Assert.assertEquals(204, rapidproService.deleteContact(jsonResponse.getString("uuid")));

	}

	@Test
	public void shouldDeleteContactAndReceiveStatusResponse() throws Exception {
		String response = rapidproService.createContact(contact);
		JSONObject jsonResponse = new JSONObject(response);
		Assert.assertEquals(204, rapidproService.deleteContact(jsonResponse.getString("uuid")));

	}

	@Test
	@Ignore // FIXME We might need a generic mobile no to test this one out
	public void shouldSendMessageAndReceiveMessageBroadcastResponse() throws Exception {
		// rapidproService.sendMessage(urns, contacts, groups, text, channel);
	}

	@Test
	public void shouldCreateFieldAndReceiveFieldKeyResponse() throws Exception {
		String response = rapidproService.addField("Test Field", "T");
		JSONObject jsonResponse = new JSONObject(response);
		Assert.assertEquals("test_field", jsonResponse.getString("key"));
		Assert.assertEquals("Test Field", jsonResponse.getString("label"));
		Assert.assertEquals("T", jsonResponse.getString("value_type"));

	}

	private void verify(JSONObject jsonResponse) throws JSONException {
		Assert.assertTrue(jsonResponse.has("uuid"));
		Assert.assertTrue(jsonResponse.has("name"));
		Assert.assertTrue(jsonResponse.has("group_uuids"));
		Assert.assertTrue(jsonResponse.has("urns"));
		Assert.assertTrue(jsonResponse.has("fields"));
		Assert.assertTrue(jsonResponse.has("groups"));
		Assert.assertEquals("Test Woman", jsonResponse.getString("name"));
		Assert.assertEquals("2016-10-10", jsonResponse.getJSONObject("fields").getString("anc1"));
		Assert.assertEquals("2016-10-20", jsonResponse.getJSONObject("fields").getString("anc2"));
		Assert.assertEquals("2016-10-30", jsonResponse.getJSONObject("fields").getString("anc3"));
		Assert.assertEquals("2016-11-10", jsonResponse.getJSONObject("fields").getString("anc4"));
		Assert.assertEquals("Pregnant Women", jsonResponse.getJSONArray("groups").getString(0));
	}
}
