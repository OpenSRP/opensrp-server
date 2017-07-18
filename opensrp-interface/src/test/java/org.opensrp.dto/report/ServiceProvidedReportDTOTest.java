package org.opensrp.dto.report;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.opensrp.dto.utils.PojoTestUtils;

import static org.junit.Assert.*;

/**
 * Created by real on 13/07/17.
 */
public class ServiceProvidedReportDTOTest {
    @Test
    public void testAccesors_shouldAccessProperField() {
        PojoTestUtils.validateAccessors(ServiceProvidedReportDTO.class);
    }

    @Test
    public void testEqualsAndHashCode() {
        EqualsVerifier.forClass(ServiceProvidedReportDTO.class).suppress(Warning.NONFINAL_FIELDS).verify();
    }

    @Test
    public void testConstructorAndwithSettersAndtoString() {
        ServiceProvidedReportDTO serviceProvidedReportDTO, serviceProvidedReportDTO2;
        serviceProvidedReportDTO = new ServiceProvidedReportDTO();
        serviceProvidedReportDTO.withId(3);
        serviceProvidedReportDTO.withDate(new LocalDate());
        serviceProvidedReportDTO.withNRHMReportMonth(7);
        serviceProvidedReportDTO.withNRHMReportYear(2017);
        assertEquals((Integer) 3, serviceProvidedReportDTO.getId());
        assertNotSame((Integer) 8, serviceProvidedReportDTO.getNrhm_report_month());

        serviceProvidedReportDTO2 = new ServiceProvidedReportDTO(1, "", "", "", new LocalDate(), "", "", "", "", "", "");
        assertTrue(serviceProvidedReportDTO2.toString().contains("id=1"));
        assertFalse(serviceProvidedReportDTO2.toString().contains("village=gangapur"));
    }

}
