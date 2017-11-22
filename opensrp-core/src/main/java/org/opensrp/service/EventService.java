package org.opensrp.service;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ektorp.CouchDbConnector;
import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;
import org.opensrp.common.AllConstants.Client;
import org.opensrp.domain.Event;
import org.opensrp.domain.Obs;
import org.opensrp.repository.AllEvents;
import org.opensrp.util.DateTimeTypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Service
public class EventService {

	private final AllEvents allEvents;

	private ClientService clientService;

	@Autowired
	public EventService(AllEvents allEvents, ClientService clientService) {
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
		try {
			List<Event> el = allEvents.findByBaseEntityAndFormSubmissionId(baseEntityId, formSubmissionId);
			return getUniqueEventFromEventList(el);
		}
		catch (IllegalArgumentException e) {
			throw new IllegalStateException(
					"Multiple events for baseEntityId and formSubmissionId combination (" + baseEntityId + ","
							+ formSubmissionId + ")");
		}
	}

	public Event getByBaseEntityAndFormSubmissionId(CouchDbConnector targetDb, String baseEntityId,
	                                                String formSubmissionId) {
		try {
			List<Event> el = allEvents.findByBaseEntityAndFormSubmissionId(targetDb, baseEntityId, formSubmissionId);
			return getUniqueEventFromEventList(el);
		}
		catch (IllegalArgumentException e) {
			throw new IllegalStateException(
					"Multiple events for baseEntityId and formSubmissionId combination (" + baseEntityId + ","
							+ formSubmissionId + ")");
		}
		catch (Exception e) {
			return null;
		}
	}

	public List<Event> findByBaseEntityId(String baseEntityId) {
		return allEvents.findByBaseEntityId(baseEntityId);
	}

	public List<Event> findByFormSubmissionId(String formSubmissionId) {
		return allEvents.findByFormSubmissionId(formSubmissionId);
	}

	public List<Event> findEventsBy(String baseEntityId, DateTime from, DateTime to, String eventType, String entityType,
	                                String providerId, String locationId, DateTime lastEditFrom, DateTime lastEditTo) {
		return allEvents
				.findEvents(baseEntityId, from, to, eventType, entityType, providerId, locationId, lastEditFrom, lastEditTo);
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
				throw new IllegalArgumentException(
						"Multiple events with identifier type " + idt + " and ID " + event.getIdentifier(idt) + " exist.");
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
			List<org.opensrp.domain.Client> clients = clientService
					.findAllByIdentifier(Client.ZEIR_ID.toUpperCase(), identifier);
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

	public synchronized Event addEvent(CouchDbConnector targetDb, Event event) {
		//		Event e = find(targetDb,event);
		//		if(e != null){
		//			throw new IllegalArgumentException("An event already exists with given list of identifiers. Consider updating data.["+e+"]");
		//		}
		if (event.getFormSubmissionId() != null
				&& getByBaseEntityAndFormSubmissionId(targetDb, event.getBaseEntityId(), event.getFormSubmissionId())
				!= null) {
			throw new IllegalArgumentException(
					"An event already exists with given baseEntity and formSubmission combination. Consider updating");
		}

		event.setDateCreated(new DateTime());

		allEvents.add(targetDb, event);
		return event;
	}

	public synchronized Event addorUpdateEvent(Event event) {
		Event existingEvent = findById(event.getId());
		if (existingEvent != null) {
			event.setDateEdited(DateTime.now());
			event.setServerVersion(null);
			event.setRevision(existingEvent.getRevision());
			allEvents.update(event);

		} else {
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
						original.getObs(null, o.getFieldCode())
								.setValue(o.getValues().size() < 2 ? o.getValue() : o.getValues());
					}
				}
				for (String k : updatedEvent.getIdentifiers().keySet()) {
					original.addIdentifier(k, updatedEvent.getIdentifier(k));
				}
			}

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

	public List<Event> getAll() {
		return allEvents.getAll();
	}

	public List<Event> findEvents(String team, String providerId, String locationId, Long serverVersion, String sortBy,
	                              String sortOrder, int limit) {
		return allEvents.findEvents(team, providerId, locationId, null, serverVersion, sortBy, sortOrder, limit);
	}

	public List<Event> findEvents(String team, String providerId, String locationId, String baseEntityId, Long serverVersion,
	                              String sortBy, String sortOrder, int limit) {
		return allEvents.findEvents(team, providerId, locationId, baseEntityId, serverVersion, sortBy, sortOrder, limit);
	}

	public List<Event> findEventsByConceptAndValue(String concept, String conceptValue) {
		return allEvents.findByConceptAndValue(concept, conceptValue);

	}

	public List<Event> findByBaseEntityAndType(String baseEntityId, String eventType) {
		return allEvents.findByBaseEntityAndType(baseEntityId, eventType);

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

}
