package org.mql.java.models;

import java.awt.DisplayMode;
import java.util.List;

import org.mql.java.util.ConsoleDisplay;

public class PackageModel {
	private String name;
	private List<ClassModel> classes;

	public PackageModel(String name) {
		this.name = name;
	}

	public PackageModel() {
		// TODO Auto-generated constructor stub
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
	    return ConsoleDisplay.displayPackage(this, 0);
	}

}
