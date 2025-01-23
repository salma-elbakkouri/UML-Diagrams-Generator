package org.mql.java.xml;

import org.mql.java.models.*;
import org.mql.java.enums.RelationType;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.*;

public class XMIConverter {

	private String filePath;

	public XMIConverter(String filePath) {
		this.filePath = filePath;
	}

	public void convertFromXML(String xmlFilePath) {
		try {
			File inputFile = new File(xmlFilePath);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(inputFile);
			doc.getDocumentElement().normalize();

			NodeList projectNodes = doc.getElementsByTagName("project");
			if (projectNodes.getLength() > 0) {
				Element projectElement = (Element) projectNodes.item(0);
				convert(projectElement);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void convert(Element projectElement) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.newDocument();

			Element xmiRoot = doc.createElement("xmi:XMI");
			xmiRoot.setAttribute("xmlns:xmi", "http://www.omg.org/XMI");
			xmiRoot.setAttribute("xmlns:uml", "http://www.omg.org/spec/UML/20090901");
			doc.appendChild(xmiRoot);

			String projectName = projectElement.getAttribute("name");
			writeXMIProject(doc, xmiRoot, projectName, projectElement);

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(filePath));
			transformer.transform(source, result);

			System.out.println("XMI file created successfully: " + filePath);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void writeXMIProject(Document doc, Element xmiRoot, String projectName, Element projectElement) {
		Element umlModel = doc.createElement("uml:Model");
		umlModel.setAttribute("xmi:type", "uml:Model");
		umlModel.setAttribute("name", projectName);
		xmiRoot.appendChild(umlModel);

		NodeList packagesNodes = projectElement.getElementsByTagName("packages");
		if (packagesNodes.getLength() > 0) {
			Element packagesElement = (Element) packagesNodes.item(0);
			writeXMIPackages(doc, umlModel, packagesElement);
		}
	}

	private void writeXMIPackages(Document doc, Element umlModel, Element packagesElement) {
		NodeList packageNodes = packagesElement.getElementsByTagName("package");
		for (int i = 0; i < packageNodes.getLength(); i++) {
			Element packageElement = (Element) packageNodes.item(i);
			writeXMIClasses(doc, umlModel, packageElement);
		}
	}

	private void writeXMIClasses(Document doc, Element umlModel, Element packageElement) {
		String packageName = packageElement.getAttribute("name");
		Element umlPackage = doc.createElement("packagedElement");
		umlPackage.setAttribute("xmi:type", "uml:Package");
		umlPackage.setAttribute("name", packageName);
		umlModel.appendChild(umlPackage);

		NodeList classNodes = packageElement.getElementsByTagName("class");
		for (int i = 0; i < classNodes.getLength(); i++) {
			Element classElement = (Element) classNodes.item(i);
			writeXMIClass(doc, umlPackage, classElement);
		}
	}

	private void writeXMIClass(Document doc, Element umlPackage, Element classElement) {
		String className = classElement.getAttribute("name");
		Element umlClass = doc.createElement("packagedElement");
		umlClass.setAttribute("xmi:type", "uml:Class");
		umlClass.setAttribute("name", className);

		writeXMIFields(doc, umlClass, classElement);
		writeXMIMethods(doc, umlClass, classElement);
		writeXMIRelationships(doc, umlClass, classElement);

		umlPackage.appendChild(umlClass);
	}

	private void writeXMIFields(Document doc, Element umlClass, Element classElement) {
		NodeList fieldsNodes = classElement.getElementsByTagName("fields");
		if (fieldsNodes.getLength() > 0) {
			Element fieldsElement = (Element) fieldsNodes.item(0);
			NodeList fieldNodes = fieldsElement.getElementsByTagName("field");
			for (int i = 0; i < fieldNodes.getLength(); i++) {
				Element fieldElement = (Element) fieldNodes.item(i);
				Element umlProperty = doc.createElement("ownedAttribute");
				umlProperty.setAttribute("name", fieldElement.getAttribute("name"));
				umlProperty.setAttribute("type", fieldElement.getAttribute("type"));
				umlClass.appendChild(umlProperty);
			}
		}
	}

	private void writeXMIMethods(Document doc, Element umlClass, Element classElement) {
		NodeList methodsNodes = classElement.getElementsByTagName("methods");
		if (methodsNodes.getLength() > 0) {
			Element methodsElement = (Element) methodsNodes.item(0);
			NodeList methodNodes = methodsElement.getElementsByTagName("method");
			for (int i = 0; i < methodNodes.getLength(); i++) {
				Element methodElement = (Element) methodNodes.item(i);
				Element umlOperation = doc.createElement("ownedOperation");
				umlOperation.setAttribute("name", methodElement.getAttribute("name"));
				umlOperation.setAttribute("returnType", methodElement.getAttribute("returnType"));
				umlClass.appendChild(umlOperation);
			}
		}
	}

	private void writeXMIRelationships(Document doc, Element umlClass, Element classElement) {
		NodeList relationshipsNodes = classElement.getElementsByTagName("relationships");
		if (relationshipsNodes.getLength() > 0) {
			Element relationshipsElement = (Element) relationshipsNodes.item(0);
			NodeList relationshipNodes = relationshipsElement.getElementsByTagName("relationship");
			for (int i = 0; i < relationshipNodes.getLength(); i++) {
				Element relationshipElement = (Element) relationshipNodes.item(i);
				Element umlAssociation = doc.createElement("association");
				umlAssociation.setAttribute("source", relationshipElement.getAttribute("sourceClass"));
				umlAssociation.setAttribute("target", relationshipElement.getAttribute("targetClass"));
				umlAssociation.setAttribute("type", relationshipElement.getAttribute("type"));
				umlClass.appendChild(umlAssociation);
			}
		}
	}
}
