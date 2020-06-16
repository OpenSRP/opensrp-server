package org.opensrp.repository.couch;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.ektorp.ComplexKey;
import org.ektorp.CouchDbConnector;
import org.ektorp.UpdateConflictException;
import org.ektorp.support.GenerateView;
import org.ektorp.support.View;
import org.ektorp.util.Assert;
import org.ektorp.util.Documents;
import org.joda.time.DateTime;
import org.motechproject.dao.MotechBaseRepository;
import org.opensrp.common.AllConstants;
import org.opensrp.domain.Event;
import org.opensrp.domain.postgres.CustomQuery;
import org.opensrp.domain.postgres.HealthId;
import org.opensrp.repository.EventsRepository;
import org.opensrp.repository.lucene.LuceneEventRepository;
import org.opensrp.search.AddressSearchBean;
import org.opensrp.search.EventSearchBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository("couchEventsRepository")
@Primary
public class AllEvents extends MotechBaseRepository<Event> implements EventsRepository {
	
	private LuceneEventRepository ler;
	
	@Autowired
	protected AllEvents(@Qualifier(AllConstants.OPENSRP_DATABASE_CONNECTOR) CouchDbConnector db, LuceneEventRepository ler) {
		super(Event.class, db);
		this.ler = ler;
	}
	
	@View(name = "all_events_by_identifier", map = "function(doc) {if (doc.type === 'Event') {for(var key in doc.identifiers) {emit(doc.identifiers[key]);}}}")
	public List<Event> findAllByIdentifier(String identifier) {
		return db.queryView(createQuery("all_events_by_identifier").key(identifier).includeDocs(true), Event.class);
	}
	
	@View(name = "all_events_by_identifier_of_type", map = "function(doc) {if (doc.type === 'Event') {for(var key in doc.identifiers) {emit([key, doc.identifiers[key]]);}}}")
	public List<Event> findAllByIdentifier(String identifierType, String identifier) {
		ComplexKey ckey = ComplexKey.of(identifierType, identifier);
		return db.queryView(createQuery("all_events_by_identifier_of_type").key(ckey).includeDocs(true), Event.class);
	}
	
	public Event findById(String id) {
		Event event = db.get(Event.class, id);
		return event;
	}
	
	@GenerateView
	public Event findByFormSubmissionId(String formSubmissionId) {
		List<Event> events = queryView("by_formSubmissionId", formSubmissionId);
		if (events == null || events.isEmpty())
			return null;
		else if (events.size() > 1) {
			throw new IllegalStateException("Multiple events for formSubmissionId " + formSubmissionId);
		} else
			return events.get(0);
	}
	
	@GenerateView
	public List<Event> findByBaseEntityId(String baseEntityId) {
		return queryView("by_baseEntityId", baseEntityId);
	}
	
	@View(name = "all_events_by_base_entity_and_form_submission", map = "function(doc) { if (doc.type === 'Event'){  emit([doc.baseEntityId, doc.formSubmissionId], doc); } }")
	public Event findByBaseEntityAndFormSubmissionId(String baseEntityId, String formSubmissionId) {
		List<Event> events = db.queryView(
		    createQuery("all_events_by_base_entity_and_form_submission").key(ComplexKey.of(baseEntityId, formSubmissionId))
		            .includeDocs(true), Event.class);
		if (events == null || events.isEmpty())
			return null;
		else if (events.size() > 1) {
			throw new IllegalStateException("Multiple events for baseEntityId and formSubmissionId combination ("
			        + baseEntityId + "," + formSubmissionId + ")");
		} else
			return events.get(0);
		
	}
	
	@View(name = "all_events_by_base_entity_and_type", map = "function(doc) { if (doc.type === 'Event'){  emit([doc.baseEntityId, doc.eventType], doc); } }")
	public List<Event> findByBaseEntityAndType(String baseEntityId, String eventType) {
		return db.queryView(createQuery("all_events_by_base_entity_and_type").key(ComplexKey.of(baseEntityId, eventType))
		        .includeDocs(true), Event.class);
	}
	
	@View(name = "all_events_by_base_entity_and_form_submission", map = "function(doc) { if (doc.type === 'Event'){  emit([doc.baseEntityId, doc.formSubmissionId], doc); } }")
	public List<Event> findByBaseEntityAndFormSubmissionId(CouchDbConnector targetDb, String baseEntityId,
	                                                       String formSubmissionId) {
		return targetDb.queryView(
		    createQuery("all_events_by_base_entity_and_form_submission").key(ComplexKey.of(baseEntityId, formSubmissionId))
		            .includeDocs(true), Event.class);
	}
	
	public List<Event> findEvents(EventSearchBean eventSearchBean) {
		return ler.getByCriteria(eventSearchBean);
	}
	
