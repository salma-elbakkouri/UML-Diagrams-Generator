package org.mql.java.test;

import org.mql.java.models.ProjectModel;
import org.mql.java.scanners.Scanner;
import org.mql.java.xml.XMIConverter;
import org.mql.java.xml.XMLWriter;

public class Examples {

	public Examples() {
//		testScan();
//		testSaveToXML();
		testConvertToXMI();
		
	}

	public void testScan() {
		System.out.println("Running test: testScan");
		Scanner scanner = new Scanner();
		ProjectModel project = scanner.scan();
		if (project != null) {
			System.out.println("Scan successful:");
			System.out.println(project);
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
		Scanner scanner = new Scanner();
		ProjectModel project = scanner.scan();
		if (project != null) {
			String xmlFilePath = "resources/projects.xml";
			String xmiFilePath = xmlFilePath.replace(".xml", ".xmi");
			XMIConverter converter = new XMIConverter(xmiFilePath);
			converter.convert(project);
			System.out.println("Project converted to XMI successfully: " + xmiFilePath);
		} else {
			System.err.println("Project scan failed; cannot convert to XMI.");
		}
	}


	public static void main(String[] args) {
		new Examples();
	}
}
