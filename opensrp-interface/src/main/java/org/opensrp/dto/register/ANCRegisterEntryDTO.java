package org.opensrp.dto.register;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;
import java.util.Map;

public class ANCRegisterEntryDTO {
    @JsonProperty
    private String ancNumber;
    @JsonProperty
    private String registrationDate;
    @JsonProperty
    private String ecNumber;
    @JsonProperty
    private String thayiCardNumber;
    @JsonProperty
    private String aadharCardNumber;
    @JsonProperty
    private String wifeName;
    @JsonProperty
    private String husbandName;
    @JsonProperty
    private String address;
    @JsonProperty
    private String wifeDOB;
    @JsonProperty
    private String phoneNumber;
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
    private String bplCardNumber;
    @JsonProperty
    private String jsyBeneficiary;
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
    private String youngestChildDOB;
    @JsonProperty
    private String lmp;
    @JsonProperty
    private String edd;
    @JsonProperty
    private String height;
    @JsonProperty
    private String bloodGroup;
    @JsonProperty
    private String isHRP;
    @JsonProperty
    private List<Map<String, String>> ancVisits;
    @JsonProperty
    private List<Map<String, String>> ifaTablets;
    @JsonProperty
    private List<Map<String, String>> ttDoses;
    @JsonProperty
    private List<Map<String, String>> hbTests;
    @JsonProperty
    private List<Map<String, String>> ancInvestigations;

    public ANCRegisterEntryDTO() {
    }

    public ANCRegisterEntryDTO withANCNumber(String ancNumber) {
        this.ancNumber = ancNumber;
        return this;
    }

