package org.opensrp.service.it;

import org.ektorp.CouchDbConnector;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.opensrp.BaseIntegrationTest;
import org.opensrp.domain.Client;
import org.opensrp.repository.AllClients;
import org.opensrp.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.utils.CouchDbAccessUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;
import static org.opensrp.util.SampleFullDomainObject.*;
import static org.utils.AssertionUtil.*;
import static org.utils.CouchDbAccessUtils.addObjectToRepository;
import static org.utils.CouchDbAccessUtils.getCouchDbConnector;

//TODO: Write couch-lucene related method test cases e.g: findByCriteria
public class ClientServiceTest extends BaseIntegrationTest {

	@Autowired
	private AllClients allClients;

	@Autowired
	private ClientService clientService;

	@Before
	public void setUp() {
		allClients.removeAll();
	}

	@After
	public void cleanUp() {
		allClients.removeAll();
	}

	@Test
	public void shouldFindByBaeEntityId() {
		String baseEntityId = "baseEntityId";
		Client expectedClient = new Client(baseEntityId);
		Client invalidClient = new Client("b2");
		Client invalidClientSecond = new Client("b3");
		List<Client> clientList = asList(expectedClient, invalidClient, invalidClientSecond);
		addObjectToRepository(clientList, allClients);

		Client actualClient = clientService.getByBaseEntityId(baseEntityId);

		assertEquals(expectedClient, actualClient);
	}

	@Test
	public void shouldFindAllClient() {
		Client expectedClient = new Client("b1");
		Client expectedClient2 = new Client("b2");
		Client expectedClient3 = new Client("b3");
		List<Client> expectedClientList = asList(expectedClient, expectedClient2, expectedClient3);
		addObjectToRepository(expectedClientList, allClients);

		List<Client> actualClientList = clientService.findAllClients();

		assertTwoListAreSameIgnoringOrder(expectedClientList, actualClientList);

	}

	@Test
	public void shouldFindAllClientsByIdentifierValue() {
		Client expectedClient = new Client("b1");
		expectedClient.addIdentifier("type", "value");
		Client expectedClient2 = new Client("b2");
		expectedClient2.addIdentifier("type", "value");
		Client invalidClient = new Client("b3");
		invalidClient.addIdentifier("type2", "value2");
		addObjectToRepository(asList(expectedClient, expectedClient2, invalidClient), allClients);
		List<Client> expectedClientList = asList(expectedClient, expectedClient2);

		List<Client> actualClientList = clientService.findAllByIdentifier("value");

		assertTwoListAreSameIgnoringOrder(expectedClientList, actualClientList);
	}

	@Test
	public void shouldFindAllClientsByIdentifierTypeAndValue() {
		Client expectedClient = new Client("b1");
		expectedClient.addIdentifier("type", "value");
		Client expectedClient2 = new Client("b2");
		expectedClient2.addIdentifier("type", "value");
		Client invalidClient = new Client("b3");
		invalidClient.addIdentifier("type2", "value2");
		addObjectToRepository(asList(expectedClient, expectedClient2, invalidClient), allClients);
		List<Client> expectedClientList = asList(expectedClient, expectedClient2);

		List<Client> actualClientList = clientService.findAllByIdentifier("type", "value");
		assertTwoListAreSameIgnoringOrder(expectedClientList, actualClientList);
	}

	@Test
	public void shouldFindAllClientsByRelationIdAndDateCreated() {
		Client expectedClient = new Client("b1");
		expectedClient.addRelationship("mother", "id");
		expectedClient.setDateCreated(new DateTime(100L, DateTimeZone.UTC));
		Client expectedClient2 = new Client("b2");
		expectedClient2.addRelationship("mother", "id");
		expectedClient2.setDateCreated(new DateTime(200L, DateTimeZone.UTC));
		Client invalidClient = new Client("b3");
		invalidClient.addRelationship("mother", "id2");
		expectedClient.setDateCreated(new DateTime(300L, DateTimeZone.UTC));
		addObjectToRepository(asList(expectedClient, expectedClient2, invalidClient), allClients);
		List<Client> expectedClientList = asList(expectedClient, expectedClient2);

		List<Client> actualClientList = clientService
				.findByRelationshipIdAndDateCreated("id", new DateTime(100L, DateTimeZone.UTC).toLocalDate().toString(),
						new DateTime(200L, DateTimeZone.UTC).toLocalDate().toString());

		assertTwoListAreSameIgnoringOrder(expectedClientList, actualClientList);
	}

