package org.buildobjects.publishers;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: fleipold
 * Date: Jan 12, 2009
 * Time: 4:38:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class AbsolutePathPublishable implements Publishable {
    final private Publishable wrappee;
    final private File basdir;


    public AbsolutePathPublishable(Publishable wrappee, File basdir) {
        this.basdir = basdir;
        this.wrappee = wrappee;
    }

    public void publish(FileSink publisher) {
        wrappee.publish(new FSFileSink(basdir));
    }
}

