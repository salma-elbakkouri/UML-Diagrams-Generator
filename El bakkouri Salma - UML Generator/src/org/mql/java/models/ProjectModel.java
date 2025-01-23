package org.mql.java.models;

import java.util.List;

import org.mql.java.util.ConsoleDisplay;

public class ProjectModel {
	private String name;
	private List<PackageModel> packages;

	public ProjectModel(String name) {
		this.name = name;
	}

	public ProjectModel() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<PackageModel> getPackages() {
		return packages;
	}

	public void setPackages(List<PackageModel> packages) {
		this.packages = packages;
	}

	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("Project: ").append(name).append("\n");
	    sb.append(getSummary()).append("\n");

	    if (!packages.isEmpty()) {
	        packages.forEach(pkg -> sb.append(ConsoleDisplay.displayPackage(pkg, 1)));
	    }

	    return sb.toString();
	}

	private String getSummary() {
	    int packageCount = packages != null ? packages.size() : 0;
	    int classCount = 0;
	    int fieldCount = 0;
	    int methodCount = 0;
	    int constructorCount = 0;
	    int relationshipCount = 0;

	    if (packages != null) {
	        for (PackageModel pkg : packages) {
	            if (pkg.getClasses() != null) {
	                classCount += pkg.getClasses().size();
	                for (ClassModel cls : pkg.getClasses()) {
	                    if (cls.getFields() != null) {
	                        fieldCount += cls.getFields().size();
	                    }
	                    if (cls.getMethods() != null) {
	                        methodCount += cls.getMethods().size();
	                    }
	                    if (cls.getConstructors() != null) {
	                        constructorCount += cls.getConstructors().size();
	                    }
	                    if (cls.getRelationships() != null) {
	                        relationshipCount += cls.getRelationships().size();
	                    }
	                }
	            }
	        }
	    }

	    return String.format(
	        "Summary:\n" +
	        "  Packages: %d\n" +
	        "  Classes: %d\n" +
	        "  Fields: %d\n" +
	        "  Methods: %d\n" +
	        "  Constructors: %d\n" +
	        "  Relationships: %d\n",
	        packageCount, classCount, fieldCount, methodCount, constructorCount, relationshipCount
	    );
	}
}
