package org.opensrp.connector.openmrs.service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.opensrp.connector.dhis2.DHIS2DatasetPush;
import org.opensrp.connector.openmrs.service.EncounterService;
import org.opensrp.connector.openmrs.service.PatientService;
import org.opensrp.form.domain.FormSubmission;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.mysql.jdbc.StringUtils;

public class TestResourceLoader {
	
	protected String openmrsOpenmrsUrl;
	
	protected String openmrsUsername;
	
	protected String openmrsPassword;
	
	protected String formDirPath;
	
	protected String dhis2Url;
	
	protected String dhis2Username;
	
	protected String dhis2Password;
	
	protected boolean pushToOpenmrsForTest;
	
	protected PatientService patientService;
	
	protected EncounterService encounterService;
	
	protected DHIS2DatasetPush dhis2DatasetPush;
	
	protected OpenmrsLocationService openmrsLocationService;
	
	public TestResourceLoader() throws IOException {
		Resource resource = new ClassPathResource("/opensrp.properties");
		Properties props = PropertiesLoaderUtils.loadProperties(resource);
		openmrsOpenmrsUrl = props.getProperty("openmrs.url");
		openmrsUsername = props.getProperty("openmrs.username");
		openmrsPassword = props.getProperty("openmrs.password");
		formDirPath = props.getProperty("form.directory.name");
		
		dhis2Url = props.getProperty("dhis2.url");
		dhis2Username = props.getProperty("dhis2.username");
		dhis2Password = props.getProperty("dhis2.password");
		
		String rc = props.getProperty("openmrs.test.make-rest-call");
		pushToOpenmrsForTest = StringUtils.isEmptyOrWhitespaceOnly(rc) ? false : Boolean.parseBoolean(rc);
		
		this.patientService = new PatientService(openmrsOpenmrsUrl, openmrsUsername, openmrsPassword);
		this.encounterService = new EncounterService(openmrsOpenmrsUrl, openmrsUsername, openmrsPassword);
		this.openmrsLocationService = new OpenmrsLocationService(openmrsOpenmrsUrl, openmrsUsername, openmrsPassword);
		this.dhis2DatasetPush = new DHIS2DatasetPush(dhis2Url, dhis2Username, dhis2Password);
		this.encounterService.setPatientService(patientService);
	}
	
	protected FormSubmission getFormSubmissionFor(String formName, Integer number) throws JsonIOException, IOException {
		ResourceLoader loader = new DefaultResourceLoader();
		String path = loader.getResource(formDirPath).getURI().getPath();
		File fsfile = new File(path + "/" + formName + "/form_submission" + (number == null ? "" : number) + ".json");
		return new Gson().fromJson(new FileReader(fsfile), FormSubmission.class);
	}
	
	protected FormSubmission getFormSubmissionFor(String formName) throws JsonIOException, IOException {
		return getFormSubmissionFor(formName, null);
	}
}
