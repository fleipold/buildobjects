package org.buildobjects.tasks;

/**
 * User: fleipold
 * Date: Oct 23, 2008
 * Time: 12:47:13 PM
 */
public class FailureException extends RuntimeException {
    private final Task task;

    public FailureException(String message, Task task, Exception cause) {
        super(message, cause);
        this.task = task;
    }

    public Task getTask() {
        return task;
    }
}
