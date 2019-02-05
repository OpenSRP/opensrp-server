package org.opensrp.domain.setting;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
import org.ektorp.support.TypeDiscriminator;
import org.opensrp.domain.BaseDataObject;

@TypeDiscriminator("doc.type == 'SettingConfiguration'")
public class SettingConfiguration extends BaseDataObject {
	
	private static final long serialVersionUID = 1890883609898207738L;
	
	@JsonProperty
	private String identifier;
	
	@JsonProperty
	private String teamId;
	
	@JsonProperty
	private String providerId;
	
	@JsonProperty
	private String locationId;
	
	@JsonProperty
	private String childLocationId;
	
	@JsonProperty
	private String version;
	
	@JsonProperty
	private List<Setting> settings;
	
	public String getIdentifier() {
		return identifier;
	}
	
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	public String getTeamId() {
		return teamId;
	}
	
	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}
	
	public String getProviderId() {
		return providerId;
	}
	
	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}
	
	public String getLocationId() {
		return locationId;
	}
	
	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}
	
	public String getChildLocationId() {
		return childLocationId;
	}
	
	public void setChildLocationId(String childLocationId) {
		this.childLocationId = childLocationId;
	}
	
	public String getVersion() {
		return version;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public List<Setting> getSettings() {
		return settings;
	}
	
	public void setSettings(List<Setting> settings) {
		this.settings = settings;
	}
}
