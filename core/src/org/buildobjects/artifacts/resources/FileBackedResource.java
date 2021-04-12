package org.buildobjects.artifacts.resources;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * User: fleipold
 * Date: Oct 31, 2008
 * Time: 5:51:29 PM
 */
public class FileBackedResource extends ResourceResourcesMixin implements Resource {
    private final Path path;
    private final File file;
    private byte[] bytes;

    public FileBackedResource(Path path, File file) {
        this.path = path;

        this.file = file;
    }

    public Path getPath() {
        return path;
    }

    public byte[] getBytes() {
        if (bytes == null){
            try {
                final FileInputStream inputStream = new FileInputStream(file);
                bytes = IOUtils.toByteArray(inputStream);
                inputStream.close();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return bytes;
    }

    public String getString() {
        return new String(getBytes());
    }
}
