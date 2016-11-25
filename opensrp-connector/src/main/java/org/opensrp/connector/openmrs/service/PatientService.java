
package org.opensrp.connector.openmrs.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opensrp.connector.HttpUtil;
import org.opensrp.domain.Address;
import org.opensrp.domain.Client;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.mysql.jdbc.StringUtils;

@Service
public class PatientService extends OpenmrsService{
	
//TODO include everything for patient registration. i.e. person, person name, patient identifier
	// include get for patient on different params like name, identifier, location, uuid, attribute,etc
	//person methods should be separate
	private static final String PERSON_URL = "ws/rest/v1/person";
	private static final String PATIENT_URL = "ws/rest/v1/patient";
	private static final String RELATION_URL = "ws/rest/v1/relationship";
	private static final String PATIENT_IDENTIFIER_URL = "identifier";
	private static final String PERSON_ATTRIBUTE_URL = "attribute";
	private static final String PERSON_ATTRIBUTE_TYPE_URL = "ws/rest/v1/personattributetype";
	private static final String PATIENT_IDENTIFIER_TYPE_URL = "ws/rest/v1/patientidentifiertype";
	
	// This ID should start with opensrp and end with uid. As matched by atomefeed module`s patient service
	public static final String OPENSRP_IDENTIFIER_TYPE = "OpenSRP Thrive UID";
	public static final String OPENSRP_IDENTIFIER_TYPE_MATCHER = "(?i)opensrp.*uid";
	public static final String OPENMRS_UUID_IDENTIFIER_TYPE = "OPENMRS_UUID";
		
	public PatientService() { }

    public PatientService(String openmrsUrl, String user, String password) {
    	super(openmrsUrl, user, password);
    }
	
    public JSONObject getPatientByIdentifier(String identifier) throws JSONException
    { 
    	System.out.println(HttpUtil.get(getURL()+"/"+PATIENT_URL, "v=full&identifier="+identifier, OPENMRS_USER, OPENMRS_PWD).body());
    	String JSON=getURL()+"/"+PATIENT_URL;
    	JSONArray p = new JSONObject(HttpUtil.get(JSON, "v=full&identifier="+identifier, OPENMRS_USER, OPENMRS_PWD).body())
    			.getJSONArray("results");
    	System.out.println(String.valueOf(p));
    	return p.length()>0?p.getJSONObject(0):null;
    }
    
    public JSONObject getPatientByIdentifier(int num) throws JSONException
    {
    	JSONArray p = new JSONObject(HttpUtil.get(getURL()
    			+"/"+RELATION_URL, "v=full&identifier="+num, OPENMRS_USER, OPENMRS_PWD).body())
    			.getJSONArray("results");
    	return p.length()>0?p.getJSONObject(0):null;
    }
    
    public JSONObject getRelativeByUuid(String uuid, boolean noRepresentationTag) throws JSONException
    {
    	return new JSONObject(HttpUtil.get(getURL()
    			+"/"+RELATION_URL+"/"+uuid, noRepresentationTag?"":"v=full", OPENMRS_USER, OPENMRS_PWD).body());
    }
    
    public JSONObject getPatientByUuid(String uuid, boolean noRepresentationTag) throws JSONException
    {
    	return new JSONObject(HttpUtil.get(getURL()
    			+"/"+PATIENT_URL+"/"+uuid, noRepresentationTag?"":"v=full", OPENMRS_USER, OPENMRS_PWD).body());
    }
    
    public JSONObject getIdentifierType(String identifierType) throws JSONException
    {
    	// we have to use this ugly approach because identifier not found throws exception and 
    	// its hard to find whether it was network error or object not found or server error
    	JSONArray res = new JSONObject(HttpUtil.get(getURL()+"/"+PATIENT_IDENTIFIER_TYPE_URL, "v=full", 
    			OPENMRS_USER, OPENMRS_PWD).body()).getJSONArray("results");
    	for (int i = 0; i < res.length(); i++) {
			if(res.getJSONObject(i).getString("display").equalsIgnoreCase(identifierType)){
				return res.getJSONObject(i);
			}
		}
    	return null;
    }
	
