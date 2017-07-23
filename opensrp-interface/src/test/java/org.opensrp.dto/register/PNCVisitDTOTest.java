package org.opensrp.dto.register;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;
import org.opensrp.dto.utils.PojoTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PNCVisitDTOTest {
    @Test
    public void testAccesors_shouldAccessProperField() {
        PojoTestUtils.validateAccessors(PNCVisitDTO.class);
    }

    @Test
    public void testEqualsAndHashCode() {
        EqualsVerifier.forClass(PNCVisitDTO.class).suppress(Warning.NONFINAL_FIELDS).verify();
    }

    @Test
    public void testToStringAndSetters() {
        String person = "khusi";
        String place = "rupnagar";
        List<Map<String, String>> childrenDetailsList = new ArrayList<>();

        PNCVisitDTO pncVisitDTO = new PNCVisitDTO()
                .withDate("")
                .withPerson(person)
                .withPlace(place)
                .withDifficulties("")
                .withAbdominalProblems("")
                .withVaginalProblems("")
                .withUrinalProblems("")
                .withBreastProblems("")
                .withChildrenDetails(childrenDetailsList);

        assertTrue(pncVisitDTO.toString().contains(person));
        assertFalse(pncVisitDTO.toString().contains("difficulties=no"));
    }
}
