package org.mql.java.models;

import java.util.List;

public class PackageModel {
	private String name;
	private List<ClassModel> classes;

	public PackageModel(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ClassModel> getClasses() {
		return classes;
	}

	public void setClasses(List<ClassModel> classes) {
		this.classes = classes;
	}
}
