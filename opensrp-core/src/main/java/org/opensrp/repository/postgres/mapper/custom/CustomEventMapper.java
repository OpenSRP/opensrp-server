package org.opensrp.repository.postgres.mapper.custom;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.opensrp.domain.postgres.CustomQuery;
import org.opensrp.domain.postgres.Event;
import org.opensrp.domain.postgres.HealthId;
import org.opensrp.repository.postgres.mapper.EventMapper;

public interface CustomEventMapper extends EventMapper {
	
	int insertSelectiveAndSetId(Event record);
	
	Event selectByDocumentId(String documentId);
	
	List<Event> selectByIdentifier(String identifier);
	
	List<Event> selectByIdentifierOfType(@Param("identifierType") String identifierType,
	                                     @Param("identifier") String identifier);
	
	List<Event> selectByBaseEntityIdConceptAndDate(@Param("baseEntityId") String baseEntityId,
	                                               @Param("concept") String concept,
	                                               @Param("conceptValue") String conceptValue,
	                                               @Param("dateFrom") Date dateFrom, @Param("dateTo") Date dateTo);
	
	List<Event> selectByBaseEntityIdAndConceptParentCode(@Param("baseEntityId") String baseEntityId,
	                                                     @Param("concept") String concept,
	                                                     @Param("parentCode") String parentCode);
	
	List<Event> selectByConceptAndValue(@Param("concept") String concept, @Param("conceptValue") String conceptValue);
	
	List<CustomQuery> getLocations(int userId);
	
	CustomQuery getUser(String userName);
	
	CustomQuery getTeamMemberId(int userId);
	
	int updateHealthId(HealthId healthId);
	
	List<HealthId> gethealthIds(boolean status, String type);
	
	List<Event> selectByProvider(@Param("serverVersion") long serverVersion, @Param("providerId") String providerId,
	                             @Param("limit") int limit);
	
	int insertHealthId(HealthId healthId);
	
	List<CustomQuery> getRoles(@Param("userId") int userId);
}
