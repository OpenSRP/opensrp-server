package org.opensrp.dto.form;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;
import org.opensrp.dto.utils.PojoTestUtils;

import static org.junit.Assert.*;


/**
 * Created by real on 13/07/17.
 */
public class MultimediaDTOTest {
    @Test
    public void testAccesors_shouldAccessProperField() {
        PojoTestUtils.validateAccessors(MultimediaDTO.class);
    }

    @Test
    public void testEqualsAndHashCode() {
        EqualsVerifier.forClass(MultimediaDTO.class).suppress(Warning.NONFINAL_FIELDS).verify();
    }

    @Test
    public void testWithFilePath_Constructor_toString() {
        MultimediaDTO multimediaDTO = new MultimediaDTO("786", "", "", "", "");
        multimediaDTO.withFilePath("desktop");
        assertEquals("desktop", multimediaDTO.getFilePath());
        assertNotSame("home", multimediaDTO.getFilePath());

        assertTrue(multimediaDTO.toString().contains("caseId=786"));
        assertFalse(multimediaDTO.toString().contains("providerId=222"));
    }
}
