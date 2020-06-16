package org.opensrp.repository.postgres;

import static org.opensrp.common.AllConstants.BaseEntity.BASE_ENTITY_ID;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.opensrp.common.AllConstants;
import org.opensrp.domain.Event;
import org.opensrp.domain.postgres.CustomQuery;
import org.opensrp.domain.postgres.EventMetadata;
import org.opensrp.domain.postgres.EventMetadataExample;
import org.opensrp.domain.postgres.EventMetadataExample.Criteria;
import org.opensrp.domain.postgres.HealthId;
import org.opensrp.repository.EventsRepository;
import org.opensrp.repository.postgres.mapper.custom.CustomEventMapper;
import org.opensrp.repository.postgres.mapper.custom.CustomEventMetadataMapper;
import org.opensrp.search.AddressSearchBean;
import org.opensrp.search.EventSearchBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("eventsRepositoryPostgres")
public class EventsRepositoryImpl extends BaseRepositoryImpl<Event> implements EventsRepository {
	
	@Autowired
	private CustomEventMapper eventMapper;
	
	@Autowired
	private CustomEventMetadataMapper eventMetadataMapper;
	
	@Override
	public Event get(String id) {
		if (StringUtils.isBlank(id)) {
			return null;
		}
		org.opensrp.domain.postgres.Event pgEvent = eventMetadataMapper.selectByDocumentId(id);
		
		return convert(pgEvent);
	}
	
	@Override
	public void add(Event entity) {
		if (entity == null || entity.getBaseEntityId() == null) {
			return;
		}
		
		if (retrievePrimaryKey(entity) != null) { //Event already added
			return;
		}
		
		if (entity.getId() == null)
			entity.setId(UUID.randomUUID().toString());
		setRevision(entity);
		
		org.opensrp.domain.postgres.Event pgEvent = convert(entity, null);
		if (pgEvent == null) {
			return;
		}
		
		int rowsAffected = eventMapper.insertSelectiveAndSetId(pgEvent);
		if (rowsAffected < 1 || pgEvent.getId() == null) {
			return;
		}
		
		EventMetadata eventMetadata = createMetadata(entity, pgEvent.getId());
		if (eventMetadata != null) {
			eventMetadataMapper.insertSelective(eventMetadata);
		}
		
	}
	
	@Override
	public void update(Event entity) {
		if (entity == null || entity.getBaseEntityId() == null) {
			return;
		}
		
		Long id = retrievePrimaryKey(entity);
		if (id == null) { // Event not added
			return;
		}
		
		setRevision(entity);
		
		org.opensrp.domain.postgres.Event pgEvent = convert(entity, id);
		if (pgEvent == null) {
			return;
		}
		
		EventMetadata eventMetadata = createMetadata(entity, id);
		if (eventMetadata == null) {
			return;
		}
		
		int rowsAffected = eventMapper.updateByPrimaryKey(pgEvent);
		if (rowsAffected < 1) {
			return;
		}
		
		EventMetadataExample eventMetadataExample = new EventMetadataExample();
		eventMetadataExample.createCriteria().andEventIdEqualTo(id).andDateDeletedIsNull();
		eventMetadata.setId(eventMetadataMapper.selectByExample(eventMetadataExample).get(0).getId());
		eventMetadataMapper.updateByPrimaryKey(eventMetadata);
		
	}
	
	@Override
	public List<Event> getAll() {
		EventMetadataExample eventMetadataExample = new EventMetadataExample();
		eventMetadataExample.createCriteria().andDateDeletedIsNull();
		List<org.opensrp.domain.postgres.Event> events = eventMetadataMapper.selectManyWithRowBounds(eventMetadataExample,
		    0, DEFAULT_FETCH_SIZE);
		return convert(events);
	}
	
