package org.opensrp.scheduler;

import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonProperty;
import org.ektorp.support.TypeDiscriminator;
import org.motechproject.model.MotechBaseDataObject;
import org.motechproject.util.DateUtil;
import org.opensrp.dto.ActionData;

/**
 * The entity which helps in identifying the type of action applicable for the entity or provider
 */
@TypeDiscriminator("doc.type === 'Action'")
public class Action extends MotechBaseDataObject {
    @JsonProperty
    private String providerId;
    @JsonProperty
    private String baseEntityId;
    @JsonProperty
    private Map<String, String> data;
    @JsonProperty
    private String actionTarget;
    @JsonProperty
    private String actionType;
    @JsonProperty
    private Boolean isActionActive;
    @JsonProperty
    private long timeStamp;
    
	public long getTimeStamp() {
		return timeStamp;
	}

	@JsonProperty
    private long version;
    @JsonProperty
    private Map<String, String> details;

    private Action() {
    }

    public Action(String baseEntityId, String providerId, ActionData actionData) {
        this.providerId = providerId;
        this.baseEntityId = baseEntityId;
        this.data = actionData.getData();
        this.actionTarget = actionData.getTarget();
        this.actionType = actionData.getType();
        this.timeStamp = DateUtil.now().getMillis();
        this.details = actionData.getDetails();
        this.isActionActive = true;
    }

    public String providerId() {
        return providerId;
    }

    public String baseEntityId() {
        return baseEntityId;
    }

    public Map<String, String> data() {
        return data;
    }

    public String actionType() {
        return actionType;
    }

    public long timestamp() {
        return timeStamp;
    }
   

    public String target() {
        return actionTarget;
    }

    public Action markAsInActive() {
        this.isActionActive = false;
        return this;
    }

    public Boolean getIsActionActive() {
        return isActionActive;
    }

    public Map<String, String> details() {
        return details;
    }

    private String getBaseEntityId() {
        return baseEntityId;
    }

    public String getActionTarget() {
        return actionTarget;
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o, "timeStamp", "revision");
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, "timeStamp", "revision");
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
