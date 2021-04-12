package org.buildobjects.compiler;

import org.buildobjects.artifacts.resources.Path;
import org.buildobjects.artifacts.resources.Resource;
import org.buildobjects.artifacts.resources.Resources;

import javax.tools.JavaFileObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * User: fleipold
 * Date: Oct 27, 2008
 * Time: 5:47:16 PM
 */
public class ResourcesReadingFileManagerAdapter {
    final private Resources inputResources;

    public ResourcesReadingFileManagerAdapter(Resources resources) {
        this.inputResources = resources;
    }

    public Iterable<JavaFileObject> listClassPathFiles(String packageName, Set<JavaFileObject.Kind> kinds, boolean recurse) {
        List<JavaFileObject> returnValue = new ArrayList<JavaFileObject>();
        final Path packagePath = new Path(packageName.replace(".", "/"));
        final Collection<Resource> resources = recurse ?
                inputResources.getResourcesInPathRescursively(packagePath)
                : inputResources.getResourcesInPath(packagePath);
        for (Resource resource : resources) {

            final InMemoryReadFileObject fileObject = new InMemoryReadFileObject(resource);
            if (kinds.contains(fileObject.getKind()) ) {
                returnValue.add(fileObject);
            }
        }

        return returnValue;

    }

}
