package org.opensrp.form.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Before;
import org.junit.Test;

public class FormDataTest {
	
	@Before
    public void setUp() throws Exception {
        
    }
	
	@Test
	public void shouldTestEqualsAndHash(){
		EqualsVerifier.forClass(FormData.class)
		.suppress(Warning.STRICT_INHERITANCE)
		.verify();
		
	}
	

}
