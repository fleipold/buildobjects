package org.buildobjects.tasks.build;

import org.buildobjects.Build;
import org.buildobjects.tasks.Task;

/**
 * User: fleipold
 * Date: Oct 15, 2008
 * Time: 12:38:48 PM
 */
abstract class AbstractTask implements Task {
    protected final Build build;

    public AbstractTask(Build build) {
        this.build = build;
    }

    public synchronized void build() {
        try {
            if (build.isBuilt(this)) {
                return;
            }
            buildInternal();
            build.setBuilt(this);
        } catch (Exception e){
            throw new RuntimeException("Task " + getName() + "failed", e);
        }

    }

    protected abstract void buildInternal();
}
