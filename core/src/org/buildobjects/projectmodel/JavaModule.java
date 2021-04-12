package org.buildobjects.projectmodel;

import org.buildobjects.artifacts.*;
import org.buildobjects.publishers.JarFileOutBuild;
import org.buildobjects.publishers.Publishable;
import org.buildobjects.publishers.PublishableCombiner;
import org.buildobjects.publishers.TestResultFileOut;
import org.buildobjects.tasks.JUnit;
import org.buildobjects.tasks.JavaC;
import org.buildobjects.tasks.build.BuildTaskFactory;

/**
 * User: fleipold
 * Date: Oct 14, 2008
 * Time: 7:55:49 PM
 */
public class JavaModule {

    private final JavaC compile;
    private final JavaC compileTests;
    private final JUnit runTests;
    private final Publishable publisher;
    private final Location location;

    public class Internals{
        public JUnit getRunTests(){
            return runTests;
        }

        public JavaC getCompileTests(){
            return compileTests;
        }

        public JavaC getCompile(){
            return compile;            
        }

    }

    public JavaModule(BuildTaskFactory tasks) {
        this (tasks, new FileLocation("."), new NullClasses(), new NullClasses());

    }

     public JavaModule(BuildTaskFactory tasks, Location location, Classes libs) {
         this (tasks, location, libs, libs);

     }

    public JavaModule(BuildTaskFactory tasks, Location location, Classes libs, Classes testlibs) {
        this.location = location;
        compile = tasks.javac(
                libs,
                location.src("src")
        );

        compileTests = tasks.javac(
                new ClassesCombiner(testlibs, compile.outputDep()),
                location.src("test")
        );



        runTests = tasks.junit(
                compileTests.outputDep(),
                location.src("test")
        );


        publisher = new PublishableCombiner(
              location.getName(),
                new JarFileOutBuild(output(), getJarName()),
                new TestResultFileOut(testResults(),"test-results.txt")
             );
    }

    protected String getJarName() {
        return location.getName()+".jar"; 
    }


    public Classes output() {
        return compile.output();
    }

    public Classes outputDep() {
        return compile.outputDep();
    }

    public TestResults testResults() {
        return runTests.results();
    }

    public Publishable publishable() {
        return publisher;
    }

    public Internals getInternals(){
        return new Internals();
    }
}
