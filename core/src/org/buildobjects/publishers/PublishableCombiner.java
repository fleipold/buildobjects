package org.buildobjects.publishers;

import org.buildobjects.tasks.FailureException;

/**
 * User: fleipold
 * Date: Oct 22, 2008
 * Time: 8:43:24 PM
 */
public class PublishableCombiner implements Publishable {
    private final String prefix;
    private final Publishable[] children;

    public PublishableCombiner(String prefix, Publishable... children) {
        this.prefix = prefix;
        this.children = children;
    }

    public PublishableCombiner(Publishable... children) {
        this("", children);
    }

    public void publish(FileSink publisher) {
        for (Publishable child : children) {
            try {
                child.publish(publisher.getChild(prefix));
            } catch (FailureException failure){
                throw new RuntimeException(failure);               
            }


        }
    }
}
