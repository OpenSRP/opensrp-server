package org.opensrp.form.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonProperty;
import org.ektorp.support.TypeDiscriminator;
import org.motechproject.model.MotechBaseDataObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@TypeDiscriminator("doc.type === 'FormSubmission'")
public class FormSubmission extends MotechBaseDataObject {
    @JsonProperty
    private String anmId;
    @JsonProperty
    private String instanceId;
    @JsonProperty
    private String formName;
    @JsonProperty
    private String entityId;
    @JsonProperty
    private long clientVersion;
    @JsonProperty
    private String formDataDefinitionVersion;
    @JsonProperty
    private FormInstance formInstance;
    @JsonProperty
    private long serverVersion;
    @JsonProperty
    private Map<String, Object> metadata;

    public FormSubmission() {
    }

    public FormSubmission(String anmId, String instanceId, String formName, String entityId, long clientVersion, String formDataDefinitionVersion, FormInstance formInstance, long serverVersion) {
        this.instanceId = instanceId;
        this.formName = formName;
        this.anmId = anmId;
        this.clientVersion = clientVersion;
        this.entityId = entityId;
        this.formInstance = formInstance;
        this.serverVersion = serverVersion;
        this.formDataDefinitionVersion = formDataDefinitionVersion;
    }

    public FormSubmission(String anmId, String instanceId, String formName, String entityId, String formDataDefinitionVersion, long clientVersion, FormInstance formInstance) {
        this(anmId, instanceId, formName, entityId, clientVersion, formDataDefinitionVersion, formInstance, 0L);
    }

    public String anmId() {
        return this.anmId;
    }

    public String instanceId() {
        return this.instanceId;
    }

    public String entityId() {
        return this.entityId;
    }

    public String formName() {
        return this.formName;
    }

    public String bindType() {
		return formInstance.bindType();
	}
    
    public String defaultBindPath() {
		return formInstance.defaultBindPath();
	}
    
    public FormInstance instance() {
        return formInstance;
    }

    public long clientVersion() {
        return this.clientVersion;
    }

    public String  formDataDefinitionVersion() {
        return this.formDataDefinitionVersion;
    }

    public long serverVersion() {
        return serverVersion;
    }

    public void setServerVersion(long serverVersion) {
        this.serverVersion = serverVersion;
    }

    public String getField(String name) {
        return formInstance.getField(name);
    }

    public Map<String, String> getFields(List<String> fieldNames) {
        Map<String, String> fieldsMap = new HashMap<>();
        for (String fieldName : fieldNames) {
            fieldsMap.put(fieldName, getField(fieldName));
        }
        return fieldsMap;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public SubFormData getSubFormByName(String name) {
        return formInstance.getSubFormByName(name);
    }
    
    public List<SubFormData> subForms() {
        return formInstance.subForms();
    }

    public Map<String, Object> getMetadata() {
		return metadata;
	}

    public Object getMetadata(String key) {
    	if(metadata == null){
    		return null;
    	}
		return metadata.get(key);
	}
    
    void addMetadata(String key, Object value) {
    	if(metadata == null){
    		metadata = new HashMap<>();
    	}
		metadata.put(key, value);
	}
    
	public void setMetadata(Map<String, Object> metadata) {
		this.metadata = metadata;
	}
	
    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(o, this, "id", "revision");
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, "id", "revision");
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
