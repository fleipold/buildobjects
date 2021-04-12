package org.buildobjects.tasks;

import org.buildobjects.artifacts.Classes;

/**
 * User: fleipold
 * Date: Oct 31, 2008
 * Time: 1:19:03 PM
 */
public interface JavaC extends Task {

    Classes output();
    Classes outputDep();
    void setPlatformClassPath(Classes platformClassPath);
}
