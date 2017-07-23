package org.opensrp.dto.register;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;
import java.util.Map;

public class PNCVisitDTO {

    @JsonProperty
    private String date;
    @JsonProperty
    private String person;
    @JsonProperty
    private String place;
    @JsonProperty
    private String difficulties;
    @JsonProperty
    private String abdominalProblems;
    @JsonProperty
    private String vaginalProblems;
    @JsonProperty
    private String urinalProblems;
    @JsonProperty
    private String breastProblems;
    @JsonProperty
    private List<Map<String, String>> childrenDetails;

    public PNCVisitDTO withDate(String date) {
        this.date = date;
        return this;
    }

    public PNCVisitDTO withPerson(String person) {
        this.person = person;
        return this;
    }

    public PNCVisitDTO withPlace(String place) {
        this.place = place;
        return this;
    }

    public PNCVisitDTO withDifficulties(String difficulties) {
        this.difficulties = difficulties;
        return this;
    }

    public PNCVisitDTO withAbdominalProblems(String abdominalProblems) {
        this.abdominalProblems = abdominalProblems;
        return this;
    }

    public PNCVisitDTO withVaginalProblems(String vaginalProblems) {
        this.vaginalProblems = vaginalProblems;
        return this;
    }

    public PNCVisitDTO withUrinalProblems(String urinalProblems) {
        this.urinalProblems = urinalProblems;
        return this;
    }

    public PNCVisitDTO withBreastProblems(String breastProblems) {
        this.breastProblems = breastProblems;
        return this;
    }

    public PNCVisitDTO withChildrenDetails(List<Map<String, String>> childrenDetails) {
        this.childrenDetails = childrenDetails;
        return this;
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
