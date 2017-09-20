package org.opensrp.web.rest.it;

import org.codehaus.jackson.JsonNode;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.opensrp.domain.Address;
import org.opensrp.domain.Client;
import org.opensrp.domain.Event;
import org.opensrp.repository.AllClients;
import org.opensrp.repository.AllEvents;
import org.opensrp.web.rest.EventResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.server.MvcResult;
import org.springframework.test.web.server.setup.MockMvcBuilders;
import org.springframework.web.util.NestedServletException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;
import static org.opensrp.common.AllConstants.BaseEntity.BASE_ENTITY_ID;
import static org.opensrp.common.AllConstants.Event.EVENT_TYPE;
import static org.opensrp.common.AllConstants.Event.PROVIDER_ID;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;

public class EventResourceTest extends BaseResourceTest {

	String baseEntityId = "1";

	String eventType = "eventType";

	DateTime eventDate = new DateTime(0l, DateTimeZone.UTC);

	String entityType = "entityType";

	String providerId = "providerId";

	String locationId = "locationId";

	String formSubmissionId = "formSubmissionId";

	String addressType = "addressType";

	String country = "country";

	String stateProvince = "stateProvince";

	String cityVillage = "cityVillage";

	String countryDistrict = "countryDistrict";

	String subDistrict = "subDistrict";

	String town = "town";

	private final static String BASE_URL = "/rest/event/";

	@Autowired
	private AllEvents allEvents;

	@Autowired
	private AllClients allClients;

	@Autowired
	private EventResource eventResource;

	@Before
	public void setUp() {
		allEvents.removeAll();
		allClients.removeAll();
	}

	@After
	public void tearDown() {
		allEvents.removeAll();
		allClients.removeAll();
	}

	@Test
	public void testRequiredProperties() {
		List<String> requiredProperties = eventResource.requiredProperties();
		assertTrue(requiredProperties.contains(PROVIDER_ID));
		assertTrue(requiredProperties.contains(EVENT_TYPE));
		assertTrue(requiredProperties.contains(BASE_ENTITY_ID));
	}

	@Test
	@Ignore //TODO: bug in `assetClassHasAllRequiredFields` method. should check base class for property.
	public void eventClassShouldHaveAllRequiredProperties() {
		assetClassHasAllRequiredFields(Event.class, eventResource.requiredProperties());
	}

	@Test
	public void shouldFindEventById() throws Exception {
		Event expectedEvent = new Event("1", "eventType", new DateTime(0l, DateTimeZone.UTC), "entityType", "providerId",
				"locationId", "formSubmissionId");
		expectedEvent.addIdentifier("key", "value");
		createEvent(Collections.singletonList(expectedEvent));

		JsonNode actualObj = getCallAsJsonNode(BASE_URL + "value", "", status().isOk());
		Event actualEvent = mapper.treeToValue(actualObj, Event.class);

		assertEquals(expectedEvent, actualEvent);

	}

	@Test
	public void shouldNotFindEvent() throws Exception {
		this.mockMvc = MockMvcBuilders.webApplicationContextSetup(this.wac).build();

		JsonNode actualObj = getCallAsJsonNode(BASE_URL + "value", "", status().isOk());

		assertNull(actualObj);
	}

	@Test
	public void shouldCreateEventWithAllRequiredProperties() throws Exception {
		Event expectedEvent = new Event("1", "eventType", new DateTime(0l, DateTimeZone.UTC), "entityType", "providerId",
				"locationId", "formSubmissionId");
		expectedEvent.addIdentifier("key", "value");

		postCallWithJsonContent(BASE_URL, mapper.writeValueAsString(expectedEvent), status().isOk());
		List<Event> allEventsInDB = allEvents.getAll();
		Event actualEvent = allEventsInDB.get(0);
		actualEvent.setDateCreated(null); //So We don't need to mock DateTimeUtil.now()

		assertEquals(1, allEventsInDB.size());
		assertEquals(expectedEvent, actualEvent);
	}

	@Test(expected = NestedServletException.class)
	public void shouldNotCreateEventWithOutProviderId() throws Exception {
		Event expectedEvent = new Event("1", "eventType", new DateTime(0l, DateTimeZone.UTC), "entityType", "providerId",
				"locationId", "formSubmissionId");
		expectedEvent.addIdentifier("key", "value");
		expectedEvent.setProviderId(null);

		postCallWithJsonContent(BASE_URL, mapper.writeValueAsString(expectedEvent), status().isOk());

		List<Event> allEventsInDb = allEvents.getAll();
		assertEquals(0, allEventsInDb.size());
	}

