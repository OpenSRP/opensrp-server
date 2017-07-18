package org.opensrp.dto.register;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;
import org.opensrp.dto.utils.PojoTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by real on 17/07/17.
 */
public class ChildRegisterDTOTest {
    @Test
    public void testAccesors_shouldAccessProperField() {
        PojoTestUtils.validateAccessors(ChildRegisterDTO.class);
    }

    @Test
    public void testEqualsAndHashCode() {
        EqualsVerifier.forClass(ChildRegisterDTO.class).suppress(Warning.NONFINAL_FIELDS).verify();
    }

    @Test
    public void testToStringOfChildRegisterDTO() {
        ChildRegisterEntryDTO childRegisterEntryDTO = new ChildRegisterEntryDTO().withVillage("gangapur");
        List<ChildRegisterEntryDTO> childRegisterEntriesList = new ArrayList<>();
        childRegisterEntriesList.add(childRegisterEntryDTO);

        ChildRegisterDTO childRegisterDTO = new ChildRegisterDTO(childRegisterEntriesList);
        assertTrue(childRegisterDTO.toString().contains("village=gangapur"));
        assertFalse(childRegisterDTO.toString().contains("thayiCardNumber=19945114390000347"));

    }
}