    public JSONObject createIdentifierType(String name, String description) throws JSONException{
		JSONObject o = convertIdentifierToOpenmrsJson(name, description);
		return new JSONObject(HttpUtil.post(getURL()+"/"+PATIENT_IDENTIFIER_TYPE_URL, "", o.toString(), OPENMRS_USER, OPENMRS_PWD).body());
	}
    
    
	public JSONObject convertIdentifierToOpenmrsJson(String name, String description) throws JSONException {
		JSONObject a = new JSONObject();
		a.put("name", name);
		a.put("description", description);
		return a;
	}
	public JSONObject convertRelationShipToOpenmrsJson(int num) throws JSONException {
		JSONObject a = new JSONObject();
		a.put("relationship", num);
		return a;
	}
	
    public JSONObject getPersonAttributeType(String attributeName) throws JSONException
    {
    	JSONArray p = new JSONObject(HttpUtil.get(getURL()+"/"+PERSON_ATTRIBUTE_TYPE_URL, 
    			"v=full&q="+attributeName, OPENMRS_USER, OPENMRS_PWD).body()).getJSONArray("results");
    	return p.length()>0?p.getJSONObject(0):null;
    }
	
	public JSONObject createPerson(Client be) throws JSONException{
		JSONObject per = convertBaseEntityToOpenmrsJson(be);
		return new JSONObject(HttpUtil.post(getURL()+"/"+PERSON_URL, "", per.toString(), OPENMRS_USER, OPENMRS_PWD).body());
	}
	
	public JSONObject convertBaseEntityToOpenmrsJson(Client be) throws JSONException {
		JSONObject per = new JSONObject();
		per.put("gender", be.getGender());
		per.put("birthdate", OPENMRS_DATE.format(be.getBirthdate().toDate()));
		per.put("birthdateEstimated", be.getBirthdateApprox());
		if(be.getDeathdate() != null){
			per.put("deathDate", OPENMRS_DATE.format(be.getDeathdate().toDate()));
		}
		
		String fn = be.getFirstName();
		String mn = be.getMiddleName()==null?"":be.getMiddleName();
		String ln = be.getLastName()==null?".":be.getLastName();
		per.put("names", new JSONArray("[{\"givenName\":\""+fn+"\",\"middleName\":\""+mn+"\", \"familyName\":\""+ln+"\"}]"));
		per.put("attributes", convertAttributesToOpenmrsJson(be.getAttributes()));
		per.put("addresses", convertAddressesToOpenmrsJson(be.getAddresses()));
		return per;
	}
	
	public JSONArray convertAttributesToOpenmrsJson(Map<String, Object> attributes) throws JSONException {
		if(CollectionUtils.isEmpty(attributes)){
			return null;
		}
		JSONArray attrs = new JSONArray();
		for (Entry<String, Object> at : attributes.entrySet()) {
			JSONObject a = new JSONObject();
			a.put("attributeType", getPersonAttributeType(at.getKey()).getString("uuid"));
			a.put("value", at.getValue());
			attrs.put(a);
		}
		
		return attrs;
	}
	
