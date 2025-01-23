package org.mql.java.scanners;

import org.mql.java.models.ProjectModel;

public class Scanner {

	public Scanner() {
		scan();
	}

	public ProjectModel scan() {
		String rootPath = System.getProperty("user.dir");
		ProjectExplorer projectExplorer = new ProjectExplorer(rootPath);
		return projectExplorer.explore();
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner();
		ProjectModel project = scanner.scan();
		if (project != null) {
			System.out.println("Scan successful.");
		} else {
			System.err.println("Scan failed!");
		}
	}

}
