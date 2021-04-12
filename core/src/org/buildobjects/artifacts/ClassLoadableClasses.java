package org.buildobjects.artifacts;

import org.buildobjects.artifacts.resources.Resources;

import java.io.File;

/**
 * User: fleipold
 * Date: Oct 22, 2008
 * Time: 3:37:36 PM
 */
public class ClassLoadableClasses implements Classes {
    Classes delegate;

    public ClassLoadableClasses(File file) {
        if (file.isDirectory()){
            delegate = new ClassFolderClasses(file);
        } else {
            delegate = new JarClasses(file

            );
        }
        
    }

    public Resources getResources() {
        return delegate.getResources();
    }
}