	@Test(expected = NestedServletException.class)
	public void shouldNotCreateEventWithOutEventType() throws Exception {
		Event expectedEvent = new Event("1", "eventType", new DateTime(0l, DateTimeZone.UTC), "entityType", "providerId",
				"locationId", "formSubmissionId");
		expectedEvent.addIdentifier("key", "value");
		expectedEvent.setEventType(null);

		postCallWithJsonContent(BASE_URL, mapper.writeValueAsString(expectedEvent), status().isOk());

		List<Event> allEventsInDb = allEvents.getAll();
		assertEquals(0, allEventsInDb.size());
	}

	@Test(expected = NestedServletException.class)
	public void shouldNotCreateEventWithOutBaseEntityId() throws Exception {
		Event expectedEvent = new Event("1", "eventType", new DateTime(0l, DateTimeZone.UTC), "entityType", "providerId",
				"locationId", "formSubmissionId");
		expectedEvent.addIdentifier("key", "value");
		expectedEvent.setBaseEntityId(null);

		postCallWithJsonContent(BASE_URL, mapper.writeValueAsString(expectedEvent), status().isOk());

		List<Event> allEventsInDb = allEvents.getAll();
		assertEquals(0, allEventsInDb.size());
	}

	@Test
	public void shouldUpdateExistingClient() throws Exception {
		Event expectedEvent = new Event("1", "eventType", new DateTime(0l, DateTimeZone.UTC), "entityType", "providerId",
				"locationId", "formSubmissionId");
		expectedEvent.addIdentifier("key", "value");
		createEvent(Collections.singletonList(expectedEvent));
		expectedEvent.addDetails("detail", "value");

		postCallWithJsonContent(BASE_URL + "value", mapper.writeValueAsString(expectedEvent), status().isOk());

		List<Event> allEventsInDb = allEvents.getAll();
		Event actualEvent = allEventsInDb.get(0);
		actualEvent.setDateEdited(null); //So We don't need to mock DateTimeUtil.now()
		assertEquals(1, allEventsInDb.size());
		assertEquals(expectedEvent, actualEvent);
	}

	@Test(expected = NestedServletException.class)
	public void shouldThrowExceptionWhileUpdateIfClientNotFound() throws Exception {
		Event expectedEvent = new Event("1", "eventType", new DateTime(0l, DateTimeZone.UTC), "entityType", "providerId",
				"locationId", "formSubmissionId");
		expectedEvent.addDetails("detail", "value");

		postCallWithJsonContent(BASE_URL + "1", mapper.writeValueAsString(expectedEvent), status().isOk());

		List<Event> allEventsInDb = allEvents.getAll();
		assertEquals(0, allEventsInDb.size());

	}

	@Test(expected = NestedServletException.class)
	public void shouldThrowExceptionWhileUpdateIfBaseEntityIdNotPresent() throws Exception {
		Event expectedEvent = new Event("1", "eventType", new DateTime(0l, DateTimeZone.UTC), "entityType", "providerId",
				"locationId", "formSubmissionId");
		expectedEvent.addIdentifier("key", "value");
		expectedEvent.setBaseEntityId(null);
		createEvent(Collections.singletonList(expectedEvent));
		Event updatedEvent = expectedEvent;
		updatedEvent.addDetails("key", "value");

		postCallWithJsonContent(BASE_URL + "value", mapper.writeValueAsString(expectedEvent), status().isOk());

		List<Event> allEventsInDb = allEvents.getAll();
		Event actualEvent = allEventsInDb.get(0);
		assertEquals(1, allEventsInDb.size());
		assertEquals(expectedEvent, actualEvent);
		assertNotSame(updatedEvent, actualEvent);
	}

	@Test(expected = NestedServletException.class)
	public void shouldThrowExceptionWhileUpdateIfEventTypeNotPresent() throws Exception {
		Event expectedEvent = new Event("1", "eventType", new DateTime(0l, DateTimeZone.UTC), "entityType", "providerId",
				"locationId", "formSubmissionId");
		expectedEvent.addIdentifier("key", "value");
		expectedEvent.setEventType(null);
		createEvent(Collections.singletonList(expectedEvent));
		Event updatedEvent = expectedEvent;
		updatedEvent.addDetails("key", "value");

		postCallWithJsonContent(BASE_URL + "value", mapper.writeValueAsString(expectedEvent), status().isOk());

		List<Event> allEventsInDb = allEvents.getAll();
		Event actualEvent = allEventsInDb.get(0);
		assertEquals(1, allEventsInDb.size());
		assertEquals(expectedEvent, actualEvent);
		assertNotSame(updatedEvent, actualEvent);
	}

