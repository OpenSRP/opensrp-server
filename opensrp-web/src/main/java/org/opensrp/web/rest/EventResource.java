package org.opensrp.web.rest;

import static java.text.MessageFormat.format;
import static org.opensrp.common.AllConstants.BaseEntity.BASE_ENTITY_ID;
import static org.opensrp.common.AllConstants.BaseEntity.LAST_UPDATE;
import static org.opensrp.common.AllConstants.Event.ENTITY_TYPE;
import static org.opensrp.common.AllConstants.Event.EVENT_DATE;
import static org.opensrp.common.AllConstants.Event.EVENT_TYPE;
import static org.opensrp.common.AllConstants.Event.LOCATION_ID;
import static org.opensrp.common.AllConstants.Event.PROVIDER_ID;
import static org.opensrp.common.AllConstants.Event.TEAM;
import static org.opensrp.common.AllConstants.Event.TEAM_ID;
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
import org.opensrp.domain.postgres.CustomQuery;
import org.opensrp.search.AddressSearchBean;
import org.opensrp.search.ClientSearchBean;
import org.opensrp.search.EventSearchBean;
import org.opensrp.service.ClientService;
import org.opensrp.service.EventService;
import org.opensrp.util.DateTimeTypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
	
	@Value("#{opensrp['opensrp.web.url']}")
	private String opensrpWebUrl;
	
	@Value("#{opensrp['opensrp.web.username']}")
	private String opensrpWebUsername;
	
	@Value("#{opensrp['opensrp.web.password']}")
	private String opensrpWebPassword;
	
	@Value("#{opensrp['opensrp.provider']}")
	private String provider;
	
	@Value("#{opensrp['opensrp.HA']}")
	private String HA;

//	@Value("#{opensrp['opensrp.role.ss']}")
	private Integer ss = 29;

