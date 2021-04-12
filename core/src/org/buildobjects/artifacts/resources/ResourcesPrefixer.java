package org.buildobjects.artifacts.resources;

import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Transformer;

import java.util.Collection;

public class ResourcesPrefixer implements Resources{
    private final Path prefix;
    private final Resources resources;

    public ResourcesPrefixer(Path prefix, Resources resources) {
        this.prefix = prefix;
        this.resources = resources;
    }

    public boolean hasResource(Path path) {
        if (!path.startsWith(prefix)){
            return false;
        }
        return resources.hasResource(path.removePrefix(prefix));
    }

    public Resource getResource(Path path) {
        return new ResourcePathPrefixer(prefix, resources.getResource(path.removePrefix(prefix)));
    }

    public Collection<Resource> getAll() {
        return wrapCollection(resources.getAll());
    }

    private Collection<Resource> wrapCollection(Collection<Resource> resourceCollection) {
        return CollectionUtils.collect(resourceCollection, new Transformer<Resource, Resource>() {
            public Resource transform(Resource resource) {
                return new ResourcePathPrefixer(prefix, resource);
            }
        });
    }

    public Collection<Resource> getResourcesInPath(Path path) {
        return wrapCollection(resources.getResourcesInPath(path.removePrefix(prefix)));
    }

    public Collection<Resource> getResourcesInPathRescursively(Path path) {
        return wrapCollection(resources.getResourcesInPathRescursively(path.removePrefix(prefix)));
    }
}
