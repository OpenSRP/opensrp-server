package org.opensrp.form.domain;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class FormSubmissionTest {

    @Test
    public void shouldGetFields(){
        FormDataTest formDataTest = new FormDataTest();
        FormData formData = formDataTest.getForm();
        FormInstance formInstance = new FormInstance(formData, "1");
        List<String> fieldNames = new ArrayList<>();
        fieldNames.add("provider_city");
        fieldNames.add("provider_town");		
        FormSubmission formSubmission = new FormSubmission("ANM1", "", "pkchild", "", "1", 0l, formInstance);
        assertEquals("providerCity", formSubmission.getFields(fieldNames).get("provider_city"));
        assertNotEquals("providerCity", formSubmission.getFields(fieldNames).get("provider_citi"));
    }
	
    @Test
    public void shouldGetSubFormByName(){
        FormDataTest formDataTest = new FormDataTest();
        FormData formData = formDataTest.getForm();
        FormInstance formInstance = new FormInstance(formData, "1");		
        List<String> fieldNames = new ArrayList<>();
        fieldNames.add("provider_city");
        fieldNames.add("provider_town");		
        FormSubmission formSubmission = new FormSubmission("ANM1", "", "pkchild", "", "1", 0l, formInstance);
        assertEquals("woman_registration", formSubmission.getSubFormByName("woman_registration").name());
        assertNotSame("woman_registrationw", formSubmission.getSubFormByName("woman_registration").name());
    }	
	
	
    @Test(expected=RuntimeException.class)
    public void shouldGetRuntimeExceptionWhenGetSubFormByName() throws Exception{
        FormDataTest formDataTest = new FormDataTest();
        FormData formData = formDataTest.getForm();
        FormInstance formInstance = new FormInstance(formData, "1");		
        List<String> fieldNames = new ArrayList<>();
        fieldNames.add("provider_city");
        fieldNames.add("provider_town");		
        FormSubmission formSubmission = new FormSubmission("ANM1", "", "pkchild", "", "1", 0l, formInstance);
        formSubmission.getSubFormByName("woman_registrations");		
    }
	
    @Test
    public void shouldAddMetadataAndGetMetadata(){
        FormDataTest formDataTest = new FormDataTest();
        FormData formData = formDataTest.getForm();
        FormInstance formInstance = new FormInstance(formData, "1");		
        List<String> fieldNames = new ArrayList<>();
        fieldNames.add("provider_city");
        fieldNames.add("provider_town");		
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("meta", "Meta");		
        FormSubmission formSubmission = new FormSubmission("ANM1", "", "pkchild", "", "1", 0l, formInstance);
        formSubmission.addMetadata("meta", "Meta");			
        assertEquals(metadata, formSubmission.getMetadata());
        assertEquals("Meta", formSubmission.getMetadata("meta"));
        Map<String, Object> metadata1 = new HashMap<>();
        metadata.put("meta1", "Meta");
        assertNotEquals("Meta1", formSubmission.getMetadata("meta"));
        assertNotEquals(metadata1, formSubmission.getMetadata());
        FormSubmission formSubmission1 = new FormSubmission("ANM1", "", "pkchild", "", "1", 0l, formInstance);
        assertNull(formSubmission1.getMetadata("meta"));		
    }
	
}