	public JSONArray convertAddressesToOpenmrsJson(List<Address> adl) throws JSONException{
		if(CollectionUtils.isEmpty(adl)){
			return null;
		}
		JSONArray jaar = new JSONArray();
		for (Address ad : adl) {
			JSONObject jao = new JSONObject();
			if(ad.getAddressFields() != null){
				jao.put("address1", ad.getAddressFieldMatchingRegex("(?i)(ADDRESS1|HOUSE_NUMBER|HOUSE|HOUSE_NO|UNIT|UNIT_NUMBER|UNIT_NO)"));
				jao.put("address2", ad.getAddressField("(?i)(ADDRESS2|STREET|STREET_NUMBER|STREET_NO|LANE)"));
				jao.put("address3", ad.getAddressField("(?i)(ADDRESS3|SECTOR|AREA)"));
				String a4 = ad.getAddressField("(?i)(ADDRESS4|SUB_DISTRICT|MUNICIPALITY|TOWN|LOCALITY|REGION)");
				a4 = StringUtils.isEmptyOrWhitespaceOnly(a4)?"":a4;
				String subd = StringUtils.isEmptyOrWhitespaceOnly(ad.getSubDistrict())?"":ad.getSubDistrict();
				String tow = StringUtils.isEmptyOrWhitespaceOnly(ad.getTown())?"":ad.getTown();
				jao.put("address4", a4+subd+tow);
				jao.put("countyDistrict", ad.getCountyDistrict());
				jao.put("cityVillage", ad.getCityVillage());

				String ad5V = "";
				for (Entry<String, String> af : ad.getAddressFields().entrySet()) {
					if(!af.getKey().matches("(?i)(ADDRESS1|HOUSE_NUMBER|HOUSE|HOUSE_NO|UNIT|UNIT_NUMBER|UNIT_NO|"
							+ "ADDRESS2|STREET|STREET_NUMBER|STREET_NO|LANE|"
							+ "ADDRESS3|SECTOR|AREA|"
							+ "ADDRESS4|SUB_DISTRICT|MUNICIPALITY|TOWN|LOCALITY|REGION|"
							+ "countyDistrict|county_district|COUNTY|DISTRICT|"
							+ "cityVillage|city_village|CITY|VILLAGE)")){
						ad5V += af.getKey()+":"+af.getValue()+";";
					}
				}
				if(!StringUtils.isEmptyOrWhitespaceOnly(ad5V)){
					jao.put("address5", ad5V);
				}
				
			}
			jao.put("address6", ad.getAddressType());
			jao.put("stateProvince", ad.getStateProvince());
			jao.put("country", ad.getCountry());
			jao.put("postalCode", ad.getPostalCode());
			jao.put("latitude", ad.getLatitude());
			jao.put("longitude", ad.getLongitude());
			if(ad.getStartDate() != null){
				jao.put("startDate", OPENMRS_DATE.format(ad.getStartDate()));
			}
			if(ad.getEndDate() != null){
				jao.put("endDate", OPENMRS_DATE.format(ad.getEndDate()));
			}
			
			jaar.put(jao);
		}
		
		return jaar;
	}
	
	public JSONObject createPatient(Client c) throws JSONException
	{
		JSONObject p = new JSONObject();
		p.put("person", createPerson(c).getString("uuid"));
		JSONArray ids = new JSONArray();
		if(c.getIdentifiers() != null){
		for (Entry<String, String> id : c.getIdentifiers().entrySet()) {
			JSONObject jio = new JSONObject();
			JSONObject idobj = getIdentifierType(id.getKey());
			if(idobj == null){
				idobj = createIdentifierType(id.getKey(), id.getKey()+" - FOR THRIVE OPENSRP");
			}
			jio.put("identifierType", idobj.getString("uuid"));
			jio.put("identifier", id.getValue());
			Object cloc = c.getAttribute("Location");
			jio.put("location", cloc == null?"Unknown Location":cloc);
			//jio.put("preferred", true);

			ids.put(jio);
		}}
		
		JSONObject jio = new JSONObject();
		JSONObject ido = getIdentifierType(OPENSRP_IDENTIFIER_TYPE);
		if(ido == null){
			ido = createIdentifierType(OPENSRP_IDENTIFIER_TYPE, OPENSRP_IDENTIFIER_TYPE+" - FOR THRIVE OPENSRP");
		}
		jio.put("identifierType", ido.getString("uuid"));
		jio.put("identifier", c.getBaseEntityId());
		Object cloc = c.getAttribute("Location");
		jio.put("location", cloc == null?"Unknown Location":cloc);
		jio.put("preferred", true);
		
		ids.put(jio);
		
		p.put("identifiers", ids);
		return new JSONObject(HttpUtil.post(getURL()+"/"+PATIENT_URL, "", p.toString(), OPENMRS_USER, OPENMRS_PWD).body());
	}
	
