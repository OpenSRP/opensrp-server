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

public class FemaleSterilizationRegisterEntryDTOTest {
    @Test
    public void testAccesors_shouldAccessProperField() {
        PojoTestUtils.validateAccessors(FemaleSterilizationRegisterEntryDTO.class);
    }

    @Test
    public void testEqualsAndHashCode() {
        EqualsVerifier.forClass(FemaleSterilizationRegisterEntryDTO.class).suppress(Warning.NONFINAL_FIELDS).verify();
    }

    @Test
    public void testToStringAndSetters() {
        List<String> followupVisitDatesList = new ArrayList<>();
        String sterilization = "pill";
        FemaleSterilizationFPDetailsDTO femaleSterilizationFPDetailsDTO = new FemaleSterilizationFPDetailsDTO
                (sterilization, "", followupVisitDatesList);

        String wifeAge = "16";
        String husbandName = "sajid";
        String husbandAge = "24";
        FemaleSterilizationRegisterEntryDTO femaleSterilizationRegisterEntryDTO = new FemaleSterilizationRegisterEntryDTO()
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
                .withFpDetails(femaleSterilizationFPDetailsDTO);
        
        assertSame("sajid", femaleSterilizationRegisterEntryDTO.getHusbandName());
        assertTrue(femaleSterilizationRegisterEntryDTO.toString().contains(wifeAge));
        assertFalse(femaleSterilizationRegisterEntryDTO.toString().contains("village=nandanpur"));
    }
}
