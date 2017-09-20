package org.opensrp.service.it;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opensrp.BaseIntegrationTest;
import org.opensrp.domain.Provider;
import org.opensrp.repository.AllProviders;
import org.opensrp.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.opensrp.util.SampleFullDomainObject.BASE_ENTITY_ID;
import static org.opensrp.util.SampleFullDomainObject.DIFFERENT_BASE_ENTITY_ID;
import static org.opensrp.util.SampleFullDomainObject.getProvider;
import static org.utils.AssertionUtil.assertTwoListAreSameIgnoringOrder;
import static org.utils.CouchDbAccessUtils.addObjectToRepository;

public class ProviderServiceTest extends BaseIntegrationTest {

	@Autowired
	public AllProviders allProviders;

	@Autowired
	public ProviderService providerService;

	@Before
	public void setUp() {
		allProviders.removeAll();
	}

	@After
	public void cleanUp() {
		allProviders.removeAll();
	}

	@Test
	public void shouldGetProviderByBaseEntityId() {
		Provider expectedProvider = getProvider();
		addObjectToRepository(Collections.singletonList(expectedProvider), allProviders);

		Provider actualProvider = providerService.getProviderByBaseEntityId(BASE_ENTITY_ID);

		assertEquals(expectedProvider, actualProvider);
	}

	@Test
	public void shouldGetAllTheProvider() {
		Provider provider = getProvider();
		Provider provider1 = getProvider();
		provider1.setBaseEntityId(DIFFERENT_BASE_ENTITY_ID);
		List<Provider> expectedProviders = asList(provider, provider1);

		addObjectToRepository(expectedProviders, allProviders);

		List<Provider> actualProviders = providerService.getAllProviders();

		assertTwoListAreSameIgnoringOrder(expectedProviders, actualProviders);
	}

	@Test
	public void shouldAddProvider() {
		Provider expectedProvider = getProvider();

		providerService.addProvider(expectedProvider);

		List<Provider> actualProviders = allProviders.getAll();
		assertEquals(1, actualProviders.size());
		assertEquals(expectedProvider, actualProviders.get(0));
	}

	@Test
	public void shouldUpdateProvider() {
		addObjectToRepository(Collections.singletonList(getProvider()), allProviders);

		Provider updatedProvider = allProviders.getAll().get(0);
		updatedProvider.setFullName(DIFFERENT_BASE_ENTITY_ID);

		providerService.updateProvider(updatedProvider);

		List<Provider> actualProviders = allProviders.getAll();
		assertEquals(1, actualProviders.size());
		assertEquals(updatedProvider, actualProviders.get(0));

	}
}
