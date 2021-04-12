package org.buildobjects.artifacts;

import org.buildobjects.artifacts.resources.Resources;

/**
 * User: fleipold
 * Date: Nov 3, 2008
 * Time: 5:33:25 PM
 */
public class ResourcesSources implements Sources {
    final private Resources resources;

    public ResourcesSources(Resources resources) {
        this.resources = resources;
    }


    public String getName() {
        return "Anonymous Sources";
    }

    public Resources getResources() {
        return resources;
    }
}
