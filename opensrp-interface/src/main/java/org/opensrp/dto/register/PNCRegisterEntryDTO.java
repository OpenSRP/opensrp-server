package org.opensrp.dto.register;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;
import java.util.Map;

public class PNCRegisterEntryDTO {
    @JsonProperty
    private String registrationDate;
    @JsonProperty
    private String thayiCardNumber;
    @JsonProperty
    private String wifeName;
    @JsonProperty
    private String husbandName;
    @JsonProperty
    private String wifeDOB;
    @JsonProperty
    private String address;
    @JsonProperty
    private String dateOfDelivery;
    @JsonProperty
    private String placeOfDelivery;
    @JsonProperty
    private String typeOfDelivery;
    @JsonProperty
    private String dischargeDate;
    @JsonProperty
    private String fpMethodName;
    @JsonProperty
    private String fpMethodDate;
    @JsonProperty
    private String deliveryComplications;
    @JsonProperty
    private List<Map<String, String>> childrenDetails;
    @JsonProperty
    private List<PNCVisitDTO> pncVisits;

    public PNCRegisterEntryDTO withRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
        return this;
    }

    public PNCRegisterEntryDTO withThayiCardNumber(String thayiCardNumber) {
        this.thayiCardNumber = thayiCardNumber;
        return this;
    }

    public PNCRegisterEntryDTO withWifeName(String wifeName) {
        this.wifeName = wifeName;
        return this;
    }

    public PNCRegisterEntryDTO withHusbandName(String husbandName) {
        this.husbandName = husbandName;
        return this;
    }

    public PNCRegisterEntryDTO withWifeDOB(String wifeDOB) {
        this.wifeDOB = wifeDOB;
        return this;
    }

    public PNCRegisterEntryDTO withAddress(String address) {
        this.address = address;
        return this;
    }

    public PNCRegisterEntryDTO withDateOfDelivery(String dateOfDelivery) {
        this.dateOfDelivery = dateOfDelivery;
        return this;
    }

    public PNCRegisterEntryDTO withPlaceOfDelivery(String placeOfDelivery) {
        this.placeOfDelivery = placeOfDelivery;
        return this;
    }

    public PNCRegisterEntryDTO withTypeOfDelivery(String typeOfDelivery) {
        this.typeOfDelivery = typeOfDelivery;
        return this;
    }

    public PNCRegisterEntryDTO withDischargeDate(String dischargeDate) {
        this.dischargeDate = dischargeDate;
        return this;
    }

    public PNCRegisterEntryDTO withFPMethodName(String fpMethodName) {
        this.fpMethodName = fpMethodName;
        return this;
    }

    public PNCRegisterEntryDTO withFPMethodDate(String fpMethodDate) {
        this.fpMethodDate = fpMethodDate;
        return this;
    }

    public PNCRegisterEntryDTO withDeliveryComplications(String deliveryComplications) {
        this.deliveryComplications = deliveryComplications;
        return this;
    }

    public PNCRegisterEntryDTO withChildrenDetails(List<Map<String, String>> childrenDetails) {
        this.childrenDetails = childrenDetails;
        return this;
    }

    public PNCRegisterEntryDTO withPNCVisits(List<PNCVisitDTO> pncVisits) {
        this.pncVisits = pncVisits;
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
