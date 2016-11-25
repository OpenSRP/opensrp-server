package org.opensrp.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.joda.time.DateTime;

public class Obs {

	@JsonProperty
	private String fieldType;
	@JsonProperty
	private String fieldDataType;
	@JsonProperty
	private String fieldCode;
	@JsonProperty
	private String parentCode;
	@JsonProperty
	private List<Object> values;
	@JsonProperty
	private String comments;
	@JsonProperty
	private String formSubmissionField;
	@JsonProperty
	private DateTime effectiveDatetime;
	
	public Obs() { }

	public Obs(String fieldType, String fieldDataType, String fieldCode, String parentCode,
			List<Object> values, String comments, String formSubmissionField) {
		this.setFieldType(fieldType);
		this.fieldDataType = fieldDataType;
		this.fieldCode = fieldCode;
		this.parentCode = parentCode;
		this.values = values;
		this.comments = comments;
		this.formSubmissionField = formSubmissionField;
	}

	public Obs(String fieldType, String fieldDataType, String fieldCode, String parentCode,
			Object value, String comments, String formSubmissionField) {
		this.setFieldType(fieldType);
		this.fieldDataType = fieldDataType;
		this.fieldCode = fieldCode;
		this.parentCode = parentCode;
		addToValueList(value);
		this.comments = comments;
		this.formSubmissionField = formSubmissionField;
	}
	
	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
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

	@JsonIgnore
	public Object getValue() {
		if(values.size() > 1){
			throw new RuntimeException("Multiset values can not be handled like single valued fields. Use function getValues");
		}
		if(values == null || values.size() == 0){
			return null;
		}
		
		return values.get(0);
	}

	@JsonIgnore
	public void setValue(Object value) {
		addToValueList(value);
	}
	
	public List<Object> getValues() {
		return values;
	}

	public void setValues(List<Object> values) {
		this.values = values;
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

	public DateTime getEffectiveDatetime() {
		return effectiveDatetime;
	}

	public void setEffectiveDatetime(DateTime effectiveDatetime) {
		this.effectiveDatetime = effectiveDatetime;
	}

	public Obs withFieldType(String fieldType) {
		this.fieldType = fieldType;
		return this;
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
		return addToValueList(value);
	}
	
	public Obs withValues(List<Object> values) {
		this.values = values;
		return this;
	}
	
	public Obs addToValueList(Object value) {
		if(values == null){
			values = new ArrayList<>();
		}
		values.add(value);
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
	
	public Obs withEffectiveDatetime(DateTime effectiveDatetime) {
		this.effectiveDatetime = effectiveDatetime;
		return this;
	}
	
	public static void main(String[] args) {
		Obs o1 = new Obs("concept", "str", "454253425", null, Arrays.asList(new String[]{"55"}), null, "WEIGHT");
		Obs o2 = new Obs("concept", "str", "6565645656", null, Arrays.asList(new String[]{"20"}), null, "HEIGHT");
	}
}
