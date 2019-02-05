package org.opensrp.repository.postgres.mapper.custom;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.opensrp.domain.postgres.Settings;
import org.opensrp.domain.postgres.SettingsMetadataExample;
import org.opensrp.repository.postgres.mapper.SettingsMetadataMapper;

public interface CustomSettingMetadataMapper extends SettingsMetadataMapper {
	
	List<Settings> selectMany(@Param("example") SettingsMetadataExample settingsExample, @Param("offset") int offset,
	                          @Param("limit") int limit);
	
	Settings selectByDocumentId(String documentId);
	
}

