package org.opensrp.repository.lucene.it;

import org.ektorp.DbAccessException;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opensrp.BaseIntegrationTest;
import org.opensrp.common.AllConstants;
import org.opensrp.domain.Address;
import org.opensrp.domain.Client;
import org.opensrp.repository.AllClients;
import org.opensrp.repository.lucene.LuceneClientRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.opensrp.common.AllConstants.BaseEntity.BASE_ENTITY_ID;
import static org.opensrp.common.AllConstants.BaseEntity.MOTHERS_INDENTIFIER;
import static org.opensrp.util.SampleFullDomainObject.*;
import static org.utils.AssertionUtil.assertTwoListAreSameIgnoringOrder;
import static org.utils.CouchDbAccessUtils.addObjectToRepository;

//TODO: test birthDate range
public class LuceneClientRepositoryTest extends BaseIntegrationTest {

	@Autowired
	public AllClients allClients;

	@Autowired
	LuceneClientRepository luceneClientRepository;

	@Before
	public void setUp() {
		allClients.removeAll();
	}

	@After
	public void cleanUp() {
		//allClients.removeAll();
	}

	@Test
	public void shouldFindBasedOnFirstNameCriteria() {
		Client expectedClient = new Client(BASE_ENTITY_ID);
		expectedClient.setFirstName(FIRST_NAME);
		expectedClient.setBirthdate(EPOCH_DATE_TIME);
		expectedClient.setAddresses(asList(getAddress()));
		expectedClient.setDateCreated(EPOCH_DATE_TIME);

		addObjectToRepository(Collections.singletonList(expectedClient), allClients);

		List<Client> actualClients = luceneClientRepository
				.getByCriteria(expectedClient.getFirstName(), null, null, null, null, null, null, null, null, null, null,
						null, null, null, null, null, null, null, null);

		assertEquals(expectedClient, actualClients.get(0));
	}

	@Test(expected = RuntimeException.class)
	public void shouldThrowExceptionIfNoCriteriaIsSelected() {
		Client expectedClient = new Client(BASE_ENTITY_ID);
		expectedClient.setFirstName(FIRST_NAME);
		expectedClient.setBirthdate(EPOCH_DATE_TIME);
		expectedClient.setAddresses(asList(getAddress()));
		expectedClient.setDateCreated(EPOCH_DATE_TIME);

		addObjectToRepository(Collections.singletonList(expectedClient), allClients);

		List<Client> actualClients = luceneClientRepository
				.getByCriteria(null, null, null, null, null, null, null, null, null, null, null,
						null, null, null, null, null, null, null, null);

		assertEquals(expectedClient, actualClients.get(0));
	}

	@Test(expected = NullPointerException.class)
	public void shouldThrowExceptionWithOutClientBirthDateField() {
		Client expectedClient = new Client(BASE_ENTITY_ID);
		expectedClient.setFirstName(FIRST_NAME);
		expectedClient.setAddresses(asList(getAddress()));
		expectedClient.setDateCreated(EPOCH_DATE_TIME);
		addObjectToRepository(Collections.singletonList(expectedClient), allClients);

		luceneClientRepository
				.getByCriteria(expectedClient.getFirstName(), null, null, null, null, null, null, null, null, null, null,
						null, null, null, null, null, null, null, null);

	}

	@Test(expected = NullPointerException.class)
	public void shouldThrowExceptionWithClientNullBirthDateField() {
		Client expectedClient = new Client(BASE_ENTITY_ID);
		expectedClient.setFirstName(FIRST_NAME);
		expectedClient.setAddresses(asList(getAddress()));
		expectedClient.setDateCreated(EPOCH_DATE_TIME);
		expectedClient.setDateCreated(null);
		addObjectToRepository(Collections.singletonList(expectedClient), allClients);

		luceneClientRepository
				.getByCriteria(expectedClient.getFirstName(), null, null, null, null, null, null, null, null, null, null,
						null, null, null, null, null, null, null, null);

	}

	@Test(expected = NullPointerException.class)
	public void shouldThrowExceptionWithOutClientDateCreatedField() {
		Client expectedClient = new Client(BASE_ENTITY_ID);
		expectedClient.setFirstName(FIRST_NAME);
		expectedClient.setAddresses(asList(getAddress()));
		expectedClient.setBirthdate(EPOCH_DATE_TIME);
		addObjectToRepository(Collections.singletonList(expectedClient), allClients);
		luceneClientRepository
				.getByCriteria(expectedClient.getFirstName(), null, null, null, null, null, null, null, null, null, null,
						null, null, null, null, null, null, null, null);

	}

	@Test(expected = NullPointerException.class)
	public void shouldThrowExceptionWithClientNullDateCreatedField() {
		Client expectedClient = new Client(BASE_ENTITY_ID);
		expectedClient.setFirstName(FIRST_NAME);
		expectedClient.setAddresses(asList(getAddress()));
		expectedClient.setBirthdate(EPOCH_DATE_TIME);
		expectedClient.setDateCreated(null);
		addObjectToRepository(Collections.singletonList(expectedClient), allClients);

		luceneClientRepository
				.getByCriteria(expectedClient.getFirstName(), null, null, null, null, null, null, null, null, null, null,
						null, null, null, null, null, null, null, null);

	}

	@Test
	public void shouldNotFindWithOutClientAddressField() {
		Client expectedClient = new Client(BASE_ENTITY_ID);
		expectedClient.setFirstName(FIRST_NAME);

		addObjectToRepository(Collections.singletonList(expectedClient), allClients);

		List<Client> actualClients = luceneClientRepository
				.getByCriteria(expectedClient.getFirstName(), null, null, null, null, null, null, null, null, null, null,
						null, null, null, null, null, null, null, null);
		assertTrue(actualClients.isEmpty());
	}

	@Test
	public void shouldNotFindWithClientNullAddressField() {
		Client expectedClient = new Client(BASE_ENTITY_ID);
		expectedClient.setFirstName(FIRST_NAME);
		expectedClient.setAddresses(null);

		addObjectToRepository(Collections.singletonList(expectedClient), allClients);

		List<Client> actualClients = luceneClientRepository
				.getByCriteria(expectedClient.getFirstName(), null, null, null, null, null, null, null, null, null, null,
						null, null, null, null, null, null, null, null);
		assertTrue(actualClients.isEmpty());
	}

	@Test
	public void shouldNotFindWithClientEmptyAddressField() {
		Client expectedClient = new Client(BASE_ENTITY_ID);
		expectedClient.setFirstName(FIRST_NAME);
		expectedClient.setAddresses(Collections.EMPTY_LIST);

		addObjectToRepository(Collections.singletonList(expectedClient), allClients);

		List<Client> actualClients = luceneClientRepository
				.getByCriteria(expectedClient.getFirstName(), null, null, null, null, null, null, null, null, null, null,
						null, null, null, null, null, null, null, null);
		assertTrue(actualClients.isEmpty());
	}

	//TODO:Fix source
	@Test
	public void canNotSearchWithDeathDate() {
		Client expectedClient = new Client(BASE_ENTITY_ID);
		expectedClient.setFirstName(FIRST_NAME);
		expectedClient.setBirthdate(EPOCH_DATE_TIME);
		expectedClient.setAddresses(asList(getAddress()));
		expectedClient.setDateCreated(EPOCH_DATE_TIME);
		expectedClient.setDeathdate(new DateTime(DateTimeZone.UTC));
		addObjectToRepository(Collections.singletonList(expectedClient), allClients);

		List<Client> actualClients = luceneClientRepository
				.getByCriteria(expectedClient.getFirstName(), null, null, null, EPOCH_DATE_TIME,
						new DateTime(DateTimeZone.UTC), null, null, null, null, null, null, null, null, null, null, null,
						null, null);

		assertEquals(0, actualClients.size());
	}

	@Test
	public void shouldFindBasedOnAll() {
		addRandomInvalidClient();
		Client expectedClient = new Client(BASE_ENTITY_ID);
		expectedClient.setFirstName(FIRST_NAME);
		expectedClient.setBirthdate(EPOCH_DATE_TIME);
		expectedClient.setAddresses(asList(getAddress()));
		expectedClient.setDateCreated(EPOCH_DATE_TIME);
		expectedClient.setGender(FEMALE);
		expectedClient.setDateEdited(EPOCH_DATE_TIME);
		expectedClient.setAttributes(attributes);
		addObjectToRepository(Collections.singletonList(expectedClient), allClients);

		List<Client> actualClients = luceneClientRepository
				.getByCriteria(expectedClient.getFirstName(), expectedClient.getGender(), EPOCH_DATE_TIME, new DateTime(),
						null, null, ATTRIBUTES_TYPE, ATTRIBUTES_VALUE, getAddress().getAddressType(),
						getAddress().getCountry(), getAddress().getStateProvince(), getAddress().getCityVillage(),
						getAddress().getCountyDistrict(), getAddress().getSubDistrict(), getAddress().getTown(),
						getAddress().getSubTown(), EPOCH_DATE_TIME, new DateTime(DateTimeZone.UTC), null);

		assertEquals(1, actualClients.size());
		assertEquals(expectedClient, actualClients.get(0));
	}

