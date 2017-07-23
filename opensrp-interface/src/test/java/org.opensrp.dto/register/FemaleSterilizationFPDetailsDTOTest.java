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

public class FemaleSterilizationFPDetailsDTOTest {
    @Test
    public void testAccesors_shouldAccessProperField() {
        PojoTestUtils.validateAccessors(FemaleSterilizationFPDetailsDTO.class);
    }

    @Test
    public void testEqualsAndHashCode() {
        EqualsVerifier.forClass(FemaleSterilizationFPDetailsDTO.class).suppress(Warning.NONFINAL_FIELDS).verify();
    }

    @Test
    public void testToString() {
        List<String> followupVisitDatesList = new ArrayList<>();
        String sterilization = "pill";
        FemaleSterilizationFPDetailsDTO femaleSterilizationFPDetailsDTO = new FemaleSterilizationFPDetailsDTO
                (sterilization, "", followupVisitDatesList);
        assertSame("pill", femaleSterilizationFPDetailsDTO.getTypeOfSterilization());
        assertTrue(femaleSterilizationFPDetailsDTO.toString().contains(sterilization));
        assertFalse(femaleSterilizationFPDetailsDTO.toString().contains("sterilizationDate=2017-07-19"));
    }
}
