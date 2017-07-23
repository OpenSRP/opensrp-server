package org.opensrp.dto;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;
import org.opensrp.dto.utils.PojoTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MonthSummaryDatumTest {
    @Test
    public void testAccesors_shouldAccessProperField() {
        PojoTestUtils.validateAccessors(MonthSummaryDatum.class);
    }

    @Test
    public void testEqualsAndHashCode() {
        EqualsVerifier.forClass(MonthSummaryDatum.class).suppress(Warning.NONFINAL_FIELDS).verify();
    }

    @Test
    public void testToStringAndConstructors() {
        List<String> externalIDsList = new ArrayList<>();
        externalIDsList.add("999");
        String month = "7";
        String year = "2017";
        String currentProgress = "80%";
        String aggregatedProgress = "50%";
        MonthSummaryDatum monthSummaryDatum = new MonthSummaryDatum(month, year, currentProgress, aggregatedProgress, externalIDsList);

        assertEquals("7", monthSummaryDatum.getMonth());
        assertNotSame("2018", monthSummaryDatum.getYear());

        assertTrue(monthSummaryDatum.toString().contains(currentProgress));
        assertFalse(monthSummaryDatum.toString().contains("898"));

    }
}