	@Test
	public void shouldFindByWithOutAddressCriteria() {
		addRandomInvalidClient();
		Client expectedClient = new Client(BASE_ENTITY_ID);
		expectedClient.setFirstName(FIRST_NAME);
		expectedClient.setBirthdate(EPOCH_DATE_TIME);
		expectedClient.setAddresses(asList(getAddress()));
		expectedClient.setDateCreated(EPOCH_DATE_TIME);
		expectedClient.setGender(FEMALE);
		expectedClient.setDateEdited(EPOCH_DATE_TIME);
		expectedClient.setAttributes(attributes);
		addObjectToRepository(Collections.singletonList(expectedClient), allClients);

		List<Client> actualClients = luceneClientRepository
				.getByCriteria(expectedClient.getFirstName(), expectedClient.getGender(), EPOCH_DATE_TIME, new DateTime(),
						null, null, ATTRIBUTES_TYPE, ATTRIBUTES_VALUE, EPOCH_DATE_TIME, new DateTime(DateTimeZone.UTC),
						null);

		assertEquals(1, actualClients.size());
		assertEquals(expectedClient, actualClients.get(0));
	}

	@Test
	public void shouldFindByWithOutNameAndGenderAndBirthDateAndDeathDateAndAttributes() {
		addRandomInvalidClient();
		Client expectedClient = new Client(BASE_ENTITY_ID);
		expectedClient.setFirstName(FIRST_NAME);
		expectedClient.setBirthdate(EPOCH_DATE_TIME);
		expectedClient.setAddresses(asList(getAddress()));
		expectedClient.setDateCreated(EPOCH_DATE_TIME);
		expectedClient.setGender(FEMALE);
		expectedClient.setDateEdited(EPOCH_DATE_TIME);
		expectedClient.setAttributes(attributes);
		addObjectToRepository(Collections.singletonList(expectedClient), allClients);
		List<Client> actualClients = luceneClientRepository
				.getByCriteria(getAddress().getAddressType(), getAddress().getCountry(), getAddress().getStateProvince(),
						getAddress().getCityVillage(), getAddress().getCountyDistrict(), getAddress().getSubDistrict(),
						getAddress().getTown(), getAddress().getSubTown(), EPOCH_DATE_TIME, new DateTime(DateTimeZone.UTC),
						null);
		assertEquals(1, actualClients.size());
		assertEquals(expectedClient, actualClients.get(0));
	}

	@Test
	public void shouldFindByStringQuery() {
		String query = "(firstName:\"firstName\"OR middleName:\"firstName\"OR lastName:\"firstName\")AND gender:female AND birthdate<date>:[1970-01-01T00:00:00 TO 2017-08-30T10:29:01] AND lastEdited<date>:[1970-01-01T00:00:00 TO 2017-08-30T04:29:01] AND attributesType:attributesValue AND addressType:addressType AND country:country AND stateProvince:stateProvince AND cityVillage:cityVillage AND countyDistrict:countryDistrict AND subDistrict:subDistrict AND town:town";
		addRandomInvalidClient();
		Client expectedClient = new Client(BASE_ENTITY_ID);
		expectedClient.setFirstName(FIRST_NAME);
		expectedClient.setBirthdate(EPOCH_DATE_TIME);
		expectedClient.setAddresses(asList(getAddress()));
		expectedClient.setDateCreated(EPOCH_DATE_TIME);
		expectedClient.setGender(FEMALE);
		expectedClient.setDateEdited(EPOCH_DATE_TIME);
		expectedClient.setAttributes(attributes);
		addObjectToRepository(Collections.singletonList(expectedClient), allClients);

		List<Client> actualClients = luceneClientRepository.getByCriteria(query);

		assertEquals(1, actualClients.size());
		assertEquals(expectedClient, actualClients.get(0));
	}

	//TODO: Fix source
	@Test(expected = DbAccessException.class)
	public void throwExcetionMotherIdentifierInRelationship() {
		Client expectedClient = new Client(BASE_ENTITY_ID);
		expectedClient.setFirstName(FIRST_NAME);
		expectedClient.setBirthdate(EPOCH_DATE_TIME);
		expectedClient.setAddresses(asList(getAddress()));
		expectedClient.setDateCreated(EPOCH_DATE_TIME);
		expectedClient.addRelationship(MOTHERS_INDENTIFIER, IDENTIFIER_VALUE);
		addObjectToRepository(Collections.singletonList(expectedClient), allClients);

		List<Client> actualClients = luceneClientRepository.getByClientByMother(MOTHERS_INDENTIFIER, IDENTIFIER_VALUE);
		assertEquals(0, actualClients.size());
	}

