package org.buildobjects;

import org.buildobjects.tasks.build.BuildTaskFactory;

import java.io.File;

/**
 * User: fleipold
 * Date: Oct 28, 2008
 * Time: 10:02:48 PM
 */                   
public abstract class BuildBase {
    protected final Build build;
    protected final BuildEnvironment environment;
    protected final BuildTaskFactory tasks;

    public BuildBase() {
       this(new SingleBuildEnvironment(new File("target")));
    }

    public BuildBase(BuildEnvironment environment) {
        this.environment = environment;
        build = environment.createBuild();
        tasks = new BuildTaskFactory(build);
    }


}
