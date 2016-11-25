package org.opensrp.api.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonProperty;
import org.ektorp.support.TypeDiscriminator;
import org.joda.time.LocalDate;
import org.motechproject.model.MotechBaseDataObject;

@TypeDiscriminator("doc.type === 'Drug'")
public class Drug extends BaseDataObject {
	@JsonProperty
	private String drugName;
	@JsonProperty
	private String drugBaseName;
	@JsonProperty
	private Map<String, String> codes;
	@JsonProperty
	private String route;
	@JsonProperty
	private String id;
	@JsonProperty
	private String creator;
	@JsonProperty
	private String doseStrength;
	@JsonProperty
	private String units;
	@JsonProperty
	private String maximumDailyDose;
	@JsonProperty
	private String minimumDailyDose;
	@JsonProperty
	private String description;
	
	
	@JsonProperty
	private String combination;
	
		
	protected Drug() {
		
	}

	public Drug(String baseEntityId) {
		
	}
	public Drug(String drugName,String drugBaseName, Map<String, String> codes,
			String route, String creator, String doseStrenght
			, String units, String maxDailyDose,String miniDailyDose,String Description,String combination) {
	this.drugName=drugName;
	this.drugBaseName=drugBaseName;
	this.codes=codes;
	this.creator=creator;
	this.doseStrength=doseStrenght;
	this.route=route;
	this.maximumDailyDose=maxDailyDose;
	this.minimumDailyDose=miniDailyDose;
	this.description=Description;
	this.combination=combination;
	
	
	}
	@Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, "id");
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }              
	public String getDoseStrength() {
		return doseStrength;
	}

	public void setDoseStrength(String doseStrength) {
		this.doseStrength = doseStrength;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public String getMaximumDailyDose() {
		return maximumDailyDose;
	}

	public void setMaximumDailyDose(String maximumDailyDose) {
		this.maximumDailyDose = maximumDailyDose;
	}

	public String getMinimumDailyDose() {
		return minimumDailyDose;
	}

	public void setMinimumDailyDose(String minimumDailyDose) {
		this.minimumDailyDose = minimumDailyDose;
	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public String getDescription() {
		return description;
	}

	
	public String getCreatorName() {
		return creator;
	}

	public void setCreatorName(String creator) {
		this.creator = creator;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCombination() {
		return combination;
	}

	public void setCombination(String combination) {
		this.combination = combination;
	}	
}
