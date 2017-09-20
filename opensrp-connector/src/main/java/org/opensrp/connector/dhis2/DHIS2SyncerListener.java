package org.opensrp.connector.dhis2;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.motechproject.scheduler.domain.MotechEvent;
import org.motechproject.server.event.annotations.MotechListener;
import org.opensrp.common.AllConstants.DHIS2Constants;
import org.opensrp.common.util.DateUtil;
import org.opensrp.domain.Client;
import org.opensrp.domain.DHIS2Marker;
import org.opensrp.repository.AllDHIS2Marker;
import org.opensrp.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DHIS2SyncerListener {
	
	private final ClientService clientService;
	
	@Autowired
	private AllDHIS2Marker allDHIS2Marker;
	
	@Autowired
	private Dhis2TrackCaptureConnector dhis2TrackCaptureConnector;
	
	@Autowired
	public DHIS2SyncerListener(ClientService clientService) {
		this.clientService = clientService;
	}
	
	@MotechListener(subjects = DHIS2Constants.DHIS2_TRACK_DATA_SYNCER_SUBJECT)
	public JSONObject pushToDHIS2(MotechEvent event) {
		JSONObject response = null;
		try {
			Long start = 0l;
			List<DHIS2Marker> lastsync = allDHIS2Marker.findByName(DHIS2Constants.DHIS2_TRACK_DATA_SYNCER_VERSION_MARKER);
			
			if (lastsync.size() == 0) {
				allDHIS2Marker.add();
				start = 0l;
			} else {
				start = lastsync == null || lastsync.get(0).getValue() == null ? 0 : lastsync.get(0).getValue();
			}
			List<Client> cl = clientService.findByServerVersion(start);
			for (Client c : cl) {
				try {
					response = sentTrackCaptureDataToDHIS2(c);
				}
				catch (Exception e) {
					System.out.println("DHIS2 Message:" + e.getMessage());
				}
			}
			allDHIS2Marker.update();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return response;
	}
	
	public JSONObject sentTrackCaptureDataToDHIS2(Client client) throws JSONException {
		
		JSONObject clientData = new JSONObject();
		JSONArray clientAttribute = new JSONArray();
		
		JSONObject fullName = new JSONObject();
		fullName.put("attribute", "pzuh7zrs9Xx");
		fullName.put("value", client.fullName());
		clientAttribute.put(fullName);
		
		JSONObject gender = new JSONObject();
		gender.put("attribute", "xDvyz0ezL4e");
		gender.put("value", client.getGender());
		clientAttribute.put(gender);
		if (client.getAttributes().containsKey("Father_NRC_Number")) {
			JSONObject Father_NRC_Number = new JSONObject();
			Father_NRC_Number.put("attribute", "UpMkVyXSk4b");
			Father_NRC_Number.put("value", client.getAttributes().get("Father_NRC_Number"));
			clientAttribute.put(Father_NRC_Number);
		}
		
		if (client.getAttributes().containsKey("Child_Register_Card_Number")) {
			JSONObject Child_Register_Card_Number = new JSONObject();
			Child_Register_Card_Number.put("attribute", "P5Ew7lka7GR");
			Child_Register_Card_Number.put("value", client.getAttributes().get("Child_Register_Card_Number"));
			clientAttribute.put(Child_Register_Card_Number);
		}
		if (client.getAttributes().containsKey("CHW_Phone_Number")) {
			JSONObject CHW_Phone_Number = new JSONObject();
			CHW_Phone_Number.put("attribute", "wCom53wUTKf");
			CHW_Phone_Number.put("value", client.getAttributes().get("CHW_Phone_Number"));
			clientAttribute.put(CHW_Phone_Number);
		}
		
		if (client.getAttributes().containsKey("CHW_Name")) {
			JSONObject CHW_Name = new JSONObject();
			CHW_Name.put("attribute", "t2C80PnQfJH");
			CHW_Name.put("value", client.getAttributes().get("CHW_Name"));
			clientAttribute.put(CHW_Name);
		}
		
		if (client.getAttributes().containsKey("Child_Birth_Certificate")) {
			JSONObject Child_Birth_Certificate = new JSONObject();
			Child_Birth_Certificate.put("attribute", "ZDWzVhjlgWK");
			Child_Birth_Certificate.put("value", client.getAttributes().get("Child_Birth_Certificate"));
			clientAttribute.put(Child_Birth_Certificate);
		}
		
		/////////////////////
		JSONArray enrollments = new JSONArray();
		JSONObject enrollmentsObj = new JSONObject();
		enrollmentsObj.put("orgUnit", "IDc0HEyjhvL");
		enrollmentsObj.put("program", "OprRhyWVIM6");
		enrollmentsObj.put("enrollmentDate", DateUtil.getTodayAsString());
		enrollmentsObj.put("incidentDate", DateUtil.getTodayAsString());
		enrollments.put(enrollmentsObj);
		
		clientData.put("attributes", clientAttribute);
		clientData.put("trackedEntity", "MCPQUTHX1Ze");
		clientData.put("orgUnit", "IDc0HEyjhvL");
		
		return dhis2TrackCaptureConnector.trackCaptureDataSendToDHIS2(clientData);
	}
}
