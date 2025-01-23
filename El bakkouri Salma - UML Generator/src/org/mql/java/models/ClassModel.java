package org.mql.java.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.mql.java.util.ConsoleDisplay;

public class ClassModel {
    private String name;
    private String type;
    private List<String> modifiers;
    private List<FieldModel> fields;
    private List<MethodModel> methods;
    private Set<RelationModel> relationships;
    private List<ConstructorModel> constructors;

    public ClassModel() {
        this.modifiers = modifiers != null ? modifiers : new ArrayList<>();
        this.fields = fields != null ? fields : new ArrayList<>();
        this.methods = methods != null ? methods : new ArrayList<>();
        this.relationships = relationships != null ? relationships : new HashSet<>();
        this.constructors = constructors != null ? constructors : new ArrayList<>();
    }

    public ClassModel(String name, String type) {
        this();
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
        this.modifiers = modifiers != null ? modifiers : new ArrayList<>();
    }

    public List<FieldModel> getFields() {
        return fields;
    }

    public void setFields(List<FieldModel> fields) {
        this.fields = fields != null ? fields : new ArrayList<>();
    }

    public List<MethodModel> getMethods() {
        return methods;
    }

    public void setMethods(List<MethodModel> methods) {
        this.methods = methods != null ? methods : new ArrayList<>();
    }

    public Set<RelationModel> getRelationships() {
        return relationships;
    }

    public void setRelationships(Set<RelationModel> relationships) {
        this.relationships = relationships != null ? relationships : new HashSet<>();
    }

    public List<ConstructorModel> getConstructors() {
        return constructors;
    }

    public void setConstructors(List<ConstructorModel> constructors) {
        this.constructors = constructors != null ? constructors : new ArrayList<>();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("|__ Class: ").append(String.join(" ", modifiers)).append(" ").append(type).append(": ").append(name)
                .append("\n");

        if (constructors != null && !constructors.isEmpty()) {
            sb.append(ConsoleDisplay.displaySection("Constructors", constructors, 1));
        }
        if (fields != null && !fields.isEmpty()) {
            sb.append(ConsoleDisplay.displaySection("Fields", fields, 1));
        }
        if (methods != null && !methods.isEmpty()) {
            sb.append(ConsoleDisplay.displaySection("Methods", methods, 1));
        }
        if (relationships != null && !relationships.isEmpty()) {
            sb.append(ConsoleDisplay.displayRelationships(name, List.copyOf(relationships), 1));
        }

        return sb.toString();
    }
}