package org.opensrp.web.rest;

import static java.text.MessageFormat.format;
import static org.opensrp.common.AllConstants.BaseEntity.BASE_ENTITY_ID;
import static org.opensrp.common.AllConstants.BaseEntity.LAST_UPDATE;
import static org.opensrp.common.AllConstants.Event.ENTITY_TYPE;
import static org.opensrp.common.AllConstants.Event.EVENT_DATE;
import static org.opensrp.common.AllConstants.Event.EVENT_TYPE;
import static org.opensrp.common.AllConstants.Event.LOCATION_ID;
import static org.opensrp.common.AllConstants.Event.PROVIDER_ID;
import static org.opensrp.web.rest.RestUtils.getDateRangeFilter;
import static org.opensrp.web.rest.RestUtils.getIntegerFilter;
import static org.opensrp.web.rest.RestUtils.getStringFilter;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;
import org.json.JSONObject;
import org.opensrp.common.AllConstants.BaseEntity;
import org.opensrp.domain.Client;
import org.opensrp.domain.Event;
import org.opensrp.service.ClientService;
import org.opensrp.service.EventService;
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
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.mysql.jdbc.StringUtils;

@Controller
@RequestMapping(value = "/rest/event")
public class EventResource extends RestResource<Event> {
	
	private static Logger logger = LoggerFactory.getLogger(EventResource.class.toString());
	
	private EventService eventService;
	
	private ClientService clientService;
	
	Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
	        .registerTypeAdapter(DateTime.class, new DateTimeTypeConverter()).create();
	
	@Autowired
	public EventResource(ClientService clientService, EventService eventService) {
		this.clientService = clientService;
		this.eventService = eventService;
	}
	
	@Override
	public Event getByUniqueId(String uniqueId) {
		return eventService.find(uniqueId);
	}
	
	@RequestMapping(value = "/getall", method = RequestMethod.GET)
	@ResponseBody
	protected List<Event> getAll() {
		return eventService.getAll();
	}
	
