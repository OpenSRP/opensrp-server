package org.opensrp.domain;


import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class EventTest {

    @Test
    public void testEqualAndHashcodeContract() {
        EqualsVerifier.forClass(Event.class)
                .withIgnoredFields("id","revision")
                .suppress(Warning.NONFINAL_FIELDS)
                .withPrefabValues(User.class, new User("ll"), new User("e"))
                .withRedefinedSuperclass()
                .verify();
    }

    @Test
    public void testGetterAndSetter() {
        Validator validator = ValidatorBuilder.create()
                .with(new SetterTester())
                .with(new GetterTester())
                .build();

        validator.validate(PojoClassFactory.getPojoClass(Event.class));
    }

    @Test
    public void testObs() {
        Event event = new Event();
        event.setObs(null);
        assertEquals(0, event.getObs().size());

        event.setObs(null);
        event.withObs(new Obs());
        assertEquals(1,  event.getObs().size());
        assertNull(event.getObs("p", "c"));

        Obs obs = new Obs().withFieldCode("concept");
        event.setObs(null);
        event.withObs(asList(obs));
        assertEquals(1,  event.getObs().size());
        assertEquals(obs, event.getObs("", "concept"));
        assertNull(event.getObs("p", "concept"));

        obs = new Obs().withFieldCode("concept");
        obs.withParentCode("p");
        event.setObs(null);
        event.withObs(obs);
        assertEquals(1,  event.getObs().size());
        assertEquals(obs, event.getObs("p", "concept"));
        assertEquals(obs, event.getObs("", "concept"));
        assertNull(event.getObs("p", "con"));
    }

    @Test
    public void testIdentifiers() {
        Event event = new Event();
        event.withIdentifiers(null);
        assertEquals(0, event.getIdentifiers().size());
        assertNull(event.getIdentifier("dd"));

        event.withIdentifiers(null);
        event.withIdentifier("type", "id");
        assertEquals(1, event.getIdentifiers().size());
        assertNull(event.getIdentifier("dd"));
        assertEquals("id", event.getIdentifier("type"));

        event.removeIdentifier("dd");
        assertEquals(1, event.getIdentifiers().size());
        assertNull(event.getIdentifier("dd"));
        assertEquals("id", event.getIdentifier("type"));

        event.removeIdentifier("type");
        assertEquals(0, event.getIdentifiers().size());
        assertNull(event.getIdentifier("type"));

        event.withIdentifiers(null);
        event.addIdentifier("type", "id");
        assertEquals(1, event.getIdentifiers().size());
        assertEquals("id", event.getIdentifier("type"));
    }

    @Test
    public void testGettingIdentifiersUsingRegex() {
        Event event = new Event();
        event.withIdentifiers(null);
        event.addIdentifier("type", "id");
        assertEquals(1, event.getIdentifiers().size());
        assertEquals("id", event.getIdentifier("type"));

        assertNull( event.getIdentifierMatchingRegex(""));
        assertEquals("id", event.getIdentifierMatchingRegex("^[a-zA-Z]+"));
        assertNull(event.getIdentifierMatchingRegex("^[0-9]+"));
        assertEquals("id", event.getIdentifierMatchingRegex("^type"));
        assertNull(event.getIdentifierMatchingRegex("^id"));
    }

    @Test
    public void testAddingDetails() {
        Event event = new Event();
        event.setDetails(null);
        event.addDetails("key", "val");
        assertEquals(1, event.getDetails().size());
        assertEquals("val", event.getDetails().get("key"));

    }

}
