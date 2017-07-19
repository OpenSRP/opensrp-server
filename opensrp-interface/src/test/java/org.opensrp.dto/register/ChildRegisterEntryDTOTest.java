package org.opensrp.dto.register;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;
import org.opensrp.dto.utils.PojoTestUtils;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ChildRegisterEntryDTOTest {
    @Test
    public void testAccesors_shouldAccessProperField() {
        PojoTestUtils.validateAccessors(ChildRegisterEntryDTO.class);
    }

    @Test
    public void testEqualsAndHashCode() {
        EqualsVerifier.forClass(ChildRegisterEntryDTO.class).suppress(Warning.NONFINAL_FIELDS).verify();
    }

    @Test
    public void testSettersOfChildRegisterEntryDTO() {
        Map<String, String> immunizationsMap = new HashMap<>();
        immunizationsMap.put("1", "sunday");
        Map<String, String> vitaminADosesMap = new HashMap<>();
        vitaminADosesMap.put("1", "vitamin A");

        ChildRegisterEntryDTO childRegisterEntryDTO = new ChildRegisterEntryDTO();
        childRegisterEntryDTO.withThayiCardNumber("");
        childRegisterEntryDTO.withWifeName("");
        childRegisterEntryDTO.withHusbandName("real");
        childRegisterEntryDTO.withVillage("nandanpur");
        childRegisterEntryDTO.withSubCenter("");
        childRegisterEntryDTO.withWifeDOB("");
        childRegisterEntryDTO.withDOB("");
        childRegisterEntryDTO.withImmunizations(immunizationsMap);
        childRegisterEntryDTO.withVitaminADoses(vitaminADosesMap);

        //System.out.println(childRegisterEntryDTO.toString());
        assertTrue(childRegisterEntryDTO.toString().contains("village=nandanpur"));
        assertTrue(childRegisterEntryDTO.toString().contains("vitaminADoses={1=vitamin A}"));
        assertFalse(childRegisterEntryDTO.toString().contains("husband=mamun"));


    }
}
