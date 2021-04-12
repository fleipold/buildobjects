package org.buildobjects.compiler;

import org.buildobjects.artifacts.resources.Path;
import org.buildobjects.artifacts.resources.Resource;
import org.buildobjects.artifacts.resources.ResourceResourcesMixin;

/**
 * User: fleipold
 * Date: Oct 31, 2008
 * Time: 4:30:08 PM
 */
public class InMemoryFileWrappingResource extends ResourceResourcesMixin implements Resource {
    final private Path path;
    private final InMemoryWriteFileObject file;

    public InMemoryFileWrappingResource(String fullyQualifiedName, InMemoryWriteFileObject file) {
        this.file = file;
        this.path = new Path(fullyQualifiedName.replace(".","/")+".class");
    }

    public Path getPath() {
        return path;
    }

    public byte[] getBytes() {
        return file.getContent();
    }

    public String getString() {
        return new String(getBytes());
    }

    
}
