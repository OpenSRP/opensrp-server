package org.opensrp.dto.register;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;
import java.util.Map;

public class CondomFPDetailsDTO {
    @JsonProperty
    private String fpAcceptanceDate;
    @JsonProperty
    private List<Map<String, String>> refills;

    public CondomFPDetailsDTO(String fpAcceptanceDate, List<Map<String, String>> refills) {
        this.fpAcceptanceDate = fpAcceptanceDate;
        this.refills = refills;
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
