package org.opensrp.repository.couch;

import java.util.List;

import org.ektorp.ComplexKey;
import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.View;
import org.opensrp.common.AllConstants;
import org.opensrp.domain.postgres.Settings;
import org.opensrp.domain.postgres.SettingsMetadata;
import org.opensrp.domain.setting.SettingConfiguration;
import org.opensrp.repository.SettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository("couchSettingRepository")
@Primary
public class SettingRepositoryImpl extends CouchDbRepositorySupport<SettingConfiguration> implements SettingRepository {
	
	@Autowired
	protected SettingRepositoryImpl(@Qualifier(AllConstants.OPENSRP_DATABASE_CONNECTOR) CouchDbConnector db) {
		super(SettingConfiguration.class, db);
		initStandardDesignDocument();
	}
	
	@View(name = "all_settings", map = "function(doc) { if (doc.type==='Setting') { emit(doc.identifier); }}")
	public List<SettingConfiguration> findAllSettings() {
		return db.queryView(createQuery("all_settings").includeDocs(true), SettingConfiguration.class);
	}
	
	@View(name = "settings_by_version", map = "function(doc) { if (doc.type==='Setting') { emit([doc.serverVersion], null); }}")
	public List<SettingConfiguration> findAllSettingsByVersion(Long lastSyncedServerVersion, String teamId) {
		ComplexKey startKey = ComplexKey.of(lastSyncedServerVersion);
		ComplexKey endKey = ComplexKey.of(Long.MAX_VALUE);
		return db.queryView(createQuery("settings_by_version").includeDocs(true).startKey(startKey).endKey(endKey),
		    SettingConfiguration.class);
	}
	
	/**
	 * Get all Settings without a server version
	 *
	 * @return settings
	 */
	@View(name = "settings_by_empty_server_version", map = "function(doc) { if ( doc.type == 'Setting' && !doc.serverVersion) { emit(doc._id, doc); } }")
	public List<SettingConfiguration> findByEmptyServerVersion() {
		return db.queryView(createQuery("settings_by_empty_server_version").limit(200).includeDocs(true),
		    SettingConfiguration.class);
	}
	
	@Override
	public void safeRemove(SettingConfiguration entity) {
		remove(entity);
	}
	
	@Override
	public SettingsMetadata saveSetting(SettingConfiguration settingConfiguration, SettingsMetadata settingMetadata) {
		//To Be Implemented		
		return null;
	}
	
	@Override
	public List<SettingConfiguration> findAllLatestSettingsByVersion(Long lastSyncedServerVersion, String t) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public SettingsMetadata getSettingMetadataByIdentifierAndTeamId(String identifier, String teamId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Settings getSettingById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