    public ANCRegisterEntryDTO withRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
        return this;
    }

    public ANCRegisterEntryDTO withECNumber(String ecNumber) {
        this.ecNumber = ecNumber;
        return this;
    }


    public ANCRegisterEntryDTO withThayiCardNumber(String thayiCardNumber) {
        this.thayiCardNumber = thayiCardNumber;
        return this;
    }


    public ANCRegisterEntryDTO withAadharCardNumber(String aadharCardNumber) {
        this.aadharCardNumber = aadharCardNumber;
        return this;
    }


    public ANCRegisterEntryDTO withWifeName(String wifeName) {
        this.wifeName = wifeName;
        return this;
    }

    public ANCRegisterEntryDTO withHusbandName(String husbandName) {
        this.husbandName = husbandName;
        return this;
    }

    public ANCRegisterEntryDTO withAddress(String address) {
        this.address = address;
        return this;
    }

    public ANCRegisterEntryDTO withWifeDOB(String wifeDOB) {
        this.wifeDOB = wifeDOB;
        return this;
    }

    public ANCRegisterEntryDTO withPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public ANCRegisterEntryDTO withWifeEducationLevel(String wifeEducationLevel) {
        this.wifeEducationLevel = wifeEducationLevel;
        return this;
    }


    public ANCRegisterEntryDTO withHusbandEducationLevel(String husbandEducationLevel) {
        this.husbandEducationLevel = husbandEducationLevel;
        return this;
    }

    public ANCRegisterEntryDTO withCaste(String caste) {
        this.caste = caste;
        return this;
    }

    public ANCRegisterEntryDTO withReligion(String religion) {
        this.religion = religion;
        return this;
    }

    public ANCRegisterEntryDTO withEconomicStatus(String economicStatus) {
        this.economicStatus = economicStatus;
        return this;
    }

    public ANCRegisterEntryDTO withBPLCardNumber(String bplCardNumber) {
        this.bplCardNumber = bplCardNumber;
        return this;
    }

    public ANCRegisterEntryDTO withJSYBeneficiary(String jsyBeneficiary) {
        this.jsyBeneficiary = jsyBeneficiary;
        return this;
    }

    public ANCRegisterEntryDTO withGravida(String gravida) {
        this.gravida = gravida;
        return this;
    }

    public ANCRegisterEntryDTO withParity(String parity) {
        this.parity = parity;
        return this;
    }

    public ANCRegisterEntryDTO withNumberOfLivingChildren(String numberOfLivingChildren) {
        this.numberOfLivingChildren = numberOfLivingChildren;
        return this;
    }

    public ANCRegisterEntryDTO withNumberOfStillBirths(String numberOfStillBirths) {
        this.numberOfStillBirths = numberOfStillBirths;
        return this;
    }

    public ANCRegisterEntryDTO withNumberOfAbortions(String numberOfAbortions) {
        this.numberOfAbortions = numberOfAbortions;
        return this;
    }

    public ANCRegisterEntryDTO withYoungestChildDOB(String youngestChildAge) {
        this.youngestChildDOB = youngestChildAge;
        return this;
    }

    public ANCRegisterEntryDTO withLMP(String lmp) {
        this.lmp = lmp;
        return this;
    }

    public ANCRegisterEntryDTO withEDD(String edd) {
        this.edd = edd;
        return this;
    }

    public ANCRegisterEntryDTO withHeight(String height) {
        this.height = height;
        return this;
    }

    public ANCRegisterEntryDTO withBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
        return this;
    }

    public ANCRegisterEntryDTO withIsHRP(String isHRP) {
        this.isHRP = isHRP;
        return this;
    }

    public ANCRegisterEntryDTO withANCVisits(List<Map<String, String>> ancVisits) {
        this.ancVisits = ancVisits;
        return this;
    }

    public ANCRegisterEntryDTO withIFATablets(List<Map<String, String>> ifaTablets) {
        this.ifaTablets = ifaTablets;
        return this;
    }

    public ANCRegisterEntryDTO withTTDoses(List<Map<String, String>> ttDoses) {
        this.ttDoses = ttDoses;
        return this;
    }

    public ANCRegisterEntryDTO withHBTests(List<Map<String, String>> hbTests) {
        this.hbTests = hbTests;
        return this;
    }

    public ANCRegisterEntryDTO withANCInvestigations(List<Map<String, String>> ancInvestigations) {
        this.ancInvestigations = ancInvestigations;
        return this;
    }

    public String getAncNumber() {
        return ancNumber;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public String getEcNumber() {
        return ecNumber;
    }

    public String getThayiCardNumber() {
        return thayiCardNumber;
    }

    public String getAadharCardNumber() {
        return aadharCardNumber;
    }

    public String getWifeName() {
        return wifeName;
    }

    public String getHusbandName() {
        return husbandName;
    }

    public String getAddress() {
        return address;
    }

    public String getWifeDOB() {
        return wifeDOB;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getWifeEducationLevel() {
        return wifeEducationLevel;
    }

    public String getHusbandEducationLevel() {
        return husbandEducationLevel;
    }

    public String getCaste() {
        return caste;
    }

    public String getReligion() {
        return religion;
    }

    public String getEconomicStatus() {
        return economicStatus;
    }

    public String getBplCardNumber() {
        return bplCardNumber;
    }

    public String getJsyBeneficiary() {
        return jsyBeneficiary;
    }

    public String getGravida() {
        return gravida;
    }

    public String getParity() {
        return parity;
    }

    public String getNumberOfLivingChildren() {
        return numberOfLivingChildren;
    }

    public String getNumberOfStillBirths() {
        return numberOfStillBirths;
    }

    public String getNumberOfAbortions() {
        return numberOfAbortions;
    }

    public String getYoungestChildDOB() {
        return youngestChildDOB;
    }

    public String getLmp() {
        return lmp;
    }

    public String getEdd() {
        return edd;
    }

    public String getHeight() {
        return height;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public String getIsHRP() {
        return isHRP;
    }

    public List<Map<String, String>> getAncVisits() {
        return ancVisits;
    }

    public List<Map<String, String>> getIfaTablets() {
        return ifaTablets;
    }

    public List<Map<String, String>> getTtDoses() {
        return ttDoses;
    }

    public List<Map<String, String>> getHbTests() {
        return hbTests;
    }

    public List<Map<String, String>> getAncInvestigations() {
        return ancInvestigations;
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
