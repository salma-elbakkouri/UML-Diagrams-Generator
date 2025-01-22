package org.mql.java.util;

import java.util.List;
import java.util.stream.Collectors;

import org.mql.java.models.ClassModel;
import org.mql.java.models.PackageModel;
import org.mql.java.models.RelationModel;

public class ConsoleDisplay {

    public static String displayPackage(PackageModel pkg, int indentLevel) {
        StringBuilder sb = new StringBuilder();
        String indent = getIndent(indentLevel);

        sb.append(indent).append("|__ Package: ").append(pkg.getName()).append("\n");
        pkg.getClasses().forEach(cls -> sb.append(displayClass(cls, indentLevel + 1)));

        return sb.toString();
    }

    public static String displayClass(ClassModel cls, int indentLevel) {
        StringBuilder sb = new StringBuilder();
        String indent = getIndent(indentLevel);

        sb.append(indent).append("|__ Class: ")
          .append(String.join(" ", cls.getModifiers())).append(" ")
          .append(cls.getType()).append(": ").append(cls.getName()).append("\n");

        if (!cls.getConstructors().isEmpty()) {
            sb.append(displaySection("Constructors", cls.getConstructors(), indentLevel + 1));
        }

        if (!cls.getFields().isEmpty()) {
            sb.append(displaySection("Fields", cls.getFields(), indentLevel + 1));
        }

        if (!cls.getMethods().isEmpty()) {
            sb.append(displaySection("Methods", cls.getMethods(), indentLevel + 1));
        }

        if (!cls.getRelationships().isEmpty()) {
            sb.append(displayRelationships(cls.getName(), List.copyOf(cls.getRelationships()), indentLevel + 1));
        }

        sb.append("\n");

        return sb.toString();
    }

    public static String displaySection(String title, List<?> items, int indentLevel) {
        StringBuilder sb = new StringBuilder();
        String indent = getIndent(indentLevel);

        sb.append(indent).append("|__ ").append(title).append(":\n");
        items.forEach(item -> sb.append(indent).append("    ").append(item).append("\n"));

        sb.append("\n");

        return sb.toString();
    }

    public static String displayRelationships(String sourceName, List<RelationModel> relationships, int indentLevel) {
        StringBuilder sb = new StringBuilder();
        String indent = getIndent(indentLevel);

        sb.append(indent).append("|__ Relationships:\n");
        relationships.stream()
            .collect(Collectors.groupingBy(RelationModel::getRelationType))
            .forEach((type, rels) -> {
                String targets = rels.stream()
                    .map(RelationModel::getTargetClass)
                    .collect(Collectors.joining(", "));
                sb.append(indent).append("    - ").append(sourceName).append(" ").append(type).append(": ").append(targets).append("\n");
            });

        sb.append("\n");

        return sb.toString();
    }

    private static String getIndent(int level) {
        return "    ".repeat(level);
    }
}