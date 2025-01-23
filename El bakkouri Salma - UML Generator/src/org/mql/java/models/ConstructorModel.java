package org.mql.java.models;

import java.util.ArrayList;
import java.util.List;

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