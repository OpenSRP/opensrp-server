package org.opensrp.dto.register;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;
import org.opensrp.dto.utils.PojoTestUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CondomRegisterEntryDTOTest {
    @Test
    public void testAccesors_shouldAccessProperField() {
        PojoTestUtils.validateAccessors(CondomRegisterEntryDTO.class);
    }

    @Test
    public void testEqualsAndHashCode() {
        EqualsVerifier.forClass(CondomRegisterEntryDTO.class).suppress(Warning.NONFINAL_FIELDS).verify();
    }

    @Test
    public void testToString() {
        Map<String, String> map = new HashMap<>();
        List<Map<String, String>> refillsList = new ArrayList<>();
        map.put("a", "panther");
        map.put("b", "raja");
        refillsList.add(map);
        CondomFPDetailsDTO condomFPDetailsDTO = new CondomFPDetailsDTO("19-07-2017", refillsList);

        CondomRegisterEntryDTO condomRegisterEntryDTO = new CondomRegisterEntryDTO().withEcNumber("")
                .withWifeName("akhi")
                .withHusbandName("")
                .withVillage("gangapur")
                .withSubCenter("nandanpur")
                .withWifeAge("16")
                .withCaste("")
                .withReligion("")
                .withNumberOfLivingMaleChildren("")
                .withNumberOfLivingFemaleChildren("")
                .withHusbandEducationLevel("")
                .withWifeEducationLevel("")
                .withFpDetails(condomFPDetailsDTO);

        assertTrue(condomRegisterEntryDTO.toString().contains("wifeName=akhi"));
        assertFalse(condomRegisterEntryDTO.toString().contains("wifeAge=18"));
    }
}
