package org.buildobjects.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.buildobjects.artifacts.resources.Resource;
import org.buildobjects.artifacts.resources.Resources;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/**
 * Felix
 * Date: 29.05.2010
 * Time: 15:42:42
 */
public class ResourcesWriter {
    public ResourcesWriter(File directory, Resources toBeWritten) {
       directory.mkdirs();
        for (Resource resource : toBeWritten.getAll()) {
            try {
                File file = new File(directory, resource.getPath().toRelativePathString());
                file.getParentFile().mkdirs();
                FileUtils.writeByteArrayToFile(file, resource.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }




    }
}
