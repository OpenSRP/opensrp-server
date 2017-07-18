package org.opensrp.register.service.handler;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opensrp.domain.Client;
import org.opensrp.domain.Event;
import org.opensrp.service.ClientService;
import org.opensrp.service.formSubmission.handler.EventsHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
abstract class BaseScheduleHandler implements EventsHandler {
	
	private static final String JSON_KEY_CONCEPT = "concept";
	private static final String JSON_KEY_PARENT_CODE = "parent_code";
	
	private static final String JSON_KEY_OBS = "obs";
	
	private static final String JSON_KEY_FIELD = "field";
	
	private static final String JSON_KEY_EVENT = "event";
	
	private static final String JSON_KEY_VALUE = "value";
	private static final String JSON_KEY_VALUES = "values";
	
	private static final String JSON_KEY_MILESTONE = "milestone";
	
	private static final String JSON_KEY_ACTION = "action";
	
	private static final String JSON_KEY_REFDATEFIELDS = "reference_date_fields";
	
	private static final String JSON_KEY_FULFILLMENTDATEFIELDS = "fulfillment_date_fields";
	private static final String JSON_KEY_FULFILLMENT_FIELDS = "fulfillment_fields";
	
	private static final String JSON_KEY_ENROLLMENTFIELDS = "enrollment_fields";
	
	private static final String JSON_KEY_EVENT_CONCEPT = "fieldCode";
	private static final String JSON_KEY_EVENT_PARENT_CONCEPT = "parentCode";
	
	private static final String JSON_KEY_NOTEMPTY = "NOT_EMPTY";
	
	private static final String JSON_KEY_PASSLOGIC = "pass_logic";
	
	private static final String JSON_KEY_FORMSUBMISSIONFIELD = "formSubmissionField";
	
	private static final String JSON_KEY_TYPE = "type";
	
	protected static Logger logger = LoggerFactory.getLogger(BaseScheduleHandler.class.toString());
	
	protected static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	
    private ClientService clientService;
    @Autowired
    public void setClientService(ClientService clientService){
        this.clientService = clientService;
    }
	/**
	 * Converts values in a json key into key-value pair
	 * 
	 * @param scheduleConfigEvent
	 * @param key
	 * @return
	 */
	protected List<Map<String, Object>> getFields(JSONObject scheduleConfigEvent, String key) {
		Map<String, Object> fieldsMap = new HashMap<String, Object>();
		List<Map<String, Object>> fieldsList = new ArrayList<Map<String, Object>>();
		try {
			if (scheduleConfigEvent.has(key)) {
				JSONArray fieldsArray = scheduleConfigEvent.getJSONArray(key);
				if (fieldsArray != null && fieldsArray.length() > 0) {
					for (int i = 0; i < fieldsArray.length(); i++) {
						JSONObject jsonObject = fieldsArray.getJSONObject(i);
						fieldsMap = jsonObjectToMap(jsonObject);
						//for concepts with parentcode, concatenate parentcode and concept to form a single key
						if(fieldsMap.containsKey(JSON_KEY_PARENT_CODE)){
													
							fieldsMap.put(JSON_KEY_CONCEPT, fieldsMap.get(JSON_KEY_CONCEPT)+"-"+fieldsMap.get(JSON_KEY_PARENT_CODE));
						}
						fieldsList.add(fieldsMap);
						
					}
				}
			}
		}
		catch (Exception e) {
			logger.error("", e);
		}
		return fieldsList;
	}
	
	protected String getMilestone(JSONObject scheduleConfigEvent) throws JSONException {
		String milestone = scheduleConfigEvent.has(JSON_KEY_MILESTONE) ? scheduleConfigEvent.getString(JSON_KEY_MILESTONE)
		        : "";
		return milestone;
	}
	
	protected String getAction(JSONObject scheduleConfigEvent) throws JSONException {
		String action = scheduleConfigEvent.has(JSON_KEY_ACTION) ? scheduleConfigEvent.getString(JSON_KEY_ACTION) : "";
		return action;
	}
	
	protected Map<String, Object> getReferenceDateFields(JSONObject scheduleConfigEvent) throws JSONException {
		JSONArray jsonArray = scheduleConfigEvent.has(JSON_KEY_REFDATEFIELDS)
		        ? scheduleConfigEvent.getJSONArray(JSON_KEY_REFDATEFIELDS) : null;
		
		        Map<String, Object> refDateFields = jsonObjectToMap(jsonArray.getJSONObject(0));
		        //for concepts with parentcode, concatenate parentcode and concept to form a single key
		        if(refDateFields.containsKey(JSON_KEY_PARENT_CODE)){
		        refDateFields.put(JSON_KEY_CONCEPT, refDateFields.get(JSON_KEY_CONCEPT)+"-"+refDateFields.get(JSON_KEY_PARENT_CODE));
		        }
		        return refDateFields;
	}
	
