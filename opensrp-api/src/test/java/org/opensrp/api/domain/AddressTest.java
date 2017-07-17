package org.opensrp.api.domain;

import org.joda.time.DateTime;
import org.junit.Test;
import org.opensrp.api.domain.Address;
import org.opensrp.common.AddressField;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.rule.impl.GetterMustExistRule;
import com.openpojo.validation.rule.impl.SetterMustExistRule;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import static org.junit.Assert.*;

public class AddressTest {

    public static final String NO_MATCH_ADDRESS_FIELD_MESSAGE = "No match. Should return NULL value.";

	@Test
    public void testAddressActivity() {
        Address address = new Address();

        assertTrue("Address should be ACTIVE when no endDate provided", address.isActive());

        DateTime currentDate = new DateTime();
        DateTime currentDatePlusOneDay = currentDate.plusDays(1);
        address.setEndDate(currentDatePlusOneDay.toDate());

		assertTrue("Address should be ACTIVE when endDate is greater than currentDate", address.isActive());


        address.setEndDate(new Date());
        assertFalse("Address should be IN-ACTIVE when endDate less than current date", address.isActive());

    }

    @Test
    public void testInvalidDurationForNoStartDateTime() {
        Address address = new Address();
        assertTrue("Duration of Address should be -1 for no startDate", address.durationInDays()==-1);
        assertTrue("Duration of Address should be -1 for no startDate", address.durationInWeeks()==-1);
        assertTrue("Duration of Address should be -1 for no startDate", address.durationInMonths()==-1);
        assertTrue("Duration of Address should be -1 for no startDate", address.durationInYears()==-1);

    }

    @Test
    public void testDurationIfNoEndDatePresent() {
        Address address = new Address();
        address.setStartDate(new Date());
        int days = address.durationInDays();
        System.out.println(days);
        boolean isLessThanADay = 0 == days;
        assertTrue("Duration will be less than a day, if start date is set to current date time.", isLessThanADay);
    }

    @Test
    public void testDurationWithBothStartDateAndEndDate() {
        DateTime currentDate = new DateTime();
        DateTime currentDatePlusOneYear = currentDate.plusYears(1);

        Address address = new Address();
        address.setStartDate(currentDate.toDate());
        address.setEndDate(currentDatePlusOneYear.toDate());

        assertEquals(1, address.durationInYears());
        assertEquals(12, address.durationInMonths());
        assertEquals(52, address.durationInWeeks());
        assertEquals(365, address.durationInDays());
    }
	
	@Test
	public void testAddressCreationWithConstructor(){
		Address address = new Address("Birthplace", new Date(), null, null, "2.222", "3.333", "75210", "Sindh", "Pakistan");
		
		assertEquals("Address type invalid", address.getAddressType(), "Birthplace");
		assertEquals("Lat value invalid", address.getLatitude(), "2.222");
		assertEquals("Long value invalid", address.getLongitute(), "3.333");
		assertEquals("Postcode value invalid", address.getPostalCode(), "75210");
		assertEquals("State value invalid", address.getState(), "Sindh");
		assertEquals("Country value invalid", address.getCountry(), "Pakistan");

	}
	
	@Test
	public void testAddressCreationgWithSetter(){
		Date sd = new Date();
		Date ed = new Date();
		Address address = new Address();
		address.withAddressType("Deathplace");
		address.withCountry("Pakistan");
		address.withState("Punjab");
		address.withPostalCode("75290");
		address.withLatitude("2.222");
		address.withLongitute("3.333");
		address.withStartDate(sd);
		address.withEndDate(ed);
		
		assertEquals("Address type invalid", address.getAddressType(), "Deathplace");
		assertEquals("Postcode value invalid", address.getPostalCode(), "75290");
		assertEquals("State value invalid", address.getState(), "Punjab");
		assertEquals("Country value invalid", address.getCountry(), "Pakistan");
        assertEquals("Lat value invalid", address.getLatitude(), "2.222");
        assertEquals("Long value invalid", address.getLongitute(), "3.333");
		assertEquals("StartDate invalid", address.getStartDate(), sd);
		assertEquals("EndDate invalid", address.getStartDate(), ed);
	}


