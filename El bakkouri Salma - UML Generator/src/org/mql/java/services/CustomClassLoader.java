package org.mql.java.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CustomClassLoader extends ClassLoader {
	private final String rootBinDirectory;

	public CustomClassLoader(String rootBinDirectory) {
		this.rootBinDirectory = rootBinDirectory;
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		try {
			String path = rootBinDirectory + File.separator + "bin" + File.separator
					+ name.replace('.', File.separatorChar) + ".class";
			byte[] classData = Files.readAllBytes(Paths.get(path));
			return defineClass(name, classData, 0, classData.length);
		} catch (IOException e) {
			throw new ClassNotFoundException("Class " + name + " not found in " + rootBinDirectory, e);
		}
	}
}