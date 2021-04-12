package org.buildobjects.artifacts.resources;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * User: fleipold
 * Date: Oct 31, 2008
 * Time: 2:38:52 PM
 */
public class InMemoryResource extends ResourceResourcesMixin implements Resource {
    private final Path path;
    private final byte[] bytes;

    public static Resource resource(Path path, byte[] contents){
        return new InMemoryResource(path, contents);
    }

    public static Resource resource(Path path, String contents){
        return new InMemoryResource(path, contents);
    }


    public InMemoryResource(Path path, byte[] contents) {
        this.path = path;
        if (path.getName().equals("package-info.java")){
            System.out.println("fsakdljflksajd");
        }
        this.bytes = contents;
    }

    public InMemoryResource(Path path, String contents) {
        this.path = path;
        this.bytes = contents.getBytes();
    }

    public InMemoryResource(Path path, InputStream is) {
            this.path = path;
        try {
            this.bytes = IOUtils.toByteArray(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Path getPath() {
        return path;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public String getString() {
        return new String(bytes);
    }
}
