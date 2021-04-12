package org.buildobjects.artifacts;


import org.buildobjects.artifacts.resources.NullResources;
import org.buildobjects.artifacts.resources.Resources;

/**
 * User: fleipold
 * Date: Oct 14, 2008
 * Time: 8:18:16 PM
 */
public class NullClasses implements Classes{

    public Resources getResources() {
        return new NullResources();
    }
}