	@Test(expected = NestedServletException.class)
	public void shouldThrowExceptionWhileUpdateIfProviderIdNotPresent() throws Exception {
		Event expectedEvent = new Event("1", "eventType", new DateTime(0l, DateTimeZone.UTC), "entityType", "providerId",
				"locationId", "formSubmissionId");
		expectedEvent.addIdentifier("key", "value");
		expectedEvent.setProviderId(null);
		createEvent(Collections.singletonList(expectedEvent));
		Event updatedEvent = expectedEvent;
		updatedEvent.addDetails("key", "value");

		postCallWithJsonContent(BASE_URL + "value", mapper.writeValueAsString(expectedEvent), status().isOk());

		List<Event> allEventsInDb = allEvents.getAll();
		Event actualEvent = allEventsInDb.get(0);
		assertEquals(1, allEventsInDb.size());
		assertEquals(expectedEvent, actualEvent);
		assertNotSame(updatedEvent, actualEvent);
	}

	@Test
	public void shouldSearchEventWithValidQuery() throws Exception {
		Event expectedEvent = new Event(baseEntityId, eventType, eventDate, entityType, providerId, locationId,
				formSubmissionId);
		expectedEvent.addIdentifier("key", "value");
		DateTime dateEdited = new DateTime(3l, DateTimeZone.UTC);
		expectedEvent.setDateCreated(dateEdited);

		Event otherEvent = new Event("2", eventType, eventDate, entityType, providerId, locationId, formSubmissionId);
		otherEvent.setDateCreated(dateEdited);
		Event otherEvent2 = new Event("3", eventType, eventDate, entityType, providerId, locationId, formSubmissionId);
		otherEvent2.setDateCreated(dateEdited);
		createEvent(asList(expectedEvent, otherEvent, otherEvent2));
		createClient();

		String searchQuery = "search?identifier=1&" + "eventType=" + eventType + "&locationId=" + locationId + "&providerId="
				+ providerId;
		JsonNode actualObj = getCallAsJsonNode(BASE_URL + searchQuery, "", status().isOk());

		Event actualEvent = mapper.treeToValue(actualObj.get(0), Event.class);
		assertEquals(1, actualObj.size());
		assertEquals(expectedEvent, actualEvent);
	}

	@Test
	public void shouldFailToSearchIfEventDoesntHaveAssociatedClient() throws Exception {
		Event expectedEvent = new Event(baseEntityId, eventType, eventDate, entityType, providerId, locationId,
				formSubmissionId);
		expectedEvent.addIdentifier("key", "value");
		DateTime dateEdited = new DateTime(3l, DateTimeZone.UTC);
		expectedEvent.setDateEdited(dateEdited);
		Event otherEvent = new Event("2", eventType, eventDate, entityType, providerId, locationId, formSubmissionId);
		Event otherEvent2 = new Event("3", eventType, eventDate, entityType, providerId, locationId, formSubmissionId);
		createEvent(asList(expectedEvent, otherEvent, otherEvent2));

		String searchQuery =
				"search?identifier= invalid" + "eventType=" + eventType + "&locationId=" + locationId + "&providerId="
						+ providerId;
		JsonNode actualObj = getCallAsJsonNode(BASE_URL + searchQuery, "", status().isOk());
		assertTrue(actualObj.isArray());
		assertEquals(0, actualObj.size());
	}

	@Test
	public void shouldFailToSearchIfEventDoesntHaveDateCreatedField() throws Exception {
		Event expectedEvent = new Event(baseEntityId, eventType, eventDate, entityType, providerId, locationId,
				formSubmissionId);
		expectedEvent.addIdentifier("key", "value");
		DateTime dateEdited = new DateTime(3l, DateTimeZone.UTC);

		Event otherEvent = new Event("2", eventType, eventDate, entityType, providerId, locationId, formSubmissionId);
		otherEvent.setDateCreated(dateEdited);
		Event otherEvent2 = new Event("3", eventType, eventDate, entityType, providerId, locationId, formSubmissionId);
		otherEvent2.setDateCreated(dateEdited);
		createEvent(asList(expectedEvent, otherEvent, otherEvent2));
		createClient();

		String searchQuery = "search?identifier=1&" + "eventType=" + eventType + "&locationId=" + locationId + "&providerId="
				+ providerId;

		JsonNode actualObj = getCallAsJsonNode(BASE_URL + searchQuery, "", status().isOk());
		assertTrue(actualObj.isArray());
		assertEquals(0, actualObj.size());
	}

