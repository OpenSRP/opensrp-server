package org.opensrp.dto.register;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;
import org.opensrp.dto.utils.PojoTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class MaleSterilizationFPDetailsDTOTest {
    @Test
    public void testAccesors_shouldAccessProperField() {
        PojoTestUtils.validateAccessors(MaleSterilizationFPDetailsDTO.class);
    }

    @Test
    public void testEqualsAndHashCode() {
        EqualsVerifier.forClass(MaleSterilizationFPDetailsDTO.class).suppress(Warning.NONFINAL_FIELDS).verify();
    }

    @Test
    public void testToString() {
        List<String> followupVisitDatesList = new ArrayList<>();

        String typeOfSterilization = "oldness";
        MaleSterilizationFPDetailsDTO maleSterilizationFPDetailsDTO = new MaleSterilizationFPDetailsDTO(typeOfSterilization, "", followupVisitDatesList);
        assertSame("oldness", maleSterilizationFPDetailsDTO.getTypeOfSterilization());
        assertTrue(maleSterilizationFPDetailsDTO.toString().contains(typeOfSterilization));
        assertFalse(maleSterilizationFPDetailsDTO.toString().contains("sterilizationDate=2017-07-19"));
    }
}
