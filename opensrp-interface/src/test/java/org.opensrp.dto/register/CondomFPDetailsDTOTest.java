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

public class CondomFPDetailsDTOTest {
    @Test
    public void testAccesors_shouldAccessProperField() {
        PojoTestUtils.validateAccessors(CondomFPDetailsDTO.class);
    }

    @Test
    public void testEqualsAndHashCode() {
        EqualsVerifier.forClass(CondomFPDetailsDTO.class).suppress(Warning.NONFINAL_FIELDS).verify();
    }

    @Test
    public void testToString() {
        Map<String, String> map = new HashMap<>();
        List<Map<String, String>> refillsList = new ArrayList<>();
        map.put("a", "panther");
        map.put("b", "raja");
        refillsList.add(map);
        CondomFPDetailsDTO condomFPDetailsDTO = new CondomFPDetailsDTO("19-07-2017", refillsList);

        assertTrue(condomFPDetailsDTO.toString().contains("fpAcceptanceDate=19-07-2017"));
        assertTrue(condomFPDetailsDTO.toString().contains("a=panther"));
        assertFalse(condomFPDetailsDTO.toString().contains("b=sensation"));
    }
}
