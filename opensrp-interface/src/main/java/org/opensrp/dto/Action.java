package org.opensrp.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Map;

public class Action {
    @JsonProperty
    private String caseID;
    @JsonProperty
    private Map<String, String> data;
    @JsonProperty
    private String actionTarget;
    @JsonProperty
    private String actionType;
    @JsonProperty
    private String timeStamp;
    @JsonProperty
    private Boolean isActionActive;
    @JsonProperty
    private Map<String, String> details;

    public Action(String caseID, String actionTarget, String actionType, Map<String, String> data, String timeStamp, Boolean isActionActive, Map<String, String> details) {
        this.caseID = caseID;
        this.data = data;
        this.timeStamp = timeStamp;
        this.actionTarget = actionTarget;
        this.actionType = actionType;
        this.isActionActive = isActionActive;
        this.details = details;
    }

    public Action() {
    }

    public String getCaseID() {
        return caseID;
    }

    public String getActionTarget() {
        return this.actionTarget;
    }

    public String getActionType() {
        return this.actionType;
    }

    public Boolean getIsActionActive() {
        return this.isActionActive;
    }

    public String getTimeStamp() {
        return this.timeStamp;
    }

    public String get(String key) {
        return data.get(key);
    }

    public Map<String, String> getData() {
        return this.data;
    }

    public Map<String, String> getDetails() {
        return this.details;
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