	/**
	 * Get schedule reference date from the reference_date_fields key
	 * 
	 * @param event
	 * @param scheduleConfigEvent
	 * @param action
	 * @return
	 * @throws JSONException
	 */
	protected String getReferenceDateForSchedule(Event event, JSONObject scheduleConfigEvent, String action)
	    throws JSONException {
		Map<String, Object> refDateFields = new HashMap<String, Object>();
		if (action.equalsIgnoreCase(ActionType.enroll.toString())) {
			refDateFields = getReferenceDateFields(scheduleConfigEvent);
		} else if (action.equalsIgnoreCase(ActionType.fulfill.toString())) {
			refDateFields = getFulfillmentDateFields(scheduleConfigEvent);
		}
		JSONObject eventJson = objectToJson(event);
		Map<String, Object> obs = getEventObs(eventJson);
		Map<String, Object> obsByFormSubmissionField = getEventObsByFormSubmissionField(eventJson);
		String dateStr = "";
		for (Map.Entry<String, Object> entry : refDateFields.entrySet()) {
			String key = entry.getKey();//"concept"
			String value = entry.getValue().toString();//"concept value or fieldname"
			if (key.equalsIgnoreCase(JSON_KEY_CONCEPT) && !refDateFields.containsKey(JSON_KEY_EVENT)) {
				//date is a concept and in the current event being processed search it in the event's obs
				if (obs.containsKey(value) && !obs.get(value).toString().isEmpty()) {
					dateStr = getDateValue(obs.get(value));
				}
			} else if (key.equalsIgnoreCase(JSON_KEY_CONCEPT) && refDateFields.containsKey(JSON_KEY_EVENT)) {
				//date is a concept and not in the current event being processed search it in the other event's obs
				//TODO fetch latest event of the type specified here from the db
				if (obs.containsKey(value) && !obs.get(value).toString().isEmpty()) {
					dateStr = getDateValue(obs.get(value));
				}
			} else if (key.equalsIgnoreCase(JSON_KEY_FIELD) && (refDateFields.get(JSON_KEY_TYPE) == null || refDateFields.get(JSON_KEY_TYPE).toString().equalsIgnoreCase("Event"))) {
				//date is a not a concept but indeed a field in the current event being processed search it in the event's doc
				if (eventJson.has(value) && !eventJson.getString(value).isEmpty()) {
					dateStr = getDateValue(eventJson.get(value));
				}
			} else if(key.equalsIgnoreCase(JSON_KEY_FORMSUBMISSIONFIELD)){
				if(obsByFormSubmissionField.containsKey(value) && !obsByFormSubmissionField.get(value).toString().isEmpty()){
					dateStr = getDateValue(obsByFormSubmissionField.get(value));
				}
			} else if(key.equalsIgnoreCase(JSON_KEY_TYPE) && value.equalsIgnoreCase("Client")){
				Client client = getClient(event);
				
				String fieldValue = refDateFields.get(JSON_KEY_FIELD) != null? refDateFields.get(JSON_KEY_FIELD).toString() : null;
				
				if(client != null && fieldValue != null){
					JSONObject clientObject  = objectToJson(client);
					if (clientObject.has(fieldValue) && !clientObject.getString(fieldValue).isEmpty()) {
						dateStr = getDateValue(clientObject.get(fieldValue));
					}
				}
				
			}
			
		}
		return dateStr;
	}
	
	/**
	 * sometimes date is in long value convert to the right format
	 * 
	 * @param value
	 * @return
	 */
	private String getDateValue(Object value) {
		String dateStr = "";
		
		if (value instanceof Long) {//sometimes date is in long for some reason
			Date date = new Date(Long.valueOf(value.toString()));
			dateStr = dateFormat.format(date);
		} else if (value.toString().contains("T")) {//sometimes the ref date is the format 2016-08-20T17:45:00.000+03:00
			int substrIndex=value.toString().indexOf("T");
			dateStr=value.toString().substring(0,substrIndex);
		} else {
			dateStr = value.toString();
			
		}
		return dateStr;
	}
	
