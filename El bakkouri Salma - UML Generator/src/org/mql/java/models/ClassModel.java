package org.mql.java.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ClassModel {
    private String name;
    private String type;
    private List<String> modifiers;
    private List<FieldModel> fields;
    private List<MethodModel> methods;
    private Set<RelationModel> relationships;
    private List<ConstructorModel> constructors;

    public ClassModel() {
        this.modifiers = new ArrayList<>();
        this.fields = new ArrayList<>();
        this.methods = new ArrayList<>();
        this.relationships = new HashSet<>();
        this.constructors = new ArrayList<>();
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
		StringBuilder sb = new StringBuilder("");
		String formattedModifiers = String.join(" ", modifiers);
		sb.append("|__ " + formattedModifiers + " " + type + ": " + name + "\n");

		if (!fields.isEmpty()) {
			sb.append("\t|__ Fields:\n");
			for (FieldModel field : fields) {
				sb.append("\t    ").append(field).append("\n");
			}
		}
		if (!constructors.isEmpty()) {
			sb.append("\t|__ Constructors:\n");
			for (ConstructorModel constructor : constructors) {
				sb.append("\t    ").append(constructor).append("\n");
			}
		}
		if (!methods.isEmpty()) {
			sb.append("\t|__ Methods:\n");
			for (MethodModel method : methods) {
				sb.append("\t    ").append(method).append("\n");
			}
		}
		if (!relationships.isEmpty()) {
			sb.append("\t|__ Relationships:\n");
			for (RelationModel relationship : relationships) {
				sb.append("\t    ").append(relationship).append("\n");
			}
		}

		return sb.toString();

	}

    
    

   
}