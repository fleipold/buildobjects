package org.buildobjects.artifacts;

import org.buildobjects.artifacts.resources.FileSystemResources;
import org.buildobjects.artifacts.resources.Resources;

import java.io.File;

/**
 * User: fleipold
 * Date: Oct 22, 2008
 * Time: 3:19:33 PM
 */
public class ClassFolderClasses implements Classes{

    private File baseDir;
    private ResourcesClasses classesDelegate;

    public ClassFolderClasses(File baseDir) {
        if (!baseDir.isDirectory()){
            throw new IllegalArgumentException("Need a directory, but got a file.");
        }
        this.baseDir = baseDir;
        FileSystemResources fileResources = new FileSystemResources(this.baseDir);
        classesDelegate = new ResourcesClasses(fileResources);
    }


    public Resources getResources() {
        return classesDelegate.getResources();

    }
}
