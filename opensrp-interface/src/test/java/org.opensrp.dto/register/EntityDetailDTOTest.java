package org.opensrp.dto.register;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;
import org.opensrp.dto.utils.PojoTestUtils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EntityDetailDTOTest {
    @Test
    public void testAccesors_shouldAccessProperField() {
        PojoTestUtils.validateAccessors(EntityDetailDTO.class);
    }

    @Test
    public void testEqualsAndHashCode() {
        EqualsVerifier.forClass(EntityDetailDTO.class).suppress(Warning.NONFINAL_FIELDS).verify();
    }

    @Test
    public void testToStringAndSetters() {
        String entityId = "x2";
        String type = "private";
        final String anmIdentifier = "";
        EntityDetailDTO entityDetailDTO = new EntityDetailDTO()
                .withEntityID(entityId)
                .withEntityType(type)
                .withThayiCardNumber("")
                .withECNumber("")
                .withANMIdentifier(anmIdentifier);
        assertTrue(entityDetailDTO.toString().contains(entityId));
        assertFalse(entityDetailDTO.toString().contains("anmIdentifier=boss"));
    }
}
