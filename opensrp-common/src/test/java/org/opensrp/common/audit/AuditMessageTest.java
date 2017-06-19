package org.opensrp.common.audit;


import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;
import org.opensrp.common.util.ComparableTester;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AuditMessageTest {

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
    public void testConsistencyWithEqual() {
        AuditMessage one = new AuditMessage(null, 123l, null, null);
        AuditMessage sameAsOne = new AuditMessage(null, 123l, null, null);

        ComparableTester.assertConsistencyWithEqual(one, sameAsOne);
    }

    @Test(expected = NullPointerException.class)
    public void testNullPointerException(){
        AuditMessage one = new AuditMessage(null, 123l, null, null);
        ComparableTester.assertNullPointerException(one);
    }

    @Test
    public void testTransitivity() {
        AuditMessage one = new AuditMessage(null, 123l, null, null);
        AuditMessage two = new AuditMessage(null, 300l, null, null);
        AuditMessage third = new AuditMessage(null, 321l, null, null);

        ComparableTester.assertTransitivity(one, two, third);
    }

    @Test
    public void testConsistency() {
        AuditMessage one = new AuditMessage(null, 123l, null, null);
        AuditMessage sameAsOne = new AuditMessage(null, 123l, null, null);
        AuditMessage different = new AuditMessage(null, 321l, null, null);

        ComparableTester.assertConsistency(one, sameAsOne, different);
    }
}
