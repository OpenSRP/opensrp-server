package org.opensrp.dto.register;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonProperty;

public class IUDFPDetailsDTO {
    @JsonProperty
    private String fpAcceptanceDate;
    @JsonProperty
    private String iudPlace;
    @JsonProperty
    private String lmpDate;
    @JsonProperty
    private String uptResult;

    public IUDFPDetailsDTO(String fpAcceptanceDate, String iudPlace, String lmpDate, String uptResult) {
        this.fpAcceptanceDate = fpAcceptanceDate;
        this.iudPlace = iudPlace;
        this.lmpDate = lmpDate;
        this.uptResult = uptResult;
    }

    public String getFpAcceptanceDate() {
        return fpAcceptanceDate;
    }

    public String getIudPlace() {
        return iudPlace;
    }

    public String getLmpDate() {
        return lmpDate;
    }

    public String getUptResult() {
        return uptResult;
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
