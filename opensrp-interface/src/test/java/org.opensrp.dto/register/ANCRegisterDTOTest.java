package org.opensrp.dto.register;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by real on 13/07/17.
 */
public class ANCRegisterDTOTest {
    @Test
    public void testEqualsAndHashCode() {
        EqualsVerifier.forClass(ANCRegisterDTO.class).suppress(Warning.NONFINAL_FIELDS).verify();
    }

    @Test
    public void testConstructor_toString() {
        List<ANCRegisterEntryDTO> ancRegisterEntries = new ArrayList<>();
        ANCRegisterEntryDTO ancRegisterEntryDTO = new ANCRegisterEntryDTO();
        ancRegisterEntries.add(ancRegisterEntryDTO.withANCNumber("anc1"));
        ANCRegisterDTO ancRegisterDTO = new ANCRegisterDTO(ancRegisterEntries);

        assertTrue(ancRegisterDTO.toString().contains("ancNumber=anc1"));
        assertFalse(ancRegisterDTO.toString().contains("registrationDate=2017-07-13"));
    }

}
