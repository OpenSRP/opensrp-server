package org.opensrp.form.domain;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;

public class FormInstanceTest {
    @Test
    public void shouldTestEqualsAndHash(){
        EqualsVerifier.forClass(FormInstance.class)
            .suppress(Warning.STRICT_INHERITANCE)
            .verify();
    }
    @Test
    public void shouldTestCOnstructorAndGetSubFormByName(){
        FormDataTest formDataTest = new FormDataTest();
        FormData formData = formDataTest.getForm();
        FormInstance formInstance = new FormInstance(formData, "1");
        FormInstance formInstance1 = new FormInstance(formData);
        formInstance1.toString();
        assertNotNull(formInstance1);
        assertEquals("woman_registration",formInstance.getSubFormByName("woman_registration").name());
        assertNotSame("woman_registrations",formInstance.getSubFormByName("woman_registration").name());
       	
    }
	
    @Test(expected=RuntimeException.class)
    public void shouldGetRuntimeExcemptionTestCOnstructorAndGetSubFormByName(){
        FormDataTest formDataTest = new FormDataTest();
        FormData formData = formDataTest.getForm();
        FormInstance formInstance = new FormInstance(formData, "1");
        FormInstance formInstance1 = new FormInstance(formData);
        formInstance1.toString();
        formInstance.getSubFormByName("woman_registrations").name();		
    }

}