	@Override
	public void safeRemove(Event entity) {
		if (entity == null || entity.getBaseEntityId() == null) {
			return;
		}
		
		Long id = retrievePrimaryKey(entity);
		if (id == null) {
			return;
		}
		
		Date dateDeleted = entity.getDateVoided() == null ? new Date() : entity.getDateVoided().toDate();
		EventMetadata eventMetadata = new EventMetadata();
		eventMetadata.setDateDeleted(dateDeleted);
		
		EventMetadataExample eventMetadataExample = new EventMetadataExample();
		eventMetadataExample.createCriteria().andEventIdEqualTo(id).andDateDeletedIsNull();
		int rowsAffected = eventMetadataMapper.updateByExampleSelective(eventMetadata, eventMetadataExample);
		if (rowsAffected < 1) {
			return;
		}
		
		org.opensrp.domain.postgres.Event pgEvent = new org.opensrp.domain.postgres.Event();
		pgEvent.setId(id);
		pgEvent.setDateDeleted(dateDeleted);
		eventMapper.updateByPrimaryKeySelective(pgEvent);
		
	}
	
	@Override
	public List<Event> findAllByIdentifier(String identifier) {
		List<org.opensrp.domain.postgres.Event> events = eventMapper.selectByIdentifier(identifier);
		return convert(events);
	}
	
	@Override
	public List<Event> findAllByIdentifier(String identifierType, String identifier) {
		List<org.opensrp.domain.postgres.Event> events = eventMapper.selectByIdentifierOfType(identifierType, identifier);
		return convert(events);
	}
	
	@Override
	public Event findById(String id) {
		return get(id);
	}
	
	@Override
	public Event findByFormSubmissionId(String formSubmissionId) {
		if (StringUtils.isBlank(formSubmissionId)) {
			return null;
		}
		EventMetadataExample example = new EventMetadataExample();
		example.createCriteria().andFormSubmissionIdEqualTo(formSubmissionId).andDateDeletedIsNull();
		List<org.opensrp.domain.postgres.Event> events = eventMetadataMapper.selectMany(example);
		if (events.size() > 1) {
			throw new IllegalStateException("Multiple events for formSubmissionId " + formSubmissionId);
		} else if (!events.isEmpty())
			return convert(events.get(0));
		else
			return null;
	}
	
	@Override
	public List<Event> findByBaseEntityId(String baseEntityId) {
		EventMetadataExample example = new EventMetadataExample();
		example.createCriteria().andBaseEntityIdEqualTo(baseEntityId).andDateDeletedIsNull();
		return convert(eventMetadataMapper.selectMany(example));
	}
	
	@Override
	public Event findByBaseEntityAndFormSubmissionId(String baseEntityId, String formSubmissionId) {
		EventMetadataExample example = new EventMetadataExample();
		example.createCriteria().andBaseEntityIdEqualTo(baseEntityId).andFormSubmissionIdEqualTo(formSubmissionId)
		        .andDateDeletedIsNull();
		List<org.opensrp.domain.postgres.Event> events = eventMetadataMapper.selectMany(example);
		if (events.size() > 1) {
			throw new IllegalStateException("Multiple events for baseEntityId and formSubmissionId combination ("
			        + baseEntityId + "," + formSubmissionId + ")");
		} else if (!events.isEmpty())
			return convert(events.get(0));
		else
			return null;
	}
	
	@Override
	public List<Event> findByBaseEntityAndType(String baseEntityId, String eventType) {
		EventMetadataExample example = new EventMetadataExample();
		example.createCriteria().andBaseEntityIdEqualTo(baseEntityId).andEventTypeEqualTo(eventType).andDateDeletedIsNull();
		return convert(eventMetadataMapper.selectMany(example));
	}
	
	@Override
	public List<Event> findByBaseEntityAndEventTypeContaining(String baseEntityId, String eventType) {
		EventMetadataExample example = new EventMetadataExample();
		example.createCriteria().andBaseEntityIdEqualTo(baseEntityId).andEventTypeLike("%" + eventType + "%")
		        .andDateDeletedIsNull();
		return convert(eventMetadataMapper.selectMany(example));
	}
	
