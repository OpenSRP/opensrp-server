package org.opensrp.dto.register;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;
import org.opensrp.dto.LocationDTO;
import org.opensrp.dto.utils.PojoTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by real on 13/07/17.
 */
public class ANMDetailsDTOTest {
    @Test
    public void testAccesors_shouldAccessProperField() {
        PojoTestUtils.validateAccessors(ANMDetailsDTO.class);
    }

    @Test
    public void testEqualsAndHashCode() {
        EqualsVerifier.forClass(ANMDetailsDTO.class).suppress(Warning.NONFINAL_FIELDS).verify();
    }

    @Test
    public void testConstructorAndToString() {
        List<ANMDetailDTO> anmDetailList = new ArrayList<>();
        LocationDTO location = new LocationDTO("nandanpur", "", "", "", "");
        ANMDetailDTO anmDetailDTO = new ANMDetailDTO("", "real", location, 1, 2, 3, 4, 5);
        anmDetailList.add(anmDetailDTO);
        ANMDetailsDTO anmDetailsDTO = new ANMDetailsDTO(anmDetailList);

        assertTrue(anmDetailsDTO.toString().contains("name=real"));
        assertTrue(anmDetailsDTO.toString().contains("sub_center=nandanpur"));
        assertFalse(anmDetailsDTO.toString().contains("district= luxmipur"));
    }
}
