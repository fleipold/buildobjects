package org.buildobjects.artifacts;


import org.buildobjects.Build;
import org.buildobjects.artifacts.resources.Resources;
import org.buildobjects.artifacts.resources.ZipFileResources;
import org.buildobjects.util.DelegatingProxy;
import org.buildobjects.util.ProxyFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * User: fleipold
 * Date: Oct 12, 2008
 * Time: 10:41:48 AM
 */
public class NestedRemoteJarClasses implements Classes {
    private final Build build;
    URL url;
    private String path;

    final Classes lazyDelegate;


    public NestedRemoteJarClasses(Build build, URL url,  String path) {
        this.build = build;
        this.url = url;
        this.path = path;
        lazyDelegate = DelegatingProxy.createLazyProxy(Classes.class, new ProxyFactory<Classes>() {
            public Classes createProxy() {
                return createClasses();
            }
        });


    }

    private Classes createClasses() {
        File file = build.getEnvironment().fetchFileInZip(url, path );
        return   new ResourcesClasses(new ZipFileResources(file));
    }

    public NestedRemoteJarClasses(Build build, String url, String path) throws MalformedURLException {
         this(build, new URL(url), path);
    }


    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NestedRemoteJarClasses that = (NestedRemoteJarClasses) o;

        if (!url.equals(that.url) && path.equals(that.path)) return false;

        return true;
    }

    public int hashCode() {
        return url.hashCode();
    }


    public Resources getResources() {
        return lazyDelegate.getResources();
    }
}