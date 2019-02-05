package org.opensrp.repository.postgres;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import org.opensrp.domain.postgres.Settings;
import org.opensrp.domain.postgres.SettingsMetadata;
import org.opensrp.domain.postgres.SettingsMetadataExample;
import org.opensrp.domain.setting.SettingConfiguration;
import org.opensrp.repository.SettingRepository;
import org.opensrp.repository.postgres.mapper.custom.CustomSettingMapper;
import org.opensrp.repository.postgres.mapper.custom.CustomSettingMetadataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("settingRepositoryPostgres")
public class SettingRepositoryImpl extends BaseRepositoryImpl<SettingConfiguration> implements SettingRepository {
	
	@Autowired
	private CustomSettingMapper settingMapper;
	
	@Autowired
	private CustomSettingMetadataMapper settingMetadataMapper;
	
	@Override
	public SettingConfiguration get(String id) {
		return convert(settingMetadataMapper.selectByDocumentId(id));
	}
	
	public Settings addSetting(SettingConfiguration entity) {
		org.opensrp.domain.postgres.Settings pgSetting = null;
		
		if (entity == null || entity.getIdentifier() == null) {
			return pgSetting;
		}
		
		if (retrievePrimaryKey(entity) != null) { // Setting already added
			return pgSetting;
		}
		
		if (entity.getId() == null)
			entity.setId(UUID.randomUUID().toString());
		setRevision(entity);
		
		pgSetting = convert(entity, null);
		if (pgSetting == null) {
			return pgSetting;
		}
		
		int rowsAffected = settingMapper.insertSelectiveAndSetId(pgSetting);
		if (rowsAffected < 1 || pgSetting.getId() == null) {
			return pgSetting;
		}
		
		SettingsMetadata metadata = createMetadata(entity, pgSetting.getId());
		if (metadata != null) {
			settingMetadataMapper.insertSelective(metadata);
		}
		
		return pgSetting;
	}
	
	@Override
	public void update(SettingConfiguration entity) {
		if (entity == null || entity.getId() == null || entity.getIdentifier() == null) {
			return;
		}
		
		Long id = retrievePrimaryKey(entity);
		
		if (id == null) { // Setting not exists
			return;
		}
		
		setRevision(entity);
		
		org.opensrp.domain.postgres.Settings pgSetting = convert(entity, id);
		
		if (pgSetting == null) {
			return;
		}
		
		SettingsMetadata metadata = createMetadata(entity, id);
		if (metadata == null) {
			return;
		}
		
		int rowsAffected = settingMapper.updateByPrimaryKey(pgSetting);
		if (rowsAffected < 1) {
			return;
		}
		
		SettingsMetadataExample metadataExample = new SettingsMetadataExample();
		metadataExample.createCriteria().andSettingsIdEqualTo(id);
		metadata.setId(settingMetadataMapper.selectByExample(metadataExample).get(0).getId());
		settingMetadataMapper.updateByPrimaryKey(metadata);
		
	}
	
	@Override
	public List<SettingConfiguration> getAll() {
		return convert(settingMetadataMapper.selectMany(new SettingsMetadataExample(), 0, DEFAULT_FETCH_SIZE));
	}
	
	@Override
	public void safeRemove(SettingConfiguration entity) {
		if (entity == null) {
			return;
		}
		
		Long id = retrievePrimaryKey(entity);
		if (id == null) {
			return;
		}
		
		SettingsMetadataExample metadataExample = new SettingsMetadataExample();
		metadataExample.createCriteria().andSettingsIdEqualTo(id);
		int rowsAffected = settingMetadataMapper.deleteByExample(metadataExample);
		if (rowsAffected < 1) {
			return;
		}
		
		settingMapper.deleteByPrimaryKey(id);
		
	}
	
	@Override
	public List<SettingConfiguration> findAllSettings() {
		return getAll();
	}
	
	@Override
	public List<SettingConfiguration> findAllSettingsByVersion(Long lastSyncedServerVersion, String teamId) {
		SettingsMetadataExample metadataExample = new SettingsMetadataExample();
		metadataExample.createCriteria().andTeamIdEqualTo(teamId)
		        .andServerVersionGreaterThanOrEqualTo(lastSyncedServerVersion);
		metadataExample.or(metadataExample.createCriteria().andTeamIdIsNull()
		        .andServerVersionGreaterThanOrEqualTo(lastSyncedServerVersion));
		return convert(settingMetadataMapper.selectMany(metadataExample, 0, DEFAULT_FETCH_SIZE));
	}
	
	@Override
	public List<SettingConfiguration> findAllLatestSettingsByVersion(Long lastSyncedServerVersion, String teamId) {
		SettingsMetadataExample metadataExample = new SettingsMetadataExample();
		metadataExample.createCriteria().andTeamIdEqualTo(teamId)
		        .andServerVersionGreaterThanOrEqualTo(lastSyncedServerVersion);
		metadataExample.or(metadataExample.createCriteria().andTeamIdIsNull()
		        .andServerVersionGreaterThanOrEqualTo(lastSyncedServerVersion));
		metadataExample.setOrderByClause("server_version DESC");
		// metadataExample.gr("server_version DESC");
		return convert(settingMetadataMapper.selectMany(metadataExample, 0, DEFAULT_FETCH_SIZE));
	}
	
