package org.opensrp.api.contract;

import org.opensrp.api.domain.User;

public interface SecurityManager {
	/*
	 * List<String> getRoles(String username); boolean hasRole(String username);
	 * List<String> getPermissions(String username); boolean
	 * hasPermission(String username, String permission);
	 */
	User login(String username, String password, AuthorizationHandler<?> callbackHandler);
	
	void logout(AuthorizationHandler<?> callbackHandler);
	
	User getUser(String username);//???????????????????????

	User authenticate(String username, String password)	throws IllegalAccessException;

	boolean verifyAuthorization(String username, AuthorizationHandler<?> callbackHandler);

	boolean verifyAuthorization(User user, AuthorizationHandler<?> callbackHandler);

	boolean hasAdminRights(String username);

	String encryptPassword(String username, String salt, String cleartextPassword);

	String encryptPassword(User user, String cleartextPassword);

	// do we really want to encrypt data across client-server. If so what would happen incase of reset of password
	byte[] encryptData(byte[] data);

	byte[] decryptData(byte[] data);
}
