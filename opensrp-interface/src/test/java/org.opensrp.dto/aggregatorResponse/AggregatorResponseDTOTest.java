package org.opensrp.dto.aggregatorResponse;


import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

/**
 * Created by real on 13/07/17.
 */
public class AggregatorResponseDTOTest {
    @Test
    public void testConstructorsNGetters() {
        AggregatorResponseDTO aggregatorResponseDTO = new AggregatorResponseDTO("indicator", 7);
        assertEquals("indicator", aggregatorResponseDTO.indicator());
        assertNotSame("no Indicator", aggregatorResponseDTO.indicator());

        assertEquals((Integer) 7, aggregatorResponseDTO.count());
        assertNotSame((Integer) 8, aggregatorResponseDTO.count());

        assertTrue(aggregatorResponseDTO.toString().contains("indicator=indicator"));
        assertFalse(aggregatorResponseDTO.toString().contains("nrhm_report_indicator_count=3"));
    }

    @Test
    public void testEqualsAndHashCode() {
        EqualsVerifier.forClass(AggregatorResponseDTO.class).suppress(Warning.NONFINAL_FIELDS).verify();
    }

}
