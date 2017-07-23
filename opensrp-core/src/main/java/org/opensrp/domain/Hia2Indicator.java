package org.opensrp.domain;

import org.codehaus.jackson.annotate.JsonProperty;

public class Hia2Indicator {
	
	@JsonProperty
	private String indicatorCode;
	
	@JsonProperty
	private String label;
	
	@JsonProperty
	private String dhisId;
	
	@JsonProperty
	private String description;
	
	@JsonProperty
	private String category;
	
	@JsonProperty
	private String value;
	
	@JsonProperty
	private String providerId;
	
	@JsonProperty
	private String updatedAt;
	
	public Hia2Indicator() {
		
	}
	
	public Hia2Indicator(String indicatorCode, String label, String dhisId, String description, String category,
	    String value, String providerId, String updatedAt) {
		this.indicatorCode = indicatorCode;
		this.label = label;
		this.dhisId = dhisId;
		this.description = description;
		this.category = category;
		this.value = value;
		this.providerId = providerId;
		this.updatedAt = updatedAt;
		
	}
	
	public Hia2Indicator(String indicatorCode, String dhisId, String value) {
		this.indicatorCode = indicatorCode;
		this.dhisId = dhisId;
		this.value = value;		
	}
	
	public String getIndicatorCode() {
		return indicatorCode;
	}
	
	public void setIndicatorCode(String indicatorCode) {
		this.indicatorCode = indicatorCode;
	}
	
	public String getLabel() {
		return label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public String getDhisId() {
		return dhisId;
	}
	
	public void setDhisId(String dhisId) {
		this.dhisId = dhisId;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getProviderId() {
		return providerId;
	}
	
	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}
	
	public String getUpdatedAt() {
		return updatedAt;
	}
	
	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}
	
}
