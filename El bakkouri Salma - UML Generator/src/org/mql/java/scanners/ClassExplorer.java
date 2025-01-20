package org.mql.java.scanners;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Vector;

import org.mql.java.models.ClassModel;
import org.mql.java.models.ConstructorModel;
import org.mql.java.models.FieldModel;
import org.mql.java.models.MethodModel;
import org.mql.java.models.ParameterModel;
import org.mql.java.models.RelationshipModel;

public class ClassExplorer {

	public List<ClassModel> extractClasses(File folder, String packageName) {
		List<ClassModel> classes = new Vector<>();
		File[] files = folder.listFiles();
		if (files == null)
			return classes;

		for (File file : files) {
			if (file.isFile() && file.getName().endsWith(".java")) {
				String className = packageName + "." + file.getName().replace(".java", "");
				ClassModel classModel = new ClassModel();

				try {
					Class<?> cls = Class.forName(className);
					classModel.setName(cls.getSimpleName());
					if (cls.isEnum()) {
						classModel.setType("enumeration");
					} else if (cls.isAnnotation()) {
						classModel.setType("annotation");
					} else if (cls.isInterface()) {
						classModel.setType("interface");
					} else {
						classModel.setType("class");
					}
					List<String> classModifiers = List.of(Modifier.toString(cls.getModifiers()).split(" "));
					classModel.setModifiers(classModifiers);
					classModel.setConstructors(extractConstructors(cls));
					classModel.setFields(extractFields(cls));
					classModel.setMethods(extractMethods(cls));
					classModel.setRelationships(detectRelationships(cls));
					classes.add(classModel);
				} catch (ClassNotFoundException e) {
					System.out.println("Class not found: " + className);
				}
			}
		}
		return classes;
	}

	public List<FieldModel> extractFields(Class<?> cls) {
		List<FieldModel> fields = new Vector<>();
		for (Field field : cls.getDeclaredFields()) {
			List<String> modifiers = List.of(Modifier.toString(field.getModifiers()).split(" "));
			FieldModel fieldModel = new FieldModel(field.getName(), field.getType().getSimpleName(), modifiers);
			fields.add(fieldModel);
		}
		return fields;
	}

	public List<ConstructorModel> extractConstructors(Class<?> cls) {
		List<ConstructorModel> constructors = new Vector<>();
		for (Constructor<?> constructor : cls.getDeclaredConstructors()) {
			List<String> modifiers = List.of(Modifier.toString(constructor.getModifiers()).split(" "));
			List<ParameterModel> parameters = extractParameters(constructor);

			ConstructorModel constructorModel = new ConstructorModel(modifiers, parameters);
			constructors.add(constructorModel);
		}
		return constructors;
	}

	public List<MethodModel> extractMethods(Class<?> cls) {
		List<MethodModel> methods = new Vector<>();
		for (Method method : cls.getDeclaredMethods()) {
			List<String> modifiers = List.of(Modifier.toString(method.getModifiers()).split(" "));
			List<ParameterModel> parameters = extractParameters(method);

			MethodModel methodModel = new MethodModel(method.getName(), method.getReturnType().getSimpleName(),
					parameters, modifiers);
			methods.add(methodModel);
		}
		return methods;
	}

	public List<RelationshipModel> detectRelationships(Class<?> cls) {
		List<RelationshipModel> relationships = new Vector<>();
		if (cls.getSuperclass() != null && !cls.getSuperclass().equals(Object.class)) {
			RelationshipModel relation = new RelationshipModel(cls.getSimpleName(), cls.getSuperclass().getSimpleName(),
					"extends");
			relationships.add(relation);
		}

		for (Class<?> interfaceFound : cls.getInterfaces()) {
			RelationshipModel relation = new RelationshipModel(cls.getSimpleName(), interfaceFound.getSimpleName(),
					"implements");
			relationships.add(relation);
		}

		for (Field field : cls.getDeclaredFields()) {
		    Class<?> fieldType = field.getType();
		    if (!fieldType.isPrimitive() && !fieldType.equals(Object.class)) {
		        relationships.add(new RelationshipModel(
		            cls.getSimpleName(), 
		            fieldType.getSimpleName(), 
		            "aggregates"
		        ));
		    }
		}

		for (Method method : cls.getDeclaredMethods()) {
			Class<?> returnType = method.getReturnType();
			if (!returnType.isPrimitive() && !returnType.equals(Object.class)) {
				relationships.add(new RelationshipModel(cls.getSimpleName(), returnType.getSimpleName(), "uses"));
			}

			for (Class<?> paramType : method.getParameterTypes()) {
				if (!paramType.isPrimitive() && !paramType.equals(Object.class)) {
					relationships.add(new RelationshipModel(cls.getSimpleName(), paramType.getSimpleName(), "uses"));
				}
			}
		}
		return relationships;
	}

	private List<ParameterModel> extractParameters(Executable executable) {
		List<ParameterModel> parameters = new Vector<>();
		for (Parameter param : executable.getParameters()) {
			String paramType = param.getType().getSimpleName();
			String paramName = param.getName();
			parameters.add(new ParameterModel(paramType, paramName));
		}
		return parameters;
	}

}
