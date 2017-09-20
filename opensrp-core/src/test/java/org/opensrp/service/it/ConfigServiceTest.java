package org.opensrp.service.it;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opensrp.BaseIntegrationTest;
import org.opensrp.domain.AppStateToken;
import org.opensrp.repository.AllAppStateTokens;
import org.opensrp.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.opensrp.util.SampleFullDomainObject.*;
import static org.opensrp.util.SampleFullDomainObject.AppStateTokenName.APP_STATE_TOKEN_NAME;
import static org.opensrp.util.SampleFullDomainObject.AppStateTokenName.DIFFERENT_APP_STATE_TOKEN_NAME;
import static org.utils.CouchDbAccessUtils.addObjectToRepository;
import static org.utils.CouchDbAccessUtils.getCouchDbConnector;

public class ConfigServiceTest extends BaseIntegrationTest {

	@Autowired
	AllAppStateTokens allAppStateTokens;

	@Autowired
	ConfigService configService;

	@Before
	public void setUp() {
		allAppStateTokens.removeAll();
	}

	@After
	public void cleanUp() {
		allAppStateTokens.removeAll();
	}

	@Test
	public void shouldFindAppStateTokenByName() {
		AppStateToken expectedAppStateToken = getAppStateToken();
		AppStateToken invalidAppStateToken = getAppStateToken();
		invalidAppStateToken.setName(DIFFERENT_APP_STATE_TOKEN_NAME.name());
		addObjectToRepository(asList(expectedAppStateToken, invalidAppStateToken), allAppStateTokens);

		AppStateToken actualAppStateToken = configService.getAppStateTokenByName(APP_STATE_TOKEN_NAME);

		assertEquals(expectedAppStateToken, actualAppStateToken);
	}

	@Test(expected = IllegalStateException.class)
	public void shouldThrowExceptionIfMultipleAppStateTokenWithSameNameFound() {
		AppStateToken expectedAppStateToken = getAppStateToken();
		AppStateToken invalidAppStateToken = getAppStateToken();
		addObjectToRepository(asList(expectedAppStateToken, invalidAppStateToken), allAppStateTokens);

		configService.getAppStateTokenByName(APP_STATE_TOKEN_NAME);

	}

	@Test
	public void shouldReturnNullIfNoTokenFound() {
		assertNull(configService.getAppStateTokenByName(APP_STATE_TOKEN_NAME));
	}

	@Test
	public void shouldFindAppStateTokenByNameUsingCouchdbConnector() throws IOException {
		AppStateToken expectedAppStateToken = getAppStateToken();
		AppStateToken invalidAppStateToken = getAppStateToken();
		invalidAppStateToken.setName(DIFFERENT_APP_STATE_TOKEN_NAME.name());
		addObjectToRepository(asList(expectedAppStateToken, invalidAppStateToken), allAppStateTokens);

		AppStateToken actualAppStateToken = configService
				.getAppStateTokenByName(getCouchDbConnector("opensrp"), APP_STATE_TOKEN_NAME);

		assertEquals(expectedAppStateToken, actualAppStateToken);
	}

	@Test
	public void shouldUpdate() {
		AppStateToken expectedAppStateToken = getAppStateToken();
		addObjectToRepository(Collections.singletonList(expectedAppStateToken), allAppStateTokens);

		expectedAppStateToken.setValue(DIFFERENT_BASE_ENTITY_ID);

		configService.updateAppStateToken(APP_STATE_TOKEN_NAME, DIFFERENT_BASE_ENTITY_ID);

		List<AppStateToken> allTokens = allAppStateTokens.getAll();
		assertEquals(1, allTokens.size());
		assertNotEquals(expectedAppStateToken.getLastEditDate(), allTokens.get(0).getLastEditDate());

		allTokens.get(0).setLastEditDate(LAST_EDIT_DATE);
		assertEquals(expectedAppStateToken, allTokens.get(0));
	}

