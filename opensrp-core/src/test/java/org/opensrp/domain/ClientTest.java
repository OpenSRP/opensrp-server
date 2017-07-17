package org.opensrp.domain;


import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.joda.time.DateTime;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class ClientTest {

    @Test
    public void testEqualAndHashcodeContract() {
        EqualsVerifier.forClass(Client.class)
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

        validator.validate(PojoClassFactory.getPojoClass(Client.class));
    }

    @Test
    public void testSetAndGetRelationship() {
        Client client = new Client();
        client.withRelationships(null);

        assertNull(client.getRelationships());
        assertNull(client.findRelatives("dd"));
        assertEquals(0, client.getRelationships("d").size());



        client.withRelationships(null);
        client.addRelationship("type", "r");
        client.addRelationship("type", "e");
        client.addRelationship("type1", "r");

        List<String> expectedRelationships = asList("type1", "type");
        List<String> expectedIds = asList("r", "e");
        assertEquals(2, client.getRelationships().size());
        assertEquals(new HashSet<>(expectedRelationships), new HashSet<>(client.getRelationships("r")));
        assertEquals(new HashSet<>(expectedIds), new HashSet<>(client.findRelatives("type")));
        assertEquals(0, client.getRelationships("d").size());

    }

    @Test
    public void testConstructor() {
        String baseEntityId = "id";
        String firstName = "first";
        String middleName = "middleName";
        String lastName = "lastName";
        DateTime birthDate = new DateTime(0l);
        boolean birthDateApprx = true;
        DateTime deathDate = new DateTime(1l);
        boolean deathDateApprox = false;
        String gender = "male";
        String type = "type";
        String identifier = "identifier";

        Client client = new Client(baseEntityId, firstName, middleName, lastName, birthDate, deathDate, birthDateApprx, deathDateApprox, gender, type, identifier);

        assertEquals(firstName, client.getFirstName());
        assertEquals(middleName, client.getMiddleName());
        assertEquals(lastName, client.getLastName());
        assertEquals(birthDate, client.getBirthdate());
        assertEquals(birthDateApprx, client.getBirthdateApprox());
        assertEquals(deathDate, client.getDeathdate());
        assertEquals(deathDateApprox, client.getDeathdateApprox());
        assertEquals(identifier, client.getIdentifier(type));
        assertEquals(gender, client.getGender());

        client = new Client(baseEntityId, firstName, middleName, lastName, birthDate, deathDate, birthDateApprx, deathDateApprox, gender);
        assertEquals(firstName, client.getFirstName());
        assertEquals(middleName, client.getMiddleName());
        assertEquals(lastName, client.getLastName());
        assertEquals(birthDate, client.getBirthdate());
        assertEquals(birthDateApprx, client.getBirthdateApprox());
        assertEquals(deathDate, client.getDeathdate());
        assertEquals(deathDateApprox, client.getDeathdateApprox());
        assertNull(client.getIdentifier(type));
        assertEquals(gender, client.getGender());
    }

    @Test
    public void testFullName() {
        Client client = new Client("ss").withFirstName("first").withMiddleName("middle").withLastName("last");
        assertEquals("first middle last", client.fullName());

        client = new Client("dd").withName("first", "middle", null);
        assertEquals("first middle", client.fullName());

        client = new Client("dd").withName("first", null, "last");
        assertEquals("first last", client.fullName());

        client = new Client("dd").withName("first", null, null);
        assertEquals("first", client.fullName());

        client = new Client("dd").withName(null, "middle", null);
        assertEquals("middle", client.fullName());

        client = new Client("dd").withName(null, "middle", "last");
        assertEquals("middle last", client.fullName());

        client = new Client("dd").withName(null, null, "last");
        assertEquals("last", client.fullName());

        client = new Client("dd").withName(null, null, null);
        assertEquals("", client.fullName());
    }



}