	@Override
	public List<Event> findEvents(EventSearchBean eventSearchBean) {
		EventMetadataExample example = new EventMetadataExample();
		Criteria criteria = example.createCriteria();
		if (StringUtils.isNotEmpty(eventSearchBean.getBaseEntityId()))
			criteria.andBaseEntityIdEqualTo(eventSearchBean.getBaseEntityId());
		if (eventSearchBean.getEventDateFrom() != null && eventSearchBean.getEventDateTo() != null)
			criteria.andEventDateBetween(eventSearchBean.getEventDateFrom().toDate(), eventSearchBean.getEventDateTo()
			        .toDate());
		if (StringUtils.isNotEmpty(eventSearchBean.getEventType()))
			criteria.andEventTypeEqualTo(eventSearchBean.getEventType());
		if (StringUtils.isNotEmpty(eventSearchBean.getEntityType()))
			criteria.andEntityTypeEqualTo(eventSearchBean.getEntityType());
		if (StringUtils.isNotEmpty(eventSearchBean.getProviderId()))
			criteria.andProviderIdEqualTo(eventSearchBean.getProviderId());
		if (StringUtils.isNotEmpty(eventSearchBean.getLocationId()))
			criteria.andLocationIdEqualTo(eventSearchBean.getLocationId());
		if (eventSearchBean.getLastEditFrom() != null && eventSearchBean.getLastEditTo() != null)
			criteria.andDateEditedBetween(eventSearchBean.getLastEditFrom().toDate(), eventSearchBean.getLastEditTo()
			        .toDate());
		if (StringUtils.isNotEmpty(eventSearchBean.getTeam()))
			criteria.andTeamEqualTo(eventSearchBean.getTeam());
		if (StringUtils.isNotEmpty(eventSearchBean.getTeamId()))
			criteria.andTeamIdEqualTo(eventSearchBean.getTeamId());
		if (!criteria.isValid())
			throw new IllegalArgumentException("Atleast one search filter must be specified");
		criteria.andDateDeletedIsNull();
		return convert(eventMetadataMapper.selectManyWithRowBounds(example, 0, DEFAULT_FETCH_SIZE));
	}
	
	@Override
	public List<Event> findEventsByDynamicQuery(String query) {
		throw new IllegalArgumentException("Dynamic query feature not supported");
	}
	
	@Override
	public List<Event> findByServerVersion(long serverVersion) {
		EventMetadataExample example = new EventMetadataExample();
		example.createCriteria().andServerVersionGreaterThanOrEqualTo(serverVersion + 1).andDateDeletedIsNull();
		return convert(eventMetadataMapper.selectManyWithRowBounds(example, 0, DEFAULT_FETCH_SIZE));
	}
	
	@Override
	public List<Event> notInOpenMRSByServerVersion(long serverVersion, Calendar calendar) {
		return convert(eventMetadataMapper.selectNotInOpenMRSByServerVersion(serverVersion, calendar.getTimeInMillis(),
		    DEFAULT_FETCH_SIZE));
	}
	
	@Override
	public List<Event> notInOpenMRSByServerVersionAndType(String type, long serverVersion, Calendar calendar) {
		return convert(eventMetadataMapper.selectNotInOpenMRSByServerVersionAndType(type, serverVersion,
		    calendar.getTimeInMillis(), DEFAULT_FETCH_SIZE));
	}
	
	@Override
	public List<Event> findByClientAndConceptAndDate(String baseEntityId, String concept, String conceptValue,
	                                                 String dateFrom, String dateTo) {
		if (StringUtils.isBlank(baseEntityId) && StringUtils.isBlank(concept) && StringUtils.isBlank(conceptValue))
			return new ArrayList<Event>();
		Date from = null;
		Date to = null;
		if (StringUtils.isNotEmpty(dateFrom))
			from = new DateTime(dateFrom).toDate();
		if (StringUtils.isNotEmpty(dateTo))
			to = new DateTime(dateTo).toDate();
		return convert(eventMapper.selectByBaseEntityIdConceptAndDate(baseEntityId, concept, conceptValue, from, to));
	}
	
	@Override
	public List<Event> findByBaseEntityIdAndConceptParentCode(String baseEntityId, String concept, String parentCode) {
		if (StringUtils.isBlank(baseEntityId) && StringUtils.isBlank(concept) && StringUtils.isBlank(parentCode))
			return new ArrayList<Event>();
		return convert(eventMapper.selectByBaseEntityIdAndConceptParentCode(baseEntityId, concept, parentCode));
	}
	
