package org.mql.java.scanners;

import java.io.File;

import org.mql.java.models.ProjectModel;

public class ProjectExplorer {
	private final String rootPath;

    public ProjectExplorer(String rootPath) {
        this.rootPath = rootPath;
    }

    public ProjectModel explore() {
        File srcFolder = new File(rootPath + File.separator + "src");
        if (srcFolder.exists() && srcFolder.isDirectory()) {
            ProjectModel project = new ProjectModel(rootPath);
            PackageExplorer packageExplorer = new PackageExplorer();
            project.setPackages(packageExplorer.explorePackages(srcFolder, ""));
            return project;
        } else {
            System.err.println("Error : 'src' folder does not exist in the project");
            return null;
        }
    }
}
