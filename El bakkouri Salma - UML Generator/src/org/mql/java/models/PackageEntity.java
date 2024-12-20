package org.mql.java.models;

import java.util.List;

public class PackageEntity {
	private String name;
	private List<ClassEntity> classes;

	public PackageEntity(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ClassEntity> getClasses() {
		return classes;
	}

	public void setClasses(List<ClassEntity> classes) {
		this.classes = classes;
	}
}
