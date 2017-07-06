package org.opensrp.form.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class FormFieldMapTest {

    @Test
    public void shouldGetFieldAttributesAndGetValuesCodesAndValueAndGetter(){
        String name = "provider_town";
        String value = "ProviderTown";
        String source = "pkchild.provider_town";
        String bindPath = "/model/instance/CensusNewMemberRegistration";
        String type = "FormSubmission";
        Map<String, String> attributes = new HashMap<>();
        attributes.put("atr1", "atrValue1");
        attributes.put("atr2", "atrValue2");
        Map<String, String> valueCodes = new HashMap<>();
        valueCodes.put("valueCode1", "valueCode11");
        valueCodes.put("valueCode2", "valueCode12");
        FormFieldMap FormFieldMap = new FormFieldMap(name, value, source, bindPath, type, attributes, valueCodes);
        assertEquals(attributes,FormFieldMap.getFieldAttributes());
        assertNotNull(FormFieldMap.getValuesCodes());		
        assertEquals(type, FormFieldMap.type());		
        assertEquals(source, FormFieldMap.getSource());
        assertEquals(bindPath, FormFieldMap.getBindPath());
        assertEquals(source, FormFieldMap.source());
        assertEquals(bindPath, FormFieldMap.bindPath());		
        assertEquals(valueCodes, FormFieldMap.valueCodes("ProviderTown"));
        assertEquals("ProviderTown", FormFieldMap.value());
    }
}
