package org.opensrp.form.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;

public class FormFieldTest {
	
    @Test
    public void shouldTestEqualsAndHash(){
        EqualsVerifier.forClass(FormField.class)
            .suppress(Warning.STRICT_INHERITANCE)
            .verify();
    }

}
