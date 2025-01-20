package org.mql.java.scanners;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.mql.java.models.PackageModel;

public class PackageExplorer {
	public List<PackageModel> explorePackages(File folder, String packageName) {
        List<PackageModel> packages = new ArrayList<>();

        File[] files = folder.listFiles();
        if (files == null) return packages;

        for (File file : files) {
            if (file.isDirectory()) {
                String newPackageName = packageName.isEmpty() ? file.getName() : packageName + "." + file.getName();
                PackageModel packageModel = new PackageModel(newPackageName);
                ClassExplorer classExplorer = new ClassExplorer();
                packageModel.setClasses(classExplorer.extractClasses(file, newPackageName));
                packages.add(packageModel);
                packages.addAll(explorePackages(file, newPackageName));
            }
        }
        return packages;
    }
}