	public JSONObject addThriveId(String baseEntityId, JSONObject patient) throws JSONException
	{
		JSONObject jio = new JSONObject();
		JSONObject ido = getIdentifierType(OPENSRP_IDENTIFIER_TYPE);
		if(ido == null){
			ido = createIdentifierType(OPENSRP_IDENTIFIER_TYPE, OPENSRP_IDENTIFIER_TYPE+" - FOR THRIVE OPENSRP");
		}
		jio.put("identifierType", ido.getString("uuid"));
		jio.put("identifier", baseEntityId);
		jio.put("location", "Unknown Location");
		jio.put("preferred", true);
		
		return new JSONObject(HttpUtil.post(getURL()+"/"+PATIENT_URL+"/"+patient.getString("uuid")+"/"+PATIENT_IDENTIFIER_URL, "", jio.toString(), OPENMRS_USER, OPENMRS_PWD).body());
	}
	
	public Client convertToClient(JSONObject patient) throws JSONException {
		Client c = new Client(null);
		JSONArray ar = patient.getJSONArray("identifiers");
		for (int i = 0; i < ar.length(); i++) {
			JSONObject ji = ar.getJSONObject(i);
			if(ji.getJSONObject("identifierType").getString("display").equalsIgnoreCase(OPENSRP_IDENTIFIER_TYPE)){
				c.setBaseEntityId(ji.getString("identifier"));
			}
			else {
				c.addIdentifier(ji.getJSONObject("identifierType").getString("display"), ji.getString("identifier"));
			}
		}
		
		c.addIdentifier(OPENMRS_UUID_IDENTIFIER_TYPE, patient.getString("uuid"));
		
		JSONObject pr = patient.getJSONObject("person");
		
		String mn = pr.getJSONObject("preferredName").has("middleName")?pr.getJSONObject("preferredName").getString("middleName"):null;
		DateTime dd = pr.has("deathDate")&&!pr.getString("deathDate").equalsIgnoreCase("null")?new DateTime(pr.getString("deathDate")):null;
		c.withFirstName(pr.getJSONObject("preferredName").getString("givenName"))
		.withMiddleName(mn)
		.withLastName(pr.getJSONObject("preferredName").getString("familyName"))
		.withGender(pr.getString("gender"))
		.withBirthdate(new DateTime(pr.getString("birthdate")), pr.getBoolean("birthdateEstimated"))
		.withDeathdate(dd, false);
		
		if(pr.has("attributes")){
			for (int i = 0; i < pr.getJSONArray("attributes").length(); i++) {
				JSONObject at = pr.getJSONArray("attributes").getJSONObject(i);
				if(at.optJSONObject("value") == null){
					c.addAttribute(at.getJSONObject("attributeType").getString("display"), at.getString("value"));
				}
				else{
					c.addAttribute(at.getJSONObject("attributeType").getString("display"), at.getJSONObject("value").getString("display"));
				}
			}
		}
		
		if(pr.has("addresses")){
			for (int i = 0; i < pr.getJSONArray("addresses").length(); i++) {
				JSONObject ad = pr.getJSONArray("addresses").getJSONObject(i);
				Date startDate = ad.has("startDate")&&!ad.getString("startDate").equalsIgnoreCase("null")?new DateTime(ad.getString("startDate")).toDate():null;
				Date endDate = ad.has("startDate")&&!ad.getString("endDate").equalsIgnoreCase("null")?new DateTime(ad.getString("endDate")).toDate():null;;
				Address a = new Address(ad.getString("address6"), startDate, endDate, null, 
						ad.getString("latitude"), ad.getString("longitude"), ad.getString("postalCode"), 
						ad.getString("stateProvince"), ad.getString("country"));
				//a.setGeopoint(geopoint);
				a.setSubTown(ad.getString("address2"));//TODO
				a.setTown(ad.getString("address3"));
				a.setSubDistrict(ad.getString("address4"));
				a.setCountyDistrict(ad.getString("countyDistrict"));
				a.setCityVillage(ad.getString("cityVillage"));
				
				c.addAddress(a);
			}
			
		}
		return c;
	}
}
