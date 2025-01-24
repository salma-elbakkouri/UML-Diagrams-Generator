package org.mql.java.models;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ConstructorModel {
    private List<String> modifiers;
    private List<ParameterModel> parameters;

    public ConstructorModel() {
        this.modifiers = new ArrayList<>();
        this.parameters = new ArrayList<>();
    }

    public ConstructorModel(List<String> modifiers, List<ParameterModel> parameters) {
        this.modifiers = modifiers != null ? modifiers : new ArrayList<>();
        this.parameters = parameters != null ? parameters : new ArrayList<>();
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
        this.parameters = parameters != null ? parameters : new ArrayList<>();
    }

    @Override
    public String toString() {
        return String.format("%s (%s)",
            String.join(" ", modifiers),
            String.join(", ", parameters.stream().map(ParameterModel::toString).toList())
        );
    }
	

    
}