	//TODO: Couch-lucene query error
	@Test
	@Ignore
	public void shouldFindByRelationShip() {
		Client expectedClient = getClient();
		expectedClient.addRelationship("mother", "id");
		expectedClient.setDateCreated(new DateTime(DateTimeZone.UTC));
		Client expectedClient2 = getClient();
		expectedClient2.setBaseEntityId("dd");
		expectedClient2.addRelationship("mother", "id");
		expectedClient2.setDateCreated(new DateTime(DateTimeZone.UTC));
		Client invalidClient = getClient();
		invalidClient.setBaseEntityId("ddss");
		invalidClient.addRelationship("mother", "id2");
		expectedClient.setDateCreated(new DateTime(DateTimeZone.UTC));
		addObjectToRepository(asList(expectedClient, expectedClient2, invalidClient), allClients);
		List<Client> expectedClientList = asList(expectedClient, expectedClient2);

		List<Client> actualClientList = clientService.findByRelationship("id");

		assertTwoListAreSameIgnoringOrder(expectedClientList, actualClientList);
	}

	@Test
	public void shouldFindByAttributeTypeAndValue() {
		Client expectedClient = new Client("b1");
		expectedClient.addAttribute("type", "value");
		Client expectedClient2 = new Client("b2");
		expectedClient2.addAttribute("type", "value");
		Client invalidClient = new Client("b3");
		invalidClient.addAttribute("type2", "value2");
		addObjectToRepository(asList(expectedClient, expectedClient2, invalidClient), allClients);
		List<Client> expectedClientList = asList(expectedClient, expectedClient2);

		List<Client> actualClientList = clientService.findAllByAttribute("type", "value");
		assertTwoListAreSameIgnoringOrder(expectedClientList, actualClientList);
	}

	@Test
	public void shouldFindAllMatchingName() {
		Client expectedClient = new Client("b1");
		expectedClient.setFirstName("first");
		expectedClient.setLastName("last");
		Client expectedClient2 = new Client("b2");
		expectedClient2.setFirstName("first");
		expectedClient2.setLastName("last");
		Client invalidClient = new Client("b3");
		invalidClient.setFirstName("invalid");
		invalidClient.setLastName("invalid");
		addObjectToRepository(asList(expectedClient, expectedClient2, invalidClient), allClients);
		List<Client> expectedClientList = asList(expectedClient, expectedClient2);

		List<Client> actualClientList = clientService.findAllByMatchingName("first");
		assertTwoListAreSameIgnoringOrder(expectedClientList, actualClientList);
	}

	/*@Test
	@Ignore
	public void shouldFindByAllCriteria() {
		Client expectedClient = SampleFullDomainObject.client;
		Client invalidClient = new Client(SampleFullDomainObject.BASE_ENTITY_ID);
		invalidClient.setFirstName("invalid");
		invalidClient.setLastName("invalid");
		addObjectToRepository(asList(expectedClient, invalidClient), allClients);
		List<Client> expectedClientList = asList(expectedClient);

		List<Client> actualClientList = clientService.findByCriteria(FIRST_NAME, );
		assertTwoListAreSameIgnoringOrder(expectedClientList, actualClientList);
	}*/
	//TODO: Repository is returning time in UTC format.
	//TODO: TEST value of Date created field.
	@Test
	public void shouldAdd() {
		Client expectedClient = getClient();

		Client actualClient = clientService.addClient(expectedClient);

		List<Client> dbClients = allClients.getAll();
		assertEquals(1, dbClients.size());

		assertEquals(expectedClient, actualClient);
		assertNewObjectCreation(expectedClient, dbClients.get(0));
	}

