package org.opensrp.form.service;

import java.util.List;
import java.util.Map;

public class SubformMap{
    private String name;
    private String entityId;
    private String bindType;
    private String defaultBindPath;
    private Map<String, String> formAttributes;
    private List<FormFieldMap> fields;
	
    public SubformMap(String entityId, String subformName, String bindType, String defaultBindPath, Map<String, String> formAttributes, List<FormFieldMap> fields) {
		this.name = subformName;
		this.entityId = entityId;
		this.bindType = bindType;
		this.defaultBindPath = defaultBindPath;
		this.fields = fields;
		this.formAttributes = formAttributes;
	}

	public String entityId() {
		return entityId;
	}
	public String name() {
		return name;
	}
	public String bindType() {
		return bindType;
	}
	public String defaultBindPath() {
		return defaultBindPath;
	}
	public Map<String, String> formAttributes() {
		return formAttributes;
	}
	public List<FormFieldMap> fields() {
		return fields;
	}
		
	public String getFieldValue(String field) {
		for (FormFieldMap f : fields) {
			if (f.name().equalsIgnoreCase(field)) {
				System.out.println(f.getName()+" : "+ f.value());
				return f.value();
			}
		}
		return null;
	}
	
	public FormFieldMap getField(String field) {
		for (FormFieldMap f : fields) {
			if (f.name().equalsIgnoreCase(field)) {
				return f;
			}
		}
		return null;
	}
}    