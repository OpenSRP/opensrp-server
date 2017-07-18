package org.opensrp.dto.report;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;
import org.opensrp.dto.LocationDTO;
import org.opensrp.dto.utils.PojoTestUtils;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by real on 13/07/17.
 */
public class AggregatedReportsDTOTest {
    @Test
    public void testAccesors_shouldAccessProperField() {
        PojoTestUtils.validateAccessors(AggregatedReportsDTO.class);
    }

    @Test
    public void testEqualsAndHashCode() {
        EqualsVerifier.forClass(AggregatedReportsDTO.class).suppress(Warning.NONFINAL_FIELDS).verify();
    }

    @Test
    public void testConstructorAndtoString() {
        Map<String, Integer> indicatorSummary = new HashMap<>();
        indicatorSummary.put("one", 1);
        LocationDTO loc = new LocationDTO("", "", "", "", "");
        AggregatedReportsDTO aggregatedReportsDTO = new AggregatedReportsDTO(indicatorSummary, loc);
        assertTrue(aggregatedReportsDTO.toString().contains("ind={one=1}"));
        assertFalse(aggregatedReportsDTO.toString().contains("ind=null"));
    }
}
