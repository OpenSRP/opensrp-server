package org.opensrp.connector.openmrs.service.it;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertTrue;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opensrp.connector.openmrs.service.OpenmrsUserService;
import org.opensrp.connector.openmrs.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-applicationContext-opensrp-connector.xml")
public class OpenmrsUserServiceTest extends OpenmrsApiService {
	
	@Autowired
	private OpenmrsUserService openmrsUserService;
	
	@Autowired
	private PatientService patientService;
	
	public OpenmrsUserServiceTest() throws IOException {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Test
	public void testGetUser() throws JSONException {
		assertEquals(openmrsUsername, openmrsUserService.getUser(openmrsUsername).getUsername());
		assertNotSame("123456gtyyy", openmrsUserService.getUser(openmrsUsername).getUsername());
	}
	
	@Test(expected = NullPointerException.class)
	public void testNullPointerExceptionForGetUser() throws JSONException {
		openmrsUserService.getUser("openmrsUsername786").getUsername();
	}
	
	@Test
	public void testAuthenticateAnDeleteSession() throws JSONException {
		assertTrue(openmrsUserService.authenticate(openmrsUsername, openmrsPassword));
		openmrsUserService.deleteSession(openmrsUsername, openmrsPassword);
	}
	
	@Test
	public void shouldTestProviderAndUser() throws JSONException {
		
		String IdentifierType = "TestIdentifierType";
		JSONObject identifier = patientService.createIdentifierType(IdentifierType, "description");
		String identifierUuid = identifier.getString(uuidKey);
		String fn = "Royals";
		String mn = "Jack";
		String ln = "nil";
		String userName = "Royal";
		String password = "Dotel@1234";
		JSONObject person = createPerson(fn, mn, ln);
		JSONObject usr = createUser(userName, password, fn, mn, ln);
		
		String getUserName = openmrsUserService.getUser(userName).getUsername();
		assertEquals("Should equal User:", userName, getUserName);
		
		openmrsUserService.createProvider(userName, IdentifierType);
		
		JSONObject provider = openmrsUserService.getProvider(IdentifierType);
		JSONObject personObject = provider.getJSONObject(personKey);
		
		assertEquals("Should equal IdentifierType:", IdentifierType, provider.get(identifierKey));
		assertEquals("Should equal Person:", fn + " " + mn + " " + ln, personObject.get(displayKey));
		assertEquals("Should equal Provider:", IdentifierType + " - " + fn + " " + mn + " " + ln, provider.get(displayKey));
		
		deletePerson(person.getString(uuidKey));
		deleteUser(usr.getString(uuidKey));
		deleteIdentifierType(identifierUuid);
		deleteProvider(provider.getString(uuidKey));
		deletePerson(personObject.getString(uuidKey).trim());
		
	}
	
}
