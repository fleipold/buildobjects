package org.buildobjects.artifacts.resources;

/**
 * User: fleipold
 * Date: Oct 31, 2008
 * Time: 2:20:34 PM
 */
public interface Resource extends Resources {
    Path getPath();

    byte[] getBytes();
    String getString();
}

