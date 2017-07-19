package org.opensrp.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

public class VillagesDTO {
    @JsonProperty
    private String district;

    @JsonProperty
    private String phcName;

    @JsonProperty
    private String phcIdentifier;

    @JsonProperty
    private String subCenter;

    @JsonProperty
    private List<String> villages;

    public VillagesDTO(String district, String phcName, String phcIdentifier, String subCenter, List<String> villages) {
        this.district = district;
        this.phcName = phcName;
        this.phcIdentifier = phcIdentifier;
        this.subCenter = subCenter;
        this.villages = villages;
    }

    public String getDistrict() {
        return district;
    }

    public String getPhcName() {
        return phcName;
    }

    public String getPhcIdentifier() {
        return phcIdentifier;
    }

    public String getSubCenter() {
        return subCenter;
    }

    public List<String> getVillages() {
        return villages;
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
