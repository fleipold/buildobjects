package org.buildobjects;

import org.buildobjects.artifacts.Initializer;

import java.io.File;

/**
 * User: fleipold
 * Date: Oct 8, 2009
 * Time: 11:35:46 PM
 */
public interface BuildEnvironment extends ResourceReader {
    Build createBuild();

    void reportResult(BuildResult result);

    File getCacheFolder(String key, Initializer initializer);
}
