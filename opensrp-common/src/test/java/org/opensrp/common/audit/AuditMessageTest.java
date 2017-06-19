package org.opensrp.common.audit;


import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

public class AuditMessageTest {

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(AuditMessage.class)
                .usingGetClass()
                .withOnlyTheseFields("messageIndex")
                .verify();
    }
}
