package org.opensrp.domain;

import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonProperty;
import org.ektorp.support.TypeDiscriminator;
import org.joda.time.DateTime;
import org.motechproject.model.MotechBaseDataObject;

/**
 * @author muhammad.ahmed@ihsinformatics.com Created on May 25, 2015
 */
@TypeDiscriminator("doc.type == 'ErrorTrace'")
public class ErrorTrace extends MotechBaseDataObject {

	/*
	 * @JsonProperty private String id;
	 */
	@JsonProperty
	private DateTime dateOccurred;
	@JsonProperty
	private String errorType;
	@JsonProperty
	private String occurredAt;
	@JsonProperty
	private String stackTrace;
	@JsonProperty
	private String status; // solved , unsolved , closed ,failed
							// acknowledged,
	@JsonProperty
	private String recordId;

	@JsonProperty
	private Date dateClosed;
	
	@JsonProperty
	private String documentType;
	@JsonProperty
	private String retryUrl;

	// dateoccured , dateclosed , errortype =name, documenttype , submiturl
	
	public ErrorTrace() {
		// TODO Auto-generated constructor stub
	}

	/** 
	 * 
	 * 
	 */
	public ErrorTrace(DateTime dateOccurred, String errorType, String occuredAt,
			String stackTrace, String status, String documentType) {
		this.dateOccurred = dateOccurred;
		// this.id=id;
		this.documentType = documentType;
		this.errorType = errorType;
		this.occurredAt = occuredAt;
		this.stackTrace = stackTrace;
		this.status = status;

	}

	public ErrorTrace(String recordId, DateTime date, String name,
			String occuredAt, String stackTrace, String status) {
		this.dateOccurred = date;
		this.recordId = recordId;
		this.errorType = name;
		this.occurredAt = occuredAt;
		this.stackTrace = stackTrace;
		this.status = status;

	}

	public DateTime getDateOccurred() {
		return dateOccurred;
	}

	public void setDateOccurred(DateTime dateOccurred) {
		this.dateOccurred = dateOccurred;
	}

	public String getErrorType() {
		return errorType;
	}

	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}

	public Date getDateClosed() {
		return dateClosed;
	}

	public void setDateClosed(Date dateClosed) {
		this.dateClosed = dateClosed;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getRetryUrl() {
		return retryUrl;
	}

	public void setRetryUrl(String retryUrl) {
		this.retryUrl = retryUrl;
	}


	public String getRecordId() {

		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	
	public DateTime getDate() {
		return dateOccurred;
	}

	public void setDate(DateTime date) {
		this.dateOccurred = date;
	}

	

	public String getOccurredAt() {
		return occurredAt;
	}

	public void setOccurredAt(String occurredAt) {
		this.occurredAt = occurredAt;
	}

	public String getStackTrace() {
		return stackTrace;
	}

	public void setStackTrace(String stackTrace) {
		this.stackTrace = stackTrace;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
