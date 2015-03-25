package org.opensrp.connector.openmrs.service;

import org.opensrp.common.util.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.opensrp.api.domain.Client;
import org.opensrp.connector.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.opensrp.api.domain.Location;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service
public class PatientService extends OpenmrsService{
	
	private LocationService locationService;
//TODO include everything for patient registration. i.e. person, person name, patient identifier
	// include get for patient on different params like name, identifier, location, uuid, attribute,etc
	//person methods should be separate
	private static final String PERSON_URL = "ws/rest/v1/person";
	private static final String PATIENT_URL = "ws/rest/v1/patient";
	private static final String PATIENT_IDENTIFIER_URL = "identifier";
	private static final String PERSON_ATTRIBUTE_URL = "attribute";
			
	
	@Autowired
    public PatientService(LocationService actionService) {
        this.locationService = locationService;
    }
	public PatientService() { }

    public PatientService(String openmrsUrl, String user, String password) {
    	super(openmrsUrl, user, password);
    }
	
    public Client getPersonName(String uuID,String name)
    {
    	Client client = null;
    	HttpUtil.get(HttpUtil.removeEndingSlash(OPENMRS_BASE_URL)
    			+"/"+PERSON_URL+"/"+uuID+"/name/"+name, "v=full&username="+OPENMRS_USER, OPENMRS_USER, OPENMRS_PWD);
    	return client;    	
    }
    
    public void getPatientIdentifier(String parentUUID,String uuId)
    {
    	HttpUtil.get(HttpUtil.removeEndingSlash(OPENMRS_BASE_URL)
    			+"/"+PATIENT_URL+"/"+parentUUID+PATIENT_IDENTIFIER_URL+"/"+uuId, "v=full&username="+OPENMRS_USER, OPENMRS_USER, OPENMRS_PWD);
    }
    
    public Location getLocation(String uuId) throws JSONException
    {
    	Location l = locationService.getLocation(uuId);
    	return l;
    }
    
    public JsonElement getPatientAllIdentifierElementByParentUUID(String parentUUID)
    {
    	JsonElement patientIdentifiers = null;
    	JsonParser parser = new JsonParser();
    	System.out.println("url:::"+HttpUtil.removeEndingSlash(OPENMRS_BASE_URL)
    			+"/"+PATIENT_URL+"/"+parentUUID);
    	HttpResponse op = (HttpResponse) HttpUtil.get(HttpUtil.removeEndingSlash(OPENMRS_BASE_URL)
    			+"/"+PATIENT_URL+"/"+parentUUID, "v=full", OPENMRS_USER, OPENMRS_PWD);
    	JsonObject jsonObject = (JsonObject) parser.parse(op.body());
    	patientIdentifiers = jsonObject.get("results");
    	return patientIdentifiers;
    }
    
    public Client getPatientAllIdentifierByParentUUID(String parentUUID)
    {
    	Client c = null;
    	System.out.println("url:::"+HttpUtil.removeEndingSlash(OPENMRS_BASE_URL)
    			+"/"+PATIENT_URL+"/"+parentUUID);
    	HttpUtil.get(HttpUtil.removeEndingSlash(OPENMRS_BASE_URL)
    			+"/"+PATIENT_URL+"/"+parentUUID, "v=full", OPENMRS_USER, OPENMRS_PWD);
    	return c;
    }
    
    public JsonElement getPatientElement(String name)
    {
    	JsonParser parser = new JsonParser();
    	System.out.println("url:::"+HttpUtil.removeEndingSlash(OPENMRS_BASE_URL)+"/"+PATIENT_URL+"?v=full&q="+name);
    	HttpResponse op = (HttpResponse) HttpUtil.get(HttpUtil.removeEndingSlash(OPENMRS_BASE_URL)
    			+"/"+PATIENT_URL, "v=full&q="+name, OPENMRS_USER, OPENMRS_PWD);
    	JsonObject jsonObject = (JsonObject) parser.parse(op.body());
    	JsonElement results = jsonObject.get("results");
    	return results;
    }
    
    public Client getPatientByName(String name)
    {
    	Client c = new Client();
    	new JsonParser();
    	JsonElement results = getPatientElement(name);
    	JsonArray resultArray = results.getAsJsonArray();
    	JsonObject personObject = null;
    	JsonElement givenNameElement = null;
    	JsonObject jsonObject2 = null;
    	JsonElement personElement = null;
    	for(JsonElement element:resultArray)
    	{
    		jsonObject2 = element.getAsJsonObject();
    		personElement = jsonObject2.get("person");
    		//personArray = personObject.ge;    		
    	}
    	personObject = personElement.getAsJsonObject();
    	personObject.get("uuid");    	
    	//TODO SEARCH CLIENT FROM REPOSITORY IF exists    	
    	System.out.println("givenNameElement::"+givenNameElement);
    	return c;
    }
    
    public void getPersonAttribute(String parentUUID)
    {
    	HttpResponse op = (HttpResponse)HttpUtil.get(HttpUtil.removeEndingSlash(OPENMRS_BASE_URL)
    			+"/"+PERSON_URL+"/"+parentUUID+"/"+PERSON_ATTRIBUTE_URL+"/", "v=full", OPENMRS_USER, OPENMRS_PWD);
    }
	public void createPerson()
	{
		String payload = "";
		JSONObject jsonObject = null;
		HttpUtil.post(OPENMRS_BASE_URL+PERSON_URL, payload, jsonObject.toString(), OPENMRS_USER, OPENMRS_PWD);
	}
	
	public void createPersonName()
	{
		String parentUUID = "";
		String requestData = "	{" +
							"	givenName:''," +
							"	familyName:''," +
							"	middleName:''," +
							"	familyName2:''," +
							"	preferred:''," +
							"	prefix:''," +
							"	familyNamePrefix:''," +
							"	familyNameSuffix:''," +
							"	degree:''" +
							"	}";
		String payload = "";
		HttpUtil.post(OPENMRS_BASE_URL+PERSON_URL+"/"+parentUUID+"/name", payload, requestData, OPENMRS_USER, OPENMRS_PWD);
	}
	
	public void createPatientIdentifier()
	{
		String parentUUID = "";
		String requestData = "";
		String payload = "";
		HttpUtil.post(OPENMRS_BASE_URL+PATIENT_URL+"/"+parentUUID+"/identifier", payload, requestData, OPENMRS_USER, OPENMRS_PWD);
	}
	
	public void createPatient()
	{
		String requestData = "";
		String payload = "";
		HttpUtil.post(OPENMRS_BASE_URL+PATIENT_URL, payload, requestData, OPENMRS_USER, OPENMRS_PWD);
	}
}

