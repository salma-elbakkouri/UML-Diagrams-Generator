package org.mql.java.scanners;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.mql.java.models.ClassModel;
import org.mql.java.models.FieldModel;
import org.mql.java.models.MethodModel;
import org.mql.java.models.PackageModel;
import org.mql.java.models.ProjectModel;

public class Scanner {
	private ProjectModel project;

	public Scanner() {
		scan();
	}

	public void scan() {
		String rootPath = System.getProperty("user.dir");
		ProjectExplorer projectExplorer = new ProjectExplorer(rootPath);
		project = projectExplorer.explore();
		if (project != null) {
			displayScanResults();
		} else {
			System.err.println("Project scanning failed!");
		}
	}

	public void displayScanResults() {
		System.out.println(project);
	}

	public static void main(String[] args) {
		new Scanner();
	}
}
