package org.mql.java.xml;

import org.mql.java.enums.RelationType;
import org.mql.java.models.ClassModel;
import org.mql.java.models.ConstructorModel;
import org.mql.java.models.FieldModel;
import org.mql.java.models.MethodModel;
import org.mql.java.models.RelationModel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class XMLGenerator {
	private Document document;

	public XMLGenerator() {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			document = builder.newDocument();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Element createElement(String tagName) {
		return document.createElement(tagName);
	}

	public Element createElement(String tagName, String textContent) {
		Element element = document.createElement(tagName);
		element.setTextContent(textContent);
		return element;
	}

	public void addAttribute(Element element, String name, String value) {
		element.setAttribute(name, value);
	}

	public void appendChild(Node parent, Node child) {
		parent.appendChild(child);
	}

	public Document getDocument() {
		return document;
	}

	public void saveToFile(String filePath) {
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(new File(filePath));
			transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Element createClassElement(ClassModel classModel) {
		Element classElement = createElement("class");
		addAttribute(classElement, "name", classModel.getName());
		addAttribute(classElement, "type", classModel.getType());
		Boolean isAbstract = false;

		Element modifiersElement = createElement("modifiers");
		for (String modifier : classModel.getModifiers()) {
			if (modifier.equals("abstract")) {
				isAbstract = true;
			}
			Element modifierElement = createElement("modifier");
			appendChild(modifiersElement, modifierElement);
		}
		addAttribute(classElement, "isAbstract", isAbstract.toString());
		appendChild(classElement, modifiersElement);

		Element fieldsElement = createElement("fields");
		for (FieldModel field : classModel.getFields()) {
			Element fieldElement = createFieldElement(field);
			appendChild(fieldsElement, fieldElement);
		}
		appendChild(classElement, fieldsElement);

		Element constructorsElement = createElement("constructors");
		for (ConstructorModel constructor : classModel.getConstructors()) {
			Element constructorElement = createConstructorElement(constructor);
			appendChild(constructorsElement, constructorElement);
		}
		appendChild(classElement, constructorsElement);

		Element methodsElement = createElement("methods");
		for (MethodModel method : classModel.getMethods()) {
			Element methodElement = createMethodElement(method);
			appendChild(methodsElement, methodElement);
		}
		appendChild(classElement, methodsElement);

		Element relationsElement = createElement("relationships");
		for (RelationModel relation : classModel.getRelationships()) {
			Element relationElement = createRelationElement(relation);
			appendChild(relationsElement, relationElement);
		}
		appendChild(classElement, relationsElement);

		return classElement;

	}

	private Element createRelationElement(RelationModel relation) {
		Element relationElement = createElement("relationship");
		addAttribute(relationElement, "sourceClass", relation.getSourceClass());
		addAttribute(relationElement, "targetClass", relation.getTargetClass());
		addAttribute(relationElement, "type", relation.getRelationType().toString());
		return relationElement;
	}

	private Element createMethodElement(MethodModel method) {
		Element methodElement = createElement("method");
		addAttribute(methodElement, "name", method.getName());
		addAttribute(methodElement, "returnType", method.getReturnType());

		Element modifiersElement = createElement("modifiers");
		for (String modifier : method.getModifiers()) {
			Element modifierElement = createElement("modifier", modifier);
			appendChild(modifiersElement, modifierElement);
		}
		appendChild(methodElement, modifiersElement);

		Element parametersElement = createElement("parameters");
		appendChild(methodElement, parametersElement);

		return methodElement;
	}

	private Element createConstructorElement(ConstructorModel constructor) {
		Element constructorElement = createElement("constructor");

		Element modifiersElement = createElement("modifiers");
		for (String modifier : constructor.getModifiers()) {
			Element modifierElement = createElement("modifier", modifier);
			appendChild(modifiersElement, modifierElement);
		}
		appendChild(constructorElement, modifiersElement);

		Element parametersElement = createElement("parameters");
		appendChild(constructorElement, parametersElement);

		return constructorElement;
	}

	private Element createFieldElement(FieldModel field) {
		Element fieldElement = createElement("field");
		addAttribute(fieldElement, "name", field.getName());
		addAttribute(fieldElement, "type", field.getType());

		Element modifiersElement = createElement("modifiers");
		for (String modifier : field.getModifiers()) {
			Element modifierElement = createElement("modifier", modifier);
			appendChild(modifiersElement, modifierElement);
		}
		appendChild(fieldElement, modifiersElement);

		return fieldElement;
	}

	public static void main(String[] args) {
		ClassModel classModel = new ClassModel();
		classModel.setName("StudentClass");
		classModel.setType("class");
		classModel.setModifiers(Arrays.asList("public", "abstract"));

		FieldModel field = new FieldModel();
		field.setName("name");
		field.setType("String");
		field.setModifiers(Arrays.asList("private"));
		classModel.setFields(Arrays.asList(field));

		ConstructorModel constructor = new ConstructorModel();
		constructor.setModifiers(Arrays.asList("public"));
		classModel.setConstructors(Arrays.asList(constructor));

		MethodModel method = new MethodModel();
		method.setName("getStudent");
		method.setReturnType("void");
		method.setModifiers(Arrays.asList("public"));
		classModel.setMethods(Arrays.asList(method));

		RelationModel relation = new RelationModel();
		relation.setSourceClass("StudentClass");
		relation.setTargetClass("PersonClass");
		relation.setRelationType(RelationType.EXTENDS);

		Set<RelationModel> relationships = new HashSet<>();
		relationships.add(relation);
		classModel.setRelationships(relationships);

		XMLGenerator generator = new XMLGenerator();
		Element classElement = generator.createClassElement(classModel);
		generator.getDocument().appendChild(classElement);

		generator.saveToFile("resources/projects.xml");
		System.out.println("XML file generated successfully!");
	}
}