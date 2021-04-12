package org.buildobjects.classpath;

import org.buildobjects.artifacts.Classes;
import org.buildobjects.artifacts.resources.Path;
import org.buildobjects.artifacts.resources.Resource;
import org.buildobjects.artifacts.resources.Resources;
import org.buildobjects.util.MixedUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * User: fleipold
 * Date: Oct 12, 2008
 * Time: 9:29:32 AM
 */
public class ClassesLoader extends ClassLoader {
    private final Resources classes;


    public ClassesLoader(Classes classes) {
        this(classes, null);
    }

    public ClassesLoader(Classes classes, ClassLoader classLoader) {
        super(classLoader);
        this.classes = (classes.getResources());
    }

    public InputStream getResourceAsStream(String name) {
        name = MixedUtils.relativePathCleaner(name);
        if (!classes.hasResource(new Path(name))){
            return null;
        }
        Resource resource = classes.getResource(new Path(name));
        return new ByteArrayInputStream(resource.getBytes());
    }

    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String resourcePath = name.replace(".", "/") + ".class";
        resourcePath = MixedUtils.relativePathCleaner(resourcePath);

        if (classes.hasResource(new Path(resourcePath))) {
            final Path path = new Path(resourcePath);
            final byte[] content = classes.getResource(path).getBytes();
            return defineClass(name, content, 0, content.length);

        }
        throw new ClassNotFoundException(name);
    }

}
