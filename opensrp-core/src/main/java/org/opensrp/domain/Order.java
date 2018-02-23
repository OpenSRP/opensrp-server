package org.opensrp.domain;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.ektorp.support.TypeDiscriminator;

@JsonIgnoreProperties(ignoreUnknown = true)
@TypeDiscriminator("doc.type == 'Order'")
public class Order extends BaseDataObject {

    @JsonProperty
    private String _rev;

    @JsonProperty
    private String _id;

    @JsonProperty
    private Long identifier;

    @JsonProperty
    private String locationId;

    @JsonProperty
    private String providerId;

    @JsonProperty
    private long dateCreatedByClient;

    public Order() {
        this._rev = String.valueOf(System.currentTimeMillis());
    }

    public long getDateCreatedByClient() {
        return dateCreatedByClient;
    }

    public void setDateCreatedByClient(long dateCreatedByClient) {
        this.dateCreatedByClient = dateCreatedByClient;
    }

    public Long getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Long identifier) {
        this.identifier = identifier;
    }

    public String get_rev() {
        return _rev;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }
}
