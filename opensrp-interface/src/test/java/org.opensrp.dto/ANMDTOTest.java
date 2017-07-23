package org.opensrp.dto;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;
import org.opensrp.dto.utils.PojoTestUtils;

import static org.junit.Assert.*;

public class ANMDTOTest {
    @Test
    public void testAccesors_shouldAccessProperField() {
        PojoTestUtils.validateAccessors(ANMDTO.class);
    }

    @Test
    public void testEqualsAndHashCode() {
        EqualsVerifier.forClass(ANMDTO.class).suppress(Warning.NONFINAL_FIELDS).verify();
    }

    @Test
    public void testToString() {
        String sub_center = "nandanpur";
        String district = "luxmipur";
        LocationDTO locationDTO = new LocationDTO(sub_center, "", "", district, "");
        String name = "1971";
        ANMDTO anmdto = new ANMDTO("", name, locationDTO);

        assertEquals("1971", anmdto.getName());
        assertNotSame("xyz", anmdto.getIdentifier());

        assertTrue(anmdto.getLocation().toString().contains("sub_center=nandanpur"));
        assertFalse(anmdto.toString().contains("district=dhaka"));
    }
}
