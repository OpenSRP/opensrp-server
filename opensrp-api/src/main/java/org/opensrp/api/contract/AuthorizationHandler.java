package org.opensrp.api.contract;

import org.opensrp.api.domain.User;

public interface AuthorizationHandler<T> {

	boolean verifyAuthorization(User user, T service) throws IllegalAccessException;
}
