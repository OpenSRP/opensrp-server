package org.opensrp.common.domain;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

public final class MonthSummary {
    @JsonProperty
    private final String month;
    @JsonProperty
    private final String year;
    @JsonProperty
    private final String currentProgress;
    @JsonProperty
    private final String aggregatedProgress;
    @JsonProperty
    private final List<String> externalIDs;

    public MonthSummary(String month, String year, String currentProgress, String aggregatedProgress, List<String> externalIDs) {
        this.month = month;
        this.year = year;
        this.currentProgress = currentProgress;
        this.aggregatedProgress = aggregatedProgress;
        this.externalIDs = externalIDs;
    }

    public String month() {
        return month;
    }

    public String year() {
        return year;
    }

    public String currentProgress() {
        return currentProgress;
    }

    public String aggregatedProgress() {
        return aggregatedProgress;
    }

    public List<String> externalIDs() {
        return externalIDs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MonthSummary)) return false;

        MonthSummary that = (MonthSummary) o;

        if (aggregatedProgress != null ? !aggregatedProgress.equals(that.aggregatedProgress) : that.aggregatedProgress != null)
            return false;
        if (currentProgress != null ? !currentProgress.equals(that.currentProgress) : that.currentProgress != null)
            return false;
        if (externalIDs != null ? !externalIDs.equals(that.externalIDs) : that.externalIDs != null) return false;
        if (month != null ? !month.equals(that.month) : that.month != null) return false;
        if (year != null ? !year.equals(that.year) : that.year != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
