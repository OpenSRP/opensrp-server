package org.opensrp.dto.form;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;
import org.opensrp.dto.utils.PojoTestUtils;

import static org.junit.Assert.*;

/**
 * Created by real on 13/07/17.
 */
public class FormSubmissionDTOTest {

    @Test
    public void testAccesors_shouldAccessProperField() {
        PojoTestUtils.validateAccessors(FormSubmissionDTO.class);
    }

    @Test
    public void testEqualsAndHashCode() {
        EqualsVerifier.forClass(FormSubmissionDTO.class).suppress(Warning.NONFINAL_FIELDS).verify();
    }

    @Test
    public void testWithServerVersion() {
        FormSubmissionDTO formSubmissionDTO = new FormSubmissionDTO("anmId", "hi", "", "", "", "", "");
        formSubmissionDTO.withServerVersion("version");
        assertEquals("version", formSubmissionDTO.getServerVersion());
        assertNotSame("null ", formSubmissionDTO.getServerVersion());

        assertTrue(formSubmissionDTO.toString().contains("anmId=anmId"));
        assertFalse(formSubmissionDTO.toString().contains("instanceId=instanceId"));
    }
}

