package org.mql.java.util;

import java.util.List;
import java.util.stream.Collectors;

import org.mql.java.models.ClassModel;
import org.mql.java.models.ConstructorModel;
import org.mql.java.models.FieldModel;
import org.mql.java.models.MethodModel;
import org.mql.java.models.PackageModel;
import org.mql.java.models.RelationModel;

public class ConsoleDisplay {

    public static String displayPackage(PackageModel pkg, int indentLevel) {
        StringBuilder sb = new StringBuilder();
        String indent = getIndent(indentLevel);

        // Display package name only if it has classes
        if (pkg.getClasses() != null && !pkg.getClasses().isEmpty()) {
            sb.append(indent).append("Package: ").append(pkg.getName()).append("\n");

            // Display each class in the package
            for (ClassModel cls : pkg.getClasses()) {
                sb.append(displayClass(cls, indentLevel + 1));
            }
        } else {
            // If the package is empty, just display its name
            sb.append(indent).append("Package: ").append(pkg.getName()).append(" (empty)\n");
        }

        return sb.toString();
    }

    public static String displayClass(ClassModel cls, int indentLevel) {
        StringBuilder sb = new StringBuilder();
        String indent = getIndent(indentLevel);

        // Display class name with modifiers and type
        sb.append(indent).append("|__ Class: ")
          .append(String.join(" ", cls.getModifiers())).append(" ")
          .append(cls.getType()).append(" ").append(cls.getName()).append("\n");

        // Display methods
        if (!cls.getMethods().isEmpty()) {
            sb.append(indent).append("    |__ Methods:\n");
            for (MethodModel method : cls.getMethods()) {
                sb.append(indent).append("        |__ ").append(method).append("\n");
            }
        }

        // Display fields
        if (!cls.getFields().isEmpty()) {
            sb.append(indent).append("    |__ Fields:\n");
            for (FieldModel field : cls.getFields()) {
                sb.append(indent).append("        |__ ").append(field).append("\n");
            }
        }

        // Display constructors
        if (!cls.getConstructors().isEmpty()) {
            sb.append(indent).append("    |__ Constructors:\n");
            for (ConstructorModel constructor : cls.getConstructors()) {
                sb.append(indent).append("        |__ ").append(constructor).append("\n");
            }
        }

        // Display relationships (grouped by type)
        if (!cls.getRelationships().isEmpty()) {
            sb.append(indent).append("    |__ Relationships:\n");
            cls.getRelationships().stream()
                .collect(Collectors.groupingBy(RelationModel::getRelationType))
                .forEach((type, rels) -> {
                    String targets = rels.stream()
                        .map(RelationModel::getTargetClass)
                        .collect(Collectors.joining(", "));
                    sb.append(indent).append("        |__ ").append(type).append(": ").append(targets).append("\n");
                });
        }

        return sb.toString();
    }

    private static String getIndent(int level) {
        return "    ".repeat(level);
    }

    
}