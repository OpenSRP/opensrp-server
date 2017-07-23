package org.opensrp.dto.register;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonProperty;

public class OCPRegisterEntryDTO {
    @JsonProperty
    private String ecNumber;
    @JsonProperty
    private String wifeName;
    @JsonProperty
    private String husbandName;
    @JsonProperty
    private String village;
    @JsonProperty
    private String subCenter;
    @JsonProperty
    private String wifeAge;
    @JsonProperty
    private String caste;
    @JsonProperty
    private String religion;
    @JsonProperty
    private String numberOfLivingMaleChildren;
    @JsonProperty
    private String numberOfLivingFemaleChildren;
    @JsonProperty
    private String lmpDate;
    @JsonProperty
    private String uptResult;
    @JsonProperty
    private String wifeEducationLevel;
    @JsonProperty
    private String husbandEducationLevel;
    @JsonProperty
    private OCPFPDetailsDTO fpDetails;

    public String getEcNumber() {
        return ecNumber;
    }

    public String getWifeName() {
        return wifeName;
    }

    public String getHusbandName() {
        return husbandName;
    }

    public String getVillage() {
        return village;
    }

    public String getSubCenter() {
        return subCenter;
    }

    public String getWifeAge() {
        return wifeAge;
    }

    public String getCaste() {
        return caste;
    }

    public String getReligion() {
        return religion;
    }

    public String getNumberOfLivingMaleChildren() {
        return numberOfLivingMaleChildren;
    }

    public String getNumberOfLivingFemaleChildren() {
        return numberOfLivingFemaleChildren;
    }

    public String getLmpDate() {
        return lmpDate;
    }

    public String getUptResult() {
        return uptResult;
    }

    public String getWifeEducationLevel() {
        return wifeEducationLevel;
    }

    public String getHusbandEducationLevel() {
        return husbandEducationLevel;
    }

    public OCPFPDetailsDTO getFpDetails() {
        return fpDetails;
    }

    public OCPRegisterEntryDTO withEcNumber(String ecNumber) {
        this.ecNumber = ecNumber;
        return this;
    }

    public OCPRegisterEntryDTO withWifeName(String wifeName) {
        this.wifeName = wifeName;
        return this;
    }

    public OCPRegisterEntryDTO withHusbandName(String husbandName) {
        this.husbandName = husbandName;
        return this;
    }

    public OCPRegisterEntryDTO withVillage(String village) {
        this.village = village;
        return this;
    }

    public OCPRegisterEntryDTO withSubCenter(String subCenter) {
        this.subCenter = subCenter;
        return this;
    }

    public OCPRegisterEntryDTO withWifeAge(String wifeAge) {
        this.wifeAge = wifeAge;
        return this;
    }

    public OCPRegisterEntryDTO withCaste(String caste) {
        this.caste = caste;
        return this;
    }

    public OCPRegisterEntryDTO withReligion(String religion) {
        this.religion = religion;
        return this;
    }

    public OCPRegisterEntryDTO withNumberOfLivingMaleChildren(String numberOfLivingMaleChildren) {
        this.numberOfLivingMaleChildren = numberOfLivingMaleChildren;
        return this;
    }

    public OCPRegisterEntryDTO withNumberOfLivingFemaleChildren(String numberOfLivingFemaleChildren) {
        this.numberOfLivingFemaleChildren = numberOfLivingFemaleChildren;
        return this;
    }

    public OCPRegisterEntryDTO withLmpDate(String lmpDate) {
        this.lmpDate = lmpDate;
        return this;
    }

    public OCPRegisterEntryDTO withUptResult(String uptResult) {
        this.uptResult = uptResult;
        return this;
    }

    public OCPRegisterEntryDTO withWifeEducationLevel(String wifeEducationLevel) {
        this.wifeEducationLevel = wifeEducationLevel;
        return this;
    }

    public OCPRegisterEntryDTO withHusbandEducationLevel(String husbandEducationLevel) {
        this.husbandEducationLevel = husbandEducationLevel;
        return this;
    }

    public OCPRegisterEntryDTO withFpDetails(OCPFPDetailsDTO fpDetails) {
        this.fpDetails = fpDetails;
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
