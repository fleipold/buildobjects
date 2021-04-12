package org.buildobjects.buildexample;

import org.buildobjects.Build;
import org.buildobjects.MultipleBuildEnvironment;
import org.buildobjects.BuildEnvironment;
import org.buildobjects.artifacts.*;
import org.buildobjects.publishers.JarFileOut;
import org.buildobjects.publishers.JarFileOutBuild;
import org.buildobjects.publishers.PublishableCombiner;
import org.buildobjects.publishers.TestResultFileOut;
import org.buildobjects.tasks.Antlr;
import org.buildobjects.tasks.JUnit;
import org.buildobjects.tasks.JavaC;
import org.buildobjects.tasks.build.BuildTaskFactory;
import static org.buildobjects.util.MixedUtils.combine;

import java.io.File;

/**
 * User: fleipold
 * Date: Oct 27, 2008
 * Time: 2:31:40 PM
 */
public class BuildHibernate {

    public static void main(String[] args) {
        BuildEnvironment environment = new MultipleBuildEnvironment(new File("hibernate-builds"));
        Build build = environment.createBuild();
        BuildTaskFactory tasks = new BuildTaskFactory(build);


        Location location = new FileLocation("/Users/fleipold/Prog/java-stuff/hibernate-3.1-source");
        Location grammars = location.child("grammar");
        Classes lib = location.jarDir("lib");
        Classes hsqldb = location.jarFile("jdbc/hsqldb.jar");

        Classes jdk5_classes = new JarDirClasses("/System/Library/Frameworks/JavaVM.framework/Versions/1.5.0/Home/bundle/Classes");

        Sources productionSources = location.src("src");

        Antlr antlr = tasks.antlr(lib,
                grammars.file("hql.g"),
                grammars.file("hql-sql.g"),
                grammars.file("sql-gen.g"));

        final Sources sources = combine(productionSources, antlr.sources());
        JavaC javac = tasks.javac(lib, sources);
        javac.setPlatformClassPath(jdk5_classes);

        JarFileOut jarFileOut = new JarFileOutBuild(javac.output(), "hibernate.jar");


        JavaC compileTests = tasks.javac(javac.outputDep(), combine(location.src("test"), location.src("etc")));

        JarFileOut testJarFileOut = new JarFileOutBuild(compileTests.output(), "hibernate-tests.jar");


        JUnit junit = tasks.junit(new ClassesCombiner(compileTests.outputDep(), hsqldb),location.src("test"));

        build.publish(new PublishableCombiner( jarFileOut, new TestResultFileOut(junit.results(), "test.txt"), testJarFileOut));


    }

}
