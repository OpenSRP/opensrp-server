package org.opensrp.repository;

import java.util.List;

import org.opensrp.domain.postgres.Settings;
import org.opensrp.domain.postgres.SettingsMetadata;
import org.opensrp.domain.setting.SettingConfiguration;

public interface SettingRepository extends BaseRepository<SettingConfiguration> {
	
	List<SettingConfiguration> findAllSettings();
	
	List<SettingConfiguration> findAllSettingsByVersion(Long lastSyncedServerVersion, String teamId);
	
	List<SettingConfiguration> findAllLatestSettingsByVersion(Long lastSyncedServerVersion, String teamId);
	
	List<SettingConfiguration> findByEmptyServerVersion();
	
	SettingsMetadata saveSetting(SettingConfiguration settingConfiguration, SettingsMetadata settingMetadata);
	
	SettingsMetadata getSettingMetadataByIdentifierAndTeamId(String identifier, String teamId);
	
	Settings getSettingById(Long id);
	
}
