package org.opensrp.form.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.opensrp.dto.form.FormSubmissionDTO;

public class FormSubmissionConverterTest {
	
    @Test
    public void shouldToFormSubmission(){
        String anmId ="ANM1";
        String instanceId = "instanceId";
        String entityId = "entityId";
        String formName = "woman_registration";
        String formInstance = null;
        String clientVersion = "09999";
        String formDataDefinitionVersion = "1";
        FormSubmissionDTO formSubmissionDTO = new FormSubmissionDTO(anmId, instanceId, entityId, formName, formInstance, clientVersion, formDataDefinitionVersion);
        formSubmissionDTO.withServerVersion("334545");		
        FormSubmissionConverter.toFormSubmissionWithVersion(formSubmissionDTO);
        assertEquals(anmId, FormSubmissionConverter.toFormSubmission(formSubmissionDTO).anmId());
    }
	
    @Test(expected=Exception.class)
    public void shouldGetExceptionToFormSubmission(){
        String anmId ="ANM1";
        String instanceId = "instanceId";
        String entityId = "entityId";
        String formName = "woman_registration";
        String formInstance = "formInstance";
        String clientVersion = "09999";
        String formDataDefinitionVersion = "1";
        FormSubmissionDTO formSubmissionDTO = new FormSubmissionDTO(anmId, instanceId, entityId, formName, formInstance, clientVersion, formDataDefinitionVersion);
        FormSubmissionConverter.toFormSubmission(formSubmissionDTO) ;		
    }

}
