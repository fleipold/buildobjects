package org.buildobjects.publishers;

import org.buildobjects.artifacts.Classes;
import org.buildobjects.artifacts.resources.InMemoryResource;
import org.buildobjects.artifacts.resources.Path;
import org.buildobjects.artifacts.resources.Resource;
import org.buildobjects.tasks.FailureException;
import org.buildobjects.tasks.Task;
import org.buildobjects.util.Zipper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.jar.Manifest;

/**
 *

 */
public class JarFileOutBuild implements JarFileOut, Task {
    private Classes toBeFiledOut;
    private String fileName;
    private RuntimeException precannedException; //yagni
    private final Manifest manifest;
    private static final Path MANIFEST_PATH = new Path("META-INF","MANIFEST.MF");
    ;

    public JarFileOutBuild(Classes toBeFiledOut, String fileName, Manifest manifest) {
        this.manifest = manifest;
        precannedException = new RuntimeException();
        
        this.toBeFiledOut =  toBeFiledOut;
        this.fileName = fileName;


    }

    public JarFileOutBuild(Classes toBeFiledOut, String fileName) {
        this(toBeFiledOut, fileName, null);
    }

    /** force writing the jar to the current directory*/
    public void write(){
        publish(new FSFileSink(new File(".")));
    }

    /** Callback for the publish */
    public void publish(FileSink publisher) {
        try {
            final Zipper zipper = new Zipper(publisher.getOutputStream(fileName));

            if (manifest != null){
                zipper.addResource(asResource(manifest));
            }

            for (Resource resource : toBeFiledOut.getResources().getAll()) {
                zipper.addResource(resource);
            }

            zipper.close();
        } catch (Exception e) {
            throw new FailureException("Failed to file out jar", this, e);
        }
    }


    public static Resource asResource(Manifest manifest) throws IOException {
        ByteArrayOutputStream content = new ByteArrayOutputStream();
        content.close();
        manifest.write(content);
        return new InMemoryResource(new Path(MANIFEST_PATH.toRelativePathString()), content.toByteArray());
    }


    public String getName() {
        return "jar: "+fileName;
    }

    public void build() {
        throw new UnsupportedOperationException();
    }
}
