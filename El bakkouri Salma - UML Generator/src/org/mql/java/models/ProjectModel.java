package org.mql.java.models;

import java.util.List;

import org.mql.java.util.ConsoleDisplay;

public class ProjectModel {
	private String name;
	private List<PackageModel> packages;

	public ProjectModel(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<PackageModel> getPackages() {
		return packages;
	}

	public void setPackages(List<PackageModel> packages) {
		this.packages = packages;
	}

	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("Project: ").append(name).append("\n");

	    if (!packages.isEmpty()) {
	        packages.forEach(pkg -> sb.append(ConsoleDisplay.displayPackage(pkg, 1)));
	    }

	    return sb.toString();
	}
}
