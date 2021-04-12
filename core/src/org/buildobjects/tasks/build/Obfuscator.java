package org.buildobjects.tasks.build;

import org.buildobjects.Build;
import org.buildobjects.artifacts.Classes;

/**
 * User: fleipold
 * Date: Oct 15, 2008
 * Time: 12:34:15 PM
 */
class Obfuscator extends AbstractTask {
    private Classes classes;
    private Classes result;

    public Classes output() {
        return result;
    }

    public Obfuscator(Build build, Classes classes) {
        super(build);
        this.classes = classes;
    }


    protected void buildInternal() {
        result = classes;
    }

    public String getName() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
