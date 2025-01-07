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

public class ClassExplorer {

	public List<ClassModel> exploreClasses(File folder, String packageName) {
        List<ClassModel> classes = new ArrayList<>();

        File[] files = folder.listFiles();
        if (files == null) return classes;

        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".java")) {
                String className = packageName + "." + file.getName().replace(".java", "");
                try {
                    Class<?> cls = Class.forName(className);
                    ClassModel classModel = new ClassModel(className, cls.isInterface() ? "interface" : "class");

                    classModel.setFields(exploreFields(cls));
                    classModel.setMethods(exploreMethods(cls));

                    classes.add(classModel);
                } catch (ClassNotFoundException e) {
                    System.out.println("Class not found: " + className);
                }
            }
        }
        return classes;
    }

    private List<FieldModel> exploreFields(Class<?> cls) {
        List<FieldModel> fields = new ArrayList<>();
        for (Field field : cls.getDeclaredFields()) {
            List<String> modifiers = List.of(Modifier.toString(field.getModifiers()).split(" "));
            fields.add(new FieldModel(field.getName(), field.getType().getSimpleName(), modifiers));
        }
        return fields;
    }

    private List<MethodModel> exploreMethods(Class<?> cls) {
        List<MethodModel> methods = new ArrayList<>();
        for (Method method : cls.getDeclaredMethods()) {
            List<String> modifiers = List.of(Modifier.toString(method.getModifiers()).split(" "));
            List<String> parameters = new ArrayList<>();
            for (Class<?> paramType : method.getParameterTypes()) {
                parameters.add(paramType.getSimpleName());
            }
            methods.add(new MethodModel(method.getName(), method.getReturnType().getSimpleName(), parameters, modifiers));
        }
        return methods;
    }

}
