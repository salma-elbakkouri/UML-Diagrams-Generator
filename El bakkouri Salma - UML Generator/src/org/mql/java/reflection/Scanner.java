package org.mql.java.reflection;

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

	private void scan() {
		String rootPath = System.getProperty("user.dir");

		ProjectExplorer projectExplorer = new ProjectExplorer(rootPath);
		project = projectExplorer.explore();

		if (project != null) {
			displayScanResults();
		} else {
			System.out.println("Project scanning failed!");
		}
	}

	private void displayScanResults() {
		System.out.println("Project: " + project.getName());
		System.out.println("--- Scan Results ---\n");
		

		for (PackageModel pkg : project.getPackages()) {
			if (!pkg.getClasses().isEmpty()) {

				System.out.println("|__ Package: " + pkg.getName());

				for (ClassModel cls : pkg.getClasses()) {
					System.out.println("    |__ Class: " + cls.getName());

					System.out.println("        |__ Fields:");
					for (FieldModel field : cls.getFields()) {
						System.out.printf("            |__ %s %s %s%n", String.join(" ", field.getModifiers()),
								field.getType(), field.getName());
					}

					System.out.println("        |__ Methods:");
					for (MethodModel method : cls.getMethods()) {
						String parameters = String.join(", ", method.getParameters());
						System.out.printf("            |__ %s %s %s(%s)%n", String.join(" ", method.getModifiers()),
								method.getReturnType(), method.getName(), parameters);
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		new Scanner();
	}
}
