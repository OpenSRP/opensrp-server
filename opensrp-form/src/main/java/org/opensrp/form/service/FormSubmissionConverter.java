package org.opensrp.form.service;

import com.google.gson.Gson;
import org.opensrp.dto.form.FormSubmissionDTO;
import org.opensrp.form.domain.FormInstance;
import org.opensrp.form.domain.FormSubmission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

import static java.lang.Long.parseLong;
import static java.lang.String.valueOf;

public class FormSubmissionConverter {
    private static Logger logger = LoggerFactory.getLogger(FormSubmissionConverter.class.toString());

    public static FormSubmissionDTO from(FormSubmission formSubmission) {
        return new FormSubmissionDTO(formSubmission.anmId(), formSubmission.instanceId(), formSubmission.entityId(), formSubmission.formName(),
                new Gson().toJson(formSubmission.instance()), valueOf(formSubmission.clientVersion()), formSubmission.formDataDefinitionVersion())
                .withServerVersion(valueOf(formSubmission.serverVersion()));
    }

    public static FormSubmission toFormSubmission(FormSubmissionDTO formSubmissionDTO) {
        try {
            FormInstance formInstance = new Gson().fromJson(formSubmissionDTO.getFormInstance(), FormInstance.class);
            return new FormSubmission(formSubmissionDTO.getAnmId(), formSubmissionDTO.getInstanceId(), formSubmissionDTO.getFormName(), formSubmissionDTO.getEntityId(),
                    formSubmissionDTO.getFormDataDefinitionVersion(), parseLong(formSubmissionDTO.getClientVersion()), formInstance);
        } catch (Exception e) {
            logger.error(MessageFormat.format("Converting Form Submission :{0}, failed with error: {1}.", formSubmissionDTO, e));
            throw e;
        }
    }

    public static FormSubmission toFormSubmissionWithVersion(FormSubmissionDTO formSubmissionDTO) {
        return new FormSubmission(formSubmissionDTO.getAnmId(), formSubmissionDTO.getInstanceId(), formSubmissionDTO.getFormName(),
                formSubmissionDTO.getEntityId(), parseLong(formSubmissionDTO.getClientVersion()), formSubmissionDTO.getFormDataDefinitionVersion(), new Gson().fromJson(formSubmissionDTO.getFormInstance(), FormInstance.class),
                parseLong(formSubmissionDTO.getServerVersion()));
    }
}