	public List<Event> findEventsByDynamicQuery(String query) {
		return ler.getByCriteria(query);
	}
	
	/**
	 * Save event to the specified db
	 * 
	 * @throws UpdateConflictException if there was an update conflict.
	 */
	public void add(CouchDbConnector targetDb, Event event) {
		Assert.isTrue(Documents.isNew(event), "entity must be new");
		targetDb.create(event);
	}
	
	@View(name = "events_by_version", map = "function(doc) { if (doc.type === 'Event') { emit([doc.serverVersion], null); } }")
	public List<Event> findByServerVersion(long serverVersion) {
		ComplexKey startKey = ComplexKey.of(serverVersion + 1);
		ComplexKey endKey = ComplexKey.of(Long.MAX_VALUE);
		return db.queryView(createQuery("events_by_version").startKey(startKey).endKey(endKey).includeDocs(true),
		    Event.class);
	}
	
	/**
	 * Find an event based on a concept and between a range of date created dates
	 * 
	 * @param concept
	 * @param conceptValue
	 * @param dateFrom
	 * @param dateTo
	 * @return
	 */
	@View(name = "event_by_concept_and_date_created", map = "function(doc) {if (doc.type === 'Event' && doc.obs) {for (var obs in doc.obs) {var fieldCode = doc.obs[obs].fieldCode;var value = doc.obs[obs].values[0];emit([doc.baseEntityId,fieldCode,value,doc.dateCreated.substring(0, 10)],null);}}}")
	public List<Event> findByClientAndConceptAndDate(String baseEntityId, String concept, String conceptValue,
	                                                 String dateFrom, String dateTo) {
		ComplexKey startKey = ComplexKey.of(baseEntityId, concept, conceptValue, dateFrom);
		ComplexKey endKey = ComplexKey.of(baseEntityId, concept, conceptValue, dateTo);
		List<Event> events = db.queryView(createQuery("event_by_concept_and_date_created").startKey(startKey).endKey(endKey)
		        .includeDocs(true), Event.class);
		return events;
	}
	
	@View(name = "event_by_concept_parent_code_and_base_entity_id", map = "function(doc) {if (doc.type === 'Event' && doc.obs) {for (var obs in doc.obs) {var fieldCode = doc.obs[obs].fieldCode;var parentCode = doc.obs[obs].parentCode;emit([doc.baseEntityId, fieldCode, parentCode], null);}}}")
	public List<Event> findByBaseEntityIdAndConceptParentCode(String baseEntityId, String concept, String parentCode) {
		ComplexKey startKey = ComplexKey.of(baseEntityId, concept, parentCode);
		ComplexKey endKey = ComplexKey.of(baseEntityId, concept, parentCode);
		List<Event> events = db.queryView(createQuery("event_by_concept_parent_code_and_base_entity_id").startKey(startKey)
		        .endKey(endKey).includeDocs(true), Event.class);
		return events;
	}
	
	@View(name = "event_by_concept_and_value", map = "function(doc) {if (doc.type === 'Event' && doc.obs) {for (var obs in doc.obs) {var fieldCode = doc.obs[obs].fieldCode;var value = doc.obs[obs].values[0];emit([fieldCode,value],null);}}}")
	public List<Event> findByConceptAndValue(String concept, String conceptValue) {
		List<Event> events = db.queryView(createQuery("event_by_concept_and_value")
		        .key(ComplexKey.of(concept, conceptValue)).includeDocs(true), Event.class);
		return events;
	}
	
	@View(name = "events_by_empty_server_version", map = "function(doc) { if (doc.type == 'Event' && !doc.serverVersion) { emit(doc._id, doc); } }")
	public List<Event> findByEmptyServerVersion() {
		return db.queryView(createQuery("events_by_empty_server_version").limit(200).includeDocs(true), Event.class);
	}
	
	@GenerateView
	public List<Event> getAll() {
		return super.getAll();
	}
	
	public List<Event> findEvents(EventSearchBean eventSearchBean, String sortBy, String sortOrder, int limit) {
		return ler.getByCriteria(eventSearchBean, sortBy, sortOrder, limit);
	}
	