//	@Value("#{opensrp['opensrp.location.tag.village']}")
	private Integer village = 33;
	
	Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
	        .registerTypeAdapter(DateTime.class, new DateTimeTypeConverter()).create();
	
	@Value("#{opensrp['opensrp.sync.search.missing.client']}")
	private boolean searchMissingClients;
	
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
	/*@RequestMapping(headers = { "Accept=application/json;charset=UTF-8" }, value = "/sync", method = RequestMethod.GET)
	@ResponseBody
	protected ResponseEntity<String> sync(HttpServletRequest request) {
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			String providerId = getStringFilter(PROVIDER_ID, request);
			String requestProviderName = request.getRemoteUser();
			String locationId = getStringFilter(LOCATION_ID, request);
			String baseEntityId = getStringFilter(BASE_ENTITY_ID, request);
			String serverVersion = getStringFilter(BaseEntity.SERVER_VERSIOIN, request);
			String team = getStringFilter(TEAM, request);
			String teamId = getStringFilter(TEAM_ID, request);
			logger.info("synced user " + providerId + locationId + teamId + ", timestamp : " + serverVersion);
			Long lastSyncedServerVersion = null;
			if (serverVersion != null) {
				lastSyncedServerVersion = Long.valueOf(serverVersion) + 1;
			}
			Integer limit = getIntegerFilter("limit", request);
			if (limit == null || limit.intValue() == 0) {
				limit = 25;
			}
			String getProviderName = "";
			List<Event> eventList = new ArrayList<Event>();
			List<Event> events = new ArrayList<Event>();
			List<Event> allEvent = new ArrayList<Event>();
			
			List<String> clientIds = new ArrayList<String>();
			List<Client> clients = new ArrayList<Client>();
			long startTime = System.currentTimeMillis();
			if (teamId != null || team != null || providerId != null || locationId != null || baseEntityId != null) {
				EventSearchBean eventSearchBean = new EventSearchBean();
				eventSearchBean.setTeam(team);
				eventSearchBean.setTeamId(teamId);
				eventSearchBean.setProviderId(providerId);
				eventSearchBean.setLocationId(locationId);
				eventSearchBean.setBaseEntityId(baseEntityId);
				eventSearchBean.setServerVersion(lastSyncedServerVersion);
				eventList = eventService.findEvents(eventSearchBean, BaseEntity.SERVER_VERSIOIN, "asc", limit);
				allEvent.addAll(eventList);
				logger.info("fetching events took: " + (System.currentTimeMillis() - startTime));
				logger.info("Initial Size:" + eventList.size());
				if (!eventList.isEmpty()) {
					for (Event event : eventList) {
						getProviderName = event.getProviderId();
						logger.info("getProviderName:" + getProviderName + ": request provider name" + requestProviderName);
						if (getProviderName.isEmpty()) {
							events.add(event);
						} else if (!getProviderName.equalsIgnoreCase(requestProviderName)) {} else {
							events.add(event);
						}
					}
					
					logger.info("After cleaning Size:" + events.size());
					for (Event event : events) {
						if (event.getBaseEntityId() != null && !event.getBaseEntityId().isEmpty()
						        && !clientIds.contains(event.getBaseEntityId())) {
							clientIds.add(event.getBaseEntityId());
						}
					}
					for (int i = 0; i < clientIds.size(); i = i + CLIENTS_FETCH_BATCH_SIZE) {
						int end = i + CLIENTS_FETCH_BATCH_SIZE < clientIds.size() ? i + CLIENTS_FETCH_BATCH_SIZE : clientIds
						        .size();
						clients.addAll(clientService.findByFieldValue(BASE_ENTITY_ID, clientIds.subList(i, end)));
					}
					logger.info("fetching clients took: " + (System.currentTimeMillis() - startTime));
				}
			}
			
			if (searchMissingClients) {
				
				List<String> foundClientIds = new ArrayList<>();
				for (Client client : clients) {
					foundClientIds.add(client.getBaseEntityId());
				}
				
				boolean removed = clientIds.removeAll(foundClientIds);
				if (removed) {
					for (String clientId : clientIds) {
						Client client = clientService.getByBaseEntityId(clientId);
						if (client != null) {
							clients.add(client);
						}
					}
				}
				logger.info("fetching missing clients took: " + (System.currentTimeMillis() - startTime));
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
	}*/
	
	/**
	 * Fetch events ordered by serverVersion ascending order and return the clients associated with
	 * the events
	 * 
	 * @param request
	 * @return a map response with events, clients and optionally msg when an error occurs
	 */
	@RequestMapping(headers = { "Accept=application/json;charset=UTF-8" }, value = "/sync", method = RequestMethod.GET)
	@ResponseBody
	protected ResponseEntity<String> sync(HttpServletRequest request) {
		String isEmptyToAdd = getStringFilter("isEmptyToAdd", request);
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			String dataProvider = request.getRemoteUser();
			CustomQuery customQuery = clientService.getUserStatus(dataProvider);
			
			if(customQuery != null && !customQuery.getEnable()){
				return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
			}
			CustomQuery user = eventService.getUser(request.getRemoteUser());
			CustomQuery teamMember = eventService.getTeamMemberId(user.getId());
			List<CustomQuery> locations = (teamMember != null)?
					clientService.getProviderLocationIdByChildRole(user.getId(), ss, village)
					: new ArrayList<CustomQuery>();
			logger.info("request.getRemoteUser():" + request.getRemoteUser());

			String location = "";
			String userType = "";
			List<Long> address = new ArrayList<Long>();
			if (locations.size() != 0) {
				for (CustomQuery locName : locations) {
					address.add(Long.valueOf(locName.getId()));
				}
				userType = "Provider";
				String providerId = request.getRemoteUser();//getStringFilter(PROVIDER_ID, request);
				String requestProviderName = request.getRemoteUser();
				String locationId = getStringFilter(LOCATION_ID, request);
				String baseEntityId = getStringFilter(BASE_ENTITY_ID, request);
				String serverVersion = getStringFilter(BaseEntity.SERVER_VERSIOIN, request);
				String team = getStringFilter(TEAM, request);
				String teamId = getStringFilter(TEAM_ID, request);
				logger.info("synced user " + providerId + locationId + teamId + ", timestamp : " + serverVersion);
				Long lastSyncedServerVersion = null;
				if (serverVersion != null) {
					lastSyncedServerVersion = Long.valueOf(serverVersion) + 1;
				}
				Integer limit = getIntegerFilter("limit", request);
				if (limit == null || limit.intValue() == 0) {
					limit = 25;
				}

				List<Event> events = new ArrayList<Event>();
				List<String> clientIds = new ArrayList<String>();
				List<Client> clients = new ArrayList<Client>();

				long startTime = System.currentTimeMillis();
				EventSearchBean eventSearchBean = new EventSearchBean();
				eventSearchBean.setTeam(team);
				eventSearchBean.setTeamId(teamId);
				eventSearchBean.setProviderId(providerId);
				eventSearchBean.setLocationId(locationId);
				eventSearchBean.setBaseEntityId(baseEntityId);
				eventSearchBean.setServerVersion(lastSyncedServerVersion);

				ClientSearchBean searchBean = new ClientSearchBean();
				searchBean.setServerVersion(lastSyncedServerVersion);
				AddressSearchBean addressSearchBean = new AddressSearchBean();

				addressSearchBean.setVillageId(address);

				if (isEmptyToAdd.equalsIgnoreCase("true")) {
					events = eventService.selectBySearchBean(addressSearchBean, lastSyncedServerVersion, providerId, 0);
				} else {
					events = eventService.findByProvider(lastSyncedServerVersion, providerId, 0);
				}

				List<String> ids = new ArrayList<String>();

				String field = "baseEntityId";

				logger.info("fetching events took: " + (System.currentTimeMillis() - startTime));
				logger.info("Initial Size:" + events.size());
				if (!events.isEmpty()) {
					for (Event event : events) {

						if (!clientIds.contains(event.getBaseEntityId())) {
							clientIds.add(event.getBaseEntityId());
						}

					}

					logger.info("fetching clients took: " + (System.currentTimeMillis() - startTime));
				}

				logger.info("ids size" + clientIds.size() + "IDs:" + clientIds.toString());
				ids.addAll(clientIds);
				clients = clientService.findByFieldValue(field, ids);

				JsonArray eventsArray = (JsonArray) gson.toJsonTree(events, new TypeToken<List<Event>>() {}.getType());

				JsonArray clientsArray = (JsonArray) gson.toJsonTree(clients, new TypeToken<List<Client>>() {}.getType());

				response.put("events", eventsArray);
				response.put("clients", clientsArray);
				response.put("no_of_events", events.size());

				return new ResponseEntity<>(gson.toJson(response), HttpStatus.OK);

			} else {
				logger.info("No location found..");
				System.err.println("NO LOCation found .....................");
				response.put("msg", "Error occurred");
				return new ResponseEntity<>(new Gson().toJson(response), HttpStatus.INTERNAL_SERVER_ERROR);
			}

		}
		catch (Exception e) {
			response.put("msg", "Error occurred");
			logger.error("", e);
			e.printStackTrace();
			return new ResponseEntity<>(new Gson().toJson(response), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(headers = { "Accept=application/json;charset=UTF-8" }, value = "/client-list-to-delete", method = RequestMethod.GET)
	@ResponseBody
	protected ResponseEntity<String> clientListToDeleteFromAPP(HttpServletRequest request) {
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			System.err.println("");
			/*List<CustomQuery> locations = eventService.getLocations(request.getRemoteUser());
			String location = "";
			String userType = "";
			List<String> address = new ArrayList<String>();
			if (locations.size() != 0) {
				for (CustomQuery locName : locations) {
					address.add(locName.getName());
				}
				userType = "Provider";
				String providerId = getStringFilter(PROVIDER_ID, request);
				String requestProviderName = request.getRemoteUser();
				String locationId = getStringFilter(LOCATION_ID, request);
				String baseEntityId = getStringFilter(BASE_ENTITY_ID, request);
				String serverVersion = getStringFilter(BaseEntity.SERVER_VERSIOIN, request);
				String team = getStringFilter(TEAM, request);
				String teamId = getStringFilter(TEAM_ID, request);
				logger.info("synced user " + providerId + locationId + teamId + ", timestamp : " + serverVersion);
				Long lastSyncedServerVersion = null;
				if (serverVersion != null) {
					lastSyncedServerVersion = Long.valueOf(serverVersion) + 1;
				}
				Integer limit = getIntegerFilter("limit", request);
				if (limit == null || limit.intValue() == 0) {
					limit = 25;
				}
				String getProviderName = "";
				List<Event> eventList = new ArrayList<Event>();
				
				List<String> clientIds = new ArrayList<String>();
				List<String> clients = new ArrayList<String>();
				List<Client> clientList = new ArrayList<Client>();
				
				long startTime = System.currentTimeMillis();
				EventSearchBean eventSearchBean = new EventSearchBean();
				eventSearchBean.setTeam(team);
				eventSearchBean.setTeamId(teamId);
				eventSearchBean.setProviderId(providerId);
				eventSearchBean.setLocationId(locationId);
				eventSearchBean.setBaseEntityId(baseEntityId);
				eventSearchBean.setServerVersion(lastSyncedServerVersion);
				eventList = eventService.findEvents(eventSearchBean, BaseEntity.SERVER_VERSIOIN, "asc", limit);
				ClientSearchBean searchBean = new ClientSearchBean();
				searchBean.setServerVersion(lastSyncedServerVersion);
				AddressSearchBean addressSearchBean = new AddressSearchBean();
				if (userType.equalsIgnoreCase(provider)) {
					addressSearchBean.setAddress2(address);
				} else if (userType.equalsIgnoreCase(HA)) {
					addressSearchBean.setAddress3(address);
				}
				clientList = clientService.findByCriteria(searchBean, addressSearchBean);
				for (Client client : clientList) {
					clientIds.add(client.getBaseEntityId());
				}
				
				List<String> ids = new ArrayList<String>();
				ids.addAll(clientIds);
				String field = "baseEntityId";
				eventList = eventService.findByFieldValue(field, ids, lastSyncedServerVersion);
				logger.info("fetching events took: " + (System.currentTimeMillis() - startTime));
				logger.info("Initial Size:" + eventList.size());
				if (!eventList.isEmpty()) {
					for (Event event : eventList) {
						getProviderName = event.getProviderId();
						logger.info("getProviderName:" + getProviderName + ": request provider name" + requestProviderName);
						if (getProviderName.isEmpty()) {

						} else if (!getProviderName.equalsIgnoreCase(requestProviderName)) {
							clients.add(event.getBaseEntityId());
						} else {

						}
					}
				}
				return new ResponseEntity<>(gson.toJson(clients), HttpStatus.OK);
				
			} else {
				logger.info("No location found..");
				return new ResponseEntity<>(new Gson().toJson(response), HttpStatus.INTERNAL_SERVER_ERROR);
			}*/
			return new ResponseEntity<>(new Gson().toJson(response), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch (Exception e) {
			response.put("msg", "Error occurred");
			e.printStackTrace();
			logger.error("", e);
			return new ResponseEntity<>(new Gson().toJson(response), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(headers = { "Accept=application/json;charset=UTF-8" }, method = POST, value = "/add")
	public ResponseEntity<HttpStatus> save(@RequestBody String data, HttpServletRequest request) {
		
		try {
			String dataProvider = request.getRemoteUser();
			CustomQuery customQuery = clientService.getUserStatus(dataProvider);
			
			if(customQuery != null && !customQuery.getEnable()){
				return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
			}
			JSONObject syncData = new JSONObject(data);
			if (!syncData.has("clients") && !syncData.has("events")) {
				return new ResponseEntity<>(BAD_REQUEST);
			}
			String getProvider = "";
			
			logger.info("dataProvider:" + dataProvider);
			
			if (syncData.has("events")) {
				ArrayList<Event> events = (ArrayList<Event>) gson.fromJson(syncData.getString("events"),
				    new TypeToken<ArrayList<Event>>() {}.getType());
				logger.info("received event size:" + events.size());
				System.out.println(dataProvider+": received event size:" + events.size());
				for (Event event : events) {
					try {
						event = eventService.processOutOfArea(event);
						event.withIsSendToOpenMRS("yes");
						eventService.addorUpdateEvent(event);
//						Client client = clientService.find(event.getBaseEntityId());
//						if (client != null) {
//							client.setServerVersion(System.currentTimeMillis());
//							clientService.addOrUpdate(client);
//						}
					} catch (Exception e) {
						logger.error(
						    "Event of type " + event.getEventType() + " for client " + event.getBaseEntityId() == null ? ""
						            : event.getBaseEntityId() + " failed to sync", e);
					}
				}
			}
			
			if (syncData.has("clients")) {
				
				ArrayList<Client> clients = (ArrayList<Client>) gson.fromJson(syncData.getString("clients"),
				    new TypeToken<ArrayList<Client>>() {}.getType());
				logger.info("received client size:" + clients.size());
				System.out.println(dataProvider+": received client size:" + clients.size());
				for (Client client : clients) {
					try {
						client.withIsSendToOpenMRS("yes");
						clientService.addOrUpdate(client);
					} catch (Exception e) {
						logger.error("Client" + client.getBaseEntityId() == null ? "" : client.getBaseEntityId()
						        + " failed to sync", e);
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
		String team = getStringFilter(TEAM, request);
		String teamId = getStringFilter(TEAM_ID, request);
		
		if (!StringUtils.isEmptyOrWhitespaceOnly(clientId)) {
			Client c = clientService.find(clientId);
			if (c == null) {
				return new ArrayList<>();
			}
			
			clientId = c.getBaseEntityId();
		}
		EventSearchBean eventSearchBean = new EventSearchBean();
		eventSearchBean.setBaseEntityId(clientId);
		eventSearchBean.setEventDateFrom(eventDate == null ? null : eventDate[0]);
		eventSearchBean.setEventDateTo(eventDate == null ? null : eventDate[1]);
		eventSearchBean.setEventType(eventType);
		eventSearchBean.setEntityType(entityType);
		eventSearchBean.setProviderId(provider);
		eventSearchBean.setLocationId(location);
		eventSearchBean.setLastEditFrom(lastEdit == null ? null : lastEdit[0]);
		eventSearchBean.setLastEditTo(lastEdit == null ? null : lastEdit[1]);
		eventSearchBean.setTeam(team);
		eventSearchBean.setTeamId(teamId);
		
		return eventService.findEventsBy(eventSearchBean);
	}
	
	@Override
	public List<Event> filter(String query) {
		return eventService.findEventsByDynamicQuery(query);
	}
	
}
