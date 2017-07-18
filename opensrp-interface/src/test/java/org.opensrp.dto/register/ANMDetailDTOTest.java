package org.opensrp.dto.register;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;
import org.opensrp.dto.LocationDTO;
import org.opensrp.dto.utils.PojoTestUtils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by real on 13/07/17.
 */
public class ANMDetailDTOTest {
    @Test
    public void testAccesors_shouldAccessProperField() {
        PojoTestUtils.validateAccessors(ANMDetailDTO.class);
    }

    @Test
    public void testEqualsAndHashCode() {
        EqualsVerifier.forClass(ANMDetailDTO.class).suppress(Warning.NONFINAL_FIELDS).verify();
    }

    @Test
    public void testToString() {
        LocationDTO location = new LocationDTO("nandanpur", "", "", "", "");
        ANMDetailDTO anmDetailDTO = new ANMDetailDTO("", "real", location, 1, 2, 3, 4, 5);
        assertTrue(anmDetailDTO.toString().contains("name=real"));
        assertFalse(anmDetailDTO.toString().contains("identifier= 123"));
    }
}