	@Test
	public void testGettingAddressFiledByRegex() {
		String stringVal = "areaVal";
		String integerVal = "1";
		String noRegexMatch = "";
		String allRegexMatch = "\\D*";
		Map<String, String> addressFields = new HashMap<>();
		addressFields.put(AddressField.AREA.name(), stringVal);


		Address address = new Address();
		address.setAddressFields(addressFields);
		address.addAddressField(AddressField.HOUSE_NUMBER.name(), integerVal);


		String invalidAddressValue = address.getAddressFieldMatchingRegex(noRegexMatch);
		assertNull(NO_MATCH_ADDRESS_FIELD_MESSAGE, invalidAddressValue);

		String validAddressValue1 = address.getAddressFieldMatchingRegex(allRegexMatch);
		assertEquals(integerVal, validAddressValue1);

		String numberAddressVAlue = address.getAddressFieldMatchingRegex(AddressField.AREA.name());
		assertEquals(stringVal, numberAddressVAlue);

	}

	@Test
	public void testGettingSpecificAddressField() {
		String stringVal = "areaVal";
		String noFieldMatch = "";
		String lowerCaseFieldName = "area";
		String upperCaseFieldName = "AREA";

		Map<String, String> addressFields = new HashMap<>();
		addressFields.put(AddressField.AREA.name(), stringVal);


		Address address = new Address();
		address.setAddressFields(addressFields);

		String nullAddressValue = address.getAddressField(noFieldMatch);
		assertNull(NO_MATCH_ADDRESS_FIELD_MESSAGE, nullAddressValue);

		String invalidAddressValue = address.getAddressField(AddressField.HOUSE_NUMBER);
		assertNull(NO_MATCH_ADDRESS_FIELD_MESSAGE, invalidAddressValue);

		String validValueWithEnum = address.getAddressField(AddressField.AREA);
		assertEquals(stringVal, validValueWithEnum);

		String validValueWithLowerCase = address.getAddressField(lowerCaseFieldName);
		assertEquals(stringVal, validValueWithLowerCase);

		String validValueWithUpperCase = address.getAddressField(upperCaseFieldName);
		assertEquals(stringVal, validValueWithUpperCase);

	}

	@Test
	public void testRemovingAddressField() {
		String stringVal = "areaVal";
		String lowerCaseFieldName = "area";
		String upperCaseFieldName = "AREA";


		Map<String, String> addressFields = new HashMap<>();
		addressFields.put(AddressField.AREA.name(), stringVal);
		Address address = new Address();
		address.setAddressFields(addressFields);

        address.removeAddressField(AddressField.HOUSE_NUMBER);
        assertEquals(1, address.getAddressFields().size());
        assertNotNull(address.getAddressField(AddressField.AREA));

		address.removeAddressField(AddressField.AREA);
		assertEquals(0, address.getAddressFields().size());
		assertNull(address.getAddressField(AddressField.AREA));



		address.addAddressField(AddressField.AREA, stringVal);

		address.removeAddressField("");
        assertEquals(1, address.getAddressFields().size());
        assertNotNull(address.getAddressField(AddressField.AREA));

        address.removeAddressField(lowerCaseFieldName);
		assertEquals(0, address.getAddressFields().size());
		assertNull(address.getAddressField(AddressField.AREA));




		address.addAddressField(AddressField.AREA, stringVal);
		address.removeAddressField(upperCaseFieldName);
		assertEquals(0, address.getAddressFields().size());
		assertNull(address.getAddressField(AddressField.AREA));

	}
	
    @Test
    public void testAddressField(){
        Address address = new Address();
        address.setAddressType("usual_residence");
        Assert.assertEquals("usual_residence", address.getAddressType());
        Assert.assertNotSame("usual_residence_1", address.getAddressType());
        address.withAddressField("address3", "Dambwa_Central");
        Map<String, String> addressFields = new HashMap<>();
        addressFields.put("address2", "morelight c566");
        address.withAddressFields(addressFields);    	
        address.withAddressField(AddressField.AREA, "Ares");
        Assert.assertEquals("morelight c566", address.getAddressField("address2"));
        Assert.assertNotSame("morelight c5666", address.getAddressField("address2"));
    }
    @Test
    public void shouldTestSetterAndGetter() {
        PojoClass pojoClass = PojoClassFactory.getPojoClass(Address.class);
        Validator pojoValidator = ValidatorBuilder.create()
            .with(new SetterMustExistRule())
            .with(new GetterMustExistRule())
            .with(new GetterTester())
            .with(new SetterTester())
            .build();

        pojoValidator.validate(pojoClass);
    }
}