	@Test
	public void shouldFilterData() throws Exception {
		Event expectedEvent = new Event(baseEntityId, eventType, eventDate, entityType, providerId, locationId,
				formSubmissionId);
		expectedEvent.addIdentifier("key", "value");
		DateTime dateEdited = new DateTime(3l, DateTimeZone.UTC);
		expectedEvent.setDateCreated(dateEdited);

		Event otherEvent = new Event("2", eventType, eventDate, entityType, providerId, locationId, formSubmissionId);
		otherEvent.setDateCreated(dateEdited);
		Event otherEvent2 = new Event("3", eventType, eventDate, entityType, providerId, locationId, formSubmissionId);
		otherEvent2.setDateCreated(dateEdited);
		List<Event> expectedEvents = asList(expectedEvent, otherEvent, otherEvent2);
		createEvent(expectedEvents);

		String searchQuery =
				"?q=" + "eventType:" + eventType + " and locationId:" + locationId + " and providerId:" + providerId;
		JsonNode actualObj = getCallAsJsonNode(BASE_URL + searchQuery, "", status().isOk());
		List<Event> actualEventList = new ArrayList<>();
		for (int i = 0; i < actualObj.size(); i++) {
			Event actualEvent = mapper.treeToValue(actualObj.get(i), Event.class);
			actualEventList.add(actualEvent);
		}

		assertTwoListAreSameIgnoringOrder(expectedEvents, actualEventList);
	}

	@Test(expected = NestedServletException.class)
	public void shouldFailToFilterDataWithoutDateCreatedField() throws Exception {
		Event expectedEvent = new Event(baseEntityId, eventType, eventDate, entityType, providerId, locationId,
				formSubmissionId);
		expectedEvent.addIdentifier("key", "value");
		Event otherEvent = new Event("2", "e", eventDate, entityType, providerId, locationId, formSubmissionId);
		otherEvent.setDateCreated(eventDate);
		Event otherEvent2 = new Event("3", eventType, eventDate, entityType, providerId, locationId, formSubmissionId);
		createEvent(asList(expectedEvent, otherEvent, otherEvent2));

		String searchQuery =
				"?q=" + "eventType:" + eventType + " and locationId:" + locationId + " and providerId:" + providerId;

		getCallAsJsonNode(BASE_URL + searchQuery, "", status().isOk());
	}

	@Test
	public void shouldReturnAllEvents() throws Exception {
		Event expectedEvent = new Event(baseEntityId, eventType, eventDate, entityType, providerId, locationId,
				formSubmissionId);
		expectedEvent.addIdentifier("key", "value");
		DateTime dateEdited = new DateTime(3l, DateTimeZone.UTC);
		expectedEvent.setDateEdited(dateEdited);
		Event otherEvent = new Event("2", eventType, eventDate, entityType, providerId, locationId, formSubmissionId);
		Event otherEvent2 = new Event("3", eventType, eventDate, entityType, providerId, locationId, formSubmissionId);
		List<Event> expectedEvents = asList(expectedEvent, otherEvent, otherEvent2);
		createEvent(expectedEvents);

		JsonNode actualObj = getCallAsJsonNode(BASE_URL + "getall", "", status().isOk());

		List<Event> actualEventList = new ArrayList<>();
		for (int i = 0; i < actualObj.size(); i++) {
			Event actualEvent = mapper.treeToValue(actualObj.get(i), Event.class);
			actualEvent.setDateCreated(null);
			actualEventList.add(actualEvent);
		}

		assertTwoListAreSameIgnoringOrder(expectedEvents, actualEventList);
	}

