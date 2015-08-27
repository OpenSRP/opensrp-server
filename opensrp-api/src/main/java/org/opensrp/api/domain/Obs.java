package org.opensrp.api.domain;

import org.opensrp.api.domain.form.FormField;
import org.opensrp.api.domain.form.FormSubmission;

/**
 * The observations applicable or noted for any {@link Event} by data entry person. Each obs links back to 
 * variable in {@link FormSubmission} represent by name in {@link FormField}. Each {@link Obs} instead of 
 * recording value with the text of label of question, maps each question with a code that links with a standard 
 * international dictionary. This helps in getting standardized reports and integration with different systems
 */
public class Obs {

	private String fieldDataType;
	private String fieldCode;
	private String parentCode;
	private Object value;
	private String comments;
	private String formSubmissionField;
	
	public Obs() { }

	public Obs(String fieldDataType, String fieldCode, String parentCode,
			Object value, String comments, String formSubmissionField) {
		this.fieldDataType = fieldDataType;
		this.fieldCode = fieldCode;
		this.parentCode = parentCode;
		this.value = value;
		this.comments = comments;
		this.formSubmissionField = formSubmissionField;
	}

	public String getFieldDataType() {
		return fieldDataType;
	}

	public void setFieldDataType(String fieldDataType) {
		this.fieldDataType = fieldDataType;
	}

	public String getFieldCode() {
		return fieldCode;
	}

	public void setFieldCode(String fieldCode) {
		this.fieldCode = fieldCode;
	}

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getFormSubmissionField() {
		return formSubmissionField;
	}

	public void setFormSubmissionField(String formSubmissionField) {
		this.formSubmissionField = formSubmissionField;
	}

	public Obs withFieldDataType(String fieldDataType) {
		this.fieldDataType = fieldDataType;
		return this;
	}

	public Obs withFieldCode(String fieldCode) {
		this.fieldCode = fieldCode;
		return this;
	}

	public Obs withParentCode(String parentCode) {
		this.parentCode = parentCode;
		return this;
	}

	public Obs withValue(Object value) {
		this.value = value;
		return this;
	}

	public Obs withComments(String comments) {
		this.comments = comments;
		return this;
	}

	public Obs withFormSubmissionField(String formSubmissionField) {
		this.formSubmissionField = formSubmissionField;
		return this;
	}	
}