	@Override
	public List<Event> findByConceptAndValue(String concept, String conceptValue) {
		if (StringUtils.isBlank(concept) && StringUtils.isBlank(conceptValue))
			return new ArrayList<Event>();
		return convert(eventMapper.selectByConceptAndValue(concept, conceptValue));
	}
	
	@Override
	public List<Event> findByEmptyServerVersion() {
		EventMetadataExample example = new EventMetadataExample();
		example.createCriteria().andDateDeletedIsNull().andServerVersionIsNull();
		example.or(example.createCriteria().andDateDeletedIsNull().andServerVersionEqualTo(0l));
		return convert(eventMetadataMapper.selectManyWithRowBounds(example, 0, DEFAULT_FETCH_SIZE));
	}
	
	@Override
	public List<Event> findEvents(EventSearchBean eventSearchBean, String sortBy, String sortOrder, int limit) {
		EventMetadataExample example = new EventMetadataExample();
		Criteria criteria = example.createCriteria();
		
		if (StringUtils.isNotEmpty(eventSearchBean.getTeam())) {
			if (eventSearchBean.getTeam().contains(",")) {
				String[] teamsArray = org.apache.commons.lang.StringUtils.split(eventSearchBean.getTeam(), ",");
				criteria.andTeamIn(Arrays.asList(teamsArray));
			} else {
				criteria.andTeamEqualTo(eventSearchBean.getTeam());
			}
		}
		
		if (StringUtils.isNotEmpty(eventSearchBean.getTeamId())) {
			if (eventSearchBean.getTeamId().contains(",")) {
				String[] teamsArray = org.apache.commons.lang.StringUtils.split(eventSearchBean.getTeamId(), ",");
				criteria.andTeamIdIn(Arrays.asList(teamsArray));
			} else {
				criteria.andTeamIdEqualTo(eventSearchBean.getTeamId());
			}
		}
		
		if (StringUtils.isNotEmpty(eventSearchBean.getProviderId())) {
			if (eventSearchBean.getProviderId().contains(",")) {
				String[] providersArray = org.apache.commons.lang.StringUtils.split(eventSearchBean.getProviderId(), ",");
				criteria.andProviderIdIn(Arrays.asList(providersArray));
			} else {
				criteria.andProviderIdEqualTo(eventSearchBean.getProviderId());
			}
		}
		if (StringUtils.isNotEmpty(eventSearchBean.getLocationId())) {
			if (eventSearchBean.getLocationId().contains(",")) {
				String[] locationArray = org.apache.commons.lang.StringUtils.split(eventSearchBean.getLocationId(), ",");
				criteria.andLocationIdIn(Arrays.asList(locationArray));
			} else {
				criteria.andLocationIdEqualTo(eventSearchBean.getLocationId());
			}
		}
		if (StringUtils.isNotEmpty(eventSearchBean.getBaseEntityId())) {
			if (eventSearchBean.getBaseEntityId().contains(",")) {
				String[] idsArray = org.apache.commons.lang.StringUtils.split(eventSearchBean.getBaseEntityId(), ",");
				criteria.andBaseEntityIdIn(Arrays.asList(idsArray));
			} else {
				criteria.andBaseEntityIdEqualTo(eventSearchBean.getBaseEntityId());
			}
		}
		if (eventSearchBean.getServerVersion() != null)
			criteria.andServerVersionGreaterThanOrEqualTo(eventSearchBean.getServerVersion());
		if (!criteria.isValid())
			throw new IllegalArgumentException("Atleast one search filter must be specified");
		
		criteria.andDateDeletedIsNull();
		example.setOrderByClause(getOrderByClause(sortBy, sortOrder));
		return convert(eventMetadataMapper.selectManyWithRowBounds(example, 0, limit));
	}
	
