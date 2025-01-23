package org.mql.java.xml;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.mql.java.models.ClassModel;
import org.mql.java.models.FieldModel;
import org.mql.java.models.MethodModel;
import org.mql.java.models.PackageModel;
import org.mql.java.models.ProjectModel;
import org.mql.java.models.RelationModel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMIConverter {

	private String filePath;

	public XMIConverter(String filePath) {
		super();
		this.filePath = filePath;
	}

	public void convert(ProjectModel project) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc;

			doc = builder.newDocument();

			Element xmiRoot = doc.createElement("xmi:XMI");
			xmiRoot.setAttribute("xmlns:xmi", "http://www.omg.org/XMI");
			xmiRoot.setAttribute("xmlns:uml", "http://www.omg.org/spec/UML/20090901");
			doc.appendChild(xmiRoot);

			writeXMIProject(doc, xmiRoot, project);

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(filePath.replace(".xml", ".xmi")));
			transformer.transform(source, result);

			System.out.println("XMI file created successfully: " + filePath.replace(".xml", ".xmi"));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void writeXMIProject(Document doc, Element xmiRoot, ProjectModel project) {
		Element umlModel = doc.createElement("uml:Model");
		umlModel.setAttribute("xmi:type", "uml:Model");
		umlModel.setAttribute("name", project.getName());
		xmiRoot.appendChild(umlModel);

		writeXMIPackages(doc, umlModel, project.getPackages());
	}

	private void writeXMIPackages(Document doc, Element umlModel, List<PackageModel> packages) {
		for (PackageModel pkg : packages) {
			Element umlPackage = doc.createElement("packagedElement");
			umlPackage.setAttribute("xmi:type", "uml:Package");
			umlPackage.setAttribute("name", pkg.getName());
			umlModel.appendChild(umlPackage);

			writeXMIClasses(doc, umlPackage, pkg.getClasses());
		}
	}

	private void writeXMIClasses(Document doc, Element umlPackage, List<ClassModel> classes) {
		for (ClassModel cls : classes) {
			Element umlClass = doc.createElement("packagedElement");
			umlClass.setAttribute("xmi:type", "uml:Class");
			umlClass.setAttribute("name", cls.getName());

			writeXMIFields(doc, umlClass, cls.getFields());
			writeXMIMethods(doc, umlClass, cls.getMethods());
			writeXMIRelationships(doc, umlClass, cls.getRelationships());

			umlPackage.appendChild(umlClass);
		}
	}

	private void writeXMIFields(Document doc, Element umlClass, List<FieldModel> fields) {
		for (FieldModel field : fields) {
			Element umlProperty = doc.createElement("ownedAttribute");
			umlProperty.setAttribute("name", field.getName());
			umlProperty.setAttribute("type", field.getType());
			umlClass.appendChild(umlProperty);
		}
	}

	private void writeXMIMethods(Document doc, Element umlClass, List<MethodModel> methods) {
		for (MethodModel method : methods) {
			Element umlOperation = doc.createElement("ownedOperation");
			umlOperation.setAttribute("name", method.getName());
			umlOperation.setAttribute("returnType", method.getReturnType());
			umlClass.appendChild(umlOperation);
		}
	}

	private void writeXMIRelationships(Document doc, Element umlClass, Set<RelationModel> relationships) {
		for (RelationModel relation : relationships) {
			Element umlAssociation = doc.createElement("association");
			umlAssociation.setAttribute("source", relation.getSourceClass());
			umlAssociation.setAttribute("target", relation.getTargetClass());
			umlAssociation.setAttribute("type", relation.getRelationType().toString());
			umlClass.appendChild(umlAssociation);
		}
	}

}