	@Test
	public void shouldSyncEventAndRelatedClient() throws Exception {

		List<Client> expectedClient = createClient();
		List<Event> expectedEvent = createEventsForSyncTest();

		String syncQuery = "sync?serverVersion=0&providerId=providerId";
		JsonNode actualObj = getCallAsJsonNode(BASE_URL + syncQuery, "", status().isOk());

		JsonNode eventObj = actualObj.get("events");
		JsonNode clientObj = actualObj.get("clients");
		int eventSize = actualObj.get("no_of_events").asInt();
		List<Event> actualEventList = new ArrayList<>();
		for (int i = 0; i < eventObj.size(); i++) {
			Event actualEvent = mapper.treeToValue(eventObj.get(i), Event.class);
			actualEventList.add(actualEvent);
		}

		List<Client> actualClientList = new ArrayList<>();
		for (int i = 0; i < clientObj.size(); i++) {
			Client actualClient = mapper.treeToValue(clientObj.get(i), Client.class);
			actualClientList.add(actualClient);
		}

		assertEquals(4, eventSize);
		assertTwoListAreSameIgnoringOrder(expectedClient, actualClientList);
		assertTwoListAreSameIgnoringOrder(expectedEvent, actualEventList);
	}

	@Test
	public void shouldAddClientAndEventFromSyncData() throws Exception {
		String synData =
				"{\"events\":[{\"identifiers\":{\"key\":\"value\"},\"baseEntityId\":\"1\",\"locationId\":\"locationId\",\"eventDate\":\"1970-01-01T00:00:00.000Z\",\"eventType\":\"eventType\",\"formSubmissionId\":\"formSubmissionId\",\"providerId\":\"providerId\",\"duration\":0,\"obs\":[],\"entityType\":\"entityType\",\"version\":1502179200127,\"dateCreated\":\"1970-01-01T00:00:00.003Z\",\"type\":\"Event\",\"id\":\"14bda0b96952ad4347732585037e1d58\"},"
						+ "{\"identifiers\":{},\"baseEntityId\":\"2\",\"locationId\":\"locationId\",\"eventDate\":\"1970-01-01T00:00:00.000Z\",\"eventType\":\"eventType\",\"formSubmissionId\":\"formSubmissionId\",\"providerId\":\"providerId\",\"duration\":0,\"obs\":[],\"entityType\":\"entityType\",\"version\":1502179200127,\"dateCreated\":\"1970-01-01T00:00:00.003Z\",\"type\":\"Event\",\"id\":\"14bda0b96952ad4347732585037e1ee8\"},"
						+ "{\"identifiers\":{},\"baseEntityId\":\"3\",\"locationId\":\"locationId\",\"eventDate\":\"1970-01-01T00:00:00.000Z\",\"eventType\":\"eventType\",\"formSubmissionId\":\"formSubmissionId\",\"providerId\":\"providerId\",\"duration\":0,\"obs\":[],\"entityType\":\"entityType\",\"version\":1502179200127,\"dateCreated\":\"1970-01-01T00:00:00.003Z\",\"type\":\"Event\",\"id\":\"14bda0b96952ad4347732585037e22d3\"},"
						+ "{\"identifiers\":{},\"baseEntityId\":\"1\",\"locationId\":\"locationId\",\"eventDate\":\"1970-01-01T00:00:00.000Z\",\"eventType\":\"eventType\",\"formSubmissionId\":\"formSubmissionId\",\"providerId\":\"providerId\",\"duration\":0,\"obs\":[],\"entityType\":\"entityType\",\"version\":1502179200127,\"dateCreated\":\"1970-01-01T00:00:00.003Z\",\"type\":\"Event\",\"id\":\"14bda0b96952ad4347732585037e233e\"}],"
						+ "\"no_of_events\":4,"
						+ "\"clients\":[{\"firstName\":\"name\",\"birthdate\":\"1970-01-01T00:00:00.000Z\",\"birthdateApprox\":false,\"gender\":\"male\",\"baseEntityId\":\"1\",\"identifiers\":{},\"addresses\":[{\"addressType\":\"addressType\",\"town\":\"town\",\"subDistrict\":\"subDistrict\",\"countyDistrict\":\"countryDistrict\",\"cityVillage\":\"cityVillage\",\"stateProvince\":\"stateProvince\",\"country\":\"country\"}],\"attributes\":{},\"dateCreated\":\"1970-01-01T00:00:00.000Z\",\"type\":\"Client\",\"id\":\"14bda0b96952ad4347732585037dff66\"},"
						+ "{\"firstName\":\"name\",\"birthdate\":\"1970-01-01T00:00:00.000Z\",\"birthdateApprox\":false,\"gender\":\"male\",\"baseEntityId\":\"2\",\"identifiers\":{},\"addresses\":[{\"addressType\":\"addressType\",\"town\":\"town\",\"subDistrict\":\"subDistrict\",\"countyDistrict\":\"countryDistrict\",\"cityVillage\":\"cityVillage\",\"stateProvince\":\"stateProvince\",\"country\":\"country\"}],\"attributes\":{},\"dateCreated\":\"1970-01-01T00:00:00.000Z\",\"type\":\"Client\",\"id\":\"14bda0b96952ad4347732585037e0c85\"},"
						+ "{\"firstName\":\"name\",\"birthdate\":\"1970-01-01T00:00:00.000Z\",\"birthdateApprox\":false,\"gender\":\"male\",\"baseEntityId\":\"3\",\"identifiers\":{},\"addresses\":[{\"addressType\":\"addressType\",\"town\":\"town\",\"subDistrict\":\"subDistrict\",\"countyDistrict\":\"countryDistrict\",\"cityVillage\":\"cityVillage\",\"stateProvince\":\"stateProvince\",\"country\":\"country\"}],\"attributes\":{},\"dateCreated\":\"1970-01-01T00:00:00.000Z\",\"type\":\"Client\",\"id\":\"14bda0b96952ad4347732585037e1519\"}]}";
		assertEquals(0, allEvents.getAll().size());
		assertEquals(0, allClients.getAll().size());

		postCallWithJsonContent(BASE_URL + "add", synData, status().isCreated());

		assertEquals(4, allEvents.getAll().size());
		assertEquals(3, allClients.getAll().size());
	}

