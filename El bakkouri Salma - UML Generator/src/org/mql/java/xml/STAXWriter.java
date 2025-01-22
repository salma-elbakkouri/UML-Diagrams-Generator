package org.mql.java.xml;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.mql.java.models.ClassModel;
import org.mql.java.models.ConstructorModel;
import org.mql.java.models.FieldModel;
import org.mql.java.models.MethodModel;
import org.mql.java.models.PackageModel;
import org.mql.java.models.ProjectModel;
import org.mql.java.models.RelationModel;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

public class STAXWriter {

	private String filePath;

	public STAXWriter(String filePath) {
		this.filePath = filePath;
	}

	public void writeProject(ProjectModel project) {
		try {
			boolean fileExists = Files.exists(Paths.get(filePath));

			FileWriter fileWriter = new FileWriter(filePath, true);
			XMLOutputFactory factory = XMLOutputFactory.newInstance();
			XMLStreamWriter writer = factory.createXMLStreamWriter(fileWriter);

			if (!fileExists) {
				writer.writeStartDocument("UTF-8", "1.0");
				writer.writeCharacters("\n");
				writer.writeStartElement("projects");
				writer.writeCharacters("\n");
			}

			writer.writeStartElement("project");
			writer.writeAttribute("path", project.getName());
			writePackages(writer, project.getPackages());

			writer.writeEndElement();
			writer.writeCharacters("\n");
		
			writer.flush();
			writer.close();
		} catch (IOException | XMLStreamException e) {
			e.printStackTrace();
		}
	}

	private void writePackages(XMLStreamWriter writer, List<PackageModel> packages) throws XMLStreamException {
		writer.writeStartElement("packages");
		for (PackageModel pkg : packages) {
			writer.writeStartElement("package");
			writer.writeAttribute("path", pkg.getName());

			writeClasses(writer, pkg.getClasses());
			writer.writeEndElement();
		}
		writer.writeEndElement();
	}

	private void writeClasses(XMLStreamWriter writer, List<ClassModel> classes) throws XMLStreamException {
		for (ClassModel cls : classes) {
			writer.writeStartElement("class");
			writer.writeAttribute("name", cls.getName());
			writer.writeAttribute("type", cls.getType());

			writeModifiers(writer, cls.getModifiers());
			writeFields(writer, cls.getFields());
			writeConstructors(writer, cls.getConstructors());
			writeMethods(writer, cls.getMethods());
			writeRelationships(writer, cls.getRelationships());

			writer.writeEndElement();
		}
	}

	private void writeModifiers(XMLStreamWriter writer, List<String> modifiers) throws XMLStreamException {
		writer.writeStartElement("modifiers");
		for (String modifier : modifiers) {
			writer.writeStartElement("modifier");
			writer.writeCharacters(modifier);
			writer.writeEndElement();
		}
		writer.writeEndElement();
	}

	private void writeFields(XMLStreamWriter writer, List<FieldModel> fields) throws XMLStreamException {
		writer.writeStartElement("fields");
		for (FieldModel field : fields) {
			writer.writeStartElement("field");
			writer.writeAttribute("name", field.getName());
			writer.writeAttribute("type", field.getType());

			writeModifiers(writer, field.getModifiers());
			writer.writeEndElement();
		}
		writer.writeEndElement();
	}

	private void writeConstructors(XMLStreamWriter writer, List<ConstructorModel> constructors)
			throws XMLStreamException {
		writer.writeStartElement("constructors");
		for (ConstructorModel constructor : constructors) {
			writer.writeStartElement("constructor");

			writeModifiers(writer, constructor.getModifiers());
			writer.writeEndElement();
		}
		writer.writeEndElement();
	}

	private void writeMethods(XMLStreamWriter writer, List<MethodModel> methods) throws XMLStreamException {
		writer.writeStartElement("methods");
		for (MethodModel method : methods) {
			writer.writeStartElement("method");
			writer.writeAttribute("name", method.getName());
			writer.writeAttribute("returnType", method.getReturnType());

			// Write method modifiers
			writeModifiers(writer, method.getModifiers());

			writer.writeEndElement(); // Close </method>
		}
		writer.writeEndElement(); // Close </methods>
	}

	private void writeRelationships(XMLStreamWriter writer, Set<RelationModel> relationships)
			throws XMLStreamException {
		writer.writeStartElement("relationships");
		for (RelationModel relation : relationships) {
			writer.writeEmptyElement("relationship");
			writer.writeAttribute("sourceClass", relation.getSourceClass());
			writer.writeAttribute("targetClass", relation.getTargetClass());
			writer.writeAttribute("type", relation.getRelationType().toString());
		}
		writer.writeEndElement();
	}
	
	
}