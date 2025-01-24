package org.mql.java.services;

import org.mql.java.models.ProjectModel;

public class Scanner {
	private final String rootPath;

	public Scanner(String rootPath) {
		this.rootPath = rootPath;
	}

	public ProjectModel scan() {
		ProjectExplorer projectExplorer = new ProjectExplorer(rootPath);
		return projectExplorer.explore();
	}
}