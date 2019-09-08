package org.opensrp.domain.postgres;

import java.util.Date;

public class ClientMetadata {
	
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column
	 * core.client_metadata.id
	 * 
	 * @mbg.generated Fri Apr 06 15:32:26 EAT 2018
	 */
	private Long id;
	
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column
	 * core.client_metadata.client_id
	 * 
	 * @mbg.generated Fri Apr 06 15:32:26 EAT 2018
	 */
	private Long clientId;
	
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column
	 * core.client_metadata.document_id
	 * 
	 * @mbg.generated Fri Apr 06 15:32:26 EAT 2018
	 */
	private String documentId;
	
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column
	 * core.client_metadata.base_entity_id
	 * 
	 * @mbg.generated Fri Apr 06 15:32:26 EAT 2018
	 */
	private String baseEntityId;
	
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column
	 * core.client_metadata.relational_id
	 * 
	 * @mbg.generated Fri Apr 06 15:32:26 EAT 2018
	 */
	private String relationalId;
	
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column
	 * core.client_metadata.server_version
	 * 
	 * @mbg.generated Fri Apr 06 15:32:26 EAT 2018
	 */
	private Long serverVersion;
	
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column
	 * core.client_metadata.openmrs_uuid
	 * 
	 * @mbg.generated Fri Apr 06 15:32:26 EAT 2018
	 */
	private String openmrsUuid;
	
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column
	 * core.client_metadata.unique_id
	 * 
	 * @mbg.generated Fri Apr 06 15:32:26 EAT 2018
	 */
	private String uniqueId;
	
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column
	 * core.client_metadata.first_name
	 * 
	 * @mbg.generated Fri Apr 06 15:32:26 EAT 2018
	 */
	private String firstName;
	
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column
	 * core.client_metadata.middle_name
	 * 
	 * @mbg.generated Fri Apr 06 15:32:26 EAT 2018
	 */
	private String middleName;
	
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column
	 * core.client_metadata.last_name
	 * 
	 * @mbg.generated Fri Apr 06 15:32:26 EAT 2018
	 */
	private String lastName;
	
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column
	 * core.client_metadata.birth_date
	 * 
	 * @mbg.generated Fri Apr 06 15:32:26 EAT 2018
	 */
	private Date birthDate;
	
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column
	 * core.client_metadata.date_deleted
	 * 
	 * @mbg.generated Fri Apr 06 15:32:26 EAT 2018
	 */
	private Date dateDeleted;
	
	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database
	 * column core.client_metadata.id
	 * 
	 * @return the value of core.client_metadata.id
	 * @mbg.generated Fri Apr 06 15:32:26 EAT 2018
	 */
	private String address2;
	
	private String address1;
	
	private String address3;

	private String division;

	private String district;

	private String cityCorporation;

	private String upazila;

	private String cityUnion;

	private String ward;

	private String village;

	public String getAddress2() {
		return address2;
	}
	
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	
	public String getAddress1() {
		return address1;
	}
	
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	
	public String getAddress3() {
		return address3;
	}
	
	public void setAddress3(String address3) {
		this.address3 = address3;
	}
	
