package org.opensrp.connector.openmrs.service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opensrp.connector.HttpUtil;
import org.opensrp.connector.openmrs.constants.OpenmrsHouseHold;
import org.opensrp.connector.openmrs.constants.OpenmrsHouseHold.HouseholdMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mysql.jdbc.StringUtils;

@Service
public class HouseholdService extends OpenmrsService{
	private static final String RELATIONSHIP_URL = "ws/rest/v1/relationship";
	private static final String RELATIONSHIP_TYPE_URL = "ws/rest/v1/relationshiptype";
	private PatientService patientService;
	private EncounterService encounterService;
	
	@Autowired
	public HouseholdService(PatientService patientService, EncounterService encounterService) {
		this.setPatientService(patientService);
		this.setEncounterService(encounterService);
	}
	
	public JSONObject getRelationshipType(String relationship) throws JSONException
    {
    	return new JSONObject(HttpUtil.get(getURL()
    			+"/"+RELATIONSHIP_TYPE_URL+"/"+relationship, "v=full", OPENMRS_USER, OPENMRS_PWD).body());
    }
	
	public JSONObject findRelationshipTypeMatching(String relationship) throws JSONException
    {
    	JSONArray r = new JSONObject(HttpUtil.get(getURL()
    			+"/"+RELATIONSHIP_TYPE_URL, "v=full&q="+relationship, OPENMRS_USER, OPENMRS_PWD).body())
    			.getJSONArray("results");
    	return r.length()>0?r.getJSONObject(0):null;
    }
	
	public JSONObject createRelationshipType(String AIsToB, String BIsToA, String description) throws JSONException{
		JSONObject o = convertRelationshipTypeToOpenmrsJson(AIsToB, BIsToA, description);
		return new JSONObject(HttpUtil.post(getURL()+"/"+RELATIONSHIP_TYPE_URL, "", o.toString(), OPENMRS_USER, OPENMRS_PWD).body());
	}
	
	public JSONObject createRelationship(String clientUuid, String isARelationship, String relativeUuid) throws JSONException{
		JSONObject o = convertRelationshipToOpenmrsJson(clientUuid, isARelationship, relativeUuid);
		return new JSONObject(HttpUtil.post(getURL()+"/"+RELATIONSHIP_URL, "", o.toString(), OPENMRS_USER, OPENMRS_PWD).body());
	}
	
	public JSONObject convertRelationshipToOpenmrsJson(String personA, String isARelationship, String personB) throws JSONException {
		JSONObject a = new JSONObject();
		a.put("personA", personA);
		a.put("relationshipType", isARelationship);
		a.put("personB", personB);
		return a;
	}
	
	public JSONObject convertRelationshipTypeToOpenmrsJson(String AIsToB, String BIsToA, String description) throws JSONException {
		JSONObject a = new JSONObject();
		a.put("aIsToB", AIsToB);
		a.put("bIsToA", BIsToA);
		a.put("description", description);
		return a;
	}
	
	public HouseholdService(String openmrsUrl, String user, String password) {
    	super(openmrsUrl, user, password);
	}
	
	public void saveHH(OpenmrsHouseHold household) throws JSONException{
		String hhrel = "Household Head";
		JSONObject hhheadrel = findRelationshipTypeMatching(hhrel);
		if(hhheadrel == null){
			createRelationshipType(hhrel, "Dependent", "Household Head and Member relationship created by OpenSRP");
		}
		
		JSONObject hhp = patientService.createPatient(household.getHouseholdHead().getClient());
		JSONObject hhe = encounterService.createEncounter(household.getHouseholdHead().getEvent().get(0));
		
		for (HouseholdMember m : household.getMembers()) {
			if(StringUtils.isEmptyOrWhitespaceOnly(m.getClient().getBaseEntity().getFirstName())
					&& m.getClient().getIdentifiers().size() < 2){//we need to ignore uuid of entity
				//skip Data push for now
			}
			else{
				JSONObject mp = patientService.createPatient(m.getClient());
				JSONObject me = encounterService.createEncounter(m.getEvent().get(0));
				
				createRelationship(hhp.getString("uuid"), hhrel, mp.getString("uuid"));
			}
		}
	}

	public PatientService getPatientService() {
		return patientService;
	}

	public void setPatientService(PatientService patientService) {
		this.patientService = patientService;
	}

	public EncounterService getEncounterService() {
		return encounterService;
	}

	public void setEncounterService(EncounterService encounterService) {
		this.encounterService = encounterService;
	}
}
