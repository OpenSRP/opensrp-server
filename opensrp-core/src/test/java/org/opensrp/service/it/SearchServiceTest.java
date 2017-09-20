package org.opensrp.service.it;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opensrp.BaseIntegrationTest;
import org.opensrp.domain.Client;
import org.opensrp.repository.AllClients;
import org.opensrp.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.opensrp.common.AllConstants.BaseEntity.BASE_ENTITY_ID;
import static org.opensrp.util.SampleFullDomainObject.*;
import static org.opensrp.util.SampleFullDomainObject.EPOCH_DATE_TIME;
import static org.utils.AssertionUtil.assertTwoListAreSameIgnoringOrder;
import static org.utils.CouchDbAccessUtils.addObjectToRepository;

public class SearchServiceTest extends BaseIntegrationTest {

	@Autowired
	public AllClients allClients;

	@Autowired
	public SearchService searchService;

	@Before
	public void setUp() {
		allClients.removeAll();
	}

	@After
	public void cleanUp() {
		allClients.removeAll();
	}

	@Test
	public void shouldSearchClient() {
		Client expectedClient = createExpectedClient();
		expectedClient.setAttributes(attributes);
		Client expectedClient2 = createExpectedClient();
		expectedClient2.addAttribute("inactive", true);
		Client expectedClient3 = createExpectedClient();
		expectedClient3.addAttribute("lost_to_follow_up", true);
		Client expectedClient4 = createExpectedClient();
		expectedClient4.addAttribute("inactive", false);
		List<Client> expectedClients = asList(expectedClient, expectedClient2, expectedClient3, expectedClient4);
		addObjectToRepository(expectedClients, allClients);

		Map<String, String> queryAttributes = new HashMap<>();
		queryAttributes.put(ATTRIBUTES_TYPE, ATTRIBUTES_VALUE);
		queryAttributes.put("inactive", "true");
		queryAttributes.put("inactive", "false");
		queryAttributes.put("lost_to_follow_up", "true");

		List<Client> actualClients = searchService
				.searchClient("first", "first", "middle", "last", expectedClient.getGender(), null, null, EPOCH_DATE_TIME,
						new DateTime(DateTimeZone.UTC), EPOCH_DATE_TIME, new DateTime(DateTimeZone.UTC), 10);

		assertTwoListAreSameIgnoringOrder(expectedClients, actualClients);
	}

	private Client createExpectedClient() {
		Client expectedClient = new Client(BASE_ENTITY_ID);
		expectedClient.setFirstName(FIRST_NAME);
		expectedClient.setMiddleName(MIDDLE_NAME);
		expectedClient.setLastName(LAST_NAME);
		expectedClient.setBirthdate(EPOCH_DATE_TIME);
		expectedClient.setAddresses(asList(getAddress()));
		expectedClient.setDateCreated(EPOCH_DATE_TIME);
		expectedClient.setGender(FEMALE);
		expectedClient.setDateEdited(EPOCH_DATE_TIME);
		expectedClient.setIdentifiers(identifier);
		return expectedClient;
	}

}
