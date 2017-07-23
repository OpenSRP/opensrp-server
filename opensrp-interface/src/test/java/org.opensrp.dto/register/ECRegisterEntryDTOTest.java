package org.opensrp.dto.register;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;
import org.opensrp.dto.utils.PojoTestUtils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class ECRegisterEntryDTOTest {
    @Test
    public void testAccesors_shouldAccessProperField() {
        PojoTestUtils.validateAccessors(ECRegisterEntryDTO.class);
    }

    @Test
    public void testEqualsAndHashCode() {
        EqualsVerifier.forClass(ECRegisterEntryDTO.class).suppress(Warning.NONFINAL_FIELDS).verify();
    }

    @Test
    public void testToStringAndSetters() {
        String headOfHousehold = "shawpan";
        String village = "gangpapur";
        ECRegisterEntryDTO ecRegisterEntryDTO = new ECRegisterEntryDTO()
                .withRegistrationDate("")
                .withECNumber("")
                .withWifeName("")
                .withHusbandName("")
                .withHouseholdAddress("")
                .withHouseholdNumber("")
                .withHeadOfHousehold(headOfHousehold)
                .withVillage(village)
                .withSubCenter("")
                .withPHC("")
                .withWifeAge("")
                .withWifeEducationLevel("")
                .withHusbandAge("")
                .withHusbandEducationLevel("")
                .withCaste("")
                .withReligion("")
                .withEconomicStatus("")
                .withGravida("")
                .withParity("")
                .withNumberOfAbortions("")
                .withNumberOfLivingChildren("")
                .withNumberOfLivingFemaleChildren("")
                .withNumberOfLivingMaleChildren("")
                .withNumberOfStillBirths("")
                .withYoungestChildAge("")
                .withCurrentFPMethod("")
                .withCurrentFPMethodStartDate("")
                .withPregnancyStatus("");

        assertTrue(ecRegisterEntryDTO.toString().contains("village=gangpapur"));
        assertFalse(ecRegisterEntryDTO.toString().contains("wifeAge=18"));
    }
}
