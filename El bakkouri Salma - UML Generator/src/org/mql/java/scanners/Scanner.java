package org.mql.java.scanners;

import org.mql.java.models.ProjectModel;
import org.mql.java.xml.DOMWriter;
import org.mql.java.xml.STAXWriter;

public class Scanner {
	private ProjectModel project;

	public Scanner() {
		scan();
	}

	public ProjectModel scan() {
		String rootPath = System.getProperty("user.dir");
		ProjectExplorer projectExplorer = new ProjectExplorer(rootPath);
		project = projectExplorer.explore();
		if (project != null) {
			 displayScanResults();
			 saveToXML("resources/projects.xml");
			return project;
		} else {
			System.err.println("Project scanning failed!");
			return null;
		}
	}

	public void displayScanResults() {
		System.out.println(project);
	}


	private void saveToXML(String filePath) {
		STAXWriter writer = new STAXWriter(filePath);
        writer.writeProject(project);
	}
	
	public static void main(String[] args) {
		new Scanner();
	}
}