	@Test(expected = RuntimeException.class)
	public void shouldThrowRuntimeExceptionWhileAddIfNoBaseEntityIdFound() {
		Client expectedClient = getClient();
		expectedClient.setBaseEntityId(null);

		clientService.addClient(expectedClient);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowIllegalArgumentExceptionIfAClientAlreadyExistWithSameIdentifier() {

		addObjectToRepository(Collections.singletonList(getClient()), allClients);
		Client expectedClient = allClients.getAll().get(0);
		expectedClient.setBaseEntityId(DIFFERENT_BASE_ENTITY_ID);

		clientService.addClient(expectedClient);
	}

	//TODO: Repository is returning time in UTC format.
	//TODO: TEST value of Date created field.
	@Test
	public void shouldAddWithCouchDbConnector() throws IOException {
		Client expectedClient = getClient();
		CouchDbConnector couchDbConnector = CouchDbAccessUtils.getCouchDbConnector("opensrp");

		Client actualClient = clientService.addClient(couchDbConnector, expectedClient);

		List<Client> dbClients = allClients.getAll();
		assertEquals(1, dbClients.size());
		assertNewObjectCreation(expectedClient, dbClients.get(0));
		assertEquals(expectedClient, actualClient);

	}

	@Test(expected = RuntimeException.class)
	public void shouldThrowRuntimeExceptionWhileAddIfNoBaseEntityIdFoundWithCouchDbConnector() throws IOException {
		Client expectedClient = getClient();
		expectedClient.setBaseEntityId(null);
		CouchDbConnector couchDbConnector = CouchDbAccessUtils.getCouchDbConnector("opensrp");

		clientService.addClient(couchDbConnector, expectedClient);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowIllegalArgumentExceptionIfAClientAlreadyExistWithSameIdentifierWithCouchDbConnector()
			throws IOException {
		Client expectedClient = getClient();
		addObjectToRepository(Collections.singletonList(expectedClient), allClients);
		expectedClient.setBaseEntityId(DIFFERENT_BASE_ENTITY_ID);
		CouchDbConnector couchDbConnector = CouchDbAccessUtils.getCouchDbConnector("opensrp");

		clientService.addClient(couchDbConnector, expectedClient);
	}

	@Test
	public void shouldFindFromClientObjectWithBaseIdentifier() {
		Client expectedClient = getClient();
		addObjectToRepository(Collections.singletonList(expectedClient), allClients);

		Client actualClient = clientService.findClient(expectedClient);

		assertEquals(expectedClient, actualClient);
	}

	@Test
	public void shouldFindFromClientWithIdentifiers() {
		Client expectedClient = getClient();
		expectedClient.setBaseEntityId(null);
		addObjectToRepository(Collections.singletonList(expectedClient), allClients);

		Client actualClient = clientService.findClient(expectedClient);

		assertEquals(expectedClient, actualClient);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionIfMultipleClientFoundWithSameIdentifier() {
		Client expectedClient = getClient();
		Client sameClient = getClient();
		sameClient.setBaseEntityId(DIFFERENT_BASE_ENTITY_ID);
		addObjectToRepository(asList(expectedClient, sameClient), allClients);
		expectedClient.setBaseEntityId(null);

		clientService.findClient(expectedClient);

	}

	@Test
	public void shouldReturnNullIfNoClientFound() {
		Client expectedClient = getClient();

		Client actualClient = clientService.findClient(expectedClient);

		assertNull(actualClient);
	}

	@Test
	public void shouldFindFromClientObjectWithBaseIdentifierWithCouchDbConnector() throws IOException {
		Client expectedClient = getClient();
		addObjectToRepository(Collections.singletonList(expectedClient), allClients);

		Client actualClient = clientService.findClient(getCouchDbConnector("opensrp"), expectedClient);

		assertEquals(expectedClient, actualClient);
	}

	@Test
	public void shouldFindFromClientWithIdentifiersWithCouchDbConnector() throws IOException {
		Client expectedClient = getClient();
		expectedClient.setBaseEntityId(null);
		addObjectToRepository(Collections.singletonList(expectedClient), allClients);

		Client actualClient = clientService.findClient(getCouchDbConnector("opensrp"), expectedClient);

		assertEquals(expectedClient, actualClient);
	}

	public void shouldReturnNullIfMultipleClientFoundWithSameIdentifierWithCouchDbConnector() throws IOException {
		Client expectedClient = getClient();
		Client sameClient = getClient();
		sameClient.setBaseEntityId(DIFFERENT_BASE_ENTITY_ID);
		addObjectToRepository(asList(expectedClient, sameClient), allClients);
		expectedClient.setBaseEntityId(null);

		Client client = clientService.findClient(getCouchDbConnector("opensrp"), expectedClient);

		assertNull(client);

	}

	@Test
	public void shouldReturnNullIfNoClientFoundWithCouchDbConnector() throws IOException {
		Client expectedClient = getClient();

		Client actualClient = clientService.findClient(getCouchDbConnector("opensrp"), expectedClient);

		assertNull(actualClient);
	}

	@Test
	public void shouldFindByUniqueIdBaseEntityId() {
		Client expectedClient = getClient();
		Client invalidClient = getClient();
		invalidClient.setBaseEntityId(DIFFERENT_BASE_ENTITY_ID);
		addObjectToRepository(asList(expectedClient, invalidClient), allClients);

		Client actualClient = clientService.find(BASE_ENTITY_ID);

		assertEquals(expectedClient, actualClient);

	}

	@Test
	public void shouldFindByUniqueIdIdentifier() {
		Client expectedClient = getClient();
		Client invalidClient = getClient();
		Map<String, String> differentIdentifiers = new HashMap<>(identifier);
		differentIdentifiers.put(IDENTIFIER_TYPE, "differentValue");
		invalidClient.setIdentifiers(differentIdentifiers);
		addObjectToRepository(asList(expectedClient, invalidClient), allClients);

		Client actualClient = clientService.find(IDENTIFIER_VALUE);

		assertEquals(expectedClient, actualClient);

	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionIfMultipleClientFoundWithSameIdentifierUsingUniqueId() {
		Client expectedClient = getClient();
		Client sameClient = getClient();
		sameClient.setBaseEntityId(DIFFERENT_BASE_ENTITY_ID);
		addObjectToRepository(asList(expectedClient, sameClient), allClients);

		clientService.find(IDENTIFIER_VALUE);

	}

	@Test
	public void shouldReturnNullIfNoClientFoundUsingUniqueId() {
		Client expectedClient = getClient();
		addObjectToRepository(Collections.singletonList(expectedClient), allClients);
		Client actualClient = clientService.find(DIFFERENT_BASE_ENTITY_ID);

		assertNull(actualClient);
	}

	//TODO: Repository is returning time in UTC format.
	//TODO: TEST value of Date edited field.
	@Test
	public void shouldUpdateClient() throws JSONException {
		Client client = getClient();
		addObjectToRepository(Collections.singletonList(client), allClients);
		Client updatedClient = allClients.getAll().get(0);
		updatedClient.setFirstName(LAST_NAME);

		clientService.updateClient(updatedClient);

		List<Client> actualClientList = allClients.getAll();
		assertEquals(1, actualClientList.size());
		assertObjectUpdate(updatedClient, actualClientList.get(0));
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionWhileUpdateIfNewClient() throws JSONException {
		Client client = getClient();

		clientService.updateClient(client);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionWhileUpdateIfClientIsNotFound() throws JSONException {
		addObjectToRepository(Collections.singletonList(getClient()), allClients);
		Client updatedClient = allClients.getAll().get(0);
		allClients.removeAll();

		clientService.updateClient(updatedClient);
	}

	@Test
	public void shouldFindByServerVersion() {
		addObjectToRepository(Collections.singletonList(getClient()), allClients);
		Client expectedClient = allClients.getAll().get(0);

		List<Client> actualClientList = clientService.findByServerVersion(expectedClient.getServerVersion() - 1);

		assertEquals(1, actualClientList.size());
		assertEquals(expectedClient, actualClientList.get(0));
	}

	@Test
	public void shouldAddIfNewEntityInAddOrUpdateMethod() {
		Client expectedClient = getClient();

		Client actualClient = clientService.addorUpdate(expectedClient);

		List<Client> dbClients = allClients.getAll();
		assertEquals(1, dbClients.size());

		assertEquals(expectedClient, actualClient);
		assertNewObjectCreation(expectedClient, dbClients.get(0));

	}

	@Test
	public void shouldUpdateIfExistingEntityInAddOrUpdateMethodRes() {
		addObjectToRepository(Collections.singletonList(getClient()), allClients);
		Client expectedClient = allClients.getAll().get(0);
		expectedClient.setFirstName(LAST_NAME);
		Long expectedServerVersion = expectedClient.getServerVersion();

		Client actualClient = clientService.addorUpdate(expectedClient);

		List<Client> dbClients = allClients.getAll();
		assertEquals(1, dbClients.size());
		assertEquals(expectedClient, actualClient);

		assertNotEquals(expectedServerVersion, dbClients.get(0).getServerVersion());
		assertObjectUpdate(expectedClient, dbClients.get(0));
	}

	@Test(expected = RuntimeException.class)
	public void shouldThrowErrorIfBaseEntityIdNotFound() {
		addObjectToRepository(Collections.singletonList(getClient()), allClients);
		Client expectedClient = allClients.getAll().get(0);
		expectedClient.setBaseEntityId(null);
		clientService.addorUpdate(expectedClient);
	}

	@Test
	public void shouldUpdateIfExistingEntityInAddOrUpdateMethodWithOutResettingServerVersion() {
		addObjectToRepository(Collections.singletonList(getClient()), allClients);
		Client expectedClient = allClients.getAll().get(0);
		expectedClient.setFirstName(LAST_NAME);
		Client actualClient = clientService.addorUpdate(expectedClient, false);

		List<Client> dbClients = allClients.getAll();
		assertEquals(1, dbClients.size());

		assertEquals(expectedClient, actualClient);
		assertObjectUpdate(expectedClient, dbClients.get(0));
	}

}