	@View(name = "all_events_by_event_type_and_version", map = "function(doc) { if (doc.type === 'Event'){  emit([doc.eventType, doc.version], null); } }")
	public List<Event> findEventByEventTypeBetweenTwoDates(String eventType) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DATE, 1);
		System.err.println("calendar.getTime():" + calendar.getTime().getTime());
		ComplexKey start = ComplexKey.of(eventType, calendar.getTime().getTime());
		ComplexKey end = ComplexKey.of(eventType, System.currentTimeMillis());
		List<Event> events = db.queryView(createQuery("all_events_by_event_type_and_version").startKey(start).endKey(end)
		        .includeDocs(true), Event.class);
		
		return events;
	}
	
	@View(name = "events_by_provider_and_entity_type", map = "function(doc) { if (doc.type === 'Event' && (doc.entityType=='child' || doc.entityType=='mother')) { emit(doc.providerId, null); } }")
	public List<Event> findByProvider(String provider) {
		return db.queryView(createQuery("events_by_provider_and_entity_type").key(provider).includeDocs(true), Event.class);
	}
	
	@View(name = "events_not_in_OpenMRS", map = "function(doc) { if (doc.type === 'Event' && doc.serverVersion) { var noId = true; for(var key in doc.identifiers) {if(key == 'OPENMRS_UUID') {noId = false;}}if(noId){emit([doc.serverVersion],  null); }} }")
	public List<Event> notInOpenMRSByServerVersion(long serverVersion, Calendar calendar) {
		long serverStartKey = serverVersion + 1;
		long serverEndKey = calendar.getTimeInMillis();
		if (serverStartKey < serverEndKey) {
			ComplexKey startKey = ComplexKey.of(serverStartKey);
			ComplexKey endKey = ComplexKey.of(serverEndKey);
			return db.queryView(createQuery("events_not_in_OpenMRS").startKey(startKey).endKey(endKey).limit(1000)
			        .includeDocs(true), Event.class);
		}
		return new ArrayList<>();
	}
	
	@View(name = "events_by_type_not_in_OpenMRS", map = "function(doc) { if (doc.type === 'Event' && doc.serverVersion) { var noId = true; for(var key in doc.identifiers) {if(key == 'OPENMRS_UUID') {noId = false;}}if(noId){emit([doc.eventType, doc.serverVersion], null); }} }")
	public List<Event> notInOpenMRSByServerVersionAndType(String type, long serverVersion, Calendar calendar) {
		long serverStartKey = serverVersion + 1;
		long serverEndKey = calendar.getTimeInMillis();
		if (serverStartKey < serverEndKey) {
			ComplexKey startKey = ComplexKey.of(type, serverStartKey);
			ComplexKey endKey = ComplexKey.of(type, serverEndKey);
			return db.queryView(createQuery("events_by_type_not_in_OpenMRS").startKey(startKey).endKey(endKey).limit(1000)
			        .includeDocs(true), Event.class);
		}
		return new ArrayList<>();
	}
	
	public synchronized Event addEvent(CouchDbConnector targetDb, Event event) {
		//		Event e = find(targetDb,event);
		//		if(e != null){
		//			throw new IllegalArgumentException("An event already exists with given list of identifiers. Consider updating data.["+e+"]");
		//		}
		if (event.getFormSubmissionId() != null
		        && getByBaseEntityAndFormSubmissionId(targetDb, event.getBaseEntityId(), event.getFormSubmissionId()) != null) {
			throw new IllegalArgumentException(
			        "An event already exists with given baseEntity and formSubmission combination. Consider updating");
		}
		
		event.setDateCreated(new DateTime());
		
		add(targetDb, event);
		return event;
	}
	
	public Event getByBaseEntityAndFormSubmissionId(CouchDbConnector targetDb, String baseEntityId, String formSubmissionId) {
		try {
			List<Event> events = findByBaseEntityAndFormSubmissionId(targetDb, baseEntityId, formSubmissionId);
			if (events.size() > 1) {
				throw new IllegalArgumentException();
			}
			if (events.size() == 0) {
				return null;
			}
			return events.get(0);
		}
		catch (IllegalArgumentException e) {
			throw new IllegalStateException("Multiple events for baseEntityId and formSubmissionId combination ("
			        + baseEntityId + "," + formSubmissionId + ")");
		}
		catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public List<Event> findByBaseEntityAndEventTypeContaining(String baseEntityId, String eventType) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void deleteByPrimaryKey(Event event) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public List<Event> findByFieldValue(String field, List<String> ids, long serverVersion) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<CustomQuery> getLocations(int userId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public CustomQuery getUser(String userName) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public CustomQuery getTeamMemberId(int userId) {
		return null;
	}
	
	@Override
	public int updateHealthId(HealthId healthId) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public List<HealthId> gethealthIds(boolean status, String type) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<Event> selectBySearchBean(AddressSearchBean addressSearchBean, long serverVersion, String providerId,
	                                      int limit) {
		return null;
	}
	
	@Override
	public Integer findEventIdByFormSubmissionId(String formSubmissionId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Event findEventByEventId(Integer eventId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<Event> selectByProvider(long serverVersion, String providerId, int limit) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int insertHealthId(HealthId healthId) {
		return 0;
	}
	
	@Override
	public List<String> getHouseholdId(Integer maxid) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<CustomQuery> getRoles(int userId) {
		// TODO Auto-generated method stub
		return null;
	}
}
