package org.buildobjects.artifacts.resources;

import org.buildobjects.util.DelegatingProxy;
import org.buildobjects.util.ProxyFactory;

import java.io.File;
import java.util.Collection;

/**
 * User: fleipold
 * Date: Oct 31, 2008
 * Time: 5:17:39 PM
 */
public class FileSystemResources implements Resources{
    Resources delegate;
    final File baseDir;


    public FileSystemResources(File baseDir) {
        this.baseDir = baseDir;
        if (!baseDir.exists()) {
            throw new RuntimeException("Directory " + baseDir + " doesn't exist.");
        }
        if (!baseDir.isDirectory()) {
            throw new RuntimeException("File " + baseDir + " is not a directory.");
        }
        delegate = DelegatingProxy.createLazyProxy(Resources.class, new ProxyFactory<Resources>() {
            public Resources createProxy() {
               return readFiles();
            }
        });

    }

    private Resources readFiles() {
        InMemoryResources res = new InMemoryResources();
        recurse(new Path(), res, baseDir);


        return res;
    }

    private void recurse(Path path, InMemoryResources res, File dir) {
        for (File file : dir.listFiles()){
            if (file.isDirectory() && !ignore(file.getName())){
                recurse(path.child(file.getName()), res, file);
            } else if (file.isFile()){
                res.add(new FileBackedResource(path.child(file.getName()),file) );

            }

        }
    }

    private boolean ignore(String name) {
        return name.startsWith(".");
    }


    public boolean hasResource(Path path) {
        return delegate.hasResource(path);
    }

    public Resource getResource(Path path) {
        return delegate.getResource(path);
    }

    public Collection<Resource> getAll() {
        return delegate.getAll();
    }

    public Collection<Resource> getResourcesInPath(Path path) {
        return delegate.getResourcesInPath(path);
    }

    public Collection<Resource> getResourcesInPathRescursively(Path path) {
        return delegate.getResourcesInPathRescursively(path);
    }
}