	@Override
	public List<SettingConfiguration> findByEmptyServerVersion() {
		SettingsMetadataExample metadataExample = new SettingsMetadataExample();
		metadataExample.createCriteria().andServerVersionIsNull();
		metadataExample.or(metadataExample.createCriteria().andServerVersionEqualTo(0l));
		return convert(settingMetadataMapper.selectMany(metadataExample, 0, DEFAULT_FETCH_SIZE));
	}
	
	@Override
	protected Long retrievePrimaryKey(SettingConfiguration settingConfiguration) {
		if (getUniqueField(settingConfiguration) == null) {
			return null;
		}
		String documentId = settingConfiguration.getId();
		
		SettingsMetadataExample metadataExample = new SettingsMetadataExample();
		metadataExample.createCriteria().andDocumentIdEqualTo(documentId);
		
		org.opensrp.domain.postgres.Settings pgSetting = settingMetadataMapper.selectByDocumentId(documentId);
		if (pgSetting == null) {
			return null;
		}
		return pgSetting.getId();
	}
	
	@Override
	protected Object getUniqueField(SettingConfiguration settingConfiguration) {
		return settingConfiguration == null ? null : settingConfiguration.getId();
	}
	
	// private Methods
	private SettingConfiguration convert(org.opensrp.domain.postgres.Settings setting) {
		if (setting == null || setting.getJson() == null || !(setting.getJson() instanceof SettingConfiguration)) {
			return null;
		}
		return (SettingConfiguration) setting.getJson();
	}
	
	private org.opensrp.domain.postgres.Settings convert(SettingConfiguration entity, Long id) {
		if (entity == null) {
			return null;
		}
		
		org.opensrp.domain.postgres.Settings pgSetting = new org.opensrp.domain.postgres.Settings();
		pgSetting.setId(id);
		pgSetting.setJson(entity);
		
		return pgSetting;
	}
	
	private List<SettingConfiguration> convert(List<org.opensrp.domain.postgres.Settings> settings) {
		if (settings == null || settings.isEmpty()) {
			return new ArrayList<>();
		}
		
		List<SettingConfiguration> settingValues = new ArrayList<>();
		for (org.opensrp.domain.postgres.Settings setting : settings) {
			SettingConfiguration convertedSetting = convert(setting);
			if (convertedSetting != null) {
				settingValues.add(convertedSetting);
			}
		}
		return settingValues;
	}
	
	private SettingsMetadata createMetadata(SettingConfiguration entity, Long id) {
		SettingsMetadata metadata = new SettingsMetadata();
		metadata.setSettingsId(id);
		metadata.setDocumentId(entity.getId());
		metadata.setIdentifier(entity.getIdentifier());
		metadata.setServerVersion(entity.getServerVersion());
		return metadata;
	}
	
	@Override
	public SettingsMetadata saveSetting(SettingConfiguration entity, SettingsMetadata metadata) {
		if (entity == null || entity.getIdentifier() == null) {
			return null;
		}
		
		if (entity.getId() == null) {
			entity.setId(UUID.randomUUID().toString());
		}
		setRevision(entity);
		
		Settings settings = convert(entity, Long.valueOf(entity.getId()));
		if (settings == null) {
			return null;
		}
		
		int rowsAffected = entity.getId() != null ? settingMapper.updateByPrimaryKeySelective(settings)
		        : settingMapper.insertSelectiveAndSetId(settings);
		if (rowsAffected < 1 || settings.getId() == null) {
			return null;
		}
		
		SettingsMetadata settingsMetadata = metadata != null ? metadata : createMetadata(entity, settings.getId());
		settingsMetadata.setServerVersion(Calendar.getInstance().getTimeInMillis());
		if (settingsMetadata != null) {
			
			if (settingsMetadata.getId() != null) {
				
				settingMetadataMapper.updateByPrimaryKeySelective(settingsMetadata);
			} else {
				
				settingMetadataMapper.insertSelective(settingsMetadata);
			}
		}
		
		return settingsMetadata;
		
	}
	
	@Override
	public void add(SettingConfiguration entity) {
		if (entity == null || entity.getIdentifier() == null) {
			return;
		}
		
		if (retrievePrimaryKey(entity) != null) { // Event already added
			return;
		}
		
		if (entity.getId() == null)
			entity.setId(UUID.randomUUID().toString());
		setRevision(entity);
		
		Settings settings = convert(entity, null);
		if (settings == null) {
			return;
		}
		
		int rowsAffected = settingMapper.insertSelectiveAndSetId(settings);
		if (rowsAffected < 1 || settings.getId() == null) {
			return;
		}
		
		SettingsMetadata settingsMetadata = createMetadata(entity, settings.getId());
		if (settingsMetadata != null) {
			settingMetadataMapper.insertSelective(settingsMetadata);
		}
		
	}
	
	@Override
	public SettingsMetadata getSettingMetadataByIdentifierAndTeamId(String identifier, String teamId) {
		SettingsMetadataExample example = new SettingsMetadataExample();
		example.createCriteria().andIdentifierEqualTo(identifier).andTeamIdEqualTo(teamId);
		
		List<SettingsMetadata> settingsMetadata = settingMetadataMapper.selectByExample(example);
		
		return !settingsMetadata.isEmpty() ? settingsMetadata.get(0) : null;
		
	}
	
	@Override
	public Settings getSettingById(Long id) {
		
		return settingMapper.selectByPrimaryKey(id);
		
	}
}
