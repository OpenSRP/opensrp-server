package org.opensrp.service.it;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opensrp.BaseIntegrationTest;
import org.opensrp.domain.BaseEntity;
import org.opensrp.repository.AllBaseEntities;
import org.opensrp.service.BaseEntityService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.opensrp.util.SampleFullDomainObject.*;
import static org.utils.AssertionUtil.assertTwoListAreSameIgnoringOrder;
import static org.utils.CouchDbAccessUtils.addObjectToRepository;

public class BaseEntityServiceTest extends BaseIntegrationTest {

	@Autowired
	public AllBaseEntities allBaseEntities;

	@Autowired
	public BaseEntityService baseEntityService;

	@Before
	public void setUp() {
		allBaseEntities.removeAll();
	}

	@After
	public void cleanUp() {
		allBaseEntities.removeAll();
	}

	@Test
	public void shouldGetAllBaseEntities() {
		BaseEntity baseEntity = getBaseEntity();
		BaseEntity baseEntity1 = getBaseEntity();
		baseEntity1.setBaseEntityId(DIFFERENT_BASE_ENTITY_ID);
		List<BaseEntity> expectedBaseEntities = asList(baseEntity, baseEntity1);
		addObjectToRepository(expectedBaseEntities, allBaseEntities);

		List<BaseEntity> actualBaseEntities = baseEntityService.getAllBaseEntities();

		assertTwoListAreSameIgnoringOrder(expectedBaseEntities, actualBaseEntities);
	}

	@Test
	public void shouldFindByEntityId() {
		BaseEntity expectedBaseEntity = getBaseEntity();
		BaseEntity invalidBaseEntity = getBaseEntity();
		invalidBaseEntity.setBaseEntityId(DIFFERENT_BASE_ENTITY_ID);
		addObjectToRepository(asList(expectedBaseEntity, invalidBaseEntity), allBaseEntities);

		BaseEntity actualBaseEntity = baseEntityService.findByBaseEntityId(BASE_ENTITY_ID);

		assertEquals(expectedBaseEntity, actualBaseEntity);
	}

	@Test
	public void shouldFindByIdentifierValue() {
		BaseEntity expectedBaseEntity = getBaseEntity();
		BaseEntity expectedBaseEntity1 = getBaseEntity();
		BaseEntity invalidBaseEntity = getBaseEntity();
		Map<String, String> differentIdentifiers = new HashMap<>(identifier);
		differentIdentifiers.put(IDENTIFIER_TYPE, DIFFERENT_BASE_ENTITY_ID);
		invalidBaseEntity.setIdentifiers(differentIdentifiers);
		List<BaseEntity> expectedBaseEntities = asList(expectedBaseEntity, expectedBaseEntity1);
		addObjectToRepository(asList(expectedBaseEntity, expectedBaseEntity1, invalidBaseEntity), allBaseEntities);

		List<BaseEntity> actualBaseEntities = baseEntityService.findByIdentifier(IDENTIFIER_VALUE);

		assertTwoListAreSameIgnoringOrder(expectedBaseEntities, actualBaseEntities);
	}

	@Test
	public void shouldFindByIdentifierTypeAndValue() {
		BaseEntity expectedBaseEntity = getBaseEntity();
		BaseEntity expectedBaseEntity1 = getBaseEntity();
		BaseEntity invalidBaseEntity = getBaseEntity();
		Map<String, String> differentIdentifiers = new HashMap<>();
		differentIdentifiers.put(DIFFERENT_BASE_ENTITY_ID, DIFFERENT_BASE_ENTITY_ID);
		invalidBaseEntity.setIdentifiers(differentIdentifiers);
		List<BaseEntity> expectedBaseEntities = asList(expectedBaseEntity, expectedBaseEntity1);
		addObjectToRepository(asList(expectedBaseEntity, expectedBaseEntity1, invalidBaseEntity), allBaseEntities);

		List<BaseEntity> actualBaseEntities = baseEntityService.findByIdentifier(IDENTIFIER_TYPE, IDENTIFIER_VALUE);

		assertTwoListAreSameIgnoringOrder(expectedBaseEntities, actualBaseEntities);
	}
}