	/**
	 * Fetch events ordered by serverVersion ascending order and return the clients associated with
	 * the events
	 * 
	 * @param request
	 * @return a map response with events, clients and optionally msg when an error occurs
	 */
	@RequestMapping(value = "/sync", method = RequestMethod.GET)
	@ResponseBody
	protected ResponseEntity<String> sync(HttpServletRequest request) {
		Map<String, Object> response = new HashMap<String, Object>();
		
		try {
			String providerId = getStringFilter(PROVIDER_ID, request);
			String locationId = getStringFilter(LOCATION_ID, request);
			String baseEntityId = getStringFilter(BASE_ENTITY_ID, request);
			String serverVersion = getStringFilter(BaseEntity.SERVER_VERSIOIN, request);
			Long lastSyncedServerVersion = null;
			if (serverVersion != null) {
				lastSyncedServerVersion = Long.valueOf(serverVersion) + 1;
			}
			String team = getStringFilter("team", request);
			Integer limit = getIntegerFilter("limit", request);
			if (limit == null || limit.intValue() == 0) {
				limit = 25;
			}
			
			List<Event> events = new ArrayList<Event>();
			List<String> clientIds = new ArrayList<String>();
			List<Client> clients = new ArrayList<Client>();
			if (team != null || providerId != null || locationId != null || baseEntityId != null) {
				events = eventService.findEvents(team, providerId, locationId, baseEntityId, lastSyncedServerVersion,
				    BaseEntity.SERVER_VERSIOIN, "asc", limit);
				if (!events.isEmpty()) {
					for (Event event : events) {
						if (event.getBaseEntityId() != null && !event.getBaseEntityId().isEmpty()
						        && !clientIds.contains(event.getBaseEntityId())) {
							clientIds.add(event.getBaseEntityId());
						}
					}
					clients = clientService.findByFieldValue(BaseEntity.BASE_ENTITY_ID, clientIds);
				}
			}
			
			JsonArray eventsArray = (JsonArray) gson.toJsonTree(events, new TypeToken<List<Event>>() {}.getType());
			
			JsonArray clientsArray = (JsonArray) gson.toJsonTree(clients, new TypeToken<List<Client>>() {}.getType());
			
			response.put("events", eventsArray);
			response.put("clients", clientsArray);
			response.put("no_of_events", events.size());
			
			return new ResponseEntity<>(gson.toJson(response), HttpStatus.OK);
			
		}
		catch (Exception e) {
			response.put("msg", "Error occurred");
			logger.error("", e);
			return new ResponseEntity<>(new Gson().toJson(response), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(headers = { "Accept=application/json" }, method = POST, value = "/add")
	public ResponseEntity<HttpStatus> save(@RequestBody String data) {
		try {
			JSONObject syncData = new JSONObject(data);
			if (!syncData.has("clients") && !syncData.has("events")) {
				return new ResponseEntity<>(BAD_REQUEST);
			}
			
			if (syncData.has("clients")) {
				
				ArrayList<Client> clients = (ArrayList<Client>) gson.fromJson(syncData.getString("clients"),
				    new TypeToken<ArrayList<Client>>() {}.getType());
				for (Client client : clients) {
					try {
						clientService.addorUpdate(client);
					}
					catch (Exception e) {
						logger.error(
						    "Client" + client.getBaseEntityId() == null ? "" : client.getBaseEntityId() + " failed to sync",
						    e);
					}
				}
				
			}
			if (syncData.has("events")) {
				ArrayList<Event> events = (ArrayList<Event>) gson.fromJson(syncData.getString("events"),
				    new TypeToken<ArrayList<Event>>() {}.getType());
				for (Event event : events) {
					try {
						event = eventService.processOutOfArea(event);
						eventService.addorUpdateEvent(event);
					}
					catch (Exception e) {
						logger.error(
						    "Event of type " + event.getEventType() + " for client " + event.getBaseEntityId() == null ? ""
						            : event.getBaseEntityId() + " failed to sync",
						    e);
					}
				}
			}
			
		}
		catch (Exception e) {
			logger.error(format("Sync data processing failed with exception {0}.- ", e));
			return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(CREATED);
	}
	
	/*	@RequestMapping(method=RequestMethod.GET)
		@ResponseBody
		public Event getByBaseEntityAndFormSubmissionId(@RequestParam String baseEntityId, @RequestParam String formSubmissionId) {
			return eventService.getByBaseEntityAndFormSubmissionId(baseEntityId, formSubmissionId);
		}*/
	
	@Override
	public Event create(Event o) {
		return eventService.addEvent(o);
	}
	
	@Override
	public List<String> requiredProperties() {
		List<String> p = new ArrayList<>();
		p.add(BASE_ENTITY_ID);
		//p.add(FORM_SUBMISSION_ID);
		p.add(EVENT_TYPE);
		//p.add(LOCATION_ID);
		//p.add(EVENT_DATE);
		p.add(PROVIDER_ID);
		//p.add(ENTITY_TYPE);
		return p;
	}
	
	@Override
	public Event update(Event entity) {
		return eventService.mergeEvent(entity);
	}
	
	public static void main(String[] args) {
		
	}
	
	@Override
	public List<Event> search(HttpServletRequest request) throws ParseException {
		String clientId = getStringFilter("identifier", request);
		DateTime[] eventDate = getDateRangeFilter(EVENT_DATE, request);//TODO
		String eventType = getStringFilter(EVENT_TYPE, request);
		String location = getStringFilter(LOCATION_ID, request);
		String provider = getStringFilter(PROVIDER_ID, request);
		String entityType = getStringFilter(ENTITY_TYPE, request);
		DateTime[] lastEdit = getDateRangeFilter(LAST_UPDATE, request);
		
		if (!StringUtils.isEmptyOrWhitespaceOnly(clientId)) {
			Client c = clientService.find(clientId);
			if (c == null) {
				return new ArrayList<>();
			}
			
			clientId = c.getBaseEntityId();
		}
		
		return eventService.findEventsBy(clientId, eventDate == null ? null : eventDate[0],
		    eventDate == null ? null : eventDate[1], eventType, entityType, provider, location,
		    lastEdit == null ? null : lastEdit[0], lastEdit == null ? null : lastEdit[1]);
	}
	
	@Override
	public List<Event> filter(String query) {
		return eventService.findEventsByDynamicQuery(query);
	}
	
}
