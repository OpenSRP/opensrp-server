package org.opensrp.api.domain;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.opensrp.api.constants.AddressField;

public class Address{

	private String addressType;
	private Date startDate;
	private Date endDate;
	private Map<String, String> addressFields;
	private String latitude;
	private String longitute;
	private String postalCode;
	private String state;
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
		for (Entry<String, String> a : addressFields.entrySet()) {
			if(a.getKey().equalsIgnoreCase(addressField)){
				return a.getValue();
			}
		}
		return null;
	}
	
	/**
	 * Returns field matching the regex. Note that incase of multiple fields matching criteria 
	 * function would return first match. The must be well formed to find out a single value
	 * @param regex
	 * @return
	 */
	public String getAddressFieldMatchingRegex(String regex) {
		for (Entry<String, String> a : addressFields.entrySet()) {
			if(a.getKey().matches(regex)){
				return a.getValue();
			}
		}
		return null;
	}

	public String getAddressField(AddressField addressField) {
		for (Entry<String, String> a : addressFields.entrySet()) {
			if(a.getKey().equalsIgnoreCase(addressField.name())){
				return a.getValue();
			}
		}
		return null;
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
		for (Entry<String, String> a : addressFields.entrySet()) {
			if(a.getKey().equalsIgnoreCase(field.name())){
				addressFields.remove(a.getKey());
			}
		}
	}
	
	public void removeAddressField(String field) {
		for (Entry<String, String> a : addressFields.entrySet()) {
			if(a.getKey().equalsIgnoreCase(field)){
				addressFields.remove(a.getKey());
			}
		}
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
		return (int) (durationInMillis()==-1?durationInMillis():(durationInMillis()/(1000*60*60*24L)));
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
