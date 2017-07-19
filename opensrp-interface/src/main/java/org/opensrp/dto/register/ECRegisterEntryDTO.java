package org.opensrp.dto.register;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonProperty;

public class ECRegisterEntryDTO {
    @JsonProperty
    private String registrationDate;
    @JsonProperty
    private String ecNumber;
    @JsonProperty
    private String wifeName;
    @JsonProperty
    private String husbandName;
    @JsonProperty
    private String householdAddress;
    @JsonProperty
    private String householdNumber;
    @JsonProperty
    private String headOfHousehold;
    @JsonProperty
    private String village;
    @JsonProperty
    private String subCenter;
    @JsonProperty
    private String phc;
    @JsonProperty
    private String wifeAge;
    @JsonProperty
    private String husbandAge;
    @JsonProperty
    private String wifeEducationLevel;
    @JsonProperty
    private String husbandEducationLevel;
    @JsonProperty
    private String caste;
    @JsonProperty
    private String religion;
    @JsonProperty
    private String economicStatus;
    @JsonProperty
    private String gravida;
    @JsonProperty
    private String parity;
    @JsonProperty
    private String numberOfLivingChildren;
    @JsonProperty
    private String numberOfStillBirths;
    @JsonProperty
    private String numberOfAbortions;
    @JsonProperty
    private String numberOfLivingMaleChildren;
    @JsonProperty
    private String numberOfLivingFemaleChildren;
    @JsonProperty
    private String youngestChildAge;
    @JsonProperty
    private String currentFPMethod;
    @JsonProperty
    private String currentFPMethodStartDate;
    @JsonProperty
    private String isPregnant;

    public ECRegisterEntryDTO withRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
        return this;
    }

    public ECRegisterEntryDTO withECNumber(String ecNumber) {
        this.ecNumber = ecNumber;
        return this;
    }

    public ECRegisterEntryDTO withWifeName(String wifeName) {
        this.wifeName = wifeName;
        return this;
    }

    public ECRegisterEntryDTO withHusbandName(String husbandName) {
        this.husbandName = husbandName;
        return this;
    }

    public ECRegisterEntryDTO withWifeAge(String wifeAge) {
        this.wifeAge = wifeAge;
        return this;
    }

    public ECRegisterEntryDTO withHusbandAge(String husbandAge) {
        this.husbandAge = husbandAge;
        return this;
    }

    public ECRegisterEntryDTO withHouseholdAddress(String householdAddress) {
        this.householdAddress = householdAddress;
        return this;
    }

    public ECRegisterEntryDTO withWifeEducationLevel(String wifeEducationLevel) {
        this.wifeEducationLevel = wifeEducationLevel;
        return this;
    }

    public ECRegisterEntryDTO withHouseholdNumber(String householdNumber) {
        this.householdNumber = householdNumber;
        return this;
    }

    public ECRegisterEntryDTO withHeadOfHousehold(String headOfHousehold) {
        this.headOfHousehold = headOfHousehold;
        return this;
    }

    public ECRegisterEntryDTO withSubCenter(String subCenter) {
        this.subCenter = subCenter;
        return this;
    }

    public ECRegisterEntryDTO withPHC(String phc) {
        this.phc = phc;
        return this;
    }

    public ECRegisterEntryDTO withVillage(String village) {
        this.village = village;
        return this;
    }

    public ECRegisterEntryDTO withHusbandEducationLevel(String husbandEducationLevel) {
        this.husbandEducationLevel = husbandEducationLevel;
        return this;
    }

    public ECRegisterEntryDTO withCaste(String caste) {
        this.caste = caste;
        return this;
    }

    public ECRegisterEntryDTO withReligion(String religion) {
        this.religion = religion;
        return this;
    }

    public ECRegisterEntryDTO withEconomicStatus(String economicStatus) {
        this.economicStatus = economicStatus;
        return this;
    }

    public ECRegisterEntryDTO withGravida(String gravida) {
        this.gravida = gravida;
        return this;
    }

    public ECRegisterEntryDTO withParity(String parity) {
        this.parity = parity;
        return this;
    }

    public ECRegisterEntryDTO withNumberOfLivingChildren(String numberOfLivingChildren) {
        this.numberOfLivingChildren = numberOfLivingChildren;
        return this;
    }

    public ECRegisterEntryDTO withNumberOfStillBirths(String numberOfStillBirths) {
        this.numberOfStillBirths = numberOfStillBirths;
        return this;
    }

    public ECRegisterEntryDTO withNumberOfAbortions(String numberOfAbortions) {
        this.numberOfAbortions = numberOfAbortions;
        return this;
    }

    public ECRegisterEntryDTO withNumberOfLivingMaleChildren(String numberOfLivingMaleChildren) {
        this.numberOfLivingMaleChildren = numberOfLivingMaleChildren;
        return this;
    }

    public ECRegisterEntryDTO withNumberOfLivingFemaleChildren(String numberOfLivingFemaleChildren) {
        this.numberOfLivingFemaleChildren = numberOfLivingFemaleChildren;
        return this;
    }

    public ECRegisterEntryDTO withYoungestChildAge(String youngestChildAge) {
        this.youngestChildAge = youngestChildAge;
        return this;
    }

    public ECRegisterEntryDTO withCurrentFPMethod(String fpMethod) {
        this.currentFPMethod = fpMethod;
        return this;
    }

    public ECRegisterEntryDTO withCurrentFPMethodStartDate(String startDate) {
        this.currentFPMethodStartDate = startDate;
        return this;
    }

    public ECRegisterEntryDTO withPregnancyStatus(String isPregnant) {
        this.isPregnant = isPregnant;
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
