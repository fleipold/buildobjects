package org.buildobjects.artifacts.resources;

import org.apache.commons.io.IOUtils;
import org.buildobjects.publishers.FileSink;
import org.buildobjects.tasks.FailureException;
import org.buildobjects.util.Zipper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Felix
 * Date: 16.05.2010
 * Time: 17:58:55
 */
public class Zip extends ResourceResourcesMixin implements Resource {
    private final Path name;
    private final Resources resources;
    private byte[] bytes;

    public Zip(Path name, Resources resources) {
        this.name = name;
        this.resources = resources;
    }

    public Zip(String name, Resources resources) {
        this(new Path(name), resources);
    }

    public Path getPath() {
        return name;
    }

    public byte[] getBytes() {
        if (bytes == null) {
            doZip();
        }
        return bytes;
    }

    private void doZip() {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();

        Zipper zipper = new Zipper(bout);
        for (Resource resource : resources.getAll()){
            zipper.addResource(resource);
        }
        zipper.close();
        try {
            bout.close();
        } catch (IOException e) {/*this won't happen*/}
        bytes = bout.toByteArray();
    }




    public String getString() {
        return getBytes().toString();
    }
}