	public Long getId() {
		return id;
	}
	
	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database
	 * column core.client_metadata.id
	 * 
	 * @param id the value for core.client_metadata.id
	 * @mbg.generated Fri Apr 06 15:32:26 EAT 2018
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database
	 * column core.client_metadata.client_id
	 * 
	 * @return the value of core.client_metadata.client_id
	 * @mbg.generated Fri Apr 06 15:32:26 EAT 2018
	 */
	public Long getClientId() {
		return clientId;
	}
	
	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database
	 * column core.client_metadata.client_id
	 * 
	 * @param clientId the value for core.client_metadata.client_id
	 * @mbg.generated Fri Apr 06 15:32:26 EAT 2018
	 */
	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}
	
	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database
	 * column core.client_metadata.document_id
	 * 
	 * @return the value of core.client_metadata.document_id
	 * @mbg.generated Fri Apr 06 15:32:26 EAT 2018
	 */
	public String getDocumentId() {
		return documentId;
	}
	
	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database
	 * column core.client_metadata.document_id
	 * 
	 * @param documentId the value for core.client_metadata.document_id
	 * @mbg.generated Fri Apr 06 15:32:26 EAT 2018
	 */
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}
	
	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database
	 * column core.client_metadata.base_entity_id
	 * 
	 * @return the value of core.client_metadata.base_entity_id
	 * @mbg.generated Fri Apr 06 15:32:26 EAT 2018
	 */
	public String getBaseEntityId() {
		return baseEntityId;
	}
	
	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database
	 * column core.client_metadata.base_entity_id
	 * 
	 * @param baseEntityId the value for core.client_metadata.base_entity_id
	 * @mbg.generated Fri Apr 06 15:32:26 EAT 2018
	 */
	public void setBaseEntityId(String baseEntityId) {
		this.baseEntityId = baseEntityId;
	}
	
	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database
	 * column core.client_metadata.relational_id
	 * 
	 * @return the value of core.client_metadata.relational_id
	 * @mbg.generated Fri Apr 06 15:32:26 EAT 2018
	 */
	public String getRelationalId() {
		return relationalId;
	}
	
	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database
	 * column core.client_metadata.relational_id
	 * 
	 * @param relationalId the value for core.client_metadata.relational_id
	 * @mbg.generated Fri Apr 06 15:32:26 EAT 2018
	 */
	public void setRelationalId(String relationalId) {
		this.relationalId = relationalId;
	}
	
	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database
	 * column core.client_metadata.server_version
	 * 
	 * @return the value of core.client_metadata.server_version
	 * @mbg.generated Fri Apr 06 15:32:26 EAT 2018
	 */
	public Long getServerVersion() {
		return serverVersion;
	}
	
	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database
	 * column core.client_metadata.server_version
	 * 
	 * @param serverVersion the value for core.client_metadata.server_version
	 * @mbg.generated Fri Apr 06 15:32:26 EAT 2018
	 */
	public void setServerVersion(Long serverVersion) {
		this.serverVersion = serverVersion;
	}
	
	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database
	 * column core.client_metadata.openmrs_uuid
	 * 
	 * @return the value of core.client_metadata.openmrs_uuid
	 * @mbg.generated Fri Apr 06 15:32:26 EAT 2018
	 */
	public String getOpenmrsUuid() {
		return openmrsUuid;
	}
	
	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database
	 * column core.client_metadata.openmrs_uuid
	 * 
	 * @param openmrsUuid the value for core.client_metadata.openmrs_uuid
	 * @mbg.generated Fri Apr 06 15:32:26 EAT 2018
	 */
	public void setOpenmrsUuid(String openmrsUuid) {
		this.openmrsUuid = openmrsUuid;
	}
	
	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database
	 * column core.client_metadata.unique_id
	 * 
	 * @return the value of core.client_metadata.unique_id
	 * @mbg.generated Fri Apr 06 15:32:26 EAT 2018
	 */
	public String getUniqueId() {
		return uniqueId;
	}
	
	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database
	 * column core.client_metadata.unique_id
	 * 
	 * @param uniqueId the value for core.client_metadata.unique_id
	 * @mbg.generated Fri Apr 06 15:32:26 EAT 2018
	 */
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}
	
	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database
	 * column core.client_metadata.first_name
	 * 
	 * @return the value of core.client_metadata.first_name
	 * @mbg.generated Fri Apr 06 15:32:26 EAT 2018
	 */
	public String getFirstName() {
		return firstName;
	}
	
	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database
	 * column core.client_metadata.first_name
	 * 
	 * @param firstName the value for core.client_metadata.first_name
	 * @mbg.generated Fri Apr 06 15:32:26 EAT 2018
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database
	 * column core.client_metadata.middle_name
	 * 
	 * @return the value of core.client_metadata.middle_name
	 * @mbg.generated Fri Apr 06 15:32:26 EAT 2018
	 */
	public String getMiddleName() {
		return middleName;
	}
	
	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database
	 * column core.client_metadata.middle_name
	 * 
	 * @param middleName the value for core.client_metadata.middle_name
	 * @mbg.generated Fri Apr 06 15:32:26 EAT 2018
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	
	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database
	 * column core.client_metadata.last_name
	 * 
	 * @return the value of core.client_metadata.last_name
	 * @mbg.generated Fri Apr 06 15:32:26 EAT 2018
	 */
	public String getLastName() {
		return lastName;
	}
	
	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database
	 * column core.client_metadata.last_name
	 * 
	 * @param lastName the value for core.client_metadata.last_name
	 * @mbg.generated Fri Apr 06 15:32:26 EAT 2018
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database
	 * column core.client_metadata.birth_date
	 * 
	 * @return the value of core.client_metadata.birth_date
	 * @mbg.generated Fri Apr 06 15:32:26 EAT 2018
	 */
	public Date getBirthDate() {
		return birthDate;
	}
	
	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database
	 * column core.client_metadata.birth_date
	 * 
	 * @param birthDate the value for core.client_metadata.birth_date
	 * @mbg.generated Fri Apr 06 15:32:26 EAT 2018
	 */
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	
	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database
	 * column core.client_metadata.date_deleted
	 * 
	 * @return the value of core.client_metadata.date_deleted
	 * @mbg.generated Fri Apr 06 15:32:26 EAT 2018
	 */
	public Date getDateDeleted() {
		return dateDeleted;
	}
	
	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database
	 * column core.client_metadata.date_deleted
	 * 
	 * @param dateDeleted the value for core.client_metadata.date_deleted
	 * @mbg.generated Fri Apr 06 15:32:26 EAT 2018
	 */
	public void setDateDeleted(Date dateDeleted) {
		this.dateDeleted = dateDeleted;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getCityCorporation() {
		return cityCorporation;
	}

	public void setCityCorporation(String cityCorporation) {
		this.cityCorporation = cityCorporation;
	}

	public String getUpazila() {
		return upazila;
	}

	public void setUpazila(String upazila) {
		this.upazila = upazila;
	}

	public String getCityUnion() {
		return cityUnion;
	}

	public void setCityUnion(String cityUnion) {
		this.cityUnion = cityUnion;
	}

	public String getWard() {
		return ward;
	}

	public void setWard(String ward) {
		this.ward = ward;
	}

	public String getVillage() {
		return village;
	}

	public void setVillage(String village) {
		this.village = village;
	}
}
