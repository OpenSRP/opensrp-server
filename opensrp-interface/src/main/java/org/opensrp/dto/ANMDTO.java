package org.opensrp.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonProperty;

public class ANMDTO {
    @JsonProperty
    private String identifier;

    @JsonProperty
    private String name;

    @JsonProperty
    private LocationDTO location;

    public ANMDTO(String identifier, String name, LocationDTO location) {
        this.identifier = identifier;
        this.name = name;
        this.location = location;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getName() {
        return name;
    }

    public LocationDTO getLocation() {
        return location;
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
