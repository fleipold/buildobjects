package org.buildobjects.artifacts;

import org.buildobjects.artifacts.resources.FileSystemResources;
import org.buildobjects.artifacts.resources.Resources;

import java.io.File;
import java.util.List;

/**
 * User: fleipold
 * Date: Sep 30, 2008
 * Time: 6:11:44 PM
 */
public class FileSources implements Sources {
    final File baseDir;
    List<File> javaFiles;
    List<File> otherFiles;
    final private String name;
    FileSystemResources resources;


    public FileSources(File baseDir, String name) {
        this.name = name;
        if (!baseDir.exists()) {
            throw new IllegalArgumentException("Not a valid source baseDir " + baseDir.getAbsolutePath());
        }
        this.baseDir = baseDir;
        resources = new FileSystemResources(this.baseDir);
    }


    public FileSources(File baseDir) {
            this(baseDir, baseDir.getName());
        }

    public FileSources(String baseDir, String name) {
        this(new File(baseDir), name);
    }

    public File getBaseDir() {
        return baseDir;
    }


    public String getName() {
        return name;
    }

    public Resources getResources() {
        return resources;
    }


}
