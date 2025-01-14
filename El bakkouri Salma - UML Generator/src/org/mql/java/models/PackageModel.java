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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("");
		sb.append("|__ Package: " + name + "\n");
		for (ClassModel cls : classes) {
			sb.append("    ").append(cls).append("\n");
		}
		return sb.toString();
	}

}
