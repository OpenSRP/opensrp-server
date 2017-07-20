package org.opensrp.dto;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import org.opensrp.dto.utils.PojoTestUtils;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ActionDataTest {
    @Test
    public void testAccesors_shouldAccessProperField() {
        PojoTestUtils.validateAccessors(ActionData.class);
    }

    @Test
    public void testEqualsAndHashCode() {
        EqualsVerifier.forClass(ActionData.class).suppress(Warning.NONFINAL_FIELDS).verify();
    }

    @Test
    public void testCreateAlert() {
        String dateString = "2017-07-20";
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        DateTime dt = formatter.parseDateTime(dateString);
        AlertStatus alertStatus = AlertStatus.closed;
        DateTime startDate = dt;
        DateTime expiryDate = dt;

        ActionData actionData = ActionData.createAlert(BeneficiaryType.mother.toString(), ActionData.SCHEDULE_NAME,
                ActionData.VISIT_CODE, alertStatus, startDate, expiryDate);
        assertEquals("alert", actionData.getTarget());
        assertEquals("createAlert", actionData.getType());
        assertNotSame("xyz", actionData.getTarget());

        assertEquals("mother", actionData.getData().get(ActionData.BENEFICIARY_TYPE));
        assertEquals("2017-07-20", actionData.getData().get(ActionData.START_DATE));
        assertNotSame(new DateTime().toLocalDate().toString(), actionData.getData().get(ActionData.START_DATE));

        assertTrue(actionData.toString().contains("alert"));
        assertFalse(actionData.toString().contains("Hooooooooo"));
    }

    @Test
    public void testMarkAlertAsClosed() {
        ActionData actionData = ActionData.markAlertAsClosed(ActionData.VISIT_CODE, ActionData.COMPLETION_DATE);
        assertEquals("closeAlert", actionData.getType());
        assertNotSame("createAlert", actionData.getType());

        assertEquals("visitCode", actionData.getData().get(ActionData.VISIT_CODE));
        assertEquals("completionDate", actionData.getData().get(ActionData.COMPLETION_DATE));
        assertNotSame("Habijabi", actionData.getData().get(ActionData.COMPLETION_DATE));
    }

    @Test
    public void testreportForIndicator() {
        ActionData actionData = ActionData.reportForIndicator("", ActionData.ANNUAL_TARGET, ActionData.MONTHLY_SUMMARIES);
        assertEquals("report", actionData.getTarget());
        assertNotSame("indicator", actionData.getType());

        assertEquals("annualTarget", actionData.getData().get(ActionData.ANNUAL_TARGET));
        assertEquals("monthlySummaries", actionData.getData().get(ActionData.MONTHLY_SUMMARIES));
        assertNotSame("Habijabi", actionData.getData().get(ActionData.ANNUAL_TARGET));
    }

    @Test
    public void testcloseBeneficiary() {
        ActionData actionData = ActionData.closeBeneficiary("", ActionData.REASON_FOR_CLOSE);
        assertEquals("close", actionData.getType());
        assertNotSame("target", actionData.getTarget());

        assertEquals("reasonForClose", actionData.getData().get(ActionData.REASON_FOR_CLOSE));
        assertNotSame("Habijabi", actionData.getData().get(ActionData.REASON_FOR_CLOSE));
    }

    @Test
    public void testFrom() {
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put(BeneficiaryType.child.toString(), "khuki");
        Map<String, String> detailsMap = new HashMap<>();
        detailsMap.put("village", "nandanpur");
        detailsMap.put("district", "luxmipur");
        String actionType = "type";
        String actionTarget = "actionTarget";
        ActionData actionData = ActionData.from(actionType, actionTarget, dataMap, detailsMap);

        assertEquals("type", actionData.getType());
        assertNotSame("target", actionData.getTarget());


        assertEquals("khuki", actionData.getData().get(BeneficiaryType.child.toString()));
        assertEquals("luxmipur", actionData.getDetails().get("district"));
        assertNotSame("gangapur", actionData.getDetails().get("village"));

    }
}
