package org.buildobjects.artifacts.resources;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.buildobjects.util.DelegatingProxy;
import org.buildobjects.util.ProxyFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * User: fleipold
 * Date: Oct 31, 2008
 * Time: 5:08:28 PM
 */
public class ZipFileResources implements Resources {
    public final File zipFile;
    Resources delegate;


    public ZipFileResources(File zip) {
        this.zipFile = zip;

        delegate = DelegatingProxy.createLazyProxy(Resources.class, new ProxyFactory<Resources>() {
            public Resources createProxy() {
                return loadZip();

            }
        } );

    }

    private Resources loadZip() {
        InMemoryResources resources = new InMemoryResources();

        try {
            ZipFile zip = new ZipFile(zipFile);
            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()){
                ZipEntry entry = entries.nextElement();
                if (entry.isDirectory()){
                    continue;
                }
                String path = entry.getName();
                InputStream input = zip.getInputStream(entry);
                final ByteArrayOutputStream bout = new ByteArrayOutputStream((int) entry.getSize());
                IOUtils.copy(input, bout);
                input.close();

                resources.add(new InMemoryResource(new Path(path), bout.toByteArray()));

            }
            zip.close();

            return resources;
        } catch (IOException e) {
            throw new RuntimeException(String.format("Could not load zipfile '%s'", zipFile.getAbsolutePath()), e);
        }
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
