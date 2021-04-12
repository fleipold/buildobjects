package org.buildobjects.tasks;

import org.buildobjects.artifacts.TestResults;
import org.buildobjects.classpath.ClassLoaderFactory;

/**
 * User: fleipold
 * Date: Oct 31, 2008
 * Time: 1:21:29 PM
 */
public interface JUnit extends Task {
    void setClassLoaderFactory(ClassLoaderFactory factory);

    TestResults results();
}
