package org.opensrp.dto.aggregatorResponse;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonProperty;

public class AggregatorResponseDTO {
    @JsonProperty
    private String indicator;

    @JsonProperty
    private Integer nrhm_report_indicator_count;

    public AggregatorResponseDTO(String indicator, Integer nrhm_report_indicator_count) {
        this.indicator = indicator;
        this.nrhm_report_indicator_count = nrhm_report_indicator_count;
    }

    public String indicator() {
        return indicator;
    }

    public Integer count() {
        return nrhm_report_indicator_count;
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
