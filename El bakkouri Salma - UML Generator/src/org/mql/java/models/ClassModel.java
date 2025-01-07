package org.mql.java.models;

import java.util.List;

public class ClassModel {
	private String name;
	private String type;
	private List<String> modifiers;
	private List<FieldModel> fields;
	private List<MethodModel> methods;

	public ClassModel(String name, String type) {
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

	public List<String> getModifiers() {
		return modifiers;
	}

	public void setModifiers(List<String> modifiers) {
		this.modifiers = modifiers;
	}

	public List<FieldModel> getFields() {
		return fields;
	}

	public void setFields(List<FieldModel> fields) {
		this.fields = fields;
	}

	public List<MethodModel> getMethods() {
		return methods;
	}

	public void setMethods(List<MethodModel> methods) {
		this.methods = methods;
	}

	@Override
	public String toString() {
		return "ClassModel{" + "name='" + name + '\'' + ", type='" + type + '\'' + ", modifiers=" + modifiers
				+ ", fields=" + fields + ", methods=" + methods + '}';
	}
}
