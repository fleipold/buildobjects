package org.buildobjects.tasks;

import org.buildobjects.artifacts.Sources;

/**
 * User: fleipold
 * Date: Oct 31, 2008
 * Time: 1:35:47 PM
 */
public interface Antlr extends Task {
    String getName();

    Sources sources();
}
