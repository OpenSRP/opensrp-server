package org.opensrp.form.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class SubformMapTest {
    @Test
    public void shouldGetFieldValueAndGetField(){
        String entityId = "entityId";
        String subformName ="woman_registration";
        String bindType = "woman";
        String defaultBindPath = "/model/instance/CensusNewMemberRegistration/HH_Member";
        Map<String, String> formAttributes =new HashMap<>();
        List<FormFieldMap> fields = new ArrayList<>();
        formAttributes.put("atr", "atrValue");
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
        fields.add(FormFieldMap);		
        SubformMap subformMap = new SubformMap(entityId, subformName, bindType, defaultBindPath, formAttributes, fields);
        assertEquals(value, subformMap.getFieldValue("provider_town"));
        assertNull(subformMap.getFieldValue(null));
        assertNull(subformMap.getField(null));		
    }

}
