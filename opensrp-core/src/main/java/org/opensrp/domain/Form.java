package org.opensrp.domain;


import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.List;
@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT)
public class Form {
    private String bind_type;

    private String default_bind_path;

    private List<FormField> fields;

    private List<SubFormDefinition> sub_forms;

   

    public Form() {
    }

    public Form(String bind_type, String default_bind_path, List<FormField> fields, List<SubFormDefinition> sub_forms) {
        this.bind_type = bind_type;
        this.default_bind_path = default_bind_path;
        this.fields = fields;
        this.sub_forms = sub_forms;
    }

    public String getBind_type() {
		return bind_type;
	}

	public void setBind_type(String bind_type) {
		this.bind_type = bind_type;
	}

	public String getDefault_bind_path() {
		return default_bind_path;
	}

	public void setDefault_bind_path(String default_bind_path) {
		this.default_bind_path = default_bind_path;
	}

	public List<FormField> getFields() {
		return fields;
	}

	public void setFields(List<FormField> fields) {
		this.fields = fields;
	}

	public List<SubFormDefinition> getSub_forms() {
		return sub_forms;
	}

	public void setSub_forms(List<SubFormDefinition> sub_forms) {
		this.sub_forms = sub_forms;
	}



}
