package org.mql.java.models;

import java.util.List;

public class Project {
	private String name;
	private List<PackageEntity> packages;

	public Project(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<PackageEntity> getPackages() {
		return packages;
	}

	public void setPackages(List<PackageEntity> packages) {
		this.packages = packages;
	}

}
