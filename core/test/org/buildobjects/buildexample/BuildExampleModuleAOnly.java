package org.buildobjects.buildexample;

import org.buildobjects.Build;
import org.buildobjects.MultipleBuildEnvironment;
import org.buildobjects.BuildEnvironment;
import org.buildobjects.artifacts.Classes;
import org.buildobjects.artifacts.ClassesCombiner;
import org.buildobjects.artifacts.FileLocation;
import org.buildobjects.artifacts.Location;
import org.buildobjects.publishers.JarFileOutBuild;
import org.buildobjects.publishers.Publishable;
import org.buildobjects.publishers.PublishableCombiner;
import org.buildobjects.publishers.TestResultFileOut;
import org.buildobjects.tasks.JUnit;
import org.buildobjects.tasks.JavaC;
import org.buildobjects.tasks.build.BuildTaskFactory;
import static org.buildobjects.util.MixedUtils.combine;

import java.io.File;

/* Functional definition of build
*   Everything is passed into the constructor, hence no cycles!
*  But a bit of a smell with all those names moduleACompileTests etcpp*/
public class BuildExampleModuleAOnly {
    public static void main(String[] args) {

        BuildEnvironment environment = new MultipleBuildEnvironment(new File("example-builds"));
        Build build = environment.createBuild();
        BuildTaskFactory tasks = new BuildTaskFactory(build);

        Location projectLocation = new FileLocation("example/testproject");

        Classes junit = projectLocation.jarFile("lib/junit-4.4.jar");
        Classes mockito = projectLocation.jarFile("lib/mockito-all-1.6.jar");

        Classes testLibs = new ClassesCombiner(junit, mockito);
        Classes commonsLang = projectLocation.jarFile("lib/commons-lang-2.3.jar");

        Location locA = projectLocation.child("module_a");

        JavaC moduleACompile = tasks.javac(
                commonsLang,
                locA.src("src")
        );

       JavaC moduleACompileTests = tasks.javac(
               combine(testLibs, moduleACompile.output()),
               locA.src("test")
        );


        JUnit moduleARunTests = tasks.junit(
                moduleACompileTests.outputDep(),
                locA.src("test")
        );



        Publishable result = new PublishableCombiner(
                        "dist",
                        new JarFileOutBuild(moduleACompile.output(), "module_a.jar"),
                        new TestResultFileOut(moduleARunTests.results(), "module_a_test.txt")
                );

        // the  steps so far did not trigger any action, so now we go
        build.publish(result);
    }
}