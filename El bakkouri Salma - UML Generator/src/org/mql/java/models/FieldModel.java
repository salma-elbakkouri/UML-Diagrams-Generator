package org.mql.java.models;

import java.util.List;

public class FieldModel {
	private String name;
	private String type;
	private List<String> modifiers;

	public FieldModel(String name, String type, List<String> modifiers) {
		this.name = name;
		this.type = type;
		this.modifiers = modifiers;
	}
	
	public FieldModel() {
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

	@Override
	public String toString() {
	    return String.format("%s %s %s", String.join(" ", modifiers), type, name);
	}


}
