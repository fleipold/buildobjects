package org.buildobjects.artifacts;

import org.buildobjects.Build;
import org.buildobjects.util.SVNWrapper;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * User: fleipold
 * Date: Oct 20, 2008
 * Time: 3:30:05 PM
 */
public class SVNLocation implements Location {
    private final Build build;
    private final URL url;
    private FileLocation localFileLocation;

    public SVNLocation(Build build, String url) {
        this.build = build;
        try {
            this.url = new URL(url);
            File cacheDirectory = build.getCacheFolder(this.url.toExternalForm(), new Initializer() {
                public void initDir(File dir) {
                    new SVNWrapper().checkout(SVNLocation.this.url, dir);

                }
            });

            build.setRevision(new SVNWrapper().update(cacheDirectory));

            localFileLocation = new FileLocation(cacheDirectory);

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getName() {
        return url.getPath();
    }

    public Location child(String relativePath) {
        return localFileLocation.child(relativePath);
    }

    public Sources src(String relativePath) {
        return localFileLocation.src(relativePath);
    }

    public Sources src(String relativePath, String sourceName) {
        return localFileLocation.src(relativePath, sourceName);
    }

    public Classes jarFile(String relativePath) {
        return localFileLocation.jarFile(relativePath);
    }

    public Classes jarDir(String relativePath) {
        return localFileLocation.jarDir(relativePath);
    }

    public File file(String relativePath) {
        return localFileLocation.file(relativePath);
    }
}
