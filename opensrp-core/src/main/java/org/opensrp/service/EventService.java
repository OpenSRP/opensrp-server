package org.opensrp.service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opensrp.common.AllConstants.Client;
import org.opensrp.domain.Event;
import org.opensrp.domain.Obs;
import org.opensrp.domain.postgres.CustomQuery;
import org.opensrp.domain.postgres.HealthId;
import org.opensrp.repository.EventsRepository;
import org.opensrp.search.AddressSearchBean;
import org.opensrp.search.EventSearchBean;
import org.opensrp.util.DateTimeTypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Service
public class EventService {
	
	private final EventsRepository allEvents;
	
	private ClientService clientService;
	
	private Integer HEALTH_ID_LIMIT = 200;
	
	@Autowired
	public EventService(EventsRepository allEvents, ClientService clientService) {
		this.allEvents = allEvents;
		this.clientService = clientService;
	}
	
	public List<Event> findAllByIdentifier(String identifier) {
		return allEvents.findAllByIdentifier(identifier);
	}
	
	public List<Event> findAllByIdentifier(String identifierType, String identifier) {
		return allEvents.findAllByIdentifier(identifierType, identifier);
	}
	
	public Event getById(String id) {
		return allEvents.findById(id);
	}
	
	public Event getByBaseEntityAndFormSubmissionId(String baseEntityId, String formSubmissionId) {
		return allEvents.findByBaseEntityAndFormSubmissionId(baseEntityId, formSubmissionId);
	}
	
	public List<Event> findByBaseEntityId(String baseEntityId) {
		return allEvents.findByBaseEntityId(baseEntityId);
	}
	
	public Event findByFormSubmissionId(String formSubmissionId) {
		return allEvents.findByFormSubmissionId(formSubmissionId);
	}
	
	public List<Event> findByFieldValue(String field, List<String> ids, long serverVersion) {
		return allEvents.findByFieldValue(field, ids, serverVersion);
	}
	
	public List<Event> findEventsBy(EventSearchBean eventSearchBean) {
		return allEvents.findEvents(eventSearchBean);
	}
	
	public List<Event> findEventsByDynamicQuery(String query) {
		return allEvents.findEventsByDynamicQuery(query);
	}
	
	private static Logger logger = LoggerFactory.getLogger(EventService.class.toString());
	
