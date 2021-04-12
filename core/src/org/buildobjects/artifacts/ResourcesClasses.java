package org.buildobjects.artifacts;

import org.buildobjects.artifacts.resources.Resources;

/**
 * User: fleipold
 * Date: Oct 31, 2008
* //todo might go away...
  //if only i had commented on the why...
 **/
public class ResourcesClasses implements Classes {

    final Resources resources;

    public ResourcesClasses(Resources resources) {
        this.resources = resources;
    }


    public Resources getResources() {
        return resources;
    }
}
