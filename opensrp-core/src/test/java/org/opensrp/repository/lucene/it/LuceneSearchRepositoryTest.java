package org.opensrp.repository.lucene.it;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opensrp.BaseIntegrationTest;
import org.opensrp.domain.Client;
import org.opensrp.repository.AllClients;
import org.opensrp.repository.lucene.LuceneSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.opensrp.common.AllConstants.BaseEntity.BASE_ENTITY_ID;
import static org.opensrp.util.SampleFullDomainObject.*;
import static org.utils.AssertionUtil.assertTwoListAreSameIgnoringOrder;
import static org.utils.CouchDbAccessUtils.addObjectToRepository;

public class LuceneSearchRepositoryTest extends BaseIntegrationTest {

	@Autowired
	public AllClients allClients;

	@Autowired
	public LuceneSearchRepository luceneSearchRepository;

	@Before
	public void setUp() {
		allClients.removeAll();
	}

	@After
	public void cleanUp() {
		allClients.removeAll();
	}

	@Test
	public void shouldSearchByAllCriteria() {
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

		List<Client> actualClients = luceneSearchRepository
				.getByCriteria("first", "first", "middle", "last", expectedClient.getGender(), null, null, EPOCH_DATE_TIME,
						new DateTime(DateTimeZone.UTC), EPOCH_DATE_TIME, new DateTime(DateTimeZone.UTC), 10);

		assertTwoListAreSameIgnoringOrder(expectedClients, actualClients);
	}

	@Test
	public void canNotSearchWithIdentifiers() {
		Client expectedClient = createExpectedClient();
		List<Client> actualClients = luceneSearchRepository
				.getByCriteria("first", "first", "middle", "last", expectedClient.getGender(), identifier, null,
						EPOCH_DATE_TIME, new DateTime(DateTimeZone.UTC), EPOCH_DATE_TIME, new DateTime(DateTimeZone.UTC),
						10);
		assertEquals(0, actualClients.size());
	}

	@Test
	public void canNotSearchWithAttributes() {
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

		List<Client> actualClients = luceneSearchRepository
				.getByCriteria("first", "first", "middle", "last", expectedClient.getGender(), null, queryAttributes,
						EPOCH_DATE_TIME, new DateTime(DateTimeZone.UTC), EPOCH_DATE_TIME, new DateTime(DateTimeZone.UTC),
						10);
		assertEquals(0, actualClients.size());
	}

	@Test
	public void shouldSearchWithStringQuery() {
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

		String query = "(firstName:first* OR middleName:first* OR lastName:first* )AND firstName:first* AND middleName:middle* AND lastName:last* AND gender:female AND birthdate<date>:[1970-01-01T00:00:00 TO 2017-09-07T10:12:18] AND lastEdited<date>:[1970-01-01T00:00:00 TO 2017-09-07T10:12:18]";
		List<Client> actualClients = luceneSearchRepository.getByCriteria(query);

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
