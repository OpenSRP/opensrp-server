package org.opensrp.domain;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
import org.ektorp.support.TypeDiscriminator;
import org.joda.time.DateTime;

@TypeDiscriminator("doc.type == 'Report'")
public class Report extends BaseEntity {
	
	@JsonProperty
	private String locationId;
	
	@JsonProperty
	private DateTime reportDate;
	
	@JsonProperty
	private String reportType;
	
	@JsonProperty
	private String formSubmissionId;
	
	@JsonProperty
	private String providerId;
	
	@JsonProperty
	private String status;
	
	@JsonProperty
	private long version;
	
	@JsonProperty
	private int duration;
	
	@JsonProperty
	private List<Hia2Indicator> hia2Indicators;
	
	public Report() {
		this.version = System.currentTimeMillis();
	}
	
	public Report(String baseEntityId, String locationId, DateTime reportDate, String reportType, String formSubmissionId,
	    String providerId, String status, long version, int duration, List<Hia2Indicator> hia2Indicators) {
		super(baseEntityId);
		this.locationId = locationId;
		this.reportDate = reportDate;
		this.reportType = reportType;
		this.formSubmissionId = formSubmissionId;
		this.providerId = providerId;
		this.status = status;
		this.version = version;
		this.duration = duration;
		this.hia2Indicators = hia2Indicators;
	}
	
	public String getLocationId() {
		return locationId;
	}
	
	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}
	
	public DateTime getReportDate() {
		return reportDate;
	}
	
	public void setReportDate(DateTime reportDate) {
		this.reportDate = reportDate;
	}
	
	public String getReportType() {
		return reportType;
	}
	
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	
	public String getFormSubmissionId() {
		return formSubmissionId;
	}
	
	public void setFormSubmissionId(String formSubmissionId) {
		this.formSubmissionId = formSubmissionId;
	}
	
	public String getProviderId() {
		return providerId;
	}
	
	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public long getVersion() {
		return version;
	}
	
	public void setVersion(long version) {
		this.version = version;
	}
	
	public int getDuration() {
		return duration;
	}
	
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	public List<Hia2Indicator> getHia2Indicators() {
		return hia2Indicators;
	}
	
	public void setHia2Indicators(List<Hia2Indicator> hia2Indicators) {
		this.hia2Indicators = hia2Indicators;
	}
	
}
