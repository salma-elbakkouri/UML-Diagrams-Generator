package org.mql.java.models;

import java.util.List;

public class MethodModel {
	private String name;
	private String returnType;
	private List<ParameterModel> parameters;
	private List<String> modifiers;

	public MethodModel(String name, String returnType, List<ParameterModel> parameters, List<String> modifiers) {
		this.name = name;
		this.returnType = returnType;
		this.parameters = parameters;
		this.modifiers = modifiers;
	}

	public MethodModel() {
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

	public List<ParameterModel> getParameters() {
		return parameters;
	}

	public void setParameters(List<ParameterModel> parameters) {
		this.parameters = parameters;
	}

	public List<String> getModifiers() {
		return modifiers;
	}

	public void setModifiers(List<String> modifiers) {
		this.modifiers = modifiers;
	}

	@Override
	public String toString() {
		StringBuilder params = new StringBuilder();
		for (int i = 0; i < parameters.size(); i++) {
			params.append(parameters.get(i));
			if (i < parameters.size() - 1) {
				params.append(", ");
			}
		}
		return String.format("%s %s %s(%s)", String.join(" ", modifiers), returnType, name, params.toString());
	}

}
