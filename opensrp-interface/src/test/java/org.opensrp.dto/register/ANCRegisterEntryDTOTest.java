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

/**
 * Created by real on 13/07/17.
 */
public class ANCRegisterEntryDTOTest {

    @Test
    public void testWithANCNumber() {
        Map<String, String> mapUtils = new HashMap<>();
        mapUtils.put("key", "value");

        List<Map<String, String>> ancVisitsList = new ArrayList<>();
        ancVisitsList.add(mapUtils);

        List<Map<String, String>> ifaTabletsList = new ArrayList<>();
        ifaTabletsList.add(mapUtils);

        List<Map<String, String>> ttDosesList = new ArrayList<>();
        ttDosesList.add(mapUtils);

        List<Map<String, String>> hbTestsList = new ArrayList<>();
        hbTestsList.add(mapUtils);

        List<Map<String, String>> ancInvestigationsList = new ArrayList<>();
        ancInvestigationsList.add(mapUtils);

        ANCRegisterEntryDTO ancRegisterEntryDTO = new ANCRegisterEntryDTO()
                .withANCNumber("anc1")
                .withRegistrationDate("registrationDate")
                .withECNumber("")
                .withThayiCardNumber("")
                .withAadharCardNumber("")
                .withWifeName("")
                .withHusbandName("")
                .withAddress("")
                .withWifeDOB("")
                .withPhoneNumber("")
                .withWifeEducationLevel("")
                .withHusbandEducationLevel("")
                .withCaste("")
                .withReligion("")
                .withEconomicStatus("")
                .withBPLCardNumber("")
                .withJSYBeneficiary("")
                .withGravida("")
                .withParity("")
                .withNumberOfLivingChildren("")
                .withNumberOfStillBirths("")
                .withNumberOfAbortions("")
                .withYoungestChildDOB("")
                .withLMP("")
                .withEDD("")
                .withHeight("")
                .withBloodGroup("")
                .withIsHRP("")
                .withANCVisits(ancVisitsList)
                .withIFATablets(ifaTabletsList)
                .withTTDoses(ttDosesList)
                .withHBTests(hbTestsList)
                .withANCInvestigations(ancInvestigationsList);
        System.out.println(ancRegisterEntryDTO.toString());

        assertTrue(ancRegisterEntryDTO.toString().contains("ancNumber=anc1"));
        assertFalse(ancRegisterEntryDTO.toString().contains("husbandName=real"));
    }

    @Test
    public void testAccesors_shouldAccessProperField() {
        PojoTestUtils.validateAccessors(ANCRegisterEntryDTO.class);
    }

    @Test
    public void testEqualsAndHashCode() {
        EqualsVerifier.forClass(ANCRegisterEntryDTO.class).suppress(Warning.NONFINAL_FIELDS).verify();
    }
}
