package org.opensrp.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonProperty;
import org.ektorp.support.TypeDiscriminator;
import org.motechproject.model.MotechBaseDataObject;

@TypeDiscriminator("doc.type == 'Multimedia'")
public class Multimedia extends MotechBaseDataObject {

	@JsonProperty
	private String caseId;
	@JsonProperty
	private String providerId;
	@JsonProperty
	private String contentType;
	@JsonProperty
	private String filePath;
	@JsonProperty
	private String fileCategory;
	
	
	public Multimedia() {

	}
	public Multimedia( String caseId, String providerId, String contentType, String filePath, String fileCategory) {
		this.caseId = caseId;
		this.providerId  = providerId; 
		this.contentType = contentType;
		this.filePath = filePath;
		this.fileCategory = fileCategory;
	}

	public Multimedia withCaseId(String caseId) {
		this.caseId = caseId;
		return this;
	}
	public Multimedia withProviderId(String providerId) {
		this.providerId = providerId;
		return this;
	}

	public Multimedia withContentType(String contentType) {
		this.contentType = contentType;
		return this;
	}

	public Multimedia withFilePath(String filePath) {
		this.filePath = filePath;
		return this;
	}

	public Multimedia withFileCategory(String fileCategory) {
		this.fileCategory = fileCategory;
		return this;
	}
	
	public String getCaseId() {
		return caseId;
	}
	public String getProviderId() {
		return providerId;
	}
	public String getContentType() {
		return contentType;
	}
	public String getFilePath() {
		return filePath;
	}
	public String getFileCategory() {
		return fileCategory;
	}

	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}
	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public void setFileCategory(String fileCategory) {
		this.fileCategory = fileCategory;
	}

	@Override
	public final boolean equals(Object o) {
		return EqualsBuilder.reflectionEquals(this, o, "id");
	}

	@Override
	public final int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this, "id");
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
