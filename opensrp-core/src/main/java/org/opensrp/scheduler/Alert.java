package org.opensrp.scheduler;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.ektorp.support.TypeDiscriminator;
import org.joda.time.DateTime;
import org.motechproject.model.MotechBaseDataObject;
import org.motechproject.util.DateUtil;
import org.opensrp.dto.AlertStatus;

/**
 * The entity which helps in identifying the type of action applicable for the entity or provider
 */
@TypeDiscriminator("doc.type === 'Alert'")
public class Alert extends MotechBaseDataObject {
	public enum AlertType{
		notification, reminder
	}
	public enum TriggerType{
		schedule, report, event, caseClosed
	}
    @JsonProperty
    private String providerId;
    @JsonProperty
    private String entityId;
    @JsonProperty
    private String beneficiaryType;
    @JsonProperty
    private String alertType;
    @JsonProperty
    private String triggerType;
    @JsonProperty
    private String triggerName;
    @JsonProperty
    private String triggerCode;
    @JsonProperty
    private String startDate;
    @JsonProperty
    private String expiryDate;
    @JsonProperty
    private String alertStatus;
    @JsonProperty
    private String closingPeriod;
    @JsonProperty
    private String dateClosed;
    @JsonProperty
    private String dateComplete;
    @JsonProperty
    private String reasonClosed;
    @JsonProperty
    private Boolean isActive;
    @JsonProperty
    private long timeStamp;
    @JsonProperty
    private Map<String, String> details;

	private Alert() {
    }
    
    public Alert(String providerId, String entityId, String beneficiaryType, AlertType alertType, 
    		TriggerType triggerType, String triggerName, String triggerCode, DateTime startDate, DateTime expiryDate,
			AlertStatus alertStatus, Map<String, String> details) {
		setProviderId(providerId);
		setEntityId(entityId);
		setBeneficiaryType(beneficiaryType);
		setAlertType(alertType.name());
		setTriggerType(triggerType.name());
		setTriggerName(triggerName);
		setTriggerCode(triggerCode);
		setStartDate(startDate.toLocalDate().toString());
		setExpiryDate(expiryDate.toLocalDate().toString());
		setAlertStatus(alertStatus.name());
		setIsActive(true);
		setTimeStamp(DateUtil.now().getMillis());
		setDetails(details);
	}

    public Alert markAlertAsClosed(String reasonForClose) {
    	if(alertStatus.equalsIgnoreCase(AlertStatus.closed.name())
    			|| alertStatus.equalsIgnoreCase(AlertStatus.complete.name())){
    		throw new IllegalStateException("Alert was found "+alertStatus);
    	}
    	this.closingPeriod = this.alertStatus;
    	this.reasonClosed = reasonForClose;
    	this.alertStatus = AlertStatus.closed.name();
    	this.dateClosed = getCurrentDateTime().toLocalDate().toString();
    	this.isActive = false;
    	
    	return this;
    }
    
    public Alert markAlertAsComplete(String completionDate) {
    	if(alertStatus.equalsIgnoreCase(AlertStatus.closed.name())
    			|| alertStatus.equalsIgnoreCase(AlertStatus.complete.name())){
    		throw new IllegalStateException("Alert was found "+alertStatus);
    	}
    	this.dateComplete = completionDate;
    	this.closingPeriod = this.alertStatus;
    	this.alertStatus = AlertStatus.complete.name();
    	this.dateClosed = getCurrentDateTime().toLocalDate().toString();
    	this.isActive = false;
    	
    	return this;
	}

	@JsonIgnore
	public DateTime getCurrentDateTime() {
	    return new DateTime();
    }

	public String providerId() {
		return providerId;
	}

	public String entityId() {
		return entityId;
	}

	public String beneficiaryType() {
		return beneficiaryType;
	}

	public String alertType() {
		return alertType;
	}

	public String triggerType() {
		return triggerType;
	}

	public String triggerName() {
		return triggerName;
	}

	public String triggerCode() {
		return triggerCode;
	}

	public String startDate() {
		return startDate;
	}

	public String expiryDate() {
		return expiryDate;
	}

	public String alertStatus() {
		return alertStatus;
	}

	public String closingPeriod() {
		return closingPeriod;
	}

	public String dateClosed() {
		return dateClosed;
	}

	public String reasonClosed() {
		return reasonClosed;
	}

	@JsonIgnore
	public Boolean isActive() {
		return isActive;
	}

	public long timestamp() {
		return timeStamp;
	}

	public Map<String, String> details() {
		if(details == null){
			details = new HashMap<>();
		}
		return details;
	}
    
    public Alert withDetails(String key, String val) {
    	if(details == null){
    		details = new HashMap<>();
    	}
    	details.put(key, val);
		return this;
	}


    String getProviderId() {
		return providerId;
	}

	String getEntityId() {
		return entityId;
	}

	String getBeneficiaryType() {
		return beneficiaryType;
	}

	String getTriggerType() {
		return triggerType;
	}
	
	String getAlertType() {
		return alertType;
	}
    public String getTriggerName() {
		return triggerName;
	}

	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}

	public String getTriggerCode() {
		return triggerCode;
	}

	public void setTriggerCode(String triggerCode) {
		this.triggerCode = triggerCode;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getAlertStatus() {
		return alertStatus;
	}

	public void setAlertStatus(String alertStatus) {
		this.alertStatus = alertStatus;
	}

	public String getClosingPeriod() {
		return closingPeriod;
	}

	public void setClosingPeriod(String closingPeriod) {
		this.closingPeriod = closingPeriod;
	}

	public String getDateClosed() {
		return dateClosed;
	}

	public void setDateClosed(String dateClosed) {
		this.dateClosed = dateClosed;
	}

	public String getDateComplete() {
		return dateComplete;
	}

	public void setDateComplete(String dateComplete) {
		this.dateComplete = dateComplete;
	}

	public String getReasonClosed() {
		return reasonClosed;
	}

	public void setReasonClosed(String reasonClosed) {
		this.reasonClosed = reasonClosed;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public Map<String, String> getDetails() {
		return details;
	}

	public void setDetails(Map<String, String> details) {
		this.details = details;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public void setBeneficiaryType(String beneficiaryType) {
		this.beneficiaryType = beneficiaryType;
	}

	public void setAlertType(String alertType) {
		this.alertType = alertType;
	}

	public void setTriggerType(String triggerType) {
		this.triggerType = triggerType;
	}

    @Override
    public final boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o, "timeStamp", "revision");
    }

    @Override
    public final int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, "timeStamp", "revision");
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