	@Test(expected = IllegalStateException.class)
	public void shouldThrowExceptionIfMultipleTokenFoundWithSameNameWhileUpdate() {
		AppStateToken expectedAppStateToken = getAppStateToken();
		AppStateToken invalidAppStateToken = getAppStateToken();
		addObjectToRepository(asList(expectedAppStateToken, invalidAppStateToken), allAppStateTokens);

		configService.updateAppStateToken(APP_STATE_TOKEN_NAME, DIFFERENT_BASE_ENTITY_ID);
	}

	@Test
	public void shouldThrowExceptionIfTokenNotFound() {

	}

	@Test
	public void shouldUpdateWithCouchDbConnector() throws IOException {
		AppStateToken expectedAppStateToken = getAppStateToken();
		addObjectToRepository(Collections.singletonList(expectedAppStateToken), allAppStateTokens);

		expectedAppStateToken.setValue(DIFFERENT_BASE_ENTITY_ID);

		configService.updateAppStateToken(getCouchDbConnector("opensrp"), APP_STATE_TOKEN_NAME, DIFFERENT_BASE_ENTITY_ID);

		List<AppStateToken> allTokens = allAppStateTokens.getAll();
		assertEquals(1, allTokens.size());
		assertNotEquals(expectedAppStateToken.getLastEditDate(), allTokens.get(0).getLastEditDate());

		allTokens.get(0).setLastEditDate(LAST_EDIT_DATE);
		assertEquals(expectedAppStateToken, allTokens.get(0));
	}

	@Test
	public void shouldRegisterNewAppStateToken() {
		AppStateToken expectedAppStateToken = getAppStateToken();
		expectedAppStateToken.setLastEditDate(0L);

		AppStateToken actualAppStateToken = configService
				.registerAppStateToken(APP_STATE_TOKEN_NAME, VALUE, APP_STATE_TOKEN_DESCRIPTION, true);

		List<AppStateToken> allTokens = allAppStateTokens.getAll();
		assertEquals(1, allTokens.size());
		assertEquals(expectedAppStateToken, allTokens.get(0));
		assertEquals(expectedAppStateToken, actualAppStateToken);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionInRegisterIfNameIsNull() {
		configService.registerAppStateToken(null, VALUE, APP_STATE_TOKEN_DESCRIPTION, true);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionInRegisterIfDescriptionIsEmpty() {
		configService.registerAppStateToken(APP_STATE_TOKEN_NAME, VALUE, "", true);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionInRegisterIfTokenAlreadyExistAndSuppressWaringIsOff() {
		AppStateToken expectedAppStateToken = getAppStateToken();
		addObjectToRepository(Collections.singletonList(expectedAppStateToken), allAppStateTokens);

		configService.registerAppStateToken(APP_STATE_TOKEN_NAME, VALUE, APP_STATE_TOKEN_DESCRIPTION, false);
	}

	@Test
	public void shouldReturnExistingTokenIfSuppressWarningIsOn() {
		AppStateToken expectedAppStateToken = getAppStateToken();
		addObjectToRepository(Collections.singletonList(expectedAppStateToken), allAppStateTokens);

		AppStateToken actualAppStateToken = configService
				.registerAppStateToken(APP_STATE_TOKEN_NAME, VALUE, APP_STATE_TOKEN_DESCRIPTION, true);

		assertEquals(expectedAppStateToken, actualAppStateToken);
	}

	@Test
	public void shouldRegisterNewTokenWithCouchdbConnector() throws IOException {
		AppStateToken expectedAppStateToken = getAppStateToken();
		expectedAppStateToken.setLastEditDate(0L);

		AppStateToken actualAppStateToken = configService
				.registerAppStateToken(getCouchDbConnector("opensrp"), APP_STATE_TOKEN_NAME, VALUE,
						APP_STATE_TOKEN_DESCRIPTION, true);

		List<AppStateToken> allTokens = allAppStateTokens.getAll();
		assertEquals(1, allTokens.size());
		assertEquals(expectedAppStateToken, allTokens.get(0));
		assertEquals(expectedAppStateToken, actualAppStateToken);
	}
}
