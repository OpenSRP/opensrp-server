package org.opensrp.domain;


import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;

import static org.junit.Assert.*;

public class LocationTest {

    @Test
    public void testEqualAndHashcodeContract() {
        EqualsVerifier.forClass(Location.class)
                .withIgnoredFields("id", "revision")
                .suppress(Warning.NONFINAL_FIELDS)
                .withPrefabValues(User.class, new User("ll"), new User("e"))
                .withPrefabValues(Location.class, new Location(), new Location().withName("sdsf"))
                .verify();
    }

    @Test
    public void testGetterAndSetter() {
        Validator validator = ValidatorBuilder.create()
                .with(new SetterTester())
                .with(new GetterTester())
                .build();

        validator.validate(PojoClassFactory.getPojoClass(Location.class));
    }

    @Test
    public void testConstructor() {
        Location location = new Location("id", "name", new Address(), null);
        assertEquals("id", location.getLocationId());
        assertEquals("name", location.getName());

        Location location1 = new Location("id","name", new Address(), new TreeMap<String, String>(), location, new HashSet<String>(), new HashMap<String, Object>());
        assertEquals(location, location1.getParentLocation());
        assertEquals(new TreeMap<String, String >(), location1.getIdentifiers());
        assertEquals(new HashMap<String, String>(), location1.getAttributes());

        Location location2 = new Location()
                .withParentLocation(location1)
                .withLocationId("id")
                .withAddress(new Address());

        assertEquals(location1, location2.getParentLocation());

    }

    @Test
    public void testIdentifiers() {
        Location location = new Location();

        location.withIdentifiers(null);

        assertNull(location.getIdentifiers());


        location.withIdentifier("type", "id");
        assertEquals(1, location.getIdentifiers().size());
        assertEquals("id", location.getIdentifier("type"));

        location.removeIdentifier("type");
        assertEquals(0, location.getIdentifiers().size());
        assertNull(location.getIdentifier("type"));

        location.withIdentifiers(null);
        location.addIdentifier("type", "id");
        assertEquals(1, location.getIdentifiers().size());
        assertEquals("id", location.getIdentifier("type"));

    }


    @Test
    public void testAttributes() {
        Location location = new Location();

        location.withAttributes(null);

        assertNull(location.getAttributes());


        location.withAttribute("type", "id");
        assertEquals(1, location.getAttributes().size());
        assertEquals("id", location.getAttribute("type"));

        location.removeAttribute("type");
        assertEquals(0, location.getAttributes().size());
        assertNull(location.getAttribute("type"));

        location.withAttributes(null);
        location.addAttribute("type", "id");
        assertEquals(1, location.getAttributes().size());
        assertEquals("id", location.getAttribute("type"));

    }

    @Test
    public void testTags() {
        Location location = new Location();

        location.withTags(null);
        assertNull(location.getTags());

        location.withTag("tag");
        location.addTag("tag2");
        assertEquals(2, location.getTags().size());
        assertTrue(location.hasTag("tag"));
        assertTrue(location.hasTag("tag2"));
        assertFalse(location.hasTag("dd"));

        assertFalse(location.removeTag("d"));
        assertTrue(location.removeTag("tag"));
        assertEquals(1, location.getTags().size());
        assertFalse(location.hasTag("tag"));

        location.withTags(null);
        location.addTag("tag");
        assertEquals(1, location.getTags().size());
        assertTrue(location.hasTag("tag"));
    }

}
