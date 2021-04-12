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
public class RemoteJarClasses implements Classes {
    private final Build build;
    URL url;

    final Classes lazyDelegate;


    public RemoteJarClasses(Build build, URL url) {
        this.build = build;
        this.url = url;
        lazyDelegate = DelegatingProxy.createLazyProxy(Classes.class, new ProxyFactory<Classes>() {
            public Classes createProxy() {
                return createClasses();
            }
        });


    }

    private Classes createClasses() {
         File file = build.getEnvironment().fetchFile(url);

        return   new ResourcesClasses(new ZipFileResources(file));
    }

    public RemoteJarClasses(Build build, String url) throws MalformedURLException {
         this(build, new URL(url));
    }


    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RemoteJarClasses that = (RemoteJarClasses) o;

        if (!url.equals(that.url)) return false;

        return true;
    }

    public int hashCode() {
        return url.hashCode();
    }


    public Resources getResources() {
        return lazyDelegate.getResources();
    }
}