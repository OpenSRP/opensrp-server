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

import java.util.HashMap;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

public class BaseEntityTest {

    @Test
    public void testGetterAndSetter() {
        Validator validator = ValidatorBuilder.create()
                .with(new SetterTester())
                .with(new GetterTester())
                .build();

        validator.validate(PojoClassFactory.getPojoClass(BaseEntity.class));
    }

    @Test
    public void testEqualAndHashcodeContract() {
        EqualsVerifier.forClass(AppStateToken.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .withRedefinedSuperclass()
                .verify();
    }

    @Test
    public void testIdentifiers() {
        BaseEntity baseEntity = new BaseEntity();

        baseEntity.withIdentifiers(null);
        assertEquals(0, baseEntity.getIdentifiers().size());

        String type = "type";
        String id = "id";
        baseEntity.withIdentifier(type, id);
        assertEquals(1, baseEntity.getIdentifiers().size());
        assertEquals(id, baseEntity.getIdentifier(type));

        baseEntity.withIdentifiers(null);
        baseEntity.addIdentifier(type, id);
        assertEquals(1, baseEntity.getIdentifiers().size());
        assertEquals(id, baseEntity.getIdentifier(type));

        baseEntity.removeIdentifier("eee");
        assertEquals(1, baseEntity.getIdentifiers().size());
        assertEquals(id, baseEntity.getIdentifier(type));

        baseEntity.removeIdentifier(type);
        assertEquals(0, baseEntity.getIdentifiers().size());
        assertNull(baseEntity.getIdentifier(type));

    }

    @Test
    public void testGettingIdentifiersUsingRegex() {
        BaseEntity baseEntity = new BaseEntity();

        baseEntity.withIdentifiers(null);
        baseEntity.addIdentifier("type", "id");
        assertEquals(1, baseEntity.getIdentifiers().size());
        assertEquals("id", baseEntity.getIdentifier("type"));

        assertNull( baseEntity.getIdentifierMatchingRegex(""));
        assertEquals("id", baseEntity.getIdentifierMatchingRegex("^[a-zA-Z]+"));
        assertNull(baseEntity.getIdentifierMatchingRegex("^[0-9]+"));
        assertEquals("id", baseEntity.getIdentifierMatchingRegex("^type"));
        assertNull(baseEntity.getIdentifierMatchingRegex("^id"));
    }

    @Test
    public void testAttributes() {
        BaseEntity baseEntity = new BaseEntity();

        baseEntity.withAttributes(null);

        assertEquals(0, baseEntity.getAttributes().size());


        baseEntity.withAttribute("type", "id");
        assertEquals(1, baseEntity.getAttributes().size());
        assertEquals("id", baseEntity.getAttribute("type"));

        baseEntity.removeAttribute("type");
        assertEquals(0, baseEntity.getAttributes().size());
        assertNull(baseEntity.getAttribute("type"));

        baseEntity.withAttributes(null);
        baseEntity.addAttribute("type", "id");
        assertEquals(1, baseEntity.getAttributes().size());
        assertEquals("id", baseEntity.getAttribute("type"));

    }

    @Test
    public void testSuperObjectCreation() {
        BaseEntity baseEntity = new BaseEntity();

        DateTime dateCreated = new DateTime(0l);
        DateTime dateEdited = new DateTime(1l);
        DateTime dateVoided = new DateTime(3l);
        User editor = new User("dd");
        String voidReason = "void";
        User creator = new User("ee");
        User voider = new User("klj");
        baseEntity.withCreator(creator)
                .withDateCreated(dateCreated)
                .withDateEdited(dateEdited)
                .withDateVoided(dateVoided)
                .withEditor(editor)
                .withVoided(true)
                .withVoidReason(voidReason)
                .withVoider(voider);

        assertEquals(creator, baseEntity.getCreator());
        assertEquals(editor, baseEntity.getEditor());
        assertEquals(voider, baseEntity.getVoider());
        assertEquals(dateCreated, baseEntity.getDateCreated());
        assertEquals(dateEdited, baseEntity.getDateEdited());
        assertEquals(dateVoided, baseEntity.getDateVoided());
        assertEquals(voidReason, baseEntity.getVoidReason());
        assertTrue(baseEntity.getVoided());

        baseEntity = new BaseEntity();
        baseEntity.setCreator(creator);
        baseEntity.setEditor(editor);
        baseEntity.setVoider(voider);
        baseEntity.setDateCreated(dateCreated);
        baseEntity.setDateEdited(dateEdited);
        baseEntity.setDateVoided(dateVoided);
        baseEntity.setVoided(false);
        baseEntity.setVoidReason(voidReason);

        assertEquals(creator, baseEntity.getCreator());
        assertEquals(editor, baseEntity.getEditor());
        assertEquals(voider, baseEntity.getVoider());
        assertEquals(dateCreated, baseEntity.getDateCreated());
        assertEquals(dateEdited, baseEntity.getDateEdited());
        assertEquals(dateVoided, baseEntity.getDateVoided());
        assertEquals(voidReason, baseEntity.getVoidReason());
        assertFalse(baseEntity.getVoided());
    }

    @Test
    public void testConstructor() {
        BaseEntity baseEntity;
        HashMap<String, String> identifiers = new HashMap<>();
        identifiers.put("k", "l");
        HashMap<String, Object> attribues = new HashMap<>();
        attribues.put("l", "k");
        Address address = new Address().withCountry("country").withAddressType("type");
        List<Address> addressList = asList(address);

        baseEntity = new BaseEntity("dd", identifiers);
        assertEquals("dd", baseEntity.getBaseEntityId());
        assertEquals(identifiers, baseEntity.getIdentifiers());

        baseEntity = new BaseEntity("dd", identifiers, attribues);
        assertEquals("dd", baseEntity.getBaseEntityId());
        assertEquals(identifiers, baseEntity.getIdentifiers());
        assertEquals(attribues, baseEntity.getAttributes());

        baseEntity = new BaseEntity("dd", identifiers, attribues, addressList);
        assertEquals("dd", baseEntity.getBaseEntityId());
        assertEquals(identifiers, baseEntity.getIdentifiers());
        assertEquals(attribues, baseEntity.getAttributes());
        assertEquals(addressList, baseEntity.getAddresses());
        assertEquals(address, baseEntity.getAddress("type"));

        baseEntity = new BaseEntity().withBaseEntityId("dd");
        assertEquals("dd", baseEntity.getBaseEntityId());
        assertEquals(null, baseEntity.getAddress("type"));

    }
}
