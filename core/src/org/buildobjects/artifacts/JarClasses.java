package org.buildobjects.artifacts;

import org.buildobjects.artifacts.resources.Resources;
import org.buildobjects.artifacts.resources.ZipFileResources;

import java.io.File;

/**
 * User: fleipold
 * Date: Oct 12, 2008
 * Time: 10:41:48 AM
 */
public class JarClasses implements Classes {
    final File jarFile;

    boolean isRead = false;
    final Classes lazyDelegate;



    public JarClasses(File jarFile) {
        this.jarFile = jarFile;
        lazyDelegate = new ResourcesClasses(new ZipFileResources(jarFile));

        if (!jarFile.exists()){
            throw new IllegalArgumentException("File "+jarFile.getAbsolutePath() + " does not exist.");
        }
        if (!jarFile.isFile()){
            throw new IllegalArgumentException("File "+jarFile.getAbsolutePath() + " is not a file.");
        }

    }

    public JarClasses(String jarFile){
         this(new File(jarFile));
    }


    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JarClasses that = (JarClasses) o;

        if (!jarFile.equals(that.jarFile)) return false;

        return true;
    }

    public int hashCode() {
        return jarFile.hashCode();
    }


    public Resources getResources() {
        return lazyDelegate.getResources();
    }
}
             