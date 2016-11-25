package org.opensrp.form.service;

import ch.lambdaj.function.convert.Converter;



import org.opensrp.common.util.DateUtil;
import org.opensrp.dto.form.FormSubmissionDTO;
import org.opensrp.form.domain.FormSubmission;
import org.opensrp.form.repository.AllFormSubmissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.opensrp.form.domain.*;

import java.util.Comparator;
import java.util.List;

import static ch.lambdaj.collection.LambdaCollections.with;
import static java.text.MessageFormat.format;
import static java.util.Collections.sort;

@Service
public class FormSubmissionService {
    private static Logger logger = LoggerFactory.getLogger(FormSubmissionService.class.toString());
    private AllFormSubmissions allFormSubmissions;
    @Autowired
    public FormSubmissionService(AllFormSubmissions allFormSubmissions) {
        this.allFormSubmissions = allFormSubmissions;
    }

    public List<FormSubmissionDTO> fetch(long formFetchToken) {
        return with(allFormSubmissions.findByServerVersion(formFetchToken)).convert(new Converter<FormSubmission, FormSubmissionDTO>() {
            @Override
            public FormSubmissionDTO convert(FormSubmission submission) {
                return FormSubmissionConverter.from(submission);
            }
        });
    }
    
    public FormSubmission findByInstanceId(String instanceId) {
        return allFormSubmissions.findByInstanceId(instanceId);
    }
    
    public List<FormSubmission> getNewSubmissionsForANM(String anmIdentifier, Long version, Integer batchSize) {
        return allFormSubmissions.findByANMIDAndServerVersion(anmIdentifier, version, batchSize);
    }

    public List<FormSubmission> getAllSubmissions(Long version, Integer batchSize) {
        return allFormSubmissions.allFormSubmissions(version, batchSize);
    }

   /* public List<org.opensrp.form.domain.Drug> getAllDrugObject(String name) {
        return allDrugs.findAllByName(name);
    }*/
    public List<FormSubmission> findByFormName(String formName, long version) {
    	return allFormSubmissions.findByFormName(formName, version);
    }
    
    public List<FormSubmission> findByMetadata(String key, Object value) {
    	return allFormSubmissions.findByMetadata(key, value);
    }
    
    public void submit(List<FormSubmissionDTO> formSubmissionsDTO) {
        List<FormSubmission> formSubmissions = with(formSubmissionsDTO).convert(new Converter<FormSubmissionDTO, FormSubmission>() {
            @Override
            public FormSubmission convert(FormSubmissionDTO submission) {
                return FormSubmissionConverter.toFormSubmission(submission);
            }
        });

        sort(formSubmissions, timeStampComparator());
        for (FormSubmission submission : formSubmissions) {
            if (allFormSubmissions.exists(submission.instanceId())) {
                logger.warn(format("Received form submission that already exists. Skipping. Submission: {0}", submission));
                continue;
            }
            logger.info(format("Saving form {0} with instance Id: {1} and for entity Id: {2}",
                    submission.formName(), submission.instanceId(), submission.entityId()));
            submission.setServerVersion(DateUtil.millis());
            allFormSubmissions.add(submission);
        }
    }

    private Comparator<FormSubmission> timeStampComparator() {
        return new Comparator<FormSubmission>() {
            public int compare(FormSubmission firstSubmission, FormSubmission secondSubmission) {
                long firstTimestamp = firstSubmission.clientVersion();
                long secondTimestamp = secondSubmission.clientVersion();
                return firstTimestamp == secondTimestamp ? 0 : firstTimestamp < secondTimestamp ? -1 : 1;
            }
        };
    }
}
