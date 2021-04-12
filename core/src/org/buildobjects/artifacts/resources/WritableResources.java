package org.buildobjects.artifacts.resources;


/**
 * User: fleipold
 * Date: Oct 31, 2008
 * Time: 2:35:32 PM
 */
public interface WritableResources extends Resources {
    void add(Resource resource);
    void deleteResource(Path path);


}
