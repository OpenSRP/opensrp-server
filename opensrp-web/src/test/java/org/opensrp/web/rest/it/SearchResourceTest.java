package org.opensrp.web.rest.it;

import org.codehaus.jackson.JsonNode;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opensrp.domain.Address;
import org.opensrp.domain.Client;
import org.opensrp.repository.AllClients;
import org.opensrp.repository.AllEvents;
import org.opensrp.web.rest.SearchResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.server.MvcResult;
import org.springframework.test.web.server.setup.MockMvcBuilders;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.opensrp.web.rest.it.ResourceTestUtility.createClients;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;

public class SearchResourceTest extends BaseResourceTest {

	private final static String BASE_URL = "/rest/search/";

	public static final DateTime DATE_CREATED = new DateTime(0l, DateTimeZone.UTC);

	public static final String MIDDLE_NAME = "middlename";

	public static final String LAST_NAME = "lastName";

	public static final String IDENTIFIER_TYPE = "type";

	public static final String IDENTIFIER = "value";

	public static final String ATTRIBUTES_NAME = "name";

	public static final String ATTRIBUTES_VALUE = "value";

	public static final String FEMALE = "female";

	@Autowired
	private SearchResource searchResource;

	@Autowired
	private AllClients allClients;

	@Autowired
	private AllEvents allEvents;

	String addressType = "addressType";

	String country = "country";

	String stateProvince = "stateProvince";

	String cityVillage = "cityVillage";

	String countryDistrict = "countryDistrict";

	String subDistrict = "subDistrict";

	String town = "town";

	String firstName = "name";

	String male = "male";

	DateTime birthDate = new DateTime(0l, DateTimeZone.UTC);

	DateTime deathDate = new DateTime(1l, DateTimeZone.UTC);

	Address address = new Address().withAddressType(addressType).withCountry(country).withStateProvince(stateProvince)
			.withCityVillage(cityVillage).withCountyDistrict(countryDistrict).withSubDistrict(subDistrict).withTown(town);

	@Before
	public void setUp() {
		allClients.removeAll();
		allEvents.removeAll();
	}

	@After
	public void cleanUp() {
		allEvents.removeAll();
		allClients.removeAll();
	}

	@Test
	public void shouldSearchClientWithFirstName() throws Exception {
		Client expectedClient = createOneSearchableClient();

		String searchQuery = "firstName=" + firstName;
		JsonNode actualObj = searchClient(searchQuery);
		Client actualClient = mapper.treeToValue(actualObj.get(0), Client.class);

		assertEquals(expectedClient, actualClient);

	}

	@Test
	public void shouldSearchClientWithMiddleName() throws Exception {
		Client expectedClient = createOneSearchableClient();

		String searchQuery = "middleName=" + MIDDLE_NAME;
		JsonNode actualObj = searchClient(searchQuery);
		Client actualClient = mapper.treeToValue(actualObj.get(0), Client.class);

		assertEquals(expectedClient, actualClient);
	}

	@Test
	public void canNotSearchIfAnyNamePortionHasCamelCaseLetter() throws Exception {
		Client expectedClient = createOneSearchableClient();

		String searchQuery = "lastName=" + LAST_NAME;
		JsonNode actualObj = searchClient(searchQuery);

		assertNull(actualObj.get(0));
	}

	@Test
	public void shouldSearchClientWithGender() throws Exception {
		Client expectedClient = createOneSearchableClient();

		String searchQuery = "gender=" + male;
		JsonNode actualObj = searchClient(searchQuery);
		Client actualClient = mapper.treeToValue(actualObj.get(0), Client.class);

		assertEquals(expectedClient, actualClient);
	}

	@Test
	public void shouldSearchClientWithBirthDate() throws Exception {
		Client expectedClient = createOneSearchableClient();

		String searchQuery = "birthdate=" + birthDate.toLocalDate().toString() + ":" + birthDate.toLocalDate().toString();
		JsonNode actualObj = searchClient(searchQuery);
		Client actualClient = mapper.treeToValue(actualObj.get(0), Client.class);

		assertEquals(expectedClient, actualClient);
	}

	@Test
	public void shouldSearchClientWithLastEdited() throws Exception {
		Client expectedClient = createOneSearchableClient();

		String searchQuery =
				"lastEdited=" + DATE_CREATED.toLocalDate().toString() + ":" + DATE_CREATED.toLocalDate().toString();
		JsonNode actualObj = searchClient(searchQuery);
		Client actualClient = mapper.treeToValue(actualObj.get(0), Client.class);

		assertEquals(expectedClient, actualClient);
	}

	@Test
	public void shouldSearchClientWithAttribute() throws Exception {
		Client expectedClient = createOneSearchableClient();

		String searchQuery = "attribute=" + ATTRIBUTES_NAME + ":" + ATTRIBUTES_VALUE;
		JsonNode actualObj = searchClient(searchQuery);
		Client actualClient = mapper.treeToValue(actualObj.get(0), Client.class);

		assertEquals(expectedClient, actualClient);
	}

	private JsonNode searchClient(String query) throws Exception {
		String searchQuery = "search?" + query;

		return getCallAsJsonNode(BASE_URL + searchQuery, "", status().isOk());
	}

	private Client createOneSearchableClient() {
		Client expectedClient = (Client) new Client("1").withFirstName(firstName).withMiddleName(MIDDLE_NAME)
				.withLastName(LAST_NAME).withGender(male).withBirthdate(birthDate, false).withDeathdate(deathDate, true)
				.withAddress(address);
		expectedClient.setDateCreated(DATE_CREATED);
		expectedClient.withIdentifier(IDENTIFIER_TYPE, IDENTIFIER);
		expectedClient.withAttribute(ATTRIBUTES_NAME, ATTRIBUTES_VALUE);

		Client otherClient = (Client) new Client("2").withFirstName("ff").withMiddleName("fd").withLastName("sfdf")
				.withGender(FEMALE).withBirthdate(birthDate, false).withDeathdate(deathDate, true).withAddress(address);
		otherClient.setDateCreated(DATE_CREATED);
		otherClient.withIdentifier("fsdf", "sfdf");
		otherClient.withAttribute("sfdf", "sfdf");
		Client otherClient2 = (Client) new Client("3").withFirstName("dd").withMiddleName("fdf").withLastName("sfd")
				.withGender(FEMALE).withBirthdate(birthDate, false).withDeathdate(deathDate, true).withAddress(address);
		;
		otherClient2.setDateCreated(DATE_CREATED);
		otherClient2.withIdentifier("hg", "ghgh");
		otherClient2.withAttribute("hg", "hgh");

		addObjectToRepository(asList(expectedClient, otherClient, otherClient2), allClients);

		return expectedClient;
	}

}
