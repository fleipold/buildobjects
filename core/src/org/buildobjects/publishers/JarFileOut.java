package org.buildobjects.publishers;

import org.buildobjects.artifacts.resources.Resource;
import org.buildobjects.tasks.Task;

/**
 * User: fleipold
 * Date: Oct 31, 2008
 * Time: 1:35:00 PM
 */
public interface JarFileOut extends Publishable {
    void write();

    void publish(FileSink publisher);

    String getName();
}
