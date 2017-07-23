package org.opensrp.dto.register;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;
import org.opensrp.dto.utils.PojoTestUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class OCPRegisterEntryDTOTest {
    @Test
    public void testAccesors_shouldAccessProperField() {
        PojoTestUtils.validateAccessors(OCPRegisterEntryDTO.class);
    }

    @Test
    public void testEqualsAndHashCode() {
        EqualsVerifier.forClass(OCPRegisterEntryDTO.class).suppress(Warning.NONFINAL_FIELDS).verify();
    }

    @Test
    public void testToStringAndSetters() {
        String wifeAge = "16";
        String husbandName = "sajid";
        String husbandAge = "24";

        String fpAcceptanceDate = "2017-07-19";
        Map<String, String> map = new HashMap<>();
        List<Map<String, String>> refillsList = new ArrayList<>();
        map.put("a", "femicon");
        map.put("b", "omicon");
        refillsList.add(map);
        OCPFPDetailsDTO ocpfpDetailsDTO = new OCPFPDetailsDTO(fpAcceptanceDate, refillsList, "", "");

        OCPRegisterEntryDTO ocpRegisterEntryDTO = new OCPRegisterEntryDTO()
                .withEcNumber("")
                .withWifeName("")
                .withWifeAge(wifeAge)
                .withWifeEducationLevel("")
                .withHusbandName(husbandName)
                .withHusbandEducationLevel("")
                .withVillage("charpara")
                .withSubCenter("")
                .withReligion("islam")
                .withNumberOfLivingFemaleChildren("")
                .withNumberOfLivingMaleChildren("")
                .withCaste("")
                .withLmpDate("")
                .withUptResult("")
                .withFpDetails(ocpfpDetailsDTO);

        assertEquals("sajid", ocpRegisterEntryDTO.getHusbandName());
        assertTrue(ocpRegisterEntryDTO.toString().contains(wifeAge));
        assertFalse(ocpRegisterEntryDTO.toString().contains("lmpDate=2017-08-19"));
    }
}
