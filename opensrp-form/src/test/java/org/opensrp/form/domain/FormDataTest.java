package org.opensrp.form.domain;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Test
    public void shouldTestConstructor(){		
        assertEquals(getForm().bindType(),"pkchild");
        assertNotSame(getForm().bindType(),"pkchilds");		
    }
	
    @Test
    public void shouldGetSubFormByName(){		
        assertEquals("woman_registration", getForm().getSubFormByName("woman_registration").name());
        assertNotSame("woman_registrations", getForm().getSubFormByName("woman_registration").name());		
    }
    public FormData getForm(){
        String bind_type = "pkchild";
        String default_bind_path = "/model/instance/Child_Vaccination_Enrollment/";
        List<FormField> fields = new ArrayList<>();
        FormField formField1 = new FormField("provider_town","pktown","pkchild.provider_town");
        FormField formField2 = new FormField("provider_city","providerCity","pkchild.provider_city");
        fields.add(formField1);
        fields.add(formField2);
        List<SubFormData> sub_forms = new ArrayList<>();
        List<Map<String, String>> instances = new ArrayList<>();		
        Map<String, String> map = new HashMap<>();
        map.put("provider_town", "pktown");
        map.put("provider_city", "providerCity");
        instances.add(map);		
        SubFormData subFormData = new SubFormData("woman_registration", instances);
        sub_forms.add(subFormData);
        FormData formData2 = new FormData(bind_type,default_bind_path,fields,sub_forms);
        return formData2;		
    }
    @Test(expected=RuntimeException.class)
    public void shouldGetRunTimeExceptionBygetSubFormByName(){
        String bind_type = "pkchild";
        String default_bind_path = "/model/instance/Child_Vaccination_Enrollment/";
        List<FormField> fields = new ArrayList<>();
        FormField formField1 = new FormField("provider_town","pktown","pkchild.provider_town");
        FormField formField2 = new FormField("provider_city","providerCity","pkchild.provider_city");
        fields.add(formField1);
        fields.add(formField2);
        List<SubFormData> sub_forms = new ArrayList<>();
        List<Map<String, String>> instances = new ArrayList<>();
        SubFormData subFormData = new SubFormData("woman_registration", instances);		
        FormData formData2 = new FormData(bind_type,default_bind_path,fields,sub_forms);	
        assertEquals("woman_registration", formData2.getSubFormByName("woman_registration").name());
        assertNotSame("woman_registrations", formData2.getSubFormByName("woman_registration").name());		
    }
    @Test
    public void shouldGetFieldsAsMap(){		
        assertEquals("providerCity", getForm().getFieldsAsMap().get("provider_city"));
        assertNotSame("providerCity", getForm().getFieldsAsMap().get("provider_cityu"));		
    }

}
