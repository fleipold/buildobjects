package org.buildobjects.artifacts;

import java.io.File;

/**
 * User: fleipold
 * Date: Oct 20, 2008
 * Time: 3:31:00 PM
 */
    public interface Location {
    String getName();

    Sources src(String relativePath);
    Sources src(String relativePath, String sourceName);

    Classes jarFile(String relativePath);

    Classes jarDir(String relativePath);

    Location child(String relativePath);

    File file(String relativePath);
}
