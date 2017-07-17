package org.opensrp.common.audit;


import nl.jqno.equalsverifier.EqualsVerifier;
import org.joda.time.DateTime;
import org.junit.Test;
import org.opensrp.common.util.ComparableTester;
import org.opensrp.common.util.ComparableVerifier;

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
    public void testComparableContract() {
        AuditMessage one = new AuditMessage(null, 123l, null, null);
        AuditMessage two = new AuditMessage(null, 300l, null, null);
        AuditMessage third = new AuditMessage(null, 321l, null, null);

        new ComparableVerifier<>(AuditMessage.class, one, two, third).verify();

    }

}
