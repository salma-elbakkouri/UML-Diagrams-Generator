package org.mql.java.reflection;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.mql.java.models.ClassEntity;
import org.mql.java.models.PackageEntity;
import org.mql.java.models.Project;

public class Scanner {
    private Project project;

    public Scanner() {
        scan();
    }

    private void scan() {
        String rootPath = System.getProperty("user.dir");
        System.out.println("Project Path: " + rootPath);

        File srcFolder = new File(rootPath + File.separator + "src");
        if (srcFolder.exists() && srcFolder.isDirectory()) {
            project = new Project(rootPath);
            project.setPackages(retrievePackages(srcFolder, ""));
        } else {
            System.out.println("Error: 'src' folder does not exist in the project.");
        }
    }

    private List<PackageEntity> retrievePackages(File folder, String packageName) {
        List<PackageEntity> packages = new ArrayList<>();

        File[] files = folder.listFiles();
        if (files == null) {
            return packages;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                String newPackageName = packageName.isEmpty() ? file.getName() : packageName + "." + file.getName();

                PackageEntity packageEntity = new PackageEntity(newPackageName);
                packageEntity.setClasses(retrieveClasses(file, newPackageName));
                packages.add(packageEntity);

                packages.addAll(retrievePackages(file, newPackageName));
            }
        }
        return packages;
    }

    private List<ClassEntity> retrieveClasses(File folder, String packageName) {
        List<ClassEntity> classes = new ArrayList<>();

        File[] files = folder.listFiles();
        if (files == null) {
            return classes;
        }

        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".java")) {
                String className = file.getName().replace(".java", "");
                ClassEntity classEntity = new ClassEntity(packageName + "." + className, "class");
                classEntity.setFields(new ArrayList<>());
                classEntity.setMethods(new ArrayList<>());
                classEntity.setAnnotations(new ArrayList<>());

                classes.add(classEntity);
            }
        }

        return classes;
    }

    private void displayScanResults() {
        System.out.println("\n=== Scan Results ===");
        System.out.println("Project: " + project.getName());

        for (PackageEntity pkg : project.getPackages()) {
            System.out.println("Package: " + pkg.getName());

            for (ClassEntity cls : pkg.getClasses()) {
                System.out.println("  Class: " + cls.getName());
            }
        }
    }

    public Project getProject() {
        return project;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner();
        Project project = scanner.getProject();
        scanner.displayScanResults();
    }
}