	/**
	 * Compatibility method inherited from couch to fetch events of a given type within the current
	 * month
	 * 
	 * @param eventType the type of event to query
	 * @return list of events of given type within the current month
	 */
	@Override
	public List<Event> findEventByEventTypeBetweenTwoDates(String eventType) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DATE, 1);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		EventMetadataExample example = new EventMetadataExample();
		example.createCriteria().andEventTypeEqualTo(eventType)
		        .andServerVersionBetween(calendar.getTimeInMillis(), System.currentTimeMillis()).andDateDeletedIsNull();
		return convert(eventMetadataMapper.selectManyWithRowBounds(example, 0, DEFAULT_FETCH_SIZE));
	}
	
	@Override
	public List<Event> findByProvider(String provider) {
		EventMetadataExample example = new EventMetadataExample();
		example.createCriteria().andProviderIdEqualTo(provider);
		return convert(eventMetadataMapper.selectManyWithRowBounds(example, 0, DEFAULT_FETCH_SIZE));
	}
	
	@Override
	protected Long retrievePrimaryKey(Event t) {
		Object uniqueId = getUniqueField(t);
		if (uniqueId == null) {
			return null;
		}
		
		String documentId = uniqueId.toString();
		
		EventMetadataExample eventMetadataExample = new EventMetadataExample();
		eventMetadataExample.createCriteria().andDocumentIdEqualTo(documentId);
		
		org.opensrp.domain.postgres.Event pgClient = eventMetadataMapper.selectByDocumentId(documentId);
		if (pgClient == null) {
			return null;
		}
		return pgClient.getId();
	}
	
	@Override
	protected Object getUniqueField(Event t) {
		if (t == null) {
			return null;
		}
		return t.getId();
	}
	
	// Private Methods	
	private Event convert(org.opensrp.domain.postgres.Event event) {
		if (event == null || event.getJson() == null || !(event.getJson() instanceof Event)) {
			return null;
		}
		return (Event) event.getJson();
	}
	
	private org.opensrp.domain.postgres.Event convert(Event event, Long primaryKey) {
		if (event == null) {
			return null;
		}
		
		org.opensrp.domain.postgres.Event pgEvent = new org.opensrp.domain.postgres.Event();
		pgEvent.setId(primaryKey);
		pgEvent.setJson(event);
		
		return pgEvent;
	}
	
	private List<Event> convert(List<org.opensrp.domain.postgres.Event> events) {
		if (events == null || events.isEmpty()) {
			return new ArrayList<>();
		}
		
		List<Event> convertedEvents = new ArrayList<>();
		for (org.opensrp.domain.postgres.Event event : events) {
			Event convertedEvent = convert(event);
			if (convertedEvent != null) {
				convertedEvents.add(convertedEvent);
			}
		}
		
		return convertedEvents;
	}
	
	private EventMetadata createMetadata(Event event, Long eventId) {
		try {
			EventMetadata eventMetadata = new EventMetadata();
			eventMetadata.setBaseEntityId(event.getBaseEntityId());
			eventMetadata.setEventId(eventId);
			eventMetadata.setDocumentId(event.getId());
			eventMetadata.setBaseEntityId(event.getBaseEntityId());
			eventMetadata.setFormSubmissionId(event.getFormSubmissionId());
			eventMetadata.setOpenmrsUuid(event.getIdentifier(AllConstants.Client.OPENMRS_UUID_IDENTIFIER_TYPE));
			eventMetadata.setEventType(event.getEventType());
			if (event.getEventDate() != null)
				eventMetadata.setEventDate(event.getEventDate().toDate());
			eventMetadata.setEntityType(event.getEntityType());
			eventMetadata.setProviderId(event.getProviderId());
			eventMetadata.setLocationId(event.getLocationId());
			eventMetadata.setTeam(event.getTeam());
			eventMetadata.setTeamId(event.getTeamId());
			eventMetadata.setServerVersion(event.getServerVersion());
			if (event.getDateCreated() != null)
				eventMetadata.setDateCreated(event.getDateCreated().toDate());
			if (event.getDateEdited() != null)
				eventMetadata.setDateEdited(event.getDateEdited().toDate());
			if (event.getDateVoided() != null)
				eventMetadata.setDateDeleted(event.getDateVoided().toDate());
			return eventMetadata;
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}
	
	@Override
	public void deleteByPrimaryKey(Event entity) {
		if (entity == null || entity.getBaseEntityId() == null) {
			return;
		}
		
		Long id = retrievePrimaryKey(entity);
		if (id == null) {
			return;
		}
		
		Date dateDeleted = entity.getDateVoided() == null ? new Date() : entity.getDateVoided().toDate();
		EventMetadata eventMetadata = new EventMetadata();
		eventMetadata.setDateDeleted(dateDeleted);
		
		EventMetadataExample eventMetadataExample = new EventMetadataExample();
		eventMetadataExample.createCriteria().andEventIdEqualTo(id).andDateDeletedIsNull();
		eventMetadataMapper.deleteByPrimaryKey(id);
		
		org.opensrp.domain.postgres.Event pgEvent = new org.opensrp.domain.postgres.Event();
		pgEvent.setId(id);
		pgEvent.setDateDeleted(dateDeleted);
		eventMapper.deleteByPrimaryKey(id);
		
	}
	
	@Override
	public List<Event> findByFieldValue(String field, List<String> ids, long serverVersion) {
		if (field.equals(BASE_ENTITY_ID) && ids != null && !ids.isEmpty()) {
			EventMetadataExample eventMetadataExample = new EventMetadataExample();
			eventMetadataExample.createCriteria().andBaseEntityIdIn(ids).andDateDeletedIsNull()
			        .andServerVersionGreaterThanOrEqualTo(serverVersion);
			List<org.opensrp.domain.postgres.Event> events = eventMetadataMapper.selectMany(eventMetadataExample);
			return convert(events);
		}
		return new ArrayList<>();
	}
	
	@Override
	public List<CustomQuery> getLocations(int userId) {
		
		return eventMapper.getLocations(userId);
	}
	
	@Override
	public CustomQuery getUser(String userName) {
		// TODO Auto-generated method stub
		return eventMapper.getUser(userName);
	}
	
	@Override
	public CustomQuery getTeamMemberId(int userId) {
		return eventMapper.getTeamMemberId(userId);
	}
	
	@Override
	public int updateHealthId(HealthId healthId) {
		return eventMapper.updateHealthId(healthId);
		
	}
	
	@Override
	public List<HealthId> gethealthIds(boolean status, String type) {
		List<HealthId> healthIds = eventMapper.gethealthIds(status, type);
		return null;
	}
	
	@Override
	public List<Event> selectBySearchBean(AddressSearchBean addressSearchBean, long serverVersion, String providerId,
	                                      int limit) {
		return convert(eventMetadataMapper.selectBySearchBean(addressSearchBean, serverVersion, providerId,
		    DEFAULT_FETCH_SIZE));
	}
	
	@Override
	public Integer findEventIdByFormSubmissionId(String formSubmissionId) {
		// TODO Auto-generated method stub
		return eventMetadataMapper.findEventIdByFormSubmissionId(formSubmissionId);
	}
	
	@Override
	public Event findEventByEventId(Integer eventId) {
		org.opensrp.domain.postgres.Event pgEvent = eventMetadataMapper.findEventByEventId(eventId);
		if (pgEvent != null) {
			return convert(pgEvent);
		} else {
			return null;
		}
	}
	
	@Override
	public List<Event> selectByProvider(long serverVersion, String providerId, int limit) {
		return convert(eventMetadataMapper.selectByProvider(serverVersion, providerId, DEFAULT_FETCH_SIZE));
	}
	
	@Override
	public int insertHealthId(HealthId healthId) {
		return eventMapper.insertHealthId(healthId);
	}
	
	@Override
	public List<String> getHouseholdId(Integer maxId) {
		// TODO Auto-generated method stub
		return eventMetadataMapper.getHouseholdId(maxId, maxId + 200);
	}
	
	@Override
	public List<CustomQuery> getRoles(int userId) {
		// TODO Auto-generated method stub
		return eventMapper.getRoles(userId);
	}
}
