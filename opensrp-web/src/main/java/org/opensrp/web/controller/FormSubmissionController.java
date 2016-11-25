package org.opensrp.web.controller;

import static ch.lambdaj.collection.LambdaCollections.with;
import static java.text.MessageFormat.format;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.opensrp.api.domain.Drug;
import org.opensrp.common.AllConstants;
import org.opensrp.connector.openmrs.constants.OpenmrsHouseHold;
import org.opensrp.connector.openmrs.service.EncounterService;
import org.opensrp.connector.openmrs.service.HouseholdService;
import org.opensrp.connector.openmrs.service.OrderService;
import org.opensrp.connector.openmrs.service.PatientService;
import org.opensrp.domain.Client;
import org.opensrp.form.domain.*;
import org.opensrp.domain.ErrorTrace;
import org.opensrp.domain.Event;
import org.opensrp.domain.Multimedia;
import org.opensrp.dto.form.FormSubmissionDTO;
import org.opensrp.form.domain.FormSubmission;
import org.opensrp.form.service.FormSubmissionConverter;
import org.opensrp.form.service.FormSubmissionService;
import org.opensrp.scheduler.SystemEvent;
import org.opensrp.scheduler.TaskSchedulerService;
import org.opensrp.service.ErrorTraceService;
import org.opensrp.service.MultimediaService;
import org.opensrp.service.formSubmission.FormEntityConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import ch.lambdaj.function.convert.Converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Controller
public class FormSubmissionController {
    private static Logger logger = LoggerFactory.getLogger(FormSubmissionController.class.toString());
    private FormSubmissionService formSubmissionService;
    private TaskSchedulerService scheduler;
    private EncounterService encounterService;
    private FormEntityConverter formEntityConverter;
    private PatientService patientService;
    private HouseholdService householdService;
    private ErrorTraceService errorTraceService;
    private OrderService DrugService;
    
    @Autowired //TODO: Julkar Confirm this
    private MultimediaService multimediaService;
    
    @Autowired
    public FormSubmissionController(FormSubmissionService formSubmissionService, TaskSchedulerService scheduler,
    		EncounterService encounterService, FormEntityConverter formEntityConverter, PatientService patientService, 
    		HouseholdService householdService, ErrorTraceService errorTraceService) {
        this.formSubmissionService = formSubmissionService;
        this.scheduler = scheduler;
        this.errorTraceService=errorTraceService;
        this.encounterService = encounterService;
        this.formEntityConverter = formEntityConverter;
        this.patientService = patientService;
        this.householdService = householdService;
    }

    @RequestMapping(method = GET, value = "/form-submissions")
    @ResponseBody
    private List<FormSubmissionDTO> getNewSubmissionsForANM(@RequestParam("anm-id") String anmIdentifier,
                                                            @RequestParam("timestamp") Long timeStamp,
                                                            @RequestParam(value = "batch-size", required = false)
                                                            Integer batchSize) {
        List<FormSubmission> newSubmissionsForANM = formSubmissionService
                .getNewSubmissionsForANM(anmIdentifier, timeStamp, batchSize);
        return with(newSubmissionsForANM).convert(new Converter<FormSubmission, FormSubmissionDTO>() {
            @Override
            public FormSubmissionDTO convert(FormSubmission submission) {
                return FormSubmissionConverter.from(submission);
            }
        });
    }

    @RequestMapping(method = GET, value="/all-form-submissions")
    @ResponseBody
    private List<FormSubmissionDTO> getAllFormSubmissions(@RequestParam("timestamp") Long timeStamp,
                                                          @RequestParam(value = "batch-size", required = false)
                                                          Integer batchSize) {
        List<FormSubmission> allSubmissions = formSubmissionService
                .getAllSubmissions(timeStamp, batchSize);
        return with(allSubmissions).convert(new Converter<FormSubmission, FormSubmissionDTO>() {
            @Override
            public FormSubmissionDTO convert(FormSubmission submission) {
                return FormSubmissionConverter.from(submission);
            }
        });
    }

    @RequestMapping(headers = {"Accept=application/json"}, method = POST, value = "/form-submissions")
    public ResponseEntity<HttpStatus> submitForms(@RequestBody List<FormSubmissionDTO> formSubmissionsDTO) {
        try {
            if (formSubmissionsDTO.isEmpty()) {
                return new ResponseEntity<>(BAD_REQUEST);
            }

            scheduler.notifyEvent(new SystemEvent<>(AllConstants.OpenSRPEvent.FORM_SUBMISSION, formSubmissionsDTO));
            
            try{
          
            ////////TODO MAIMOONA : SHOULD BE IN EVENT but event needs to be moved to web so for now kept here
            String json = new Gson().toJson(formSubmissionsDTO);
            System.out.println("MMMMMMMMMMMYYYYYYYYYYYYYY::"+json);
            List<FormSubmissionDTO> formSubmissions = new Gson().fromJson(json, new TypeToken<List<FormSubmissionDTO>>() {
            }.getType());
            List<FormSubmission> fsl = with(formSubmissions).convert(new Converter<FormSubmissionDTO, FormSubmission>() {
                @Override
                public FormSubmission convert(FormSubmissionDTO submission) {
                    return FormSubmissionConverter.toFormSubmission(submission);
                }
            });
	            for (FormSubmission formSubmission : fsl) {
	            	try{
	            		addFormToOpenMRS(formSubmission);
	            	}
	            	catch(Exception e){
	            		e.printStackTrace();
	            		ErrorTrace errorTrace=new ErrorTrace(new Date(), "Parse Exception", "", e.getStackTrace().toString(), "Unsolved", formSubmission.formName());
						errorTrace.setRecordId(formSubmission.instanceId());
						errorTraceService.addError(errorTrace);
	            	}
	    		}
            }
            catch(Exception e){
            	e.printStackTrace();
            }
            logger.debug(format("Added Form submissions to queue.\nSubmissions: {0}", formSubmissionsDTO));
        } catch (Exception e) {
            logger.error(format("Form submissions processing failed with exception {0}.\nSubmissions: {1}", e, formSubmissionsDTO));
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(CREATED);
    }
    
    private void addFormToOpenMRS(FormSubmission formSubmission) throws ParseException, IllegalStateException, JSONException{
//    	if(formEntityConverter.isOpenmrsForm(formSubmission)){
    		Client c = formEntityConverter.getClientFromFormSubmission(formSubmission);
			Event e = formEntityConverter.getEventFromFormSubmission(formSubmission);
			Map<String, Map<String, Object>> dep = formEntityConverter.getDependentClientsFromFormSubmission(formSubmission);

    		// TODO temporary because not necessarily we register inner entity for Household only
    		if(formSubmission.formName().toLowerCase().contains("household") 
    				|| formSubmission.formName().toLowerCase().contains("census") ){
    			OpenmrsHouseHold hh = new OpenmrsHouseHold(c, e);
    			for (Map<String, Object> cm : dep.values()) {
    				hh.addHHMember((Client)cm.get("client"), (Event)cm.get("event"));
    			}
    			
    			householdService.saveHH(hh, true);
    		}
    		else {
    			JSONObject p = patientService.getPatientByIdentifier(c.getBaseEntityId());
    			if(p == null){
    				System.out.println(patientService.createPatient(c));
    			}
        	
    			System.out.println(encounterService.createEncounter(e));
    			
    			for (Map<String, Object> cm : dep.values()) {
    				Client cin = (Client)cm.get("client");
    				Event evin = (Event)cm.get("event");
    				JSONObject pin = patientService.getPatientByIdentifier(cin.getBaseEntityId());
        			if(pin == null){
        				System.out.println(patientService.createPatient(cin));
        			}
            	
        			System.out.println(encounterService.createEncounter(evin));
    			}
    		}
    	//}
    }
    @RequestMapping(headers = {"Accept=application/json"}, method = GET, value = "/multimedia-file")
    @ResponseBody
    public List<Multimedia> getFiles(@RequestParam("anm-id") String providerId) {
    	
    	List<Multimedia> allMultimedias = multimediaService.getMultimediaFiles(providerId);
    	
    	return with(allMultimedias).convert(new Converter<Multimedia, Multimedia>() {
			@Override
			public Multimedia convert(Multimedia md) {
				return new Multimedia(md.getBaseEntityId(), md.getProviderId(), md.getContentType(), md.getFilePath(), md.getFileCategory());
			}
		});
    }
    @RequestMapping(headers = {"Accept=multipart/form-data"}, method = POST, value = "/multimedia-file")
    public ResponseEntity<String> uploadFiles(@RequestParam("anm-id") String providerId, @RequestParam("entity-id") String entityId,@RequestParam("content-type") String contentType, @RequestParam("file-category") String fileCategory, @RequestParam("file") MultipartFile file) {
    	
    	Multimedia Multimedia = new Multimedia(entityId, providerId, contentType, null, fileCategory);
    	
    	String status = multimediaService.saveMultimediaFile(Multimedia, file);
    	 
    	 return new ResponseEntity<>(new Gson().toJson(status), HttpStatus.OK);
    }
}
