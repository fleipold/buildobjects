package org.buildobjects.artifacts.resources;

import java.util.Collection;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

/**
 * Felix
 * Date: 05.06.2010
 * Time: 08:52:41
 */
public abstract class ResourceResourcesMixin implements Resource{
    public boolean hasResource(Path path) {
        return getPath().equals(path);
    }

    public Resource getResource(Path path) {
        if (getPath().equals(path)){
            return this;
        }
        return null;
    }

    public Collection<Resource> getAll() {
        return asList((Resource)this);
    }

    public Collection<Resource> getResourcesInPath(Path path) {
        if(getPath().startsWith(path) && getPath().removePrefix(path).getComponents().length == 1){
            return asList((Resource)this);
        }
        return emptyList();
    }

    public Collection<Resource> getResourcesInPathRescursively(Path path) {
        if(getPath().startsWith(path)){
            return asList((Resource)this);
        }
        return emptyList();
    }
}
