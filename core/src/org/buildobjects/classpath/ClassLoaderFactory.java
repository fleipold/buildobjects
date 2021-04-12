package org.buildobjects.classpath;

import org.buildobjects.artifacts.Classes;

/**
 * User: fleipold
 * Date: Oct 20, 2008
 * Time: 12:37:44 PM
 */
public interface ClassLoaderFactory {
    ClassLoader getClassLoader(Classes classes, ClassLoader parentLoader);
}
