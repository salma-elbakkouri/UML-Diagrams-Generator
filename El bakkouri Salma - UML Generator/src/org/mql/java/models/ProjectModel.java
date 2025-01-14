package org.mql.java.models;

import java.util.List;

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
		StringBuilder sb = new StringBuilder("");
		sb.append("Project: " + name + "\n");
		for (PackageModel pkg : packages) {
			if (!pkg.getClasses().isEmpty()) {
				sb.append(pkg).append("\n");
			}
		}
		return sb.toString();
	}

}
