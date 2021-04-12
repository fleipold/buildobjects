package org.buildobjects.artifacts;

import org.buildobjects.artifacts.resources.Resources;

import java.io.File;
import java.util.HashSet;
import java.util.Set;


public class JarDirClasses implements Classes {
    final File baseDir;
    Classes classes;


    public JarDirClasses(File folder) {
        if (!folder.exists()) {
            throw new RuntimeException("Library baseDir " + folder + " doesn't exist.");
        }
        if (!folder.isDirectory()) {
            throw new RuntimeException("Library baseDir " + folder + " is not a baseDir.");
        }

        this.baseDir = folder;
    }

    public JarDirClasses(String folder) {
        this(new File(folder));

    }


    private Classes figureOutClasses() {
        Set<Classes> returnValue = new HashSet<Classes>();

        File[] files = baseDir.listFiles();
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".jar")) {
                returnValue.add(new JarClasses(file));
            }
        }
        return new ClassesCombiner(returnValue);

    }

    public Classes getClasses() {
        if (classes == null) {
            classes = figureOutClasses();
        }
        return classes;
    }

    public Resources getResources() {
        return getClasses().getResources();
    }
}