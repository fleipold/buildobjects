package org.buildobjects.util;

import org.buildobjects.artifacts.resources.Path;
import org.buildobjects.artifacts.resources.Resource;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Zipper {
    private ZipOutputStream zout;
    Set<Path> writtenFiles=new HashSet<Path>();

    public Zipper() {
    }

    public Zipper(OutputStream outputStream) {
        zout = new ZipOutputStream(outputStream);
    }

    public void addResource(Resource resource) {
        if (!writtenFiles.contains(resource.getPath())) {
        } else {
            //System.out.println("Duplicate resource "+resource.getPath());
            //todo: might want a warning here...
            return;
        }
        writtenFiles.add(resource.getPath());

        final ZipEntry zipEntry = new ZipEntry(resource.getPath().toRelativePathString());
        try {
            zout.putNextEntry(zipEntry);
            zout.write(resource.getBytes());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void close() {
        try {
            zout.close();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }                                    
    }
}