	@Test
	public void shouldThrowErrorIfSyncDataDoesntHaveClientAndEvent() throws Exception {
		String synData = "{\"no_of_events\":0}";
		assertEquals(0, allEvents.getAll().size());
		assertEquals(0, allClients.getAll().size());

		postCallWithJsonContent(BASE_URL + "add", synData, status().isBadRequest());

		assertEquals(0, allEvents.getAll().size());
		assertEquals(0, allClients.getAll().size());
	}

	private List<Event> createEventsForSyncTest() {
		Event expectedEvent = new Event(baseEntityId, eventType, eventDate, entityType, providerId, locationId,
				formSubmissionId);
		expectedEvent.addIdentifier("key", "value");
		DateTime dateEdited = new DateTime(3l, DateTimeZone.UTC);
		expectedEvent.setDateCreated(dateEdited);

		Event oE = new Event("1", eventType, eventDate, entityType, providerId, locationId, formSubmissionId);
		oE.setDateCreated(dateEdited);
		Event otherEvent = new Event("2", eventType, eventDate, entityType, providerId, locationId, formSubmissionId);
		otherEvent.setDateCreated(dateEdited);
		Event otherEvent2 = new Event("3", eventType, eventDate, entityType, providerId, locationId, formSubmissionId);
		otherEvent2.setDateCreated(dateEdited);

		createEvent(asList(expectedEvent, otherEvent, otherEvent2, oE));
		return asList(expectedEvent, otherEvent, otherEvent2, oE);
	}

	private List<Client> createClient() {
		Address address = new Address().withAddressType(addressType).withCountry(country).withStateProvince(stateProvince)
				.withCityVillage(cityVillage).withCountyDistrict(countryDistrict).withSubDistrict(subDistrict)
				.withTown(town);

		Client expectedClient = (Client) new Client("1").withFirstName("name").withGender("male")
				.withBirthdate(new DateTime(0l, DateTimeZone.UTC), false).withAddress(address);
		expectedClient.setDateCreated(new DateTime(0l, DateTimeZone.UTC));

		Client otherClient = (Client) new Client("2").withFirstName("name").withGender("male")
				.withBirthdate(new DateTime(0l, DateTimeZone.UTC), false).withAddress(address);
		otherClient.setDateCreated(new DateTime(0l, DateTimeZone.UTC));
		Client otherClient2 = (Client) new Client("3").withFirstName("name").withGender("male")
				.withBirthdate(new DateTime(0l, DateTimeZone.UTC), false).withAddress(address);
		otherClient2.setDateCreated(new DateTime(0l, DateTimeZone.UTC));

		createClient(asList(expectedClient, otherClient, otherClient2));
		return asList(expectedClient, otherClient, otherClient2);

	}

	private void createEvent(List<Event> events) {
		addObjectToRepository(events, allEvents);
	}

	private void createClient(List<Client> allClient) {
		addObjectToRepository(allClient, allClients);
	}

}
