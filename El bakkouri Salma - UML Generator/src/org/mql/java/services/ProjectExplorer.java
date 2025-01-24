package org.mql.java.services;

import java.io.File;

import org.mql.java.models.ProjectModel;

public class ProjectExplorer {
	private final String rootPath;

	public ProjectExplorer(String rootPath) {
		this.rootPath = rootPath;
	}

	public ProjectModel explore() {
		File binFolder = new File(rootPath + File.separator + "bin");
		if (binFolder.exists() && binFolder.isDirectory()) {
			ProjectModel project = new ProjectModel(rootPath);
			PackageExplorer packageExplorer = new PackageExplorer(rootPath);
			project.setPackages(packageExplorer.explorePackages(binFolder, ""));
			return project;
		} else {
			System.err.println("Error: 'bin' folder does not exist in the project at " + binFolder.getAbsolutePath());
			return null;
		}
	}
}