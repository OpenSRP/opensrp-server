package org.opensrp.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Map;

public class ActionData {
    public static final String BENEFICIARY_TYPE = "beneficiaryType";
    public static final String SCHEDULE_NAME = "scheduleName";
    public static final String VISIT_CODE = "visitCode";
    public static final String ALERT_STATUS = "alertStatus";
    public static final String START_DATE = "startDate";
    public static final String EXPIRY_DATE = "expiryDate";
    public static final String TARGET = "alert";
    public static final String TYPE = "createAlert";
    public static final String COMPLETION_DATE = "completionDate";
    public static final String ANNUAL_TARGET = "annualTarget";
    public static final String MONTHLY_SUMMARIES = "monthlySummaries";
    public static final String REASON_FOR_CLOSE = "reasonForClose";
    private Map<String, String> data;
    private String target;
    private String type;
    private Map<String, String> details;

    public static ActionData createAlert(String beneficiaryType, String scheduleName, String visitCode,
                                         AlertStatus alertStatus, DateTime startDate, DateTime expiryDate) {
        return new ActionData(TARGET, TYPE)
                .with(BENEFICIARY_TYPE, beneficiaryType)
                .with(SCHEDULE_NAME, scheduleName)
                .with(VISIT_CODE, visitCode)
                .with(ALERT_STATUS, alertStatus.value())
                .with(START_DATE, startDate.toLocalDate().toString())
                .with(EXPIRY_DATE, expiryDate.toLocalDate().toString());
    }

    public static ActionData markAlertAsClosed(String visitCode, String completionDate) {
        return new ActionData(TARGET, "closeAlert")
                .with(VISIT_CODE, visitCode)
                .with(COMPLETION_DATE, completionDate);
    }

    public static ActionData reportForIndicator(String indicator, String annualTarget, String monthSummaries) {
        return new ActionData("report", indicator)
                .with(ANNUAL_TARGET, annualTarget)
                .with(MONTHLY_SUMMARIES, monthSummaries);
    }

    public static ActionData closeBeneficiary(String target, String reasonForClose) {
        return new ActionData(target, "close")
                .with(REASON_FOR_CLOSE, reasonForClose);
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