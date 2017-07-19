package org.opensrp.dto.register;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;
import org.opensrp.dto.utils.PojoTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ECRegisterDTOTest {
    @Test
    public void testAccesors_shouldAccessProperField() {
        PojoTestUtils.validateAccessors(ECRegisterDTO.class);
    }

    @Test
    public void testEqualsAndHashCode() {
        EqualsVerifier.forClass(ECRegisterDTO.class).suppress(Warning.NONFINAL_FIELDS).verify();
    }

    @Test
    public void testToString() {
        List<ECRegisterEntryDTO> ecRegisterEntriesList = new ArrayList<>();
        ECRegisterEntryDTO ecRegisterEntryDTO = new ECRegisterEntryDTO().withVillage("nandanpur");
        ecRegisterEntriesList.add(ecRegisterEntryDTO);

        ECRegisterDTO ecRegisterDTO = new ECRegisterDTO(ecRegisterEntriesList);

        assertTrue(ecRegisterDTO.toString().contains("village=nandanpur"));
        assertFalse(ecRegisterDTO.toString().contains("wifeAge=18"));
    }
}
