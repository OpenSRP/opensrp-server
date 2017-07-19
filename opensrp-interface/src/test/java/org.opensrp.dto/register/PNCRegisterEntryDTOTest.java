package org.opensrp.dto.register;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;
import org.opensrp.dto.utils.PojoTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PNCRegisterEntryDTOTest {
    @Test
    public void testAccesors_shouldAccessProperField() {
        PojoTestUtils.validateAccessors(PNCRegisterEntryDTO.class);
    }

    @Test
    public void testEqualsAndHashCode() {
        EqualsVerifier.forClass(PNCRegisterEntryDTO.class).suppress(Warning.NONFINAL_FIELDS).verify();
    }

    @Test
    public void testToStringAndSetters() {
        String husbandName = "sajid";

        List<Map<String, String>> childrenDetailsList = new ArrayList<>();
        List<PNCVisitDTO> pncVisitsList = new ArrayList<>();

        PNCRegisterEntryDTO pncRegisterEntryDTO = new PNCRegisterEntryDTO();
        pncRegisterEntryDTO.withRegistrationDate("");
        pncRegisterEntryDTO.withThayiCardNumber("");
        pncRegisterEntryDTO.withWifeName("");
        pncRegisterEntryDTO.withHusbandName(husbandName);
        pncRegisterEntryDTO.withWifeDOB("");
        pncRegisterEntryDTO.withAddress("");
        pncRegisterEntryDTO.withDateOfDelivery("");
        pncRegisterEntryDTO.withPlaceOfDelivery("");
        pncRegisterEntryDTO.withTypeOfDelivery("");
        pncRegisterEntryDTO.withDischargeDate("");
        pncRegisterEntryDTO.withFPMethodName("");
        pncRegisterEntryDTO.withFPMethodDate("");
        pncRegisterEntryDTO.withDeliveryComplications("");
        pncRegisterEntryDTO.withChildrenDetails(childrenDetailsList);
        pncRegisterEntryDTO.withPNCVisits(pncVisitsList);

        assertTrue(pncRegisterEntryDTO.toString().contains(husbandName));
        assertFalse(pncRegisterEntryDTO.toString().contains("wifeDob=2017-08-19"));

    }
}
