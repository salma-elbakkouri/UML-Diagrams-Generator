package org.mql.java.models;

import java.util.List;

public class ClassModel {
	private String name;
	private String type;
	private List<String> modifiers;
	private List<FieldModel> fields;
	private List<MethodModel> methods;
	private List<RelationshipModel> relationships;

	public List<RelationshipModel> getRelationships() {
		return relationships;
	}

	public void setRelationships(List<RelationshipModel> relationships) {
		this.relationships = relationships;
	}

	public ClassModel() {
		// TODO Auto-generated constructor stub
	}

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
		StringBuilder sb = new StringBuilder("");
		String formattedModifiers = String.join(" ", modifiers);
		sb.append("|__ " + formattedModifiers + " " + type + ": " + name + "\n");

		if (!fields.isEmpty()) {
			sb.append("\t|__ Fields:\n");
			for (FieldModel field : fields) {
				sb.append("\t    ").append(field).append("\n");
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
			for (RelationshipModel relationship : relationships) {
				sb.append("\t    ").append(relationship).append("\n");
			}
		}

		return sb.toString();

	}

}
