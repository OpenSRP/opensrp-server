package org.opensrp.common.domain;


import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;
import org.opensrp.common.audit.AuditMessage;

import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class MonthSummeryTest {

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(MonthSummary.class)
                .verify();
    }
}
