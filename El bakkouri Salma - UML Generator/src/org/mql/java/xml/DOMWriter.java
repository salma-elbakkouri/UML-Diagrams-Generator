package org.mql.java.xml;

import org.mql.java.models.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

public class DOMWriter {

    private String filePath;

    public DOMWriter(String filePath) {
        this.filePath = filePath;
    }

    public void writeProject(ProjectModel project) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc;

            if (Files.exists(Paths.get(filePath))) {
                doc = builder.parse(new File(filePath));
            } else {
                doc = builder.newDocument();
                Element rootElement = doc.createElement("projects");
                doc.appendChild(rootElement);
            }

            Element rootElement = doc.getDocumentElement();
            Element projectElement = doc.createElement("project");
            projectElement.setAttribute("path", project.getName());
            projectElement.setAttribute("name", project.getName());
            writePackages(doc, projectElement, project.getPackages());
            rootElement.appendChild(projectElement);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filePath));
            transformer.transform(source, result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writePackages(Document doc, Element projectElement, List<PackageModel> packages) {
        Element packagesElement = doc.createElement("packages");
        for (PackageModel pkg : packages) {
            Element packageElement = doc.createElement("package");
            packageElement.setAttribute("path", pkg.getName());
            packageElement.setAttribute("name", pkg.getName());
            writeClasses(doc, packageElement, pkg.getClasses());
            packagesElement.appendChild(packageElement);
        }
        projectElement.appendChild(packagesElement);
    }

    private void writeClasses(Document doc, Element packageElement, List<ClassModel> classes) {
        for (ClassModel cls : classes) {
            Element classElement = doc.createElement("class");
            classElement.setAttribute("name", cls.getName());
            classElement.setAttribute("type", cls.getType());
            writeModifiers(doc, classElement, cls.getModifiers());
            writeFields(doc, classElement, cls.getFields());
            writeConstructors(doc, classElement, cls.getConstructors());
            writeMethods(doc, classElement, cls.getMethods());
            writeRelationships(doc, classElement, cls.getRelationships());

            packageElement.appendChild(classElement);
        }
    }

    private void writeModifiers(Document doc, Element classElement, List<String> modifiers) {
        Element modifiersElement = doc.createElement("modifiers");
        for (String modifier : modifiers) {
            Element modifierElement = doc.createElement("modifier");
            modifierElement.setTextContent(modifier);
            modifiersElement.appendChild(modifierElement);
        }
        classElement.appendChild(modifiersElement);
    }

    private void writeFields(Document doc, Element classElement, List<FieldModel> fields) {
        Element fieldsElement = doc.createElement("fields");
        for (FieldModel field : fields) {
            Element fieldElement = doc.createElement("field");
            fieldElement.setAttribute("name", field.getName());
            fieldElement.setAttribute("type", field.getType());

            writeModifiers(doc, fieldElement, field.getModifiers());
            fieldsElement.appendChild(fieldElement);
        }
        classElement.appendChild(fieldsElement);
    }

    private void writeConstructors(Document doc, Element classElement, List<ConstructorModel> constructors) {
        Element constructorsElement = doc.createElement("constructors");
        for (ConstructorModel constructor : constructors) {
            Element constructorElement = doc.createElement("constructor");
            writeModifiers(doc, constructorElement, constructor.getModifiers());
            constructorsElement.appendChild(constructorElement);
        }
        classElement.appendChild(constructorsElement);
    }

    private void writeMethods(Document doc, Element classElement, List<MethodModel> methods) {
        Element methodsElement = doc.createElement("methods");
        for (MethodModel method : methods) {
            Element methodElement = doc.createElement("method");
            methodElement.setAttribute("name", method.getName());
            methodElement.setAttribute("returnType", method.getReturnType());

            writeModifiers(doc, methodElement, method.getModifiers());
            methodsElement.appendChild(methodElement);
        }
        classElement.appendChild(methodsElement);
    }

    private void writeRelationships(Document doc, Element classElement, Set<RelationModel> relationships) {
        Element relationshipsElement = doc.createElement("relationships");
        for (RelationModel relation : relationships) {
            Element relationshipElement = doc.createElement("relationship");
            relationshipElement.setAttribute("sourceClass", relation.getSourceClass());
            relationshipElement.setAttribute("targetClass", relation.getTargetClass());
            relationshipElement.setAttribute("type", relation.getRelationType().toString());
            relationshipsElement.appendChild(relationshipElement);
        }
        classElement.appendChild(relationshipsElement);
    }
}