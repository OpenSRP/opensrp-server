package org.opensrp.connector;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.opensrp.api.domain.Event;
import org.opensrp.api.domain.Obs;
import org.opensrp.connector.openmrs.service.EncounterService;
import org.opensrp.connector.openmrs.service.HouseholdService;
import org.opensrp.connector.openmrs.service.LocationService;
import org.opensrp.connector.openmrs.service.OpenmrsService;
import org.opensrp.connector.openmrs.service.PatientService;
import org.opensrp.connector.openmrs.service.UserService;
import org.opensrp.form.domain.FormField;
import org.opensrp.form.domain.FormSubmission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OpenmrsConnector {

	private EncounterService encounterService;
	private HouseholdService householdService; 
	private PatientService patientService;
	private LocationService locationService;
	private UserService userService;
	private FormAttributeMapper formAttributeMapper;
	
	public OpenmrsConnector() {	}
	
	@Autowired
	public OpenmrsConnector(EncounterService encounterService,
			HouseholdService householdService, PatientService patientService,
			LocationService locationService, UserService userService, FormAttributeMapper formAttributeMapper) {
		this.encounterService = encounterService;
		this.householdService = householdService;
		this.patientService = patientService;
		this.locationService = locationService;
		this.userService = userService;
		this.formAttributeMapper = formAttributeMapper;
	}
	
	public Event convertFormSubmissionToEvent(FormSubmission fs) throws ParseException {
		Map<String, String> m = new HashMap<String, String>();
		m.put("openmrs_entity" , "encounter");
		m.put("openmrs_entity_id" , "encounter_date");
		String encounterDateField = formAttributeMapper.getFieldName(m , fs);

		m.clear();
		m.put("openmrsForm" , "encounter");
		m.put("openmrs_entity_id" , "encounter_date");
		String eventType = formAttributeMapper.getFieldName(m , fs);//???????????? BY ATTRIB only
		Event e = new Event()
			.withBaseEntityId("4f363c0d-485f-4753-8735-617d6a3454e6")
			.withEventDate(OpenmrsService.OPENMRS_DATE.parse(fs.getField(encounterDateField)))
			.withEventType("anc visit")
			.withLocationId("testloc")
			.withProviderId("010700cc-1656-483d-b54d-785de49d293c")
			;
		
		for (FormField fl : fs.instance().form().fields()) {
			Map<String, String> att = formAttributeMapper.getAttributesForField(fl.name(), fs);
			if(att.size()>0 && att.get("openmrs_entity").equalsIgnoreCase("concept"))
				e.addObs(new Obs("concept", att.get("openmrs_entity_id"), 
						att.get("openmrs_entity_parent"), fl.value(), null, null));
		}
		return e;
	}
}
