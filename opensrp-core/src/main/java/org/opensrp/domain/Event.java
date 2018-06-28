package org.opensrp.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.ektorp.support.TypeDiscriminator;
import org.joda.time.DateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
@TypeDiscriminator("doc.type == 'Event'")
public class Event extends BaseDataObject {

    @JsonProperty
    private Map<String, String> identifiers;

    @JsonProperty
    private String baseEntityId;

    @JsonProperty
    private String locationId;

    @JsonProperty
    private String childLocationId;

    @JsonProperty
    private DateTime eventDate;

    @JsonProperty
    private String eventType;

    @JsonProperty
    private String formSubmissionId;

    @JsonProperty
    private String providerId;

    @JsonProperty
    private String status;

    @JsonProperty
    private Map<String, DateTime> statusHistory;

    @JsonProperty
    private String priority;

    @JsonProperty
    private List<String> episodeOfCare;

    @JsonProperty
    private List<String> referrals;

    @JsonProperty
    private String category;

    @JsonProperty
    private int duration;

    @JsonProperty
    private String reason;

    @JsonProperty
    private List<Obs> obs;

    @JsonProperty
    private String entityType;

    @JsonProperty
    private Map<String, String> details;

    @JsonProperty
    private long version;

    @JsonProperty
    private List<Photo> photos;

    @JsonProperty
    private String teamId;

    @JsonProperty
    private String team;

    public Event() {
        this.version = System.currentTimeMillis();
    }

    public Event(String baseEntityId, String eventType, DateTime eventDate, String entityType, String providerId,
                 String locationId, String formSubmissionId) {
        this.baseEntityId = baseEntityId;
        this.eventType = eventType;
        this.eventDate = eventDate;
        this.entityType = entityType;
        this.providerId = providerId;
        this.locationId = locationId;
        this.formSubmissionId = formSubmissionId;
        this.version = System.currentTimeMillis();
    }

    public Event(String baseEntityId, String eventType, DateTime eventDate, String entityType, String providerId,
                 String locationId, String formSubmissionId, String teamId, String team) {
        this(baseEntityId, eventType, eventDate, entityType, providerId, locationId, formSubmissionId);
        setTeamId(teamId);
        setTeam(team);
    }

    public Event(String baseEntityId, String eventType, DateTime eventDate, String entityType, String providerId,
                 String locationId, String childLocationId, String formSubmissionId, String teamId, String team, Integer clientApplicationVersion, Integer clientDatabaseVersion) {
        this(baseEntityId, eventType, eventDate, entityType, providerId, locationId, formSubmissionId,teamId,team);
        setClientApplicationVersion(clientApplicationVersion);
        setClientDatabaseVersion(clientDatabaseVersion);
        setChildLocationId(childLocationId);
    }

    public List<Obs> getObs() {
        if (obs == null) {
            obs = new ArrayList<>();
        }
        return obs;
    }

    public Obs getObs(String parent, String concept) {
        if (obs == null) {
            obs = new ArrayList<>();
        }
        for (Obs o : obs) {
            // parent is blank OR matches with obs parent
            if ((StringUtils.isBlank(parent)
                    || (StringUtils.isNotBlank(o.getParentCode()) && parent.equalsIgnoreCase(o.getParentCode())))
                    && o.getFieldCode().equalsIgnoreCase(concept)) {
                return o; //TODO handle duplicates
            }
        }
        return null;
    }

    /**
     * WARNING: Overrides all existing obs
     *
     * @param obs
     * @return
     */
    public void setObs(List<Obs> obs) {
        this.obs = obs;
    }

    public void addObs(Obs observation) {
        if (obs == null) {
            obs = new ArrayList<>();
        }

        obs.add(observation);
    }

    public String getBaseEntityId() {
        return baseEntityId;
    }

    public void setBaseEntityId(String baseEntityId) {
        this.baseEntityId = baseEntityId;
    }

    public Map<String, String> getIdentifiers() {
        if (identifiers == null) {
            identifiers = new HashMap<>();
        }
        return identifiers;
    }

    public String getIdentifier(String identifierType) {
        if (identifiers == null) {
            return null;
        }
        for (String k : identifiers.keySet()) {
            if (k.equalsIgnoreCase(identifierType)) {
                return identifiers.get(k);
            }
        }
        return null;
    }

    /**
     * Returns field matching the regex. Note that incase of multiple fields matching criteria
     * function would return first match. The must be well formed to find out a single value
     *
     * @param regex
     * @return
     */
    public String getIdentifierMatchingRegex(String regex) {
        for (Entry<String, String> a : getIdentifiers().entrySet()) {
            if (a.getKey().matches(regex)) {
                return a.getValue();
            }
        }
        return null;
    }

    public void setIdentifiers(Map<String, String> identifiers) {
        this.identifiers = identifiers;
    }

    public void addIdentifier(String identifierType, String identifier) {
        if (identifiers == null) {
            identifiers = new HashMap<>();
        }

        identifiers.put(identifierType, identifier);
    }

    public void removeIdentifier(String identifierType) {
        identifiers.remove(identifierType);
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getChildLocationId() {
        return childLocationId;
    }

    public void setChildLocationId(String childLocationId) {
        this.childLocationId = childLocationId;
    }

    public DateTime getEventDate() {
        return eventDate;
    }

    public void setEventDate(DateTime eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getFormSubmissionId() {
        return formSubmissionId;
    }

    public void setFormSubmissionId(String formSubmissionId) {
        this.formSubmissionId = formSubmissionId;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Map<String, String> getDetails() {
        return details;
    }

    public void setDetails(Map<String, String> details) {
        this.details = details;
    }



    public void addDetails(String key, String val) {
        if (details == null) {
            details = new HashMap<>();
        }
        details.put(key, val);
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public Event withBaseEntityId(String baseEntityId) {
        this.baseEntityId = baseEntityId;
        return this;
    }

    /**
     * WARNING: Overrides all existing identifiers
     *
     * @param identifiers
     * @return
     */
    public Event withIdentifiers(Map<String, String> identifiers) {
        this.identifiers = identifiers;
        return this;
    }

    public Event withIdentifier(String identifierType, String identifier) {
        if (identifiers == null) {
            identifiers = new HashMap<>();
        }
        identifiers.put(identifierType, identifier);
        return this;
    }

    public Event withLocationId(String locationId) {
        this.locationId = locationId;
        return this;
    }

    public Event withChildLocationId(String childLocationId) {
        this.childLocationId = childLocationId;
        return this;
    }

    public Event withEventDate(DateTime eventDate) {
        this.eventDate = eventDate;
        return this;
    }

    public Event withEventType(String eventType) {
        this.eventType = eventType;
        return this;
    }

    public Event withFormSubmissionId(String formSubmissionId) {
        this.formSubmissionId = formSubmissionId;
        return this;
    }

    public Event withProviderId(String providerId) {
        this.providerId = providerId;
        return this;
    }

    public Event withEntityType(String entityType) {
        this.entityType = entityType;
        return this;
    }

    /**
     * WARNING: Overrides all existing obs
     *
     * @param obs
     * @return
     */
    public Event withObs(List<Obs> obs) {
        this.obs = obs;
        return this;
    }

    public Event withObs(Obs observation) {
        if (obs == null) {
            obs = new ArrayList<>();
        }
        obs.add(observation);
        return this;
    }

    @Override
    public final boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o, "id", "revision");
    }

    @Override
    public final int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, "id", "revision");
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}