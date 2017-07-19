package org.opensrp.dto.register;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;
import org.opensrp.dto.utils.PojoTestUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class IUDRegisterEntryDTOTest {
    @Test
    public void testAccesors_shouldAccessProperField() {
        PojoTestUtils.validateAccessors(IUDRegisterEntryDTO.class);
    }

    @Test
    public void testEqualsAndHashCode() {
        EqualsVerifier.forClass(IUDRegisterEntryDTO.class).suppress(Warning.NONFINAL_FIELDS).verify();
    }

    @Test
    public void testToStringAndSetters() {
        String wifeAge = "16";
        String husbandName = "sajid";
        String husbandAge = "24";
        String fpAcceptanceDate = "2017-07-19";
        IUDFPDetailsDTO iudfpDetailsDTO = new IUDFPDetailsDTO(fpAcceptanceDate, "", "", "");

        IUDRegisterEntryDTO iudRegisterEntryDTO = new IUDRegisterEntryDTO()
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
                .withLmpDate("")
                .withUptResult("")
                .withFpDetails(iudfpDetailsDTO);

        assertEquals("sajid", iudRegisterEntryDTO.getHusbandName());
        assertTrue(iudRegisterEntryDTO.toString().contains(wifeAge));
        assertFalse(iudRegisterEntryDTO.toString().contains("lmpDate=2017-08-19"));
    }

}
