package org.opensrp.service.it;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opensrp.BaseIntegrationTest;
import org.opensrp.domain.User;
import org.opensrp.repository.AllUsers;
import org.opensrp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.opensrp.util.SampleFullDomainObject.BASE_ENTITY_ID;
import static org.opensrp.util.SampleFullDomainObject.DIFFERENT_BASE_ENTITY_ID;
import static org.opensrp.util.SampleFullDomainObject.getUser;
import static org.utils.AssertionUtil.assertTwoListAreSameIgnoringOrder;
import static org.utils.CouchDbAccessUtils.addObjectToRepository;

public class UserServiceTest extends BaseIntegrationTest {

	@Autowired
	public AllUsers allUsers;

	@Autowired
	public UserService userService;

	@Before
	public void setUp() {
		allUsers.removeAll();
	}

	@After
	public void cleanUp() {
		allUsers.removeAll();
	}

	@Test
	public void shouldFindByBaseEntityId() {
		User expectedUser = getUser();
		addObjectToRepository(Collections.singletonList(expectedUser), allUsers);

		User actualUser = userService.getUserByEntityId(BASE_ENTITY_ID);

		assertEquals(expectedUser, actualUser);
	}

	@Test
	public void shouldReturnAllUser() {
		User user = getUser();
		User user1 = getUser();
		user1.setUsername(DIFFERENT_BASE_ENTITY_ID);
		List<User> expectedUsers = asList(user, user1);
		addObjectToRepository(expectedUsers, allUsers);

		List<User> actualUsers = userService.getAllUsers();

		assertTwoListAreSameIgnoringOrder(expectedUsers, actualUsers);

	}

	@Test
	public void shouldAddNewUser() {
		User expectedUser = getUser();

		userService.addUser(expectedUser);

		List<User> actualUsers = allUsers.getAll();
		assertEquals(1, actualUsers.size());
		assertEquals(expectedUser, actualUsers.get(0));
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionIfExistingUser() {
		addObjectToRepository(Collections.singletonList(getUser()), allUsers);
		userService.addUser(allUsers.getAll().get(0));
	}

	@Test
	public void shouldUpdateExistingUser() {
		addObjectToRepository(Collections.singletonList(getUser()), allUsers);
		User updatedUser = allUsers.getAll().get(0);
		updatedUser.setUsername(DIFFERENT_BASE_ENTITY_ID);

		userService.updateUser(updatedUser);

		List<User> actualUsers = allUsers.getAll();
		assertEquals(1, actualUsers.size());
		assertEquals(updatedUser, actualUsers.get(0));
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionIfNewWhileUpdate() {
		userService.updateUser(getUser());
	}
}
