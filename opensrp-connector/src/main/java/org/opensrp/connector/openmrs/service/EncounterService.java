package org.opensrp.connector.openmrs.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opensrp.api.domain.Event;
import org.opensrp.api.domain.Obs;
import org.opensrp.common.util.HttpResponse;
import org.opensrp.connector.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;

import com.mysql.jdbc.StringUtils;

public class EncounterService extends OpenmrsService{
	private static final String ENCOUNTER_URL = "ws/rest/v1/encounter";
	private static final String ENCOUNTER__TYPE_URL = "ws/rest/v1/encountertype";
	private PatientService patientService;

	@Autowired
	public EncounterService(PatientService patientService) {
		this.patientService = patientService;
	}

	public EncounterService(String openmrsUrl, String user, String password) {
    	super(openmrsUrl, user, password);
	}
	
	public String createEncounter(Event e, String uuid) throws JSONException{
		//uuid = patientService.findpatient(e.getBaseEntityId());//TODO
		JSONObject enc = new JSONObject();
		enc.put("encounterDatetime", OPENMRS_DATE.format(e.getEventDate()));
		enc.put("patient", uuid);
		enc.put("encounterType", e.getEventType());
		enc.put("location", e.getLocationId());
		enc.put("provider", e.getProviderId());

		List<Obs> ol = e.getObs();
		Map<String, JSONObject> p = new HashMap<>();
		Map<String, List<JSONObject>> pc = new HashMap<>();
		
		for (Obs obs : ol) {
			//if no parent simply make it root obs
			if(StringUtils.isEmptyOrWhitespaceOnly(obs.getParentCode())){
				p.put(obs.getFieldCode(), convertObsToJson(obs));
			}
			else {
				//find parent obs if not found search and fill or create one
				JSONObject parentObs = p.get(obs.getParentCode());
				if(parentObs == null){
					p.put(obs.getParentCode(), convertObsToJson(getOrCreateParent(ol, obs)));
				}
				// find if any other exists with same parent if so add to the list otherwise create new list
				List<JSONObject> obl = pc.get(obs.getParentCode());
				if(obl == null){
					obl = new ArrayList<>();
				}
				obl.add(convertObsToJson(obs));
				pc.put(obs.getParentCode(), obl);
			}
		}
		
		JSONArray obar = new JSONArray();
		for (String ok : p.keySet()) {
			JSONObject obo = p.get(ok);
			
			List<JSONObject> cob = pc.get(ok);
			if(cob != null && cob.size() > 0) {
				obo.put("groupMembers", new JSONArray(cob));
			}
			
			obar.put(obo);
		}
		enc.put("obs", obar);
		
		HttpResponse op = HttpUtil.post(HttpUtil.removeEndingSlash(OPENMRS_BASE_URL)+"/"+ENCOUNTER_URL, "", enc.toString(), OPENMRS_USER, OPENMRS_PWD);
		return op.body();
	}
	
	private JSONObject convertObsToJson(Obs o) throws JSONException{
		JSONObject obo = new JSONObject();
		obo.put("concept", o.getFieldCode());
		if(o.getValue() != null && !StringUtils.isEmptyOrWhitespaceOnly(o.getValue().toString())) {
			obo.put("value", o.getValue());
		}
		
		return obo;
	}
	
	private Obs getOrCreateParent(List<Obs> obl, Obs o){
		for (Obs obs : obl) {
			if(o.getParentCode().equalsIgnoreCase(obs.getFieldCode())){
				return obs;
			}
		}
		return new Obs("concept", o.getParentCode(), null, null, null, null);
	}
}
