package org.opensrp.dto;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;
import org.opensrp.dto.register.PNCVisitDTO;
import org.opensrp.dto.utils.PojoTestUtils;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ActionTest {
    @Test
    public void testAccesors_shouldAccessProperField() {
        PojoTestUtils.validateAccessors(Action.class);
    }

    @Test
    public void testEqualsAndHashCode() {
        EqualsVerifier.forClass(Action.class).suppress(Warning.NONFINAL_FIELDS).verify();
    }

    @Test
    public void testToStringAndConstructor() {
        String caseID = "AAA7";
        String actionTarget = "";
        String actionType = "";
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("1", "data1");
        dataMap.put("2", "data2");
        String timeStamp = "";
        Boolean isActionActive = true;
        Map<String, String> detailsMapd = new HashMap<>();
        Action action = new Action(caseID, actionTarget, actionType, dataMap, timeStamp, isActionActive, detailsMapd);
        assertEquals("data2", action.get("2"));
        assertTrue(action.toString().contains(caseID));
        assertFalse(action.toString().contains("80%"));
    }
}
