package org.mql.java.models;

import java.util.List;

public class ClassEntity {
	private String name;
	private String type;
	private List<FieldEntity> fields;
	private List<MethodEntity> methods;
	private List<String> annotations;

	public ClassEntity(String name, String type) {
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<FieldEntity> getFields() {
		return fields;
	}

	public void setFields(List<FieldEntity> fields) {
		this.fields = fields;
	}

	public List<MethodEntity> getMethods() {
		return methods;
	}

	public void setMethods(List<MethodEntity> methods) {
		this.methods = methods;
	}

	public List<String> getAnnotations() {
		return annotations;
	}

	public void setAnnotations(List<String> annotations) {
		this.annotations = annotations;
	}
}
