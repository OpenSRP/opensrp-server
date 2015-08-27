package org.opensrp.api;

import java.util.Date;

import static org.junit.Assert.*;

import org.junit.Test;
import org.opensrp.api.constants.Gender;
import org.opensrp.api.domain.BaseEntity;
import org.opensrp.api.domain.User;

public class UserTest {

	@Test
	public void userTest(){
		User u = new User();
		Date db = new Date();
		Date dd = new Date();
		u.withBaseEntity(new BaseEntity().withAttribute("attr1", "strval")
			.withAttribute("attr2", 2)
			.withAttribute("attr3", true)
			.withName("fn", "mn", "ln")
			.withBirthdate(db, true)
			.withDeathdate(dd, false)
			.withGender(Gender.MALE))
			.withPermission("view")
			.withPermission("edit")
			.withPermission("add")
			.withRole("r1")
			.withRole("r2")
			.withStatus("active");
		
		assertEquals("", (String)u.getBaseEntity().getAttribute("attr1"), "strval");
		assertEquals("", (int)u.getBaseEntity().getAttribute("attr2"), 2);
		assertEquals("", (boolean)u.getBaseEntity().getAttribute("attr3"), true);
		assertNull("", u.getBaseEntity().getAttribute("attr8"));
		u.getBaseEntity().removeAttribute("attr3");
		assertNull("", u.getBaseEntity().getAttribute("attr3"));
		assertEquals("", u.getBaseEntity().getFirstName(), "fn");
		assertEquals("", u.getBaseEntity().getMiddleName(), "mn");
		assertEquals("", u.getBaseEntity().getLastName(), "ln");
		assertEquals("", u.getBaseEntity().getBirthdate(), db);
		assertEquals("", u.getBaseEntity().getBirthdateApprox(), true);
		assertEquals("", u.getBaseEntity().getDeathdate(), dd);
		assertEquals("", u.getBaseEntity().getDeathdateApprox(), false);
		assertEquals("", u.getBaseEntity().getGender(), "MALE");
		assertEquals("", u.getStatus(), "active");
		assertEquals("", u.hasPermission("view"), true);
		assertEquals("", u.hasPermission("edit"), true);
		assertEquals("", u.hasPermission("add"), true);
		assertEquals("", u.hasPermission("delete"), false);
		assertEquals("", u.hasRole("r1"), true);
		assertEquals("", u.hasRole("r2"), true);
		assertEquals("", u.hasRole("r4"), false);
		assertEquals("", u.hasAdminRights(), false);
		assertEquals("", u.isDefaultAdmin(), false);

		u.addRole("admin");
		assertEquals("", u.hasAdminRights(), true);
		assertEquals("", u.isDefaultAdmin(), false);

		u.addRole("administrator");
		assertEquals("", u.hasAdminRights(), true);
		assertEquals("", u.isDefaultAdmin(), false);
		
		u.getBaseEntity().setLastName("admin");
		assertEquals("", u.hasAdminRights(), true);
		assertEquals("", u.isDefaultAdmin(), false);
		
		u.getBaseEntity().setFirstName("admin");
		assertEquals("", u.hasAdminRights(), true);
		assertEquals("", u.isDefaultAdmin(), true);

		u.removeRole("admin");
		assertEquals("", u.hasAdminRights(), true);
		assertEquals("", u.isDefaultAdmin(), true);
		
		u.removeRole("administrator");
		assertEquals("", u.hasAdminRights(), false);
		assertEquals("", u.isDefaultAdmin(), false);
		
	}
	
	
}
