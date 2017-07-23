package org.opensrp.dto.register;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonProperty;
import org.opensrp.dto.LocationDTO;

public class EntityDetailDTO {
    @JsonProperty
    private String entityId;
    @JsonProperty
    private String entityType;
    @JsonProperty
    private String thayiCardNumber;
    @JsonProperty
    private String ecNumber;
    @JsonProperty
    private String anmIdentifier;

    public String getEntityId() {
        return entityId;
    }

    public String getEntityType() {
        return entityType;
    }

    public String getThayiCardNumber() {
        return thayiCardNumber;
    }

    public String getEcNumber() {
        return ecNumber;
    }

    public String getAnmIdentifier() {
        return anmIdentifier;
    }

    public EntityDetailDTO withThayiCardNumber(String thayiCardNumber) {
        this.thayiCardNumber = thayiCardNumber;
        return this;
    }

    public EntityDetailDTO withECNumber(String ecNumber) {
        this.ecNumber = ecNumber;
        return this;
    }

    public EntityDetailDTO withEntityID(String entityId) {
        this.entityId = entityId;
        return this;
    }

    public EntityDetailDTO withANMIdentifier(String anmIdentifier) {
        this.anmIdentifier = anmIdentifier;
        return this;
    }

    public EntityDetailDTO withEntityType(String type) {
        this.entityType = type;
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
