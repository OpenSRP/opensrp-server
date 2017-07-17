package org.opensrp.service.formSubmission.handler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opensrp.domain.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component
public class EventsRouter {
	
	private static Logger logger = LoggerFactory.getLogger(EventsRouter.class.toString());
	
	@Autowired
	private IHandlerMapper handlerMapper;
	
	// @Value("#{opensrp['schedule.config.json.path']}")
	// String scheduleConfigPath;
	@Value("#{opensrp['schedules.config.files.dir']}")
	String scheduleConfigFilesPath;
	
	// private static final String JSON_KEY_SCHEDULES = "schedules";
	private static final String JSON_KEY_HANDLER = "handler";
	
	private static final String JSON_KEY_TYPES = "types";
	
	private static final String JSON_KEY_SCHEDULE_NAME = "name";
	
	private static final String JSON_KEY_EVENTS = "events";
	
	ResourceLoader loader = new DefaultResourceLoader();
	
	File scheduleConfigsFolder = null;
	
	Event event;

	@Autowired
	public EventsRouter(IHandlerMapper handlerMapper, @Value("#{opensrp['schedules.config.files.dir']}") String scheduleConfigFilesPath) {
		this.handlerMapper = handlerMapper;
		this.scheduleConfigFilesPath = scheduleConfigFilesPath;
	}

	/**
	 * @param event
	 */
	public void route(Event _event) {
		try {
			event = _event;
			if (scheduleConfigFilesPath != null && !scheduleConfigFilesPath.isEmpty()) {
				
				String schedulesStr = getScheduleConfigs();
				
				JSONArray schedulesJsonObject = new JSONArray("[" + schedulesStr + "]");
				//iterate through concatenated schedule-configs files to retrieve the events and compare with the current event from the db
				for (int i = 0; i < schedulesJsonObject.length(); i++) {
					JSONObject scheduleJsonObject = schedulesJsonObject.getJSONObject(i);
					String handler = scheduleJsonObject.has(JSON_KEY_HANDLER)?scheduleJsonObject.getString(JSON_KEY_HANDLER):"VaccinesScheduleHandler";
					JSONArray eventsJsonArray = scheduleJsonObject.getJSONArray(JSON_KEY_EVENTS);
					String scheduleName = scheduleJsonObject.getString(JSON_KEY_SCHEDULE_NAME);
					processScheduleConfigEvents(eventsJsonArray, handler,scheduleName);
					
				}
				
			}
		}
		catch (IOException | JSONException e) {
			logger.error("", e);
		}
		
	}
	
	/**
	 * This method iterates through 'events' defined in the schedule-configs to see if they match
	 * the current event, if so get the handler and process the event
	 * 
	 * @param eventsJsonArray
	 * @param handler
	 * @throws JSONException
	 */
	private void processScheduleConfigEvents(JSONArray eventsJsonArray, String handler,String scheduleName) throws JSONException {
		//iterate through the events in the scheduleconfigs to see if the current event (the one passed to this route method) has a schedule handler
		for (int j = 0; j < eventsJsonArray.length(); j++) {
			JSONObject scheduleConfigEvent = eventsJsonArray.getJSONObject(j);
			JSONArray eventTypesJsonArray = scheduleConfigEvent.getJSONArray(JSON_KEY_TYPES);
			List<String> eventsList = jsonArrayToList(eventTypesJsonArray);
			
			if (eventsList.contains(event.getEventType())) {
				if (handlerMapper.handlerMap().get(handler) != null) {
					handlerMapper.handlerMap().get(handler).handle(event, scheduleConfigEvent,scheduleName);
				}
				
			}
			
		}
	}
	
	/**
	 * This method merges all the files in the schedule-configs folder to create one
	 * jsonobject/array
	 * 
	 * @return
	 * @throws IOException
	 */
	private String getScheduleConfigs() throws IOException {
		if (scheduleConfigsFolder == null && loader.getResource(scheduleConfigFilesPath).exists())
			scheduleConfigFilesPath = loader.getResource(scheduleConfigFilesPath).getURI().getPath();
		scheduleConfigsFolder = new File(scheduleConfigFilesPath);
		String scheduleConfigMapping = "";
		File[] scheduleFiles = scheduleConfigsFolder.listFiles();
		for (int i = 0; i < scheduleFiles.length; i++) {
			final File fileEntry = scheduleFiles[i];
			String scheduleConfig = FileUtils.readFileToString(new File(fileEntry.getAbsolutePath()), "UTF-8");
			scheduleConfigMapping += (i + 1 == scheduleFiles.length) ? scheduleConfig : scheduleConfig.concat(",");
			
		}
		return scheduleConfigMapping;
	}
	
	private List<String> jsonArrayToList(JSONArray jsonArray) throws JSONException {
		List<String> values = new ArrayList<String>();
		if (jsonArray == null) {
			return values;
		}
		
		for (int i = 0; i < jsonArray.length(); i++) {
			values.add((String) jsonArray.get(i));
		}
		return values;
	}


}
