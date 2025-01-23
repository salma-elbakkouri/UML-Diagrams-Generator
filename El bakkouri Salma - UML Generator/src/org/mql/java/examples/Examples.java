package org.mql.java.examples;

import java.util.List;

import org.mql.java.models.ProjectModel;
import org.mql.java.scanners.Scanner;
import org.mql.java.xml.XMIConverter;
import org.mql.java.xml.XMLParser;
import org.mql.java.xml.XMLWriter;

public class Examples {

    public Examples() {
//    	testScan();
//    	testSaveToXML();
//      testConvertToXMI();
    	testParseXML();
    }

    public void testScan() {
        System.out.println("Running test: testScan");
        Scanner scanner = new Scanner();
        ProjectModel project = scanner.scan();
        if (project != null) {
            System.out.println("Scan successful.");
        } else {
            System.err.println("Scan failed!");
        }
    }

    public void testSaveToXML() {
        System.out.println("Running test: testSaveToXML");
        Scanner scanner = new Scanner();
        ProjectModel project = scanner.scan();
        if (project != null) {
            String xmlFilePath = "resources/projects.xml";
            XMLWriter writer = new XMLWriter(xmlFilePath);
            writer.writeProject(project);
            System.out.println("Project saved to XML successfully: " + xmlFilePath);
        } else {
            System.err.println("Project scan failed; cannot save to XML.");
        }
    }

    public void testConvertToXMI() {
        System.out.println("Running test: testConvertToXMI");
        String xmlFilePath = "resources/projects.xml";
        XMIConverter converter = new XMIConverter(xmlFilePath.replace(".xml", ".xmi"));
        converter.convertFromXML(xmlFilePath);
        System.out.println("Project converted to XMI successfully.");
    }

    public void testParseXML() {
        System.out.println("Running test: testParseXML");
        String xmlFilePath = "resources/projects.xml";
        XMLParser parser = new XMLParser(xmlFilePath);
        List<ProjectModel> projects = parser.parseProjects();
        if (projects != null && !projects.isEmpty()) {
            System.out.println("Parsed Projects:");
            for (ProjectModel project : projects) {
                System.out.println(project);
            }
        } else {
            System.err.println("Failed to parse XML file.");
        }
    }

    public static void main(String[] args) {
        new Examples();
    }
}