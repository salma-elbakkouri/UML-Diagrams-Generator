package org.mql.java.models;

import java.util.List;

public class MethodEntity {
	private String name;
	private String returnType;
	private List<String> parameters;
	private List<String> modifiers;

	public MethodEntity(String name, String returnType, List<String> parameters, List<String> modifiers) {
		this.name = name;
		this.returnType = returnType;
		this.parameters = parameters;
		this.modifiers = modifiers;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public List<String> getParameters() {
		return parameters;
	}

	public void setParameters(List<String> parameters) {
		this.parameters = parameters;
	}

	public List<String> getModifiers() {
		return modifiers;
	}

	public void setModifiers(List<String> modifiers) {
		this.modifiers = modifiers;
	}
}
