package org.mql.java.scanners;

import org.mql.java.models.ProjectModel;

public class Scanner {
	private ProjectModel project;

	public Scanner() {
		scan();
	}

	public ProjectModel scan() {
		String rootPath = System.getProperty("user.dir");
		ProjectExplorer projectExplorer = new ProjectExplorer(rootPath);
		project = projectExplorer.explore();
		displayScanResults();
		return project;
	}

	public void displayScanResults() {
		if (project != null) {
			System.out.println("Project Scan Results:");
			System.out.println(project);
		} else {
			System.err.println("No project found during scanning!");
		}
	}

	public static void main(String[] args) {
		new Scanner();
	}

}
