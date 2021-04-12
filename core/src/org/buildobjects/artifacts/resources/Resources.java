package org.buildobjects.artifacts.resources;


import java.util.Collection;

/**
 * User: fleipold
 * Date: Oct 31, 2008
 * Time: 2:34:11 PM
 */
public interface Resources {
    boolean hasResource(Path path);
        Resource getResource(Path path);
        Collection<Resource> getAll();
        Collection<Resource> getResourcesInPath(Path path);
        Collection<Resource> getResourcesInPathRescursively(Path path);
}