	protected Map<String, Object> getFulfillmentDateFields(JSONObject scheduleConfigEvent) throws JSONException {
		
		JSONArray jsonArray = scheduleConfigEvent.has(JSON_KEY_FULFILLMENTDATEFIELDS)
		        ? scheduleConfigEvent.getJSONArray(JSON_KEY_FULFILLMENTDATEFIELDS) : null;
		        Map<String, Object> refDateFields = jsonObjectToMap(jsonArray.getJSONObject(0));
		        //for concepts with parentcode, concatenate parentcode and concept to form a single key
		        if(refDateFields.containsKey(JSON_KEY_PARENT_CODE)){
		        refDateFields.put(JSON_KEY_CONCEPT, refDateFields.get(JSON_KEY_CONCEPT)+"-"+refDateFields.get(JSON_KEY_PARENT_CODE));
		        }
		return refDateFields;
	}
	
	protected String getPassLogic(JSONObject scheduleConfigEvent) throws JSONException {
		String passLogic = scheduleConfigEvent.has(JSON_KEY_PASSLOGIC) ? scheduleConfigEvent.getString(JSON_KEY_PASSLOGIC)
		        : "";
		return passLogic;
	}
	
	protected enum ActionType {
		enroll, unenroll, fulfill
	}
	
	/**
	 * check if event qualifies the client into a schedule by comparing the enrollment_fields and
	 * fulfillment_date_fields values with the values in the event (from the db)
	 * 
	 * @param event
	 * @param scheduleConfigEvent
	 * @return
	 * @throws JSONException
	 */
	protected boolean evaluateEvent(Event event, JSONObject scheduleConfigEvent) throws JSONException {
		String passLogic = getPassLogic(scheduleConfigEvent);
		String action = getAction(scheduleConfigEvent);
		List<Map<String, Object>> fieldsList = new ArrayList<Map<String, Object>>();
		if (action.equalsIgnoreCase(ActionType.enroll.toString())) {
			fieldsList = getFields(scheduleConfigEvent, JSON_KEY_ENROLLMENTFIELDS);
		} else if (action.equalsIgnoreCase(ActionType.fulfill.toString())) {
			fieldsList = getFields(scheduleConfigEvent, JSON_KEY_FULFILLMENT_FIELDS);
		}
		
		JSONObject eventJson = objectToJson(event);
		Map<String, Object> obs = getEventObs(eventJson);
		
		Map<String, Object> obsByFormSubmissionField =  getEventObsByFormSubmissionField(eventJson);
		boolean result = false;
		List<Boolean> results= new ArrayList<Boolean>();//sometimes there are more than one conditions to be satisfied
		for (Map<String, Object> scheduleFields : fieldsList) {
			for (Map.Entry<String, Object> entry : scheduleFields.entrySet()) {
				String key = entry.getKey();//"concept"
				String value = entry.getValue().toString();//"concept value from the json config file"
				String scheduleValue = scheduleFields.get(JSON_KEY_VALUE).toString();//"value- either not_empty or a concept mapping"
				if (key.equalsIgnoreCase(JSON_KEY_CONCEPT)) {
					//it's a concept search it in the event's obs
					//key="fieldCode";
					if (obs.containsKey(value)) {//check if the concept mapping exists in the obs
						if (obs.get(value).toString().equalsIgnoreCase(scheduleValue)
						        || (!obs.get(value).toString().isEmpty()
						                && scheduleValue.equalsIgnoreCase(JSON_KEY_NOTEMPTY))) {
							results.add(true);
							//passlogic AND means that all the fields must have the specified values in the schedule configs else just return when the first value is true
							if (!passLogic.equalsIgnoreCase("AND"))
								return result;
						}else{
							results.add(false);
						}
					}
				} else if (key.equalsIgnoreCase(JSON_KEY_FIELD)) { //not a concept so get the value from the main doc e.g eventDate
					String fieldValue = eventJson.has(value) ? eventJson.getString(value) : "";
				//	String fieldValue = eventJson.has(fieldName) ? eventJson.getString(fieldName) : "";
					if (fieldValue.equalsIgnoreCase(scheduleValue)
					        || (!fieldValue.isEmpty() && scheduleValue.equalsIgnoreCase(JSON_KEY_NOTEMPTY))) {
						results.add(true);
						//passlogic AND means that all the fields must have the specified values in the schedule configs else just return when the first value is true
						if (!passLogic.equalsIgnoreCase("AND"))
							return result;
					}
					
				} else if(key.equalsIgnoreCase(JSON_KEY_FORMSUBMISSIONFIELD)){
					if (obsByFormSubmissionField.containsKey(value)) {//check if the concept mapping exists in the obs
						if (obsByFormSubmissionField.get(value).toString().equalsIgnoreCase(scheduleValue)
						        || (!obsByFormSubmissionField.get(value).toString().isEmpty()
						                && scheduleValue.equalsIgnoreCase(JSON_KEY_NOTEMPTY))) {
							results.add(true);
							//passlogic AND means that all the fields must have the specified values in the schedule configs else just return when the first value is true
							if (!passLogic.equalsIgnoreCase("AND"))
								return result;
						}
					}
				}
				
			}
		}
		return (results.contains(false)|| results.isEmpty())?false:true;
	}
	
