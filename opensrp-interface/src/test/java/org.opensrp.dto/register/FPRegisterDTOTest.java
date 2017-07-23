package org.opensrp.dto.register;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;
import org.opensrp.dto.utils.PojoTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FPRegisterDTOTest {
    @Test
    public void testAccesors_shouldAccessProperField() {
        PojoTestUtils.validateAccessors(FPRegisterDTO.class);
    }

    @Test
    public void testEqualsAndHashCode() {
        EqualsVerifier.forClass(FPRegisterDTO.class).suppress(Warning.NONFINAL_FIELDS).verify();
    }

    @Test
    public void testToStringAndSetters() {
        List<IUDRegisterEntryDTO> iudRegisterEntriesList = new ArrayList<>();
        String husbandName = "sajid";
        IUDRegisterEntryDTO iudRegisterEntryDTO = new IUDRegisterEntryDTO().withHusbandName(husbandName);
        iudRegisterEntriesList.add(iudRegisterEntryDTO);

        List<CondomRegisterEntryDTO> condomRegisterEntriesList = new ArrayList<>();
        List<OCPRegisterEntryDTO> ocpRegisterEntriesList = new ArrayList<>();
        List<MaleSterilizationRegisterEntryDTO> maleSterilizationRegisterEntriesList = new ArrayList<>();
        List<FemaleSterilizationRegisterEntryDTO> femaleSterilizationRegisterEntriesList = new ArrayList<>();

        Integer reportingYear = 2017;
        FPRegisterDTO fpRegisterDTO = new FPRegisterDTO(iudRegisterEntriesList, condomRegisterEntriesList,
                ocpRegisterEntriesList, maleSterilizationRegisterEntriesList, femaleSterilizationRegisterEntriesList, reportingYear);

        assertEquals((Integer) 2017, fpRegisterDTO.getReportingYear());
        assertTrue(fpRegisterDTO.toString().contains(husbandName));
        assertFalse(fpRegisterDTO.toString().contains("lmpDate=2017-08-19"));
    }
}
