package org.opensrp.scheduler;

import com.openpojo.reflection.filters.FilterPackageInfo;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.rule.impl.GetterMustExistRule;
import com.openpojo.validation.rule.impl.SetterMustExistRule;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.joda.time.DateTime;
import org.junit.Test;
import org.opensrp.dto.AlertStatus;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;


public class AlertTest {

    @Test
    public void testEqualityAndHashCode() {
        EqualsVerifier.forClass(Alert.class).withIgnoredFields("timeStamp", "revision")
                .suppress(Warning.NONFINAL_FIELDS).verify();
    }

    @Test
    public void testConstructorGetterAndSetter() {
        Validator validator = ValidatorBuilder.create()
                // Add Rules to validate structure for POJO_PACKAGE
                // See com.openpojo.validation.rule.impl for more ...
                .with(new GetterMustExistRule())
                .with(new SetterMustExistRule())
                // Add Testers to validate behaviour for POJO_PACKAGE
                // See com.openpojo.validation.test.impl for more ...
                .with(new SetterTester())
                .with(new GetterTester())
                .build();

        validator.validate(PojoClassFactory.getPojoClass(Alert.class));
    }

    @Test(expected = IllegalStateException.class)
    public void testExceptionMarkAsClosedWithClosedAlert() {
        Alert alert = new Alert("pr", "en", "ben",
                Alert.AlertType.notification, Alert.TriggerType.event, "", "",
                new DateTime(1l), new DateTime(2l), AlertStatus.closed,
                new HashMap<String, String>());

        alert.markAlertAsClosed("reason");
    }

    @Test(expected = IllegalStateException.class)
    public void testExceptionMarkAsClosedWithCompletedAlert() {
        Alert alert = new Alert("pr", "en", "ben",
                Alert.AlertType.notification, Alert.TriggerType.event, "", "",
                new DateTime(1l), new DateTime(2l), AlertStatus.complete,
                new HashMap<String, String>());

        alert.markAlertAsClosed("reason");
    }

    @Test
    public void testMarkAsClosed() {
        Alert alert = new Alert("pr", "en", "ben",
                Alert.AlertType.notification, Alert.TriggerType.event, "", "",
                new DateTime(1l), new DateTime(2l), AlertStatus.normal,
                new HashMap<String, String>());

        Alert spyAlert = spy(alert);
        when(spyAlert.getCurrentDateTime()).thenReturn(new DateTime(1l));
        spyAlert.markAlertAsClosed("reason");

        assertEquals("reason", spyAlert.reasonClosed());
        assertEquals(AlertStatus.normal.name(), spyAlert.closingPeriod());
        assertEquals(AlertStatus.closed.name(), spyAlert.alertStatus());
        assertEquals(new DateTime(1l).toLocalDate().toString(), spyAlert.dateClosed());
        assertFalse(spyAlert.isActive());
    }

    @Test(expected = IllegalStateException.class)
    public void testExceptionMarkAsCompletedWithCompletedAlert() {
        Alert alert = new Alert("pr", "en", "ben",
                Alert.AlertType.notification, Alert.TriggerType.event, "", "",
                new DateTime(1l), new DateTime(2l), AlertStatus.complete,
                new HashMap<String, String>());

        alert.markAlertAsComplete("reason");
    }

    @Test(expected = IllegalStateException.class)
    public void testExceptionMarkAsCompletedWithClosedAlert() {
        Alert alert = new Alert("pr", "en", "ben",
                Alert.AlertType.notification, Alert.TriggerType.event, "", "",
                new DateTime(1l), new DateTime(2l), AlertStatus.closed,
                new HashMap<String, String>());

        alert.markAlertAsComplete("reason");
    }

    @Test
    public void testMarkAsCompleted() {
        Alert alert = new Alert("pr", "en", "ben",
                Alert.AlertType.notification, Alert.TriggerType.event, "", "",
                new DateTime(1l), new DateTime(2l), AlertStatus.normal,
                new HashMap<String, String>());


        Alert spyAlert = spy(alert);
        when(spyAlert.getCurrentDateTime()).thenReturn(new DateTime(1l));
        spyAlert.markAlertAsComplete("date");

        assertEquals("date", spyAlert.getDateComplete());
        assertEquals( AlertStatus.normal.name(), spyAlert.closingPeriod());
        assertEquals(AlertStatus.complete.name(), spyAlert.alertStatus() );
        assertEquals(new DateTime(1l).toLocalDate().toString(), spyAlert.dateClosed());
        assertFalse(spyAlert.isActive());
    }

}
