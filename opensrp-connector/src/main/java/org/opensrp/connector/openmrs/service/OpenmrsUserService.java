package org.opensrp.connector.openmrs.service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opensrp.api.domain.User;
import org.opensrp.common.util.HttpResponse;
import org.opensrp.connector.HttpUtil;
import org.springframework.stereotype.Service;

@Service
public class OpenmrsUserService extends OpenmrsService{

	private static final String AUTHENTICATION_URL = "ws/rest/v1/session";
	private static final String USER_URL = "ws/rest/v1/user";
	private static final String TEAM_MEMBER_URL = "ws/rest/v1/teammodule/member";
	
    public OpenmrsUserService() { }

    public OpenmrsUserService(String openmrsUrl, String user, String password) {
    	super(openmrsUrl, user, password);
    }

	public boolean authenticate(String username, String password) throws JSONException {
		HttpResponse op = HttpUtil.get(HttpUtil.removeEndingSlash(OPENMRS_BASE_URL)+"/"+AUTHENTICATION_URL, "", username, password);
		return new JSONObject(op.body()).getBoolean("authenticated");
	}

	public User getUser(String username) throws JSONException {
		HttpResponse op = HttpUtil.get(HttpUtil.removeEndingSlash(OPENMRS_BASE_URL)+"/"+USER_URL, "v=full&username="+username, OPENMRS_USER, OPENMRS_PWD);
		JSONArray res = new JSONObject(op.body()).getJSONArray("results");
		if(res.length() == 0){
			return null;
		}
		JSONObject obj = res.getJSONObject(0);
		JSONObject p = obj.getJSONObject("person");
		User u = new User(obj.getString("uuid"), obj.getString("username"), null, null,
				p.getString("display"), "", "", null, null, p.getString("gender"));
		//Object ploc;
		JSONArray a = p.getJSONArray("attributes");
		
		for (int i = 0; i < a.length(); i++) {
			String ad = a.getJSONObject(i).getString("display");
			u.getBaseEntity().addAttribute(ad.substring(0,ad.indexOf("=")-1), ad.substring(ad.indexOf("=")+2));
		}
		
		JSONArray per = obj.getJSONArray("privileges");
		
		for (int i = 0; i < per.length(); i++) {
			u.addPermission(per.getJSONObject(i).getString("name"));
		}
		
		JSONArray rol = obj.getJSONArray("roles");
		
		for (int i = 0; i < rol.length(); i++) {
			u.addRole(rol.getJSONObject(i).getString("name"));
		}
		
		u.getBaseEntity().addAttribute("_PERSON_UUID", p.getString("uuid"));
		return u;
	}
	
	public JSONObject getPersonByUser(String username) throws JSONException {
		HttpResponse op = HttpUtil.get(HttpUtil.removeEndingSlash(OPENMRS_BASE_URL)+"/"+USER_URL, "v=full&username="+username, OPENMRS_USER, OPENMRS_PWD);
		JSONArray res = new JSONObject(op.body()).getJSONArray("results");
		if(res.length() == 0){
			return null;
		}
		JSONObject obj = res.getJSONObject(0);
		JSONObject p = obj.getJSONObject("person");
		return p;
	}
	
	public JSONObject getTeamMember(String uuid) throws JSONException{
		HttpResponse op = HttpUtil.get(HttpUtil.removeEndingSlash(OPENMRS_BASE_URL)+"/"+TEAM_MEMBER_URL+"/"+uuid, "v=full", OPENMRS_USER, OPENMRS_PWD);
		return new JSONObject(op.body());
	}
}
