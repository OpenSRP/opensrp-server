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

public class MaleSterilizationRegisterEntryDTOTest {
    @Test
    public void testAccesors_shouldAccessProperField() {
        PojoTestUtils.validateAccessors(MaleSterilizationRegisterEntryDTO.class);
    }

    @Test
    public void testEqualsAndHashCode() {
        EqualsVerifier.forClass(MaleSterilizationRegisterEntryDTO.class).suppress(Warning.NONFINAL_FIELDS).verify();
    }

    @Test
    public void testToStringAndSetters() {
        List<String> followupVisitDatesList = new ArrayList<>();

        String typeOfSterilization = "oldness";
        MaleSterilizationFPDetailsDTO maleSterilizationFPDetailsDTO = new MaleSterilizationFPDetailsDTO
                (typeOfSterilization, "", followupVisitDatesList);
        String wifeAge = "16";
        String husbandName = "sajid";
        String husbandAge = "24";
        MaleSterilizationRegisterEntryDTO maleSterilizationRegisterEntryDTO = new MaleSterilizationRegisterEntryDTO()
                .withEcNumber("")
                .withWifeName("")
                .withWifeAge(wifeAge)
                .withWifeEducationLevel("")
                .withHusbandName(husbandName)
                .withHusbandAge(husbandAge)
                .withHusbandEducationLevel("")
                .withVillage("charpara")
                .withSubCenter("")
                .withReligion("islam")
                .withNumberOfLivingFemaleChildren("")
                .withNumberOfLivingMaleChildren("")
                .withCaste("")
                .withFpDetails(maleSterilizationFPDetailsDTO);

        assertSame("sajid", maleSterilizationRegisterEntryDTO.getHusbandName());
        assertTrue(maleSterilizationRegisterEntryDTO.toString().contains(wifeAge));
        assertFalse(maleSterilizationRegisterEntryDTO.toString().contains("village=nandanpur"));
    }
}
