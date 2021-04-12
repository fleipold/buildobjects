package org.buildobjects;

/**
 * User: fleipold
 * Date: Oct 21, 2008
 * Time: 5:56:03 PM
 */
public enum BuildState {
    IN_PROGRESS, FAILED, SUCCEEDED {
    public boolean succeeded() {
        return true;
    }};

    public boolean succeeded() {
        return false;
    }

    final public boolean failed() {
        return !succeeded();
    }

}
