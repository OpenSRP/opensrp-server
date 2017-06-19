package org.opensrp.common.audit;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import java.util.Map;

public final class AuditMessage implements Comparable<AuditMessage> {
    private final DateTime time;
    private final long messageIndex;
    private final AuditMessageType type;
    private final Map<String, String> extraData;

    public AuditMessage(DateTime time, long messageIndex, AuditMessageType type, Map<String, String> data) {
        this.time = time;
        this.messageIndex = messageIndex;
        this.type = type;
        this.extraData = data;
    }

    public AuditMessageType type() {
        return type;
    }

    public long index() {
        return messageIndex;
    }

    public Map<String, String> data() {
        return extraData;
    }

    public DateTime time() {
        return time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AuditMessage that = (AuditMessage) o;

        return messageIndex == that.messageIndex;
    }

    @Override
    public int hashCode() {
        return (int) (messageIndex ^ (messageIndex >>> 32));
    }

    @Override
    public int compareTo(AuditMessage other) {
        return Long.valueOf(messageIndex - other.messageIndex).intValue();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
