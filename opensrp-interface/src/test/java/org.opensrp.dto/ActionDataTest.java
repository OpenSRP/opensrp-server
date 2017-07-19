package org.opensrp.dto;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.opensrp.dto.utils.PojoTestUtils;

import java.util.HashMap;
import java.util.Map;

public class ActionDataTest {
    @Test
    public void testAccesors_shouldAccessProperField() {
        PojoTestUtils.validateAccessors(ActionData.class);
    }

    @Test
    public void testEqualsAndHashCode() {
        EqualsVerifier.forClass(ActionData.class).suppress(Warning.NONFINAL_FIELDS).verify();
    }
}
