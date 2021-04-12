package org.buildobjects.buildexample;

import org.buildobjects.Build;
import org.buildobjects.MultipleBuildEnvironment;
import org.buildobjects.BuildEnvironment;
import org.buildobjects.artifacts.Classes;
import org.buildobjects.artifacts.ClassesCombiner;
import org.buildobjects.artifacts.FileLocation;
import org.buildobjects.artifacts.Location;
import org.buildobjects.projectmodel.JavaModule;
import org.buildobjects.publishers.PublishableCombiner;
import org.buildobjects.tasks.build.BuildTaskFactory;

import java.io.File;

/* Functional definition of build
*   Everything is passed into the constructor, hence no cycles!
*  But a bit of a smell with all those names moduleACompileTests etcpp*/
public class BuildExampleMoreRefined {

    public BuildExampleMoreRefined(BuildEnvironment environment) {
        Build build = environment.createBuild();


        BuildTaskFactory tasks = new BuildTaskFactory(build);

        //Location location = new SVNLocation(build, "http://buildobjects.googlecode.com/svn/trunk/example/testproject");
        Location location = new FileLocation("example/testproject-multiple-modules");


        Classes junit = location.jarFile("lib/junit-4.4.jar");
        Classes mockito = location.jarFile("lib/mockito-all-1.6.jar");

        Classes testLibs = new ClassesCombiner(junit, mockito);

        Classes commonsLang = location.jarFile("lib/commons-lang-2.3.jar");


        JavaModule moduleA = new JavaModule(tasks, location.child("module_a"), commonsLang, testLibs);

        JavaModule moduleB = new JavaModule(tasks, location.child("module_b"), moduleA.outputDep(), junit);

        //nothing happens until here:
        PublishableCombiner buildResult = new PublishableCombiner(moduleA.publishable(), moduleB.publishable());

        build.publish(buildResult);
    }

    public static void main(String[] args) {
        new BuildExampleMoreRefined(new MultipleBuildEnvironment(new File("example-builds")));

    }
}