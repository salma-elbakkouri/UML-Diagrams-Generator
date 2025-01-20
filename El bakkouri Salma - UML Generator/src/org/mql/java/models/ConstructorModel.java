package org.mql.java.models;

import java.util.List;
import java.util.stream.Collectors;

public class ConstructorModel {
	private List<String> modifiers;
	private List<ParameterModel> parameters;

	public ConstructorModel() {
	}

	public ConstructorModel(List<String> modifiers, List<ParameterModel> parameters) {
		super();
		this.modifiers = modifiers;
		this.parameters = parameters;
	}

	public List<String> getModifiers() {
		return modifiers;
	}

	public void setModifiers(List<String> modifiers) {
		this.modifiers = modifiers;
	}

	public List<ParameterModel> getParameters() {
		return parameters;
	}

	public void setParameters(List<ParameterModel> parameters) {
		this.parameters = parameters;
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
		return String.format("%s Constructor(%s)", String.join(" ", modifiers), params.toString());
	}

}
