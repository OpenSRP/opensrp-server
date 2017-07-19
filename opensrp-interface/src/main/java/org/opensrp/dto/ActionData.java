package org.opensrp.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Map;

public class ActionData {
    private Map<String, String> data;
    private String target;
    private String type;
    private Map<String, String> details;

    public static ActionData createAlert(String beneficiaryType, String scheduleName, String visitCode,
                                         AlertStatus alertStatus, DateTime startDate, DateTime expiryDate) {
        return new ActionData("alert", "createAlert")
                .with("beneficiaryType", beneficiaryType)
                .with("scheduleName", scheduleName)
                .with("visitCode", visitCode)
                .with("alertStatus", alertStatus.value())
                .with("startDate", startDate.toLocalDate().toString())
                .with("expiryDate", expiryDate.toLocalDate().toString());
    }

    public static ActionData markAlertAsClosed(String visitCode, String completionDate) {
        return new ActionData("alert", "closeAlert")
                .with("visitCode", visitCode)
                .with("completionDate", completionDate);
    }

    public static ActionData reportForIndicator(String indicator, String annualTarget, String monthSummaries) {
        return new ActionData("report", indicator)
                .with("annualTarget", annualTarget)
                .with("monthlySummaries", monthSummaries);
    }

    public static ActionData closeBeneficiary(String target, String reasonForClose) {
        return new ActionData(target, "close")
                .with("reasonForClose", reasonForClose);
    }

    public static ActionData from(String actionType, String actionTarget, Map<String, String> data, Map<String, String> details) {
        ActionData actionData = new ActionData(actionTarget, actionType);
        actionData.data.putAll(data);
        actionData.details.putAll(details);
        return actionData;
    }

    private ActionData(String target, String type) {
        this.target = target;
        this.type = type;
        data = new HashMap<String, String>();
        details = new HashMap<String, String>();
    }

    private ActionData with(String key, String value) {
        data.put(key, value);
        return this;
    }

    public Map<String, String> getData() {
        return this.data;
    }

    public String getTarget() {
        return target;
    }

    public String getType() {
        return type;
    }

    public Map<String, String> getDetails() {
        return details;
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
