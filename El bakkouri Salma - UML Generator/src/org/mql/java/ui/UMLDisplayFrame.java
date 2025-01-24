package org.mql.java.ui;

import java.awt.BorderLayout;
import java.io.File;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import org.mql.java.models.ProjectModel;
import org.mql.java.services.Scanner;
import org.mql.java.xml.XMIConverter;
import org.mql.java.xml.XMLParser;
import org.mql.java.xml.XMLWriter;

public class UMLDisplayFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    public UMLDisplayFrame(String projectPath) throws ClassNotFoundException {
        setTitle("Class Diagram Viewer");
        setSize(1000, 650);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        Scanner scanner = new Scanner(projectPath);
        System.out.println("UI Path: " + projectPath);
        File binFolder = new File(projectPath + File.separator + "bin");
        
        System.out.println("Bin Folder Exists: " + binFolder.exists());
        ProjectModel project = scanner.scan();

        if (project != null) {
            String xmlFilePath = "resources/projects.xml";
            XMLWriter writer = new XMLWriter(xmlFilePath);
            writer.writeProject(project); 

            XMLParser parser = new XMLParser(xmlFilePath);
            List<ProjectModel> projects = parser.parseProjects();

            if (projects != null && !projects.isEmpty()) {
                ProjectModel latestProject = projects.get(projects.size() - 1);

                ClassDiagramPanel diagramPanel = new ClassDiagramPanel(latestProject);
                JScrollPane scrollPane = new JScrollPane(diagramPanel);
                add(scrollPane, BorderLayout.CENTER);
            } else {
                System.err.println("Failed to parse XML file or no projects found.");
            }
        } else {
            System.err.println("Scan failed!");
        }

        setLocationRelativeTo(null);
        setVisible(true);
    }
}