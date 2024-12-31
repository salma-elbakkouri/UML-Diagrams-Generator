package org.mql.java.reflection;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.mql.java.models.ClassEntity;
import org.mql.java.models.FieldEntity;
import org.mql.java.models.MethodEntity;
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
            System.out.println("error: 'src' folder does not exist in the project");
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
                String className = packageName + "." + file.getName().replace(".java", "");
                try {
                    Class<?> clazz = Class.forName(className);
                    ClassEntity classEntity = new ClassEntity(className, clazz.isInterface() ? "interface" : "class");

                    List<FieldEntity> fields = new ArrayList<>();
                    for (Field field : clazz.getDeclaredFields()) {
                        List<String> modifiers = List.of(Modifier.toString(field.getModifiers()).split(" "));
                        fields.add(new FieldEntity(field.getName(), field.getType().getSimpleName(), modifiers));
                    }
                    classEntity.setFields(fields);

                    List<MethodEntity> methods = new ArrayList<>();
                    for (Method method : clazz.getDeclaredMethods()) {
                        List<String> methodModifiers = List.of(Modifier.toString(method.getModifiers()).split(" "));
                        List<String> parameters = new ArrayList<>();
                        for (Class<?> paramType : method.getParameterTypes()) {
                            parameters.add(paramType.getSimpleName());
                        }
                        methods.add(new MethodEntity(method.getName(), method.getReturnType().getSimpleName(), parameters, methodModifiers));
                    }
                    classEntity.setMethods(methods);

                    classes.add(classEntity);
                } catch (ClassNotFoundException e) {
                    System.out.println("Class not found: " + className);
                }
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

                System.out.println("    Fields:");
                for (FieldEntity field : cls.getFields()) {
                    System.out.println("      " + String.join(" ", field.getModifiers()) + " " + field.getType() + " " + field.getName());
                }

                System.out.println("    Methods:");
                for (MethodEntity method : cls.getMethods()) {
                    String parameters = String.join(", ", method.getParameters());
                    System.out.println("      " + String.join(" ", method.getModifiers()) + " " + method.getReturnType() + " " + method.getName() + "(" + parameters + ")");
                }
            }
        }
    }

    public Project getProject() {
        return project;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner();
        scanner.displayScanResults();
    }
}