	/**
	 * Put all obs into a key(concept), value (concept value) pair for easier searching
 	 * To accomodate situations whereby we've concepts with parentcodes, concatenate the concept and parentcode to form the map key 	 * @param event
	 * @return
	 */
	private Map<String, Object> getEventObs(JSONObject event) {
		Map<String, Object> obs = new HashMap<String, Object>();
		try {
			if (event.has(JSON_KEY_OBS)) {
				JSONArray obsArray = event.getJSONArray(JSON_KEY_OBS);
				if (obsArray != null && obsArray.length() > 0) {
					for (int i = 0; i < obsArray.length(); i++) {
						JSONObject object = obsArray.getJSONObject(i);
						String key = object.has(JSON_KEY_EVENT_CONCEPT) ? object.getString(JSON_KEY_EVENT_CONCEPT) : null;
						key = (object.has(JSON_KEY_EVENT_PARENT_CONCEPT)&&!object.getString(JSON_KEY_EVENT_PARENT_CONCEPT).isEmpty()) ? key+"-"+object.getString(JSON_KEY_EVENT_PARENT_CONCEPT) : key; 
						String value = getConceptValue(object);
						// : object.has("values") ? object.get("values").toString() : null;
						if (key != null && value != null) {
							obs.put(key, value);
						}
					}
				}
			}
		}
		catch (Exception e) {
			logger.error("", e);
		}
		return obs;
	}
	
	/**
	 * Put all obs into a key(formSubmissionField), value (formSubmissionField value) pair for easier searching
	 * 
	 * @param event
	 * @return
	 */
	private Map<String, Object> getEventObsByFormSubmissionField(JSONObject event) {
		Map<String, Object> obs = new HashMap<String, Object>();
		try {
			if (event.has(JSON_KEY_OBS)) {
				JSONArray obsArray = event.getJSONArray(JSON_KEY_OBS);
				if (obsArray != null && obsArray.length() > 0) {
					for (int i = 0; i < obsArray.length(); i++) {
						JSONObject object = obsArray.getJSONObject(i);
						String key = object.has(JSON_KEY_FORMSUBMISSIONFIELD) ? object.getString(JSON_KEY_FORMSUBMISSIONFIELD) : null;
						String value = getConceptValue(object);
						if (key != null && value != null) {
							obs.put(key, value);
						}
					}
				}
			}
		}
		catch (Exception e) {
			logger.error("", e);
		}
		return obs;
	}
	
	private String getConceptValue(JSONObject object) throws JSONException{
		String value="";
		if(object.has(JSON_KEY_VALUE)){
			value=object.getString(JSON_KEY_VALUE);
		}else if(object.has(JSON_KEY_VALUES)){
			JSONArray array=object.getJSONArray(JSON_KEY_VALUES);
			value= array.length()>0?array.getString(0):"";
		}
		
		return value;
	}
	
	/**
	 * Convert object pojo to a jsonobject
	 * 
	 * @param event
	 * @return
	 */
	private JSONObject objectToJson(Object object) {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json;
		try {
			json = ow.writeValueAsString(object);
			JSONObject eventJson = new JSONObject(json);
			return eventJson;
		}
		catch (IOException e) {
			logger.error("", e);
			return null;
		}
		catch (JSONException e) {
			logger.error("", e);
			return null;
		}
		
	}
	
	/**
	 * Convert a jsonobject into a map -key/value
	 * 
	 * @param jsonObject
	 * @return
	 * @throws JSONException
	 */
	private Map<String, Object> jsonObjectToMap(JSONObject jsonObject) throws JSONException {
		
		if (jsonObject == null) {
			return null;
		}
		
		Map<String, Object> fieldsMap = new HashMap<String, Object>();
		
		for (int j = 0; j < jsonObject.names().length(); j++) {
			String jsonObjectKey = jsonObject.names().getString(j);
			String jsonObjectValue = jsonObject.get(jsonObject.names().getString(j)).toString();
			if (jsonObjectKey != null && jsonObjectValue != null) {
				fieldsMap.put(jsonObjectKey, jsonObjectValue);
			}
		}
		return fieldsMap;
	}
	
	private Client getClient(Event event){
		if(event == null){
			return null;
		}
		return clientService.getByBaseEntityId(event.getBaseEntityId());
	}
	
}
