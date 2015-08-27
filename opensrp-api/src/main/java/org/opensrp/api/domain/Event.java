package org.opensrp.api.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.opensrp.api.domain.form.FormSubmission;

/**
 * An {@link Event} is a logical happening or experience of {@link BaseEntity} with the system as defined by the 
 * program workflow. The difference of an {@link Event} and other activities in system is that the data of {@link Event} 
 * is meaningful for business clients of system and also the data links back with international standard dictionaries. 
 * An {@link Event} in system is recorded via a form filled by data {@link Provider}. The form data is represented by 
 * {@link FormSubmission}
 */
public class Event extends BaseDataObject{

	private String baseEntityId;
	private BaseEntity baseEntity;
	private String locationId;
	private Location location;
	private Date eventDate;
	private String eventType;
	private String formSubmissionId;
	private String providerId;
	private Provider provider;
	private List<Obs> obs;
	
	public Event() {}

	public Event(String eventType, Date eventDate, String formSubmissionId, String providerId, 
			String baseEntityId, String firstName, String middleName, String lastName, Date birthdate, 
			Date deathdate, Boolean birthdateApprox, Boolean deathdateApprox, String gender) {
		this.baseEntity = new BaseEntity(baseEntityId, firstName, middleName, lastName, birthdate, deathdate, birthdateApprox, deathdateApprox, gender, null, null);
		this.baseEntityId = baseEntityId;
		this.eventType = eventType;
		this.eventDate = eventDate;
		this.formSubmissionId = formSubmissionId;
		this.providerId = providerId;
	}
	
	public List<Obs> getObs() {
		return obs;
	}

	/**
	 * WARNING: Overrides all existing obs
	 * @param obs
	 * @return
	 */
	public void setObs(List<Obs> obs) {
		this.obs = obs;
	}
	
	public void addObs(Obs observation) {
		if(obs == null){
			obs = new ArrayList<>();
		}
		
		obs.add(observation);
	}
	
	public String getBaseEntityId() {
		return baseEntityId;
	}
	
	public void setBaseEntityId(String baseEntityId) {
		this.baseEntityId = baseEntityId;
	}

	public BaseEntity getBaseEntity() {
		return baseEntity;
	}

	public void setBaseEntity(BaseEntity baseEntity) {
		this.baseEntity = baseEntity;
		this.baseEntityId = baseEntity.getId();
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Date getEventDate() {
		return eventDate;
	}

	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
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

	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
	}

	public Event withBaseEntity(BaseEntity baseEntity) {
		this.baseEntity = baseEntity;
		this.baseEntityId = baseEntity.getId();
		return this;
	}
	
	public Event withBaseEntityId(String baseEntityId) {
		this.baseEntityId = baseEntityId;
		return this;
	}

	public Event withLocationId(String locationId) {
		this.locationId = locationId;
		return this;
	}

	public Event withLocation(Location location) {
		this.location = location;
		return this;
	}

	public Event withEventDate(Date eventDate) {
		this.eventDate = eventDate;
		return this;
	}

	public Event withEventType(String eventType) {
		this.eventType = eventType;
		return this;
	}

	public Event withFormSubmissionId(String formSubmissionId) {
		this.formSubmissionId = formSubmissionId;
		return this;
	}

	public Event withProviderId(String providerId) {
		this.providerId = providerId;
		return this;
	}

	public Event withProvider(Provider provider) {
		this.provider = provider;
		return this;
	}
	
	/**
	 * WARNING: Overrides all existing obs
	 * @param obs
	 * @return
	 */
	public Event withObs(List<Obs> obs) {
		this.obs = obs;
		return this;
	}
	
	public Event withObs(Obs observation) {
		if(obs == null){
			obs = new ArrayList<>();
		}
		obs.add(observation);
		return this;
	}

	@Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
