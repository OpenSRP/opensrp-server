package org.opensrp.connector.atomfeed.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.opensrp.connector.atomfeed.AllFailedEventsInMemoryImpl;
import org.opensrp.connector.atomfeed.AllMarkersInMemoryImpl;
import org.opensrp.connector.dhis2.it.DHIS2AggregateConnectorTest;
import org.opensrp.connector.openmrs.EncounterAtomfeed;
import org.opensrp.connector.openmrs.PatientAtomfeed;
import org.opensrp.connector.openmrs.service.EncounterService;
import org.opensrp.connector.openmrs.service.OpenmrsUserService;
import org.opensrp.connector.openmrs.service.PatientService;
import org.opensrp.connector.openmrs.service.it.OpenmrsApiService;
import org.opensrp.domain.Address;
import org.opensrp.domain.Client;
import org.opensrp.domain.Event;
import org.opensrp.domain.Obs;
import org.opensrp.repository.AllClients;
import org.opensrp.repository.AllEvents;
import org.opensrp.service.ClientService;
import org.opensrp.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-applicationContext-opensrp-connector.xml")
public class AtomFeedTest extends OpenmrsApiService {
	
	@Autowired
	ClientService cs;
	
	@Autowired
	private PatientService patientService;
	
	@Autowired
	private OpenmrsUserService openmrsUserService;
	
	@Autowired
	EventService es;
	
	@Autowired
	private AllClients allClients;
	
	@Autowired
	private AllEvents allEvents;
	
	@Autowired
	private EncounterService encounterService;
	
	public AtomFeedTest() throws IOException {
		super();
	}
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		allEvents.removeAll();
		allClients.removeAll();
	}
	
	@Test
	public void shouldReadEventsCreatedEvents() throws Exception {
		/* create client info**/
		String fn = "moli";
		String mn = "raw";
		String ln = "mon";
		Map<String, String> addressFields = new HashMap<>();
		addressFields.put("ADDRESS1", "Dhaka");
		addressFields.put("ADDRESS2", "Dhaka");
		addressFields.put("ADDRESS3", "gazipur");
		addressFields.put("ADDRESS4", "gazipur");
		addressFields.put("ADDRESS4", "gazipur sadar");
		
		String attributeName = "personAttribute";
		JSONObject attribute = createPersonAttributeType("Description", attributeName);
		Map<String, Object> attributes = new HashMap<>();
		attributes.put(attributeName, "test value");
		List<Address> addresses = new ArrayList<>();
		addresses.add(new Address("BIRTH", DateTime.now(), DateTime.now(), addressFields, "LAT", "LON", "PCODE", "SINDH",
		        "PK"));
		addresses.add(new Address("DEATH", DateTime.now(), DateTime.now(), addressFields, "LATd", "LONd", "dPCODE", "KPK",
		        "PKD"));
		Map<String, Object> attribs = new HashMap<>();
		
		String baseEntity = UUID.randomUUID().toString();
		Client c = new Client(baseEntity).withFirstName(fn).withMiddleName(mn).withLastName(ln)
		        .withBirthdate(new DateTime(), true).withDeathdate(new DateTime(), false).withGender("MALE");
		c.withAddresses(addresses).withAttributes(attributes);
		JSONObject patient = patientService.createPatient(c);
		
		/** create Event Info **/
		String encounterType = "RegistartionForPerson";
		String prividerID = "detal";
		Event expectedEvent = new Event(baseEntity, encounterType, new DateTime(0l, DateTimeZone.UTC), "entityType",
		        prividerID, "locationId", "formSubmissionId");
		expectedEvent.addIdentifier("key", "value");
		
		List<Obs> observations = new ArrayList<>();
		observations.add(new DHIS2AggregateConnectorTest().getObsWithVaccine("opv1"));
		expectedEvent.setObs(observations);
		
		String IdentifierType = "TestIdentifierType";
		JSONObject identifier = patientService.createIdentifierType(IdentifierType, "description");
		String identifierUuid = identifier.getString(uuidKey);
		
		String userName = prividerID;
		String password = "Dotel@1234";
		createPerson(fn, mn, ln);
		JSONObject usr = createUser(userName, password, fn, mn, ln);
		
		openmrsUserService.createProvider(userName, IdentifierType);
		
		JSONObject provider = openmrsUserService.getProvider(IdentifierType);
		JSONObject person = provider.getJSONObject(personKey);
		
		JSONObject returnEncounterType = encounterService.createEncounterType(encounterType, "Test desc");
		JSONObject returnEvent = encounterService.createEncounter(expectedEvent);
		
		JSONObject createdPerson = patient.getJSONObject(personKey);
		
		String uuid = createdPerson.getString(uuidKey);
		/**** end event info ****/
		
		/**** start atomfeed for patient ****/
		PatientAtomfeed paf = new PatientAtomfeed(new AllMarkersInMemoryImpl(), new AllFailedEventsInMemoryImpl(),
		        openmrsOpenmrsUrl, patientService, cs);
		
		paf.processEvents();
		
		/**** start atomfeed for event ****/
		EncounterAtomfeed eaf = new EncounterAtomfeed(new AllMarkersInMemoryImpl(), new AllFailedEventsInMemoryImpl(),
		        openmrsOpenmrsUrl, encounterService, es);
		eaf.processEvents();
		
		/*** finding info ********/
		Client client = new Client("baseEntityId");
		Map<String, String> identifiers = new HashMap<>();
		identifiers.put(OPENMRS_UUIDKey, uuid);
		client.setIdentifiers(identifiers);
		Client existingClient = cs.findClient(client);
		String euuid = existingClient.getIdentifier(OPENMRS_UUIDKey);
		
		Event actualEvent = es.findByBaseEntityId(existingClient.getBaseEntityId()).get(0);
		
		/*** Cleaning data *********/
		
		deletePerson(createdPerson.getString(uuidKey));
		deleteUser(usr.getString(uuidKey));
		deleteIdentifierType(identifierUuid);
		deleteProvider(provider.getString(uuidKey));
		deletePerson(person.getString(uuidKey).trim());
		deleteEncounter(returnEvent.getString(uuidKey));
		deleteEncounterType(returnEncounterType.getString(uuidKey));
		//deletePersonAttributeType(attribute.getString("uuid"));
		
		assertEquals(uuid, euuid);
		assertNotNull(existingClient);
		assertEquals(encounterType, actualEvent.getEventType());
		assertNotNull(actualEvent);
		
	}
	
}
