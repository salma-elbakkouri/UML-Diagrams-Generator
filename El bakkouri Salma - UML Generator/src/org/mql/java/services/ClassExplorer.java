package org.mql.java.services;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.mql.java.enums.RelationType;
import org.mql.java.models.ClassModel;
import org.mql.java.models.ConstructorModel;
import org.mql.java.models.FieldModel;
import org.mql.java.models.MethodModel;
import org.mql.java.models.ParameterModel;
import org.mql.java.models.RelationModel;

public class ClassExplorer {

	private String rootBinDirectory;

	public ClassExplorer(String rootBinDirectory) {
		this.rootBinDirectory = rootBinDirectory;
	}

	public List<ClassModel> extractClasses(File folder, String packageName) throws ClassNotFoundException {
		List<ClassModel> classes = new ArrayList<>();
		File[] files = folder.listFiles();

		if (files == null)
			return classes;

		for (File file : files) {
			if (file.isDirectory()) {
				String newPackage = packageName.isEmpty() ? file.getName() : packageName + "." + file.getName();
				classes.addAll(extractClasses(file, newPackage));
			} else if (file.getName().endsWith(".class")) {
				String className = packageName.isEmpty() ? file.getName().replace(".class", "")
						: packageName + "." + file.getName().replace(".class", "");

				try {
					CustomClassLoader customClassLoader = new CustomClassLoader(rootBinDirectory);
					Class<?> cls = customClassLoader.loadClass(className);

					ClassModel classModel = new ClassModel();

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
				} catch (Exception e) {
					System.out.println("Error loading class: " + className + ". Reason: " + e.getMessage());
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

	public Set<RelationModel> detectRelationships(Class<?> cls) {
		Set<RelationModel> relationships = new HashSet<>();
		if (cls.getSuperclass() != null && !cls.getSuperclass().equals(Object.class)) {
			RelationModel relation = new RelationModel(cls.getSimpleName(), cls.getSuperclass().getSimpleName(),
					RelationType.EXTENDS);
			relationships.add(relation);
		}

		for (Class<?> interfaceFound : cls.getInterfaces()) {
			RelationModel relation = new RelationModel(cls.getSimpleName(), interfaceFound.getSimpleName(),
					RelationType.IMPLEMENTS);
			relationships.add(relation);
		}

		for (Field field : cls.getDeclaredFields()) {
			Class<?> fieldType = field.getType();
			if (!fieldType.isPrimitive() && !fieldType.equals(Object.class)) {
				RelationModel relation = new RelationModel(cls.getSimpleName(), fieldType.getSimpleName(),
						RelationType.AGGREGATES);
				relationships.add(relation);
			}
		}

		for (Method method : cls.getDeclaredMethods()) {
			Class<?> returnType = method.getReturnType();
			if (!returnType.isPrimitive() && !returnType.equals(Object.class)) {
				RelationModel relation = new RelationModel(cls.getSimpleName(), returnType.getSimpleName(),
						RelationType.USES);
				relationships.add(relation);
			}

			for (Class<?> paramType : method.getParameterTypes()) {
				if (!paramType.isPrimitive() && !paramType.equals(Object.class)) {
					RelationModel relation = new RelationModel(cls.getSimpleName(), paramType.getSimpleName(),
							RelationType.USES);
					relationships.add(relation);
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