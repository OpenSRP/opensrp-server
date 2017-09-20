package org.opensrp.dto.form;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonProperty;

public class FormSubmissionDTO {

	@JsonProperty
	private String anmId;

	@JsonProperty
	private String instanceId;

	@JsonProperty
	private String entityId;

	@JsonProperty
	private String formName;

	@JsonProperty
	private String formInstance;

	@JsonProperty
	private String clientVersion;

	@JsonProperty
	private String serverVersion;

	@JsonProperty
	private String formDataDefinitionVersion;

	private FormSubmissionDTO() {

	}

	public FormSubmissionDTO(String anmId, String instanceId, String entityId, String formName, String formInstance,
	                         String clientVersion, String formDataDefinitionVersion) {
		this.anmId = anmId;
		this.instanceId = instanceId;
		this.entityId = entityId;
		this.formName = formName;
		this.formInstance = formInstance;
		this.clientVersion = clientVersion;
		this.formDataDefinitionVersion = formDataDefinitionVersion;
	}

	public FormSubmissionDTO withServerVersion(String version) {
		this.serverVersion = version;
		return this;
	}

	public String getAnmId() {
		return this.anmId;
	}

	public String getInstanceId() {
		return this.instanceId;
	}

	public String getEntityId() {
		return this.entityId;
	}

	public String getFormName() {
		return this.formName;
	}

	public String getFormInstance() {
		return this.formInstance;
	}

	public String getClientVersion() {
		return this.clientVersion;
	}

	public String getFormDataDefinitionVersion() {
		return this.formDataDefinitionVersion;
	}

	public String getServerVersion() {
		return serverVersion;
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
