package org.opensrp.dto.register;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

public class FemaleSterilizationFPDetailsDTO {
    @JsonProperty
    private String typeOfSterilization;
    @JsonProperty
    private String sterilizationDate;
    @JsonProperty
    private List<String> followupVisitDates;

    public String getTypeOfSterilization() {
        return typeOfSterilization;
    }

    public String getSterilizationDate() {
        return sterilizationDate;
    }

    public List<String> getFollowupVisitDates() {
        return followupVisitDates;
    }

    public FemaleSterilizationFPDetailsDTO(String typeOfSterilization, String sterilizationDate, List<String> followupVisitDates) {
        this.typeOfSterilization = typeOfSterilization;
        this.sterilizationDate = sterilizationDate;
        this.followupVisitDates = followupVisitDates;
    }

    @Override
    public final boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public final int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
