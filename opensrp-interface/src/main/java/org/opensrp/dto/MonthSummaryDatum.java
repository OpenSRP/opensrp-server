package org.opensrp.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

public class MonthSummaryDatum {
    private String month;
    private String year;
    private String currentProgress;
    private String aggregatedProgress;
    private List<String> externalIDs;

    public MonthSummaryDatum(String month, String year, String currentProgress, String aggregatedProgress, List<String> externalIDs) {
        this.month = month;
        this.year = year;
        this.currentProgress = currentProgress;
        this.aggregatedProgress = aggregatedProgress;
        this.externalIDs = externalIDs;
    }

    public MonthSummaryDatum() {
    }

    public String getMonth() {
        return month;
    }

    public String getYear() {
        return year;
    }

    public String getCurrentProgress() {
        return currentProgress;
    }

    public String getAggregatedProgress() {
        return aggregatedProgress;
    }

    public List<String> getExternalIDs() {
        return externalIDs;
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