	@Test
	public void shouldFindByBaseEntityFieldAndValue() {
		Client expectedClient = new Client(BASE_ENTITY_ID);
		expectedClient.setFirstName(FIRST_NAME);
		expectedClient.setBirthdate(EPOCH_DATE_TIME);
		expectedClient.setAddresses(asList(getAddress()));
		expectedClient.setDateCreated(EPOCH_DATE_TIME);

		addObjectToRepository(Collections.singletonList(expectedClient), allClients);

		List<Client> actualClients = luceneClientRepository
				.getByFieldValue(AllConstants.BaseEntity.BASE_ENTITY_ID, BASE_ENTITY_ID);
		assertEquals(1, actualClients.size());
		assertEquals(expectedClient, actualClients.get(0));
	}

	@Test
	public void shouldReturnEmptyForBaseEntityFieldAndValue() {
		Client expectedClient = new Client(BASE_ENTITY_ID);
		expectedClient.setFirstName(FIRST_NAME);
		expectedClient.setBirthdate(EPOCH_DATE_TIME);
		expectedClient.setAddresses(asList(getAddress()));
		expectedClient.setDateCreated(EPOCH_DATE_TIME);
		String value = null;

		addObjectToRepository(Collections.singletonList(expectedClient), allClients);

		List<Client> actualClients = luceneClientRepository.getByFieldValue(AllConstants.BaseEntity.BASE_ENTITY_ID, value);
		assertEquals(0, actualClients.size());
	}

	@Test
	public void shouldFindByBaseEntityFieldAndValues() {
		Client expectedClient = new Client(BASE_ENTITY_ID);
		expectedClient.setFirstName(FIRST_NAME);
		expectedClient.setBirthdate(EPOCH_DATE_TIME);
		expectedClient.setAddresses(asList(getAddress()));
		expectedClient.setDateCreated(EPOCH_DATE_TIME);

		Client expectedClient2 = new Client(DIFFERENT_BASE_ENTITY_ID);
		expectedClient2.setFirstName(FIRST_NAME);
		expectedClient2.setBirthdate(EPOCH_DATE_TIME);
		expectedClient2.setAddresses(asList(getAddress()));
		expectedClient2.setDateCreated(EPOCH_DATE_TIME);

		addObjectToRepository(asList(expectedClient, expectedClient2), allClients);

		List<Client> actualClients = luceneClientRepository
				.getByFieldValue(AllConstants.BaseEntity.BASE_ENTITY_ID, asList(BASE_ENTITY_ID, DIFFERENT_BASE_ENTITY_ID));
		assertEquals(2, actualClients.size());
		assertTwoListAreSameIgnoringOrder(asList(expectedClient, expectedClient2), actualClients);
	}

	@Test
	public void shouldReturnEmptyForNullOrEmptyValues() {
		Client expectedClient = new Client(BASE_ENTITY_ID);
		expectedClient.setFirstName(FIRST_NAME);
		expectedClient.setBirthdate(EPOCH_DATE_TIME);
		expectedClient.setAddresses(asList(getAddress()));
		expectedClient.setDateCreated(EPOCH_DATE_TIME);

		Client expectedClient2 = new Client(DIFFERENT_BASE_ENTITY_ID);
		expectedClient2.setFirstName(FIRST_NAME);
		expectedClient2.setBirthdate(EPOCH_DATE_TIME);
		expectedClient2.setAddresses(asList(getAddress()));
		expectedClient2.setDateCreated(EPOCH_DATE_TIME);

		addObjectToRepository(asList(expectedClient, expectedClient2), allClients);

		List<Client> actualClients = luceneClientRepository
				.getByFieldValue(AllConstants.BaseEntity.BASE_ENTITY_ID, Collections.EMPTY_LIST);
		assertEquals(0, actualClients.size());

		List<String> nullList = null;
		actualClients = luceneClientRepository.getByFieldValue(AllConstants.BaseEntity.BASE_ENTITY_ID, nullList);
		assertEquals(0, actualClients.size());

	}

	private void addRandomInvalidClient() {
		for (int i = 0; i < 100; i++) {
			Client client = new Client(BASE_ENTITY_ID + i);
			client.setFirstName(FIRST_NAME + i);
			client.setBirthdate(EPOCH_DATE_TIME);
			client.setAddresses(asList(new Address()));
			client.setDateCreated(EPOCH_DATE_TIME);
			client.setGender(FEMALE);
			client.setDateEdited(EPOCH_DATE_TIME);
			client.setAttributes(attributes);
			addObjectToRepository(Collections.singletonList(client), allClients);
		}
	}

}
