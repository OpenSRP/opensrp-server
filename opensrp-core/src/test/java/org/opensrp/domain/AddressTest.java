package org.opensrp.domain;


import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;
import org.joda.time.DateTime;
import org.junit.Test;
import org.opensrp.common.AddressField;

import static org.junit.Assert.*;

public class AddressTest {

    @Test
    public void testGetterAndSetter() {
        Validator validator = ValidatorBuilder.create()
                .with(new SetterTester())
                .with(new GetterTester())
                .build();

        validator.validate(PojoClassFactory.getPojoClass(Address.class));
    }

    @Test
    public void testSetAndGetAddressField() {
        Address address = new Address();
        address.withAddressFields(null);
        assertNull(address.getAddressFields());

        address.withAddressField("field", "val");
        assertEquals("val", address.getAddressField("field"));
        assertNull(address.getAddressField("ddd"));

        address.withAddressFields(null);
        assertNull(address.getAddressFields());

        address.withAddressField(AddressField.AREA, "val");
        assertEquals("val", address.getAddressField(AddressField.AREA));
        assertNull(address.getAddressField(AddressField.HOUSE_NUMBER));

        address.withAddressFields(null);
        assertNull(address.getAddressFields());



    }


    @Test
    public void testAddAndRemoveAddressField() {
        Address address = new Address();
        address.withAddressFields(null);
        assertNull(address.getAddressFields());

        address.addAddressField("field", "val");
        address.addAddressField(AddressField.AREA, "val");
        assertEquals(2, address.getAddressFields().size());
        assertEquals("val", address.getAddressField("field"));
        assertEquals("val", address.getAddressField(AddressField.AREA));

        address.removeAddressField("ddd");
        address.removeAddressField(AddressField.HOUSE_NUMBER);
        assertEquals(2, address.getAddressFields().size());
        assertEquals("val", address.getAddressField("field"));
        assertEquals("val", address.getAddressField(AddressField.AREA));

        address.removeAddressField("field");
        address.removeAddressField(AddressField.AREA);
        assertEquals(0, address.getAddressFields().size());
        assertNull( address.getAddressField("field"));
        assertNull(address.getAddressField(AddressField.AREA));
    }

    @Test
    public void testGetAddressFieldUsingRegex() {
        Address address = new Address();
        address.withAddressFields(null);
        assertNull(address.getAddressFields());

        address.addAddressField(AddressField.AREA, "area");
        address.addAddressField("field", "val");

        assertNull( address.getAddressFieldMatchingRegex(""));
        assertNull(address.getAddressFieldMatchingRegex("^[0-9]+"));
        assertEquals("val", address.getAddressFieldMatchingRegex("^field"));
        assertNull(address.getAddressFieldMatchingRegex("^TYPE"));
        assertEquals("area", address.getAddressFieldMatchingRegex("^AREA"));
        assertNull(address.getAddressFieldMatchingRegex("^area"));
        assertNull(address.getAddressFieldMatchingRegex("^id"));

    }

    @Test
    public void testIsActive() {
        Address address = new Address();
        assertTrue(address.isActive());

        address.withEndDate(new DateTime().plusDays(5));
        assertTrue(address.isActive());

        address.withEndDate(new DateTime().minusDays(5));
        assertFalse(address.isActive());
    }

    @Test
    public void testDurationCalculation() {
        Address address = new Address();
        assertEquals(-1, address.durationInDays());

        address.withStartDate(new DateTime().minusDays(5));
        assertEquals(5, address.durationInDays());

        address.withStartDate(new DateTime());
        address.withEndDate(new DateTime().plusMonths(2));
        assertEquals(2, address.durationInMonths());

        address.withEndDate(new DateTime().plusWeeks(2));
        assertEquals(2, address.durationInWeeks());

        address.withEndDate(new DateTime().plusYears(2));
        assertEquals(2, address.durationInYears());
    }

    @Test
    public void testCreatingObject() {
        Address address = new Address();
        String latitude = "222";
        String longitude = "22";
        String geopoint = "geopoint";
        String postalCode = "postalCode";
        String town = "town";
        String subDistrict = "sub";
        String countyDistrict = "count";
        String cityVillage = "village";
        String stateProvince = "province";
        String country = "country";
        address.withLatitude(latitude)
                .withLongitude(longitude)
                .withGeopoint(geopoint)
                .withPostalCode(postalCode)
                .withTown(town)
                .withSubDistrict(subDistrict)
                .withCountyDistrict(countyDistrict)
                .withCityVillage(cityVillage)
                .withStateProvince(stateProvince)
                .withCountry(country);

        assertEquals(latitude, address.getLatitude());
        assertEquals(longitude, address.getLongitude());
        assertEquals(geopoint, address.getGeopoint());
        assertEquals(postalCode, address.getPostalCode());
        assertEquals(town, address.getTown());
        assertEquals(subDistrict, address.getSubDistrict());
        assertEquals(countyDistrict, address.getCountyDistrict());
        assertEquals(cityVillage, address.getCityVillage());
        assertEquals(country, address.getCountry());
    }
}
