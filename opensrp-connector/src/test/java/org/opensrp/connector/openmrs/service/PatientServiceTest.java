package org.opensrp.connector.openmrs.service;

import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.opensrp.connector.FormAttributeMapper;

public class PatientServiceTest {

	private PatientService patientService;
	@Before
	public void setUp() throws Exception {
		initMocks(this);	      
		patientService = new PatientService("http://46.101.51.199:8080/openmrs/", "admin", "5rpAdmin");
	}
	
	@Test
    public void shouldCreatePersonName() {
		patientService.getPatientByName("john");
		patientService.getPersonAttribute("010700cc-1656-483d-b54d-785de49d293c");
		patientService.getPatientAllIdentifierByParentUUID("010700cc-1656-483d-b54d-785de49d293c");
	}
}
