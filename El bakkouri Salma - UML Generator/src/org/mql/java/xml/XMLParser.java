package org.mql.java.xml;

import org.mql.java.enums.RelationType;
import org.mql.java.models.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class XMLParser {
	private XMLNode rootNode;

	public XMLParser(String source) {
		this.rootNode = new XMLNode(source);
	}

	public List<ProjectModel> parseProjects() {
		List<ProjectModel> projects = new ArrayList<>();
		XMLNode[] projectNodes = rootNode.children();

		for (XMLNode projectNode : projectNodes) {
			if (projectNode.getName().equals("project")) {
				ProjectModel project = parseProject(projectNode);
				projects.add(project);
			}
		}

		return projects;
	}

	private ProjectModel parseProject(XMLNode projectNode) {
		ProjectModel project = new ProjectModel();
		project.setName(projectNode.attribute("name"));

		XMLNode[] packageNodes = projectNode.children();
		List<PackageModel> packages = new ArrayList<>();

		for (XMLNode packageNode : packageNodes) {
			if (packageNode.getName().equals("packages")) {
				packages.addAll(parsePackages(packageNode));
			}
		}

		project.setPackages(packages);
		return project;
	}

	private List<PackageModel> parsePackages(XMLNode packagesNode) {
		List<PackageModel> packages = new ArrayList<>();
		XMLNode[] packageNodes = packagesNode.children();

		for (XMLNode packageNode : packageNodes) {
			if (packageNode.getName().equals("package")) {
				packages.add(parsePackage(packageNode));
			}
		}

		return packages;
	}

	private PackageModel parsePackage(XMLNode packageNode) {
		PackageModel packageModel = new PackageModel();
		packageModel.setName(packageNode.attribute("name"));

		XMLNode[] classNodes = packageNode.children();
		List<ClassModel> classes = new ArrayList<>();

		for (XMLNode classNode : classNodes) {
			if (classNode.getName().equals("class")) {
				classes.add(parseClass(classNode));
			}
		}

		packageModel.setClasses(classes);
		return packageModel;
	}

	private ClassModel parseClass(XMLNode classNode) {
		ClassModel classModel = new ClassModel();
		classModel.setName(classNode.attribute("name"));
		classModel.setType(classNode.attribute("type"));

		XMLNode[] classChildren = classNode.children();
		List<String> modifiers = new ArrayList<>();
		List<FieldModel> fields = new ArrayList<>();
		List<MethodModel> methods = new ArrayList<>();
		Set<RelationModel> relationships = new HashSet<>();
		List<ConstructorModel> constructors = new ArrayList<>();

		for (XMLNode child : classChildren) {
			switch (child.getName()) {
			case "modifiers":
				modifiers.addAll(parseModifiers(child));
				break;
			case "fields":
				fields.addAll(parseFields(child));
				break;
			case "methods":
				methods.addAll(parseMethods(child));
				break;
			case "relationships":
				relationships.addAll(parseRelationships(child));
				break;
			case "constructors":
				constructors.addAll(parseConstructors(child));
				break;
			}
		}

		classModel.setModifiers(modifiers);
		classModel.setFields(fields);
		classModel.setMethods(methods);
		classModel.setRelationships(relationships);
		classModel.setConstructors(constructors);

		return classModel;
	}

	private List<String> parseModifiers(XMLNode modifiersNode) {
		List<String> modifiers = new ArrayList<>();
		XMLNode[] modifierNodes = modifiersNode.children();

		for (XMLNode modifierNode : modifierNodes) {
			if (modifierNode.getName().equals("modifier")) {
				modifiers.add(modifierNode.getValue());
			}
		}

		return modifiers;
	}

	private List<ConstructorModel> parseConstructors(XMLNode constructorsNode) {
		List<ConstructorModel> constructors = new ArrayList<>();
		XMLNode[] constructorNodes = constructorsNode.children();

		for (XMLNode constructorNode : constructorNodes) {
			if (constructorNode.getName().equals("constructor")) {
				ConstructorModel constructor = new ConstructorModel();
				constructor.setModifiers(parseModifiers(constructorNode.child("modifiers")));

				XMLNode parametersNode = constructorNode.child("parameters");
				if (parametersNode != null) {
					constructor.setParameters(parseParameters(parametersNode));
				}

				constructors.add(constructor);
			}
		}

		return constructors;
	}

	private List<ParameterModel> parseParameters(XMLNode parametersNode) {
		List<ParameterModel> parameters = new ArrayList<>();
		XMLNode[] parameterNodes = parametersNode.children();

		for (XMLNode parameterNode : parameterNodes) {
			if (parameterNode.getName().equals("parameter")) {
				ParameterModel parameter = new ParameterModel();
				parameter.setName(parameterNode.attribute("name"));
				parameter.setType(parameterNode.attribute("type"));
				parameters.add(parameter);
			}
		}

		return parameters;
	}

	private List<FieldModel> parseFields(XMLNode fieldsNode) {
		List<FieldModel> fields = new ArrayList<>();
		XMLNode[] fieldNodes = fieldsNode.children();

		for (XMLNode fieldNode : fieldNodes) {
			if (fieldNode.getName().equals("field")) {
				FieldModel field = new FieldModel();
				field.setName(fieldNode.attribute("name"));
				field.setType(fieldNode.attribute("type"));

				XMLNode modifiersNode = fieldNode.child("modifiers");
				if (modifiersNode != null) {
					field.setModifiers(parseModifiers(modifiersNode));
				} else {
					field.setModifiers(new ArrayList<>());
				}

				fields.add(field);
			}
		}

		return fields;
	}

	private List<MethodModel> parseMethods(XMLNode methodsNode) {
		List<MethodModel> methods = new ArrayList<>();
		XMLNode[] methodNodes = methodsNode.children();

		for (XMLNode methodNode : methodNodes) {
			if (methodNode.getName().equals("method")) {
				MethodModel method = new MethodModel();
				method.setName(methodNode.attribute("name"));
				method.setReturnType(methodNode.attribute("returnType"));

				XMLNode modifiersNode = methodNode.child("modifiers");
				if (modifiersNode != null) {
					method.setModifiers(parseModifiers(modifiersNode));
				} else {
					method.setModifiers(new ArrayList<>()); 
				}

				XMLNode parametersNode = methodNode.child("parameters");
				if (parametersNode != null) {
					method.setParameters(parseParameters(parametersNode));
				} else {
					method.setParameters(new ArrayList<>());
				}

				methods.add(method);
			}
		}

		return methods;
	}

	private Set<RelationModel> parseRelationships(XMLNode relationshipsNode) {
		Set<RelationModel> relationships = new HashSet<>();
		XMLNode[] relationshipNodes = relationshipsNode.children();
		for (XMLNode relationshipNode : relationshipNodes) {
			if (relationshipNode.getName().equals("relationship")) {
				RelationModel relation = new RelationModel();
				relation.setSourceClass(relationshipNode.attribute("sourceClass"));
				relation.setTargetClass(relationshipNode.attribute("targetClass"));
				relation.setRelationType(RelationType.valueOf(relationshipNode.attribute("type")));
				relationships.add(relation);
			}
		}

		return relationships;
	}
}
