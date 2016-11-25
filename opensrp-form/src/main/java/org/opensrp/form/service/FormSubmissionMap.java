package org.opensrp.form.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.opensrp.form.domain.FormSubmission;

public class FormSubmissionMap {
	public String providerId() {
		return fs.anmId();
	}

	public String instanceId() {
		return fs.instanceId();
	}

	public String formName() {
		return fs.formName();
	}

	public String entityId() {
		return fs.entityId();
	};

	public long clientTimestamp() {
		return fs.clientVersion();
	};

	public String formVersion() {
		return fs.formDataDefinitionVersion();
	}

	public long serverTimestamp() {
		return fs.serverVersion();
	}

	public String bindType() {
		return fs.bindType();
	}

	public String bindPath() {
		return fs.defaultBindPath();
	}
	
    public Map<String, String> formAttributes() {
		return formAttributes;
	}

    public List<FormFieldMap> fields() {
		return fields;
	}
    
    public List<SubformMap> subforms() {
		return subforms;
	}
    
    public String getFieldValue(String field) {
    	
		for (FormFieldMap f : fields) {
			if(f.name().equalsIgnoreCase(field)){
				return f.value();
			}
		}
		return null;
	}
    public FormFieldMap getField(String field) {
		for (FormFieldMap f : fields) {
			if(f.name().equalsIgnoreCase(field)){
				return f;
			}
		}
		return null;
	}
    
    public SubformMap getSubform(String entityId, String subformName) {
		for (SubformMap sf : subforms) {
			if(sf.name().equalsIgnoreCase(subformName) && sf.entityId().equalsIgnoreCase(entityId)){
				return sf;
			}
		}
		return null;
	}
    
    // Fields below should not be editable by external modules
	void setFormAttributes(Map<String, String> formAttributes) {
		this.formAttributes = formAttributes;
	}
	void setFields(List<FormFieldMap> fields) {
		this.fields = fields;
	}
	void setSubforms(List<SubformMap> subforms) {
		this.subforms = subforms;
	}

	private Map<String, String> formAttributes;
	private List<FormFieldMap> fields = new ArrayList<>();
	private List<SubformMap> subforms = new ArrayList<SubformMap>();
	private FormSubmission fs;
    
    public FormSubmissionMap(FormSubmission fs, Map<String, String> formAttributes, 
    		List<FormFieldMap> fields, List<SubformMap> subforms) {
		this.fs = fs;
		this.formAttributes = formAttributes;
		this.fields = fields;
		this.subforms = subforms;
	}

    
}