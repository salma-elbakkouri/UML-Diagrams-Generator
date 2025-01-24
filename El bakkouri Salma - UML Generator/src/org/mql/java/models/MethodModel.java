package org.mql.java.models;

import java.util.ArrayList;
import java.util.List;

public class MethodModel {
	private String name;
	private String returnType;
	private List<ParameterModel> parameters;
	private List<String> modifiers;

	public MethodModel(String name, String returnType, List<ParameterModel> parameters, List<String> modifiers) {
		this.name = name;
		this.returnType = returnType;
		this.parameters = parameters != null ? parameters : new ArrayList<>();
		this.modifiers = modifiers != null ? modifiers : new ArrayList<>();
	}

	public MethodModel() {
		this.parameters = new ArrayList<>();
		this.modifiers = new ArrayList<>();
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
		this.parameters = parameters != null ? parameters : new ArrayList<>();
	}

	public List<String> getModifiers() {
		return modifiers;
	}

	public void setModifiers(List<String> modifiers) {
		this.modifiers = modifiers != null ? modifiers : new ArrayList<>();
	}

	@Override
	public String toString() {
		return String.format("%s %s %s(%s)", String.join(" ", modifiers), returnType, name,
				String.join(", ", parameters.stream().map(ParameterModel::toString).toList()));
	}

}