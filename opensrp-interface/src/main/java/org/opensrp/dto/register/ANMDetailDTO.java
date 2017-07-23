package org.opensrp.dto.register;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonProperty;
import org.opensrp.dto.LocationDTO;

public class ANMDetailDTO {
    @JsonProperty
    private String identifier;

    @JsonProperty
    private String name;

    @JsonProperty
    private LocationDTO location;

    @JsonProperty
    private int ecCount;

    @JsonProperty
    private int fpCount;

    @JsonProperty
    private int ancCount;

    @JsonProperty
    private int pncCount;

    @JsonProperty
    private int childCount;

    public ANMDetailDTO(String identifier, String name, LocationDTO location, int ecCount, int fpCount, int ancCount, int pncCount, int childCount) {
        this.identifier = identifier;
        this.name = name;
        this.location = location;
        this.ecCount = ecCount;
        this.fpCount = fpCount;
        this.ancCount = ancCount;
        this.pncCount = pncCount;
        this.childCount = childCount;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocationDTO getLocation() {
        return location;
    }

    public void setLocation(LocationDTO location) {
        this.location = location;
    }

    public int getEcCount() {
        return ecCount;
    }

    public void setEcCount(int ecCount) {
        this.ecCount = ecCount;
    }

    public int getFpCount() {
        return fpCount;
    }

    public void setFpCount(int fpCount) {
        this.fpCount = fpCount;
    }

    public int getAncCount() {
        return ancCount;
    }

    public void setAncCount(int ancCount) {
        this.ancCount = ancCount;
    }

    public int getPncCount() {
        return pncCount;
    }

    public void setPncCount(int pncCount) {
        this.pncCount = pncCount;
    }

    public int getChildCount() {
        return childCount;
    }

    public void setChildCount(int childCount) {
        this.childCount = childCount;
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
