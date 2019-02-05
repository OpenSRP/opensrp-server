package org.opensrp.repository.postgres.mapper.custom;

import org.opensrp.domain.postgres.Settings;
import org.opensrp.repository.postgres.mapper.SettingsMapper;

public interface CustomSettingMapper extends SettingsMapper {
	
	int insertSelectiveAndSetId(Settings setting);
}
