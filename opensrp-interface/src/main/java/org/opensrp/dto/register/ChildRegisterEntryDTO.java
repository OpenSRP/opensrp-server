package org.opensrp.dto.register;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Map;

public class ChildRegisterEntryDTO {
    @JsonProperty
    private String thayiCardNumber;
    @JsonProperty
    private String wifeName;
    @JsonProperty
    private String husbandName;
    @JsonProperty
    private String village;
    @JsonProperty
    private String subCenter;
    @JsonProperty
    private String wifeDOB;
    @JsonProperty
    private String dob;
    @JsonProperty
    private Map<String, String> immunizations;
    @JsonProperty
    private Map<String, String> vitaminADoses;

    public ChildRegisterEntryDTO withThayiCardNumber(String thayiCardNumber) {
        this.thayiCardNumber = thayiCardNumber;
        return this;
    }

    public ChildRegisterEntryDTO withWifeName(String wifeName) {
        this.wifeName = wifeName;
        return this;
    }

    public ChildRegisterEntryDTO withHusbandName(String husbandName) {
        this.husbandName = husbandName;
        return this;
    }

    public ChildRegisterEntryDTO withVillage(String village) {
        this.village = village;
        return this;
    }

    public ChildRegisterEntryDTO withSubCenter(String subCenter) {
        this.subCenter = subCenter;
        return this;
    }

    public ChildRegisterEntryDTO withWifeDOB(String wifeDOB) {
        this.wifeDOB = wifeDOB;
        return this;
    }

    public ChildRegisterEntryDTO withDOB(String DOB) {
        this.dob = DOB;
        return this;
    }

    public ChildRegisterEntryDTO withImmunizations(Map<String, String> immunizations) {
        this.immunizations = immunizations;
        return this;
    }

    public ChildRegisterEntryDTO withVitaminADoses(Map<String, String> vitaminADoses) {
        this.vitaminADoses = vitaminADoses;
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
