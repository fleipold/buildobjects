package org.buildobjects.artifacts.resources;

import java.util.Collection;
import java.util.Collections;

/**
 * User: fleipold
 * Date: Nov 2, 2008
 * Time: 11:14:34 PM
 */
public class NullResources implements Resources {
    public boolean hasResource(Path path) {
        return false;
    }

    public Resource getResource(Path path) {
        return null;
    }

    public Collection<Resource> getAll() {
        return Collections.emptySet();
    }

    public Collection<Resource> getResourcesInPath(Path path) {
        return Collections.emptySet();
    }

    public Collection<Resource> getResourcesInPathRescursively(Path path) {
        return Collections.emptySet();
    }
}
