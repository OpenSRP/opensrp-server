//package org.opensrp.connector.openmrs.service;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.opensrp.common.util.HttpUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class RelationShipService extends OpenmrsService{
//	private static final String RELATION_SHIP_URL = "ws/rest/v1/relationship";
//
//	
//	@Autowired
//	public RelationShipService(String openmrsUrl, String user, String password) {
//    	super(openmrsUrl, user, password);
//	}
//	
//    public JSONObject createRelationShip(String personA,String personB, String relationshipType) throws JSONException{
//		JSONObject relationShip = convertRelationShipToOpenmrsJson(personA, personB, relationshipType);
//		return new JSONObject(HttpUtil.post(getURL()+"/"+RELATION_SHIP_URL	, "", relationShip.toString(), OPENMRS_USER, OPENMRS_PWD).body());
//	}
//    
//    public JSONObject convertRelationShipToOpenmrsJson(String personA,String personB, String relationshipType) throws JSONException {
//		JSONObject relationShip = new JSONObject();
//		relationShip.put("personA", personA);
//		relationShip.put("personB", personB);
//		relationShip.put("relationshipType", relationshipType);
//		return relationShip;
//	}
//	
//	
//}
