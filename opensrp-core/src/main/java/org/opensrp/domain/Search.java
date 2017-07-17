package org.opensrp.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonProperty;
import org.ektorp.support.TypeDiscriminator;
import org.joda.time.DateTime;
import org.opensrp.common.Gender;

@TypeDiscriminator("doc.type == 'Client'")
public class Search extends BaseEntity {
	@JsonProperty
	private String firstName;
	@JsonProperty
	private String middleName;
	@JsonProperty
	private String lastName;
	@JsonProperty
	private DateTime birthdate;
	@JsonProperty
	private Boolean birthdateApprox;
	@JsonProperty
	private String gender;
	@JsonProperty
	private Map<String, List<String>> relationships;

	protected Search() {

	}

	public Search(String baseEntityId) {
		super(baseEntityId);
	}

	public Search(String baseEntityId, String firstName, String middleName,
			String lastName, DateTime birthdate, Boolean birthdateApprox,
			String gender) {
		super(baseEntityId);
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.birthdate = birthdate;
		this.birthdateApprox = birthdateApprox;
		this.gender = gender;
	}

	public Search(String baseEntityId, String firstName, String middleName,
			String lastName, DateTime birthdate, 
			Boolean birthdateApprox, String gender,
			String identifierType, String identifier) {
		super(baseEntityId);
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.birthdate = birthdate;
		this.birthdateApprox = birthdateApprox;
		this.gender = gender;
		addIdentifier(identifierType, identifier);
	}

	public Search(String baseEntityId, String firstName, String middleName,
			String lastName, DateTime birthdate, 
			Boolean birthdateApprox, String gender,
			List<Address> addresses, Map<String, String> identifiers,
			Map<String, Object> attributes) {
		super(baseEntityId);
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.birthdate = birthdate;
		this.birthdateApprox = birthdateApprox;
		this.gender = gender;
		setIdentifiers(identifiers);
		setAddresses(addresses);
		setAttributes(attributes);
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String fullName() {
		String n = "";
		if (StringUtils.isNotBlank(firstName)) {
			n += firstName;
		}
		if (StringUtils.isNotBlank(middleName)) {
			n += " " + middleName;
		}
		if (StringUtils.isNotBlank(lastName)) {
			n += " " + lastName;
		}
		return n.trim();
	}

	public DateTime getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(DateTime birthdate) {
		this.birthdate = birthdate;
	}

	public Boolean getBirthdateApprox() {
		return birthdateApprox;
	}

	public void setBirthdateApprox(Boolean birthdateApprox) {
		this.birthdateApprox = birthdateApprox;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Map<String, List<String>> getRelationships() {
		return relationships;
	}

	public void setRelationships(Map<String, List<String>> relationships) {
		this.relationships = relationships;
	}

	public Search withFirstName(String firstName) {
		this.firstName = firstName;
		return this;
	}

	public Search withMiddleName(String middleName) {
		this.middleName = middleName;
		return this;
	}

	public Search withLastName(String lastName) {
		this.lastName = lastName;
		return this;
	}

	public Search withName(String firstName, String middleName, String lastName) {
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		return this;
	}

	public Search withBirthdate(DateTime birthdate, Boolean isApproximate) {
		this.birthdate = birthdate;
		this.birthdateApprox = isApproximate;
		return this;
	}

	public Search withGender(String gender) {
		this.gender = gender;
		return this;
	}

	public Search withGender(Gender gender) {
		this.gender = gender.name();
		return this;
	}

	/**
	 * Overrides the existing data
	 */
	public Search withRelationships(Map<String, List<String>> relationships) {
		this.relationships = relationships;
		return this;
	}

	public List<String> findRelatives(String relationshipType) {
		if (relationships == null) {
			relationships = new HashMap<>();
		}

		return relationships.get(relationshipType);
	}

	public void addRelationship(String relationType, String relativeEntityId) {
		if (relationships == null) {
			relationships = new HashMap<>();
		}

		List<String> relatives = findRelatives(relationType);
		if (relatives == null) {
			relatives = new ArrayList<>();
		}
		relatives.add(relativeEntityId);
		relationships.put(relationType, relatives);
	}

	public List<String> getRelationships(String relativeEntityId) {
		List<String> relations = new ArrayList<String>();
		for (Entry<String, List<String>> rl : relationships.entrySet()) {
		    List<String > relativeEntityIdList = rl.getValue();
		    for(String entityId : relativeEntityIdList) {
                if (entityId.equalsIgnoreCase(relativeEntityId)) {
                    relations.add(rl.getKey());
                    break;
                }
            }
		}
		return relations;
	}

	@Override
	public final boolean equals(Object o) {
		return EqualsBuilder.reflectionEquals(this, o, "id", "revision");
	}

	@Override
	public final int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this, "id", "revision");
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
