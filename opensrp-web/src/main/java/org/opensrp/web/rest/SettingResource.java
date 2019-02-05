package org.opensrp.web.rest;

import static java.text.MessageFormat.format;
import static org.opensrp.web.rest.RestUtils.getStringFilter;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;
import org.opensrp.common.AllConstants;
import org.opensrp.common.AllConstants.BaseEntity;
import org.opensrp.domain.postgres.SettingsMetadata;
import org.opensrp.domain.setting.SettingConfiguration;
import org.opensrp.service.SettingService;
import org.opensrp.util.DateTimeTypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Controller
@RequestMapping(value = "/rest/settings")
public class SettingResource {
	
	private SettingService settingService;
	
	private static Logger logger = LoggerFactory.getLogger(EventResource.class.toString());
	
	private String TEAM_ID = "teamId";
	
	Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
	        .registerTypeAdapter(DateTime.class, new DateTimeTypeConverter()).create();
	
	@Autowired
	public void setSettingService(SettingService settingService) {
		this.settingService = settingService;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/sync")
	@ResponseBody
	public List<SettingConfiguration> findSettingsByVersion(HttpServletRequest request) {
		String serverVersion = getStringFilter(BaseEntity.SERVER_VERSIOIN, request);
		String teamId = getStringFilter(TEAM_ID, request);
		Long lastSyncedServerVersion = null;
		if (serverVersion != null) {
			lastSyncedServerVersion = Long.valueOf(serverVersion) + 1;
		}
		return settingService.findLatestSettingsByVersionAndTeamId(lastSyncedServerVersion, teamId);
	}
	
	@RequestMapping(headers = { "Accept=application/json" }, method = POST, value = "/sync")
	public ResponseEntity<JSONObject> saveSetting(@RequestBody String data) {
		JSONObject response = new JSONObject();
		
		try {
			JSONObject syncData = new JSONObject(data);
			
			if (!syncData.has("settingConfigurations")) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			} else {
				
				JSONArray clientSettings = syncData.getJSONArray(AllConstants.Event.SETTING_CONFIGURATIONS);
				JSONArray dbSettingsArray = new JSONArray();
				
				SettingsMetadata dbSettingMetadata = null;
				
				for (int i = 0; i < clientSettings.length(); i++) {
					
					dbSettingMetadata = settingService.saveSetting(clientSettings.getString(i).toString());
					dbSettingsArray.put(dbSettingMetadata.getIdentifier());
					
				}
				
				response.append("validated_records", dbSettingsArray);
				
			}
			
		}
		
		catch (Exception e) {
			logger.error(format("Sync data processing failed with exception {0}.- ", e));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<JSONObject>(response, HttpStatus.OK);
	}
}