	public Event find(String uniqueId) {
		try {
			List<Event> el = allEvents.findAllByIdentifier(uniqueId);
			return getUniqueEventFromEventList(el);
		}
		catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Multiple events with identifier " + uniqueId + " exist.");
		}
	}
	
	public Event find(Event event) {
		for (String idt : event.getIdentifiers().keySet()) {
			try {
				List<Event> el = allEvents.findAllByIdentifier(event.getIdentifier(idt));
				return getUniqueEventFromEventList(el);
			}
			catch (IllegalArgumentException e) {
				throw new IllegalArgumentException("Multiple events with identifier type " + idt + " and ID "
				        + event.getIdentifier(idt) + " exist.");
			}
		}
		return null;
	}
	
	public Event findById(String eventId) {
		try {
			if (eventId == null || eventId.isEmpty()) {
				return null;
			}
			return allEvents.findById(eventId);
		}
		catch (Exception e) {
			logger.error("", e);
		}
		return null;
	}
	
	public synchronized Event addEvent(Event event) {
		Event e = find(event);
		if (e != null) {
			throw new IllegalArgumentException(
			        "An event already exists with given list of identifiers. Consider updating data.[" + e + "]");
		}
		
		if (event.getFormSubmissionId() != null
		        && getByBaseEntityAndFormSubmissionId(event.getBaseEntityId(), event.getFormSubmissionId()) != null) {
			throw new IllegalArgumentException(
			        "An event already exists with given baseEntity and formSubmission combination. Consider updating");
		}
		
		event.setDateCreated(DateTime.now());
		allEvents.add(event);
		return event;
	}
	
	/**
	 * An out of area event is used to record services offered outside a client's catchment area.
	 * The event usually will have a client unique identifier(ZEIR_ID) as the only way to identify
	 * the client.This method finds the client based on the identifier and assigns a basentityid to
	 * the event
	 * 
	 * @param event
	 * @return
	 */
	public synchronized Event processOutOfArea(Event event) {
		if (event.getBaseEntityId() == null || event.getBaseEntityId().isEmpty()) {
			//get events identifiers;
			String identifier = event.getIdentifier(Client.ZEIR_ID);
			List<org.opensrp.domain.Client> clients = clientService.findAllByIdentifier(Client.ZEIR_ID.toUpperCase(),
			    identifier);
			if (clients != null && !clients.isEmpty()) {
				org.opensrp.domain.Client client = clients.get(0);
				//set providerid to the last providerid who served this client in their catchment (assumption)
				List<Event> existingEvents = findByBaseEntityAndType(client.getBaseEntityId(), "Birth Registration");
				if (existingEvents != null && !existingEvents.isEmpty()) {
					
					event.getIdentifiers().remove(Client.ZEIR_ID.toUpperCase());
					event.setBaseEntityId(client.getBaseEntityId());
					//Map<String, String> identifiers = event.getIdentifiers();
					//event identifiers are unique so removing zeir_id since baseentityid has been found
					//also out of area service events stick with the providerid so that they can sync back to them for reports generation
					if (!event.getEventType().startsWith("Out of Area Service")) {
						event.setProviderId(existingEvents.get(0).getProviderId());
						Map<String, String> details = new HashMap<String, String>();
						details.put("out_of_catchment_provider_id", event.getProviderId());
						event.setDetails(details);
					}
					
				}
				
			}
		}
		
		return event;
	}
	
	public synchronized Event addorUpdateEvent(Event event) {
		Integer eventId = allEvents.findEventIdByFormSubmissionId(event.getFormSubmissionId());
		
		//Event getEvent = findByFormSubmissionId(event.getFormSubmissionId());
		if (eventId != null) {
			Event getEvent = allEvents.findEventByEventId(eventId);
			if (getEvent != null) {
				
				event.setDateEdited(DateTime.now());
				event.setServerVersion(System.currentTimeMillis());
				event.setId(getEvent.getId());
				event.setDateCreated(getEvent.getDateCreated());
				allEvents.update(event);
			}
		} else {
			event.setServerVersion(System.currentTimeMillis());
			event.setDateCreated(DateTime.now());
			allEvents.add(event);
		}
		return event;
	}
	
	public void updateEvent(Event updatedEvent) {
		// If update is on original entity
		if (updatedEvent.isNew()) {
			throw new IllegalArgumentException(
			        "Event to be updated is not an existing and persisting domain object. Update database object instead of new pojo");
		}
		
		updatedEvent.setDateEdited(DateTime.now());
		allEvents.update(updatedEvent);
	}
	
	public void updateEventServerVersion(Event updatedEvent) {
		// If update is on original entity
		if (updatedEvent.isNew()) {
			throw new IllegalArgumentException(
			        "Event to be updated is not an existing and persisting domain object. Update database object instead of new pojo");
		}
		
		updatedEvent.setDateEdited(DateTime.now());
		updatedEvent.setServerVersion(System.currentTimeMillis());
		allEvents.update(updatedEvent);
	}
	
	//TODO Review and add test cases as well
	public Event mergeEvent(Event updatedEvent) {
		try {
			Event original = find(updatedEvent);
			if (original == null) {
				throw new IllegalArgumentException("No event found with given list of identifiers. Consider adding new!");
			}
			
			Gson gs = new GsonBuilder().registerTypeAdapter(DateTime.class, new DateTimeTypeConverter()).create();
			JSONObject originalJo = new JSONObject(gs.toJson(original));
			
			JSONObject updatedJo = new JSONObject(gs.toJson(updatedEvent));
			List<Field> fn = Arrays.asList(Event.class.getDeclaredFields());
			
			JSONObject mergedJson = new JSONObject();
			if (originalJo.length() > 0) {
				mergedJson = new JSONObject(originalJo, JSONObject.getNames(originalJo));
			}
			if (updatedJo.length() > 0) {
				for (Field key : fn) {
					String jokey = key.getName();
					if (updatedJo.has(jokey))
						mergedJson.put(jokey, updatedJo.get(jokey));
				}
				
				original = gs.fromJson(mergedJson.toString(), Event.class);
				
				for (Obs o : updatedEvent.getObs()) {
					// TODO handle parent
					if (original.getObs(null, o.getFieldCode()) == null) {
						original.addObs(o);
					} else {
						original.getObs(null, o.getFieldCode()).setComments(o.getComments());
						original.getObs(null, o.getFieldCode()).setEffectiveDatetime(o.getEffectiveDatetime());
						original.getObs(null, o.getFieldCode()).setValue(
						    o.getValues().size() < 2 ? o.getValue() : o.getValues());
					}
				}
				for (String k : updatedEvent.getIdentifiers().keySet()) {
					original.addIdentifier(k, updatedEvent.getIdentifier(k));
				}
			}
			for (String k : updatedEvent.getIdentifiers().keySet()) {
				original.addIdentifier(k, updatedEvent.getIdentifier(k));
			}
			original.setServerVersion(System.currentTimeMillis());
			original.setDateEdited(DateTime.now());
			allEvents.update(original);
			return original;
		}
		catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<Event> findByServerVersion(long serverVersion) {
		return allEvents.findByServerVersion(serverVersion);
	}
	
	public List<Event> notInOpenMRSByServerVersion(long serverVersion, Calendar calendar) {
		return allEvents.notInOpenMRSByServerVersion(serverVersion, calendar);
	}
	
	public List<Event> notInOpenMRSByServerVersionAndType(String type, long serverVersion, Calendar calendar) {
		return allEvents.notInOpenMRSByServerVersionAndType(type, serverVersion, calendar);
	}
	
	public List<Event> getAll() {
		return allEvents.getAll();
	}
	
	public List<Event> findEvents(EventSearchBean eventSearchBean, String sortBy, String sortOrder, int limit) {
		return allEvents.findEvents(eventSearchBean, sortBy, sortOrder, limit);
	}
	
	public List<Event> findEvents(EventSearchBean eventSearchBean) {
		return allEvents.findEvents(eventSearchBean);
	}
	
	public List<Event> findEventsByConceptAndValue(String concept, String conceptValue) {
		return allEvents.findByConceptAndValue(concept, conceptValue);
		
	}
	
	public List<Event> findByBaseEntityAndType(String baseEntityId, String eventType) {
		return allEvents.findByBaseEntityAndType(baseEntityId, eventType);
		
	}
	
	public List<Event> findByBaseEntityAndEventTypeContaining(String baseEntityId, String eventType) {
		return allEvents.findByBaseEntityAndEventTypeContaining(baseEntityId, eventType);
		
	}
	
	private Event getUniqueEventFromEventList(List<Event> events) throws IllegalArgumentException {
		if (events.size() > 1) {
			throw new IllegalArgumentException();
		}
		if (events.size() == 0) {
			return null;
		}
		return events.get(0);
	}
	
	public List<Event> findByProviderAndEntityType(String provider) {
		return allEvents.findByProvider(provider);
	}
	
	public void deleteByPrimaryKey(Event event) {
		allEvents.deleteByPrimaryKey(event);
		
	}
	
	public List<CustomQuery> getLocations(String userName) {
		CustomQuery user = allEvents.getUser(userName);
		if (user != null) {
			return allEvents.getLocations(user.getId());
		} else {
			return new ArrayList<CustomQuery>();
		}
		
	}
	
	public CustomQuery getUser(String username) {
		return allEvents.getUser(username);
	}
	
	public List<CustomQuery> getRoles(int userId) {
		List<CustomQuery> roles = allEvents.getRoles(userId);
		if (roles.size() != 0) {
			return roles;
		} else {
			return null;
		}
	}
	
	public CustomQuery getTeamMemberId(int userId) {
		return allEvents.getTeamMemberId(userId);
	}
	
	public JSONObject getHealthId() {
		List<HealthId> gethHealthIds = allEvents.gethealthIds(false, "Reserved");
		for (HealthId healthId : gethHealthIds) {}
		HealthId h = new HealthId();
		h.setId(530139);
		
		allEvents.updateHealthId(h);
		return null;
		
	}
	
	public List<Event> selectBySearchBean(AddressSearchBean addressSearchBean, long serverVersion, String providerId,
	                                      int limit) {
		return allEvents.selectBySearchBean(addressSearchBean, serverVersion, providerId, limit);
	}
	
	public List<Event> findByProvider(long serverVersion, String providerId, int limit) {
		return allEvents.selectByProvider(serverVersion, providerId, limit);
	}
	
	public Integer findEventIdByFormSubmissionId(String formSubmissionId) {
		return allEvents.findEventIdByFormSubmissionId(formSubmissionId);
	}
	
	public int insertHealthId(HealthId healthId) {
		return allEvents.insertHealthId(healthId);
	}
	
	public JSONArray generateHouseholdId(int[] villageIds) throws Exception {
		JSONArray villageCodes = new JSONArray();
		for (int i = 0; i < villageIds.length; i++) {
			if (villageIds[i] == 0)
				break;
			CustomQuery number = clientService.getMaxHealthId(villageIds[i]);
			
			//List<Integer> listOfInteger = IntStream.rangeClosed(number.getMaxHealthId()+1, number.getMaxHealthId()+HEALTH_ID_LIMIT).boxed().collect(Collectors.toList());
			//List<String> listOfString = convertIntListToStringList( listOfInteger, s -> StringUtils.leftPad(String.valueOf(s), 4, "0"));
			List<String> listOfString = allEvents.getHouseholdId(number.getMaxHealthId() + 1);
			System.err.println(listOfString);
			
			HealthId healthId = new HealthId();
			
			healthId.setCreated(new Date());
			healthId.sethId(String.valueOf(number.getMaxHealthId() + HEALTH_ID_LIMIT));
			healthId.setLocationId(villageIds[i]);
			healthId.setStatus(true);
			
			long isSaved = insertHealthId(healthId);
			if (isSaved > 0) {
				JSONObject villageCode = new JSONObject();
				villageCode.put("village_id", villageIds[i]);
				JSONArray ids = new JSONArray();
				for (String healthId1 : listOfString) {
					ids.put(healthId1);
				}
				villageCode.put("generated_code", ids);
				villageCodes.put(villageCode);
			}
		}
		return villageCodes;
	}
	
	public static <T, U> List<U> convertIntListToStringList(List<T> listOfInteger, Function<T, U> function) {
		return listOfInteger.stream().map(function).collect(Collectors.toList());
	}
	
}
