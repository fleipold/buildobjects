package org.buildobjects.artifacts.resources;

public class ResourcePathPrefixer extends ResourceResourcesMixin implements Resource {
    private final Path prefix;
    private final Resource resource;

    public ResourcePathPrefixer(Path prefix, Resource resource) {
        this.prefix = prefix;
        this.resource = resource;
    }

    public Path getPath() {
        return prefix.concatenate(resource.getPath());
    }

    public byte[] getBytes() {
        return resource.getBytes();
    }

    public String getString() {
        return resource.getString();
    }
}
