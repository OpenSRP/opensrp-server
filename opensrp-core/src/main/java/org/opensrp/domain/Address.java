package org.opensrp.domain;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.opensrp.api.constants.AddressField;

public class Address{

	@JsonProperty
	private String addressType;
	@JsonProperty
	private Date startDate;
	@JsonProperty
	private Date endDate;
	@JsonProperty
	private Map<String, String> addressFields;
	@JsonProperty
	private String latitude;
	@JsonProperty
	private String longitute;
	@JsonProperty
	private String postalCode;
	@JsonProperty
	private String state;
	@JsonProperty
	private String country;
	
	
	public Address() {	}

	public Address(String addressType, Date startDate, Date endDate, Map<String, String> addressFields, 
			String latitude, String longitute, String postalCode, String state, String country) {
		this.addressType = addressType;
		this.startDate = startDate;
		this.endDate = endDate;
		this.addressFields = addressFields;
		this.latitude = latitude;
		this.longitute = longitute;
		this.postalCode = postalCode;
		this.state = state;
		this.country = country;
	}

	public String getAddressType() {
		return addressType;
	}

	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Map<String, String> getAddressFields() {
		return addressFields;
	}
	
	public String getAddressField(String addressField) {
		return addressFields.get(addressField);
	}

	public String getAddressField(AddressField addressField) {
		return addressFields.get(addressField.name());
	}
	
	/**
	 * WARNING: Overrides all existing fields
	 * @param addressFields
	 * @return
	 */
	public void setAddressFields(Map<String, String> addressFields) {
		this.addressFields = addressFields;
	}

	public void addAddressField(String field, String value) {
		if(addressFields == null){
			addressFields = new HashMap<>();
		}
		addressFields.put(field, value);
	}
	
	/**
	 * Add field name from a list of predefined options from enum {@link AddressField}
	 * @param field
	 * @param value
	 */
	public void addAddressField(AddressField field, String value) {
		if(addressFields == null){
			addressFields = new HashMap<>();
		}
		addressFields.put(field.name(), value);
	}
	
	public void removeAddressField(AddressField field) {
		addressFields.remove(field.name());
	}
	
	public void removeAddressField(String field) {
		addressFields.remove(field);
	}
	
	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitute() {
		return longitute;
	}

	public void setLongitute(String longitute) {
		this.longitute = longitute;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	


	/**
	 * True if endDate is null or endDate is in future
	 * @return
	 */
	@JsonIgnore
	public boolean isActive() {
		return endDate==null||endDate.after(new Date());
	}

	/**
	 * If startDate is not specified returns -1. If endDate is not specified duration is from startDate to current date 
	 * @return
	 */
	private long durationInMillis() {
		if(startDate == null){
			return -1;
		}
		if(endDate == null){
			return new Date().getTime()-startDate.getTime();
		}
		
		return endDate.getTime()-startDate.getTime();
	}

	/**
	 * If startDate is not specified returns -1. If endDate is not specified duration is from startDate to current date 
	 * @return
	 */
	public int durationInDays() {
		return (int) (durationInMillis()==-1?durationInMillis():(durationInMillis()/(1000*60*60*24)));
	}
	/**
	 * If startDate is not specified returns -1. If endDate is not specified duration is from startDate to current date 
	 * @return
	 */
	public int durationInWeeks() {
		return durationInDays()==-1?durationInDays():(durationInDays()/7);
	}
	/**
	 * If startDate is not specified returns -1. If endDate is not specified duration is from startDate to current date 
	 * @return
	 */
	public int durationInMonths() {
		return durationInDays()==-1?durationInDays():((int) (durationInDays()/30));
	}
	/**
	 * If startDate is not specified returns -1. If endDate is not specified duration is from startDate to current date 
	 * @return
	 */
	public int durationInYears() {
		return durationInDays()==-1?durationInDays():(durationInDays()/365);
	}
	
	/**
	 * The type address represents
	 * @param addressType
	 * @return
	 */
	public Address withAddressType(String addressType) {
		this.addressType = addressType;
		return this;
	}

	/**
	 * The date when address was started or owned
	 * @param endDate
	 * @return
	 */
	public Address withStartDate(Date startDate) {
		this.startDate = startDate;
		return this;
	}

	/**
	 * The date when address was outdated or abandoned
	 * @param endDate
	 * @return
	 */
	public Address withEndDate(Date endDate) {
		this.endDate = endDate;
		return this;
	}

	/**
	 * WARNING: Overrides all existing fields
	 * @param addressFields
	 * @return
	 */
	public Address withAddressFields(Map<String, String> addressFields) {
		this.addressFields = addressFields;
		return this;
	}

	public Address withAddressField(String field, String value) {
		if(addressFields == null){
			addressFields = new HashMap<>();
		}
		addressFields.put(field, value);
		return this;
	}
	
	public Address withAddressField(AddressField field, String value) {
		if(addressFields == null){
			addressFields = new HashMap<>();
		}
		addressFields.put(field.name(), value);
		return this;
	}

	public Address withLatitude(String latitude) {
		this.latitude = latitude;
		return this;
	}

	public Address withLongitute(String longitute) {
		this.longitute = longitute;
		return this;
	}

	public Address withPostalCode(String postalCode) {
		this.postalCode = postalCode;
		return this;
	}

	public Address withState(String state) {
		this.state = state;
		return this;
	}

	public Address withCountry(String country) {
		this.country = country;
		return this;
	}
	
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
