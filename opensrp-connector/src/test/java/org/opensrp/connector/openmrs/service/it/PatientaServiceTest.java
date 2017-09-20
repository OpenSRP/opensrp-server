package org.opensrp.connector.openmrs.service.it;

import static junit.framework.Assert.assertEquals;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opensrp.connector.openmrs.service.PatientService;
import org.opensrp.domain.Multimedia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-applicationContext-opensrp-connector.xml")
public class PatientaServiceTest extends OpenmrsApiService {
	
	String baseEntity = UUID.randomUUID().toString();
	
	@Autowired
	private PatientService patientService;
	
	public PatientaServiceTest() throws IOException {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Before
	public void setup() {
		
	}
	
	@Test
	public void shouldCreatePatient() throws JSONException {
		String fn = "jack";
		String mn = "bgu";
		String ln = "nil";
		
		String attributeName = "HHAttribute";
		JSONObject attribute = createPersonAttributeType("Description", attributeName);
		
		JSONObject patient = EventClient.getCreatedPatientData(fn, mn, ln, "b-8912819" + new Random().nextInt(99),
		    attributeName, baseEntity);
		JSONObject person = patient.getJSONObject(personKey);
		String personName = person.getString(displayKey);
		String uuid = patient.getString(uuidKey);
		deletePerson(uuid);
		assertEquals("Should equal Person:", fn + " " + mn + " " + ln, personName);
		deletePersonAttributeType(attribute.getString(uuidKey));
		
	}
	
	@Test
	public void testPatientImageUpload() throws IOException, JSONException {
		
		String fn = "moushumi";
		String mn = "sumaita";
		String ln = "khan";
		
		String attributeName = "PatientAttributeName";
		JSONObject attribute = createPersonAttributeType("Description", attributeName);
		String OpenSRPThriveUID = "b-8912819" + new Random().nextInt(99);
		JSONObject patient = EventClient.getCreatedPatientData(fn, mn, ln, OpenSRPThriveUID, attributeName, baseEntity);
		
		Multimedia multimedia = new Multimedia();
		multimedia.setFilePath("/multimedia/sumon/images/1.jpg");
		multimedia.setCaseId(OpenSRPThriveUID);
		multimedia.setFileCategory("dp");
		multimedia.setProviderId("sumon");
		multimedia.setContentType("Image");
		String expectedResponse = "Patient Image is  successfully uploaded !";
		List<String> resposne = patientService.patientImageUpload(multimedia);
		String uuids = patient.getString(uuidKey);
		deletePerson(uuids);
		
		deletePersonAttributeType(attribute.getString(uuidKey));
		assertEquals(expectedResponse, resposne.get(0));
	}
}
