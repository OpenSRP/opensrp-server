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

public class OCPFPDetailsDTOTest {
    @Test
    public void testAccesors_shouldAccessProperField() {
        PojoTestUtils.validateAccessors(OCPFPDetailsDTO.class);
    }

    @Test
    public void testEqualsAndHashCode() {
        EqualsVerifier.forClass(OCPFPDetailsDTO.class).suppress(Warning.NONFINAL_FIELDS).verify();
    }

    @Test
    public void testToString() {
        String fpAcceptanceDate = "2017-07-19";
        Map<String, String> map = new HashMap<>();
        List<Map<String, String>> refillsList = new ArrayList<>();
        map.put("a", "femicon");
        map.put("b", "omicon");
        refillsList.add(map);
        OCPFPDetailsDTO ocpfpDetailsDTO = new OCPFPDetailsDTO(fpAcceptanceDate, refillsList, "", "");
        
        assertEquals("2017-07-19", ocpfpDetailsDTO.getFpAcceptanceDate());
        assertTrue(ocpfpDetailsDTO.toString().contains(fpAcceptanceDate));
        assertFalse(ocpfpDetailsDTO.toString().contains("lmpDate=2017-08-19"));
    }
}

