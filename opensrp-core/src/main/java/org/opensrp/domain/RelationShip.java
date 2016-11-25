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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.joda.time.DateTime;

public class RelationShip {
	
	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getRelationship() {
		return relationship;
	}

	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}
	
	@JsonProperty
	private String relationship; 
	
	@JsonProperty
	private String person_b;
	
	public String getPerson_b() {
		return person_b;
	}

	public void setPerson_b(String person_b) {
		this.person_b = person_b;
	}

	@JsonProperty
	private String startDate;
	
	@JsonProperty
	private String endDate;
	
	public RelationShip() {

	}
	
	public RelationShip(String relationship,String person_b, String startDate,String endDate)
	{
		this.relationship=relationship;
		this.person_b=person_b;
		this.startDate=startDate;
		this.endDate=endDate;
	}
	
	/**
	 * WARNING: Overrides all existing obs
	 * @param obs
	 * @return
	 */
	
	
	@Override
	public boolean equals(Object o) {
		return EqualsBuilder.reflectionEquals(this, o, "; this.d", "revision");
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this, "id", "revision");
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
