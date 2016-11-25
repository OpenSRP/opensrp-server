package org.opensrp.domain;

import java.awt.Image;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonProperty;
import org.ektorp.support.TypeDiscriminator;
import org.joda.time.DateTime;
import org.motechproject.model.MotechBaseDataObject;

@TypeDiscriminator("doc.type == 'Multimedia'")
public class Multimedia extends BaseDataObject {

	@JsonProperty
	private String fileName;
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
	@JsonProperty
	private String extension;
	@JsonProperty
	private DateTime uploadDate;
	@JsonProperty
	private String description;
	@JsonProperty
	private String fileSize;
	@JsonProperty
	private Map<String,String> comments;
	
	public Multimedia(String caseId) {
		this.caseId=caseId;
	}
	
	public Multimedia() {
	}
	
	public Multimedia(String caseId, String providerId, String contentType, String filePath, String fileCategory)
	{
		this.caseId = caseId;
		this.providerId  = providerId; 
		this.contentType = contentType;
		this.filePath = filePath;
		this.fileCategory = fileCategory;
		
	}
	public
	Multimedia(String name, String caseId, String providerId, String contentType, String filePath, String fileCategory, String extension, 
			DateTime  uploadDate, String description, String fileSize, Image previewImage, Map<String,String> comments) {
		this.caseId = caseId;
		this.providerId  = providerId; 
		this.contentType = contentType;
		this.filePath = filePath;
		this.fileCategory = fileCategory;
		this.uploadDate=uploadDate;
		this.description=description;
		this.fileSize=fileSize;
		this.comments=comments;
		this.extension=extension;
	}
	public String getName() {
		return fileName;
	}

	public void setName(String name) {
		this.fileName = name;
	}
	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}
	public DateTime  getUploadDate() {
		return uploadDate;
	}
	public void setUploadDate(DateTime  uploadDate) {
		this.uploadDate = uploadDate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getFileSize() {
		return fileSize;
	}
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
	public Map<String, String> getComments() {
		return comments;
	}
	public void setComments(Map<String, String> comments) {
		this.comments = comments;
	}
	public Multimedia withBaseEntityId(String caseId) {
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
	
	public Multimedia withDescription(String description) {
		this.description = description;
		return this;
	}
	public Multimedia withFileCategory(String fileCategory) {
		this.fileCategory = fileCategory;
		return this;
	}
	
	public Multimedia withFileName(String name) {
		this.fileName = name;
		return this;
	}
	
	public Multimedia withFileSize(String size) {
		this.fileSize = size;
		return this;
	}
	
	public Multimedia withUploadDate(DateTime  uploadDate) {
		this.uploadDate = uploadDate;
		return this;
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getBaseEntityId() {
		return caseId;
	}

	public void setBaseEntityId(String baseEntityId) {
		this.caseId = baseEntityId;
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
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
