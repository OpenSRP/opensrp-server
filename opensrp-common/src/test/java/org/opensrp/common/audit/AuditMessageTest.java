package org.opensrp.common.audit;


import nl.jqno.equalsverifier.EqualsVerifier;
import org.joda.time.DateTime;
import org.junit.Test;
import org.opensrp.common.util.ComparableTester;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;

public class AuditMessageTest {

    @Test
    public void constructorTest() {
        DateTime dateTime = new DateTime(2020,12,12,12,12,12);
        long messageIndex = 200l;
        AuditMessageType messageType = AuditMessageType.NORMAL;
        Map<String, String> extraData = new HashMap<>();
        extraData.put("a", "1");
        extraData.put("b", "2");

        AuditMessage auditMessage = new AuditMessage(dateTime, messageIndex, messageType, extraData);

        assertEquals(dateTime, auditMessage.time());
        assertEquals(messageIndex, auditMessage.index());
        assertEquals(messageType, auditMessage.type());
        assertEquals(extraData, auditMessage.data());
    }

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(AuditMessage.class)
                .usingGetClass()
                .withOnlyTheseFields("messageIndex")
                .verify();
    }

    @Test
    public void testComparisonReversal() {
        AuditMessage one = new AuditMessage(null, 123l, null, null);
        AuditMessage sameAsOne = new AuditMessage(null, 123l, null, null);
        AuditMessage different = new AuditMessage(null, 321l, null, null);

        ComparableTester.assertComparisonReversal(one, sameAsOne);
        ComparableTester.assertComparisonReversal(sameAsOne, different);
        ComparableTester.assertComparisonReversal(one, different);
    }

    @Test
    public void testComparisonConsistencyWithEqual() {
        AuditMessage one = new AuditMessage(null, 123l, null, null);
        AuditMessage sameAsOne = new AuditMessage(null, 123l, null, null);

        ComparableTester.assertConsistencyWithEqual(one, sameAsOne);
    }

    @Test(expected = NullPointerException.class)
    public void testComparisonNullPointerException(){
        AuditMessage one = new AuditMessage(null, 123l, null, null);
        ComparableTester.assertNullPointerException(one);
    }

    @Test
    public void testComparisonTransitivity() {
        AuditMessage one = new AuditMessage(null, 123l, null, null);
        AuditMessage two = new AuditMessage(null, 300l, null, null);
        AuditMessage third = new AuditMessage(null, 321l, null, null);

        ComparableTester.assertTransitivity(one, two, third);
    }

    @Test
    public void testComparisonConsistency() {
        AuditMessage one = new AuditMessage(null, 123l, null, null);
        AuditMessage sameAsOne = new AuditMessage(null, 123l, null, null);
        AuditMessage different = new AuditMessage(null, 321l, null, null);

        ComparableTester.assertConsistency(one, sameAsOne, different);
    }
}
