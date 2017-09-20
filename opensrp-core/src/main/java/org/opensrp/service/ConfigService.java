package org.opensrp.service;

import java.util.List;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.ektorp.CouchDbConnector;
import org.opensrp.domain.AppStateToken;
import org.opensrp.repository.AllAppStateTokens;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mysql.jdbc.StringUtils;

@Service
public class ConfigService {

	private final AllAppStateTokens allAppStateTokens;

	@Autowired
	public ConfigService(AllAppStateTokens allAppStateTokens) {
		this.allAppStateTokens = allAppStateTokens;
	}

	/**
	 * @param tokenName
	 * @return AppStateToken with given name. Since model is supposed to keep track of system`s state at any given time it throws IllegalStateException incase multiple Tokens found with same name.
	 */
	public AppStateToken getAppStateTokenByName(Enum<?> tokenName) {
		List<AppStateToken> ol = allAppStateTokens.findByName(tokenName.name());
		return getUniqueAppStateTokeFromTokenList(ol, tokenName);
	}

	/**
	 * Gets appstatetoken from the specified database
	 *
	 * @param db
	 * @param tokenName
	 * @return AppStateToken with given name. Since model is supposed to keep track of system`s state at any given time it throws IllegalStateException incase multiple Tokens found with same name.
	 */
	public AppStateToken getAppStateTokenByName(CouchDbConnector db, Enum<?> tokenName) {
		List<AppStateToken> ol = allAppStateTokens.findByName(db, tokenName.name());
		return getUniqueAppStateTokeFromTokenList(ol, tokenName);
	}

	public void updateAppStateToken(Enum<?> tokenName, Object value) {
		List<AppStateToken> ol = allAppStateTokens.findByName(tokenName.name());
		AppStateToken ast = updateUniqueAppStateToken(ol, tokenName, value);
		allAppStateTokens.update(ast);
	}

	public void updateAppStateToken(CouchDbConnector db, Enum<?> tokenName, Object value) {
		List<AppStateToken> ol = allAppStateTokens.findByName(db, tokenName.name());
		AppStateToken ast = updateUniqueAppStateToken(ol, tokenName, value);
		allAppStateTokens.update(db, ast);
	}

	/**
	 * Registers a new token to manage the specified variable state (by token name) of App.
	 * Throws IllegalArgumentException if tokenName or description is not provided or if name is not unique
	 * i.e. already exists in system and flag suppressExceptionIfExists is false.
	 *
	 * @param tokenName
	 * @param defaultValue
	 * @param description
	 * @param suppressExceptionIfExists
	 * @return The newly registered token.
	 */
	public AppStateToken registerAppStateToken(Enum<?> tokenName, Object defaultValue, String description,
	                                           boolean suppressExceptionIfExists) {

		checkIfNameAndDescriptionExist(tokenName, description);

		List<AppStateToken> atl = allAppStateTokens.findByName(tokenName.name());

		AppStateToken existingAppStateToken = checkIfTokenAlreadyExist(atl, tokenName, suppressExceptionIfExists);

		if (existingAppStateToken != null) {
			return existingAppStateToken;
		}

		AppStateToken token = new AppStateToken(tokenName.name(), defaultValue, 0L, description);
		allAppStateTokens.add(token);
		return token;
	}

	/**
	 * Registers a new token to manage the specified variable state (by token name) of App. The token is registered in the specified db
	 *
	 * @param db
	 * @param tokenName
	 * @param defaultValue
	 * @param description
	 * @param suppressExceptionIfExists
	 * @return
	 */
	public AppStateToken registerAppStateToken(CouchDbConnector db, Enum<?> tokenName, Object defaultValue,
	                                           String description, boolean suppressExceptionIfExists) {
		checkIfNameAndDescriptionExist(tokenName, description);

		List<AppStateToken> atl = allAppStateTokens.findByName(db, tokenName.name());
		AppStateToken existingAppStateToken = checkIfTokenAlreadyExist(atl, tokenName, suppressExceptionIfExists);

		if (existingAppStateToken != null) {
			return existingAppStateToken;
		}

		AppStateToken token = new AppStateToken(tokenName.name(), defaultValue, 0L, description);
		allAppStateTokens.add(db, token);
		return token;
	}

	private AppStateToken getUniqueAppStateTokeFromTokenList(List<AppStateToken> appStateTokens, Enum tokenName) {
		if (appStateTokens.size() > 1) {
			throw new IllegalStateException("System was found to have multiple token with same name (" + tokenName.name()
					+ "). This can lead to potential critical inconsistencies.");
		}

		return appStateTokens.size() == 0 ? null : appStateTokens.get(0);
	}

	private AppStateToken updateUniqueAppStateToken(List<AppStateToken> allAppStateTokens, Enum<?> tokenName, Object value) {
		if (allAppStateTokens.size() > 1) {
			throw new IllegalStateException("System was found to have multiple token with same name (" + tokenName.name()
					+ "). This can lead to potential critical inconsistencies.");
		}

		if (allAppStateTokens.size() == 0) {
			throw new IllegalStateException("Property with name (" + tokenName.name() + ") not found.");
		}

		AppStateToken ast = allAppStateTokens.get(0);
		ast.setValue(value);
		ast.setLastEditDate(System.currentTimeMillis());
		return ast;
	}

	private void checkIfNameAndDescriptionExist(Enum<?> tokenName, String description) {
		if (tokenName == null || StringUtils.isEmptyOrWhitespaceOnly(description)) {
			throw new IllegalArgumentException("Token name and description must be provided");
		}
	}

	private AppStateToken checkIfTokenAlreadyExist(List<AppStateToken> appStateTokens, Enum<?> tokenName,
	                                               boolean suppressExceptionIfExists) {
		if (appStateTokens.size() > 0) {
			if (!suppressExceptionIfExists) {
				throw new IllegalArgumentException("Token with given name (" + tokenName.name() + ") already exists.");
			}
			return appStateTokens.get(0);
		}
		return null;
	}
}
