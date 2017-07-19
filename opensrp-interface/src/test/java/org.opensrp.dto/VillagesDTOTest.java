package org.opensrp.dto;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;
import org.opensrp.dto.utils.PojoTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class VillagesDTOTest {
    @Test
    public void testAccesors_shouldAccessProperField() {
        PojoTestUtils.validateAccessors(VillagesDTO.class);
    }

    @Test
    public void testEqualsAndHashCode() {
        EqualsVerifier.forClass(VillagesDTO.class).suppress(Warning.NONFINAL_FIELDS).verify();
    }


    @Test
    public void testToString() {
        String sub_center = "nandanpur";
        String district = "luxmipur";
        List<String> villagesList = new ArrayList<>();
        VillagesDTO villagesDTO = new VillagesDTO(district, "", "", sub_center, villagesList);
        System.out.println(villagesDTO.toString());
        assertEquals("luxmipur", villagesDTO.getDistrict());
        assertNotSame("xyz", villagesDTO.getPhcIdentifier());

        assertTrue(villagesDTO.toString().contains("subCenter=nandanpur"));
        assertFalse(villagesDTO.toString().contains("district=dhaka"));
    }

}
