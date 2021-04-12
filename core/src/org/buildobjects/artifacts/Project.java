package org.buildobjects.artifacts;

import java.net.URL;

/**
 * User: fleipold
 * Date: Oct 21, 2008
 * Time: 5:42:23 PM
 */
public class Project {
    private final URL bootstrap;

    public Project(URL bootstrap) {

        this.bootstrap = bootstrap;
    }


    public URL getBootstrap() {
        return bootstrap;
    }
}
