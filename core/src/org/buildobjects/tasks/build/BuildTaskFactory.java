package org.buildobjects.tasks.build;

import org.buildobjects.Build;
import org.buildobjects.artifacts.Classes;
import org.buildobjects.artifacts.NestedRemoteJarClasses;
import org.buildobjects.artifacts.RemoteJarClasses;
import org.buildobjects.artifacts.Sources;
import org.buildobjects.publishers.JarFileOut;
import org.buildobjects.publishers.JarFileOutBuild;
import org.buildobjects.tasks.Antlr;
import org.buildobjects.tasks.JUnit;
import org.buildobjects.tasks.JavaC;
import org.buildobjects.tasks.Java;

import java.io.File;
import java.net.URL;

/**
 * User: fleipold
 * Date: Oct 14, 2008
 * Time: 3:10:56 PM
 */
public class BuildTaskFactory {

    final private Build build;

    public BuildTaskFactory(Build build) {
        this.build = build;
    }

    public JavaC javac(Classes dependencies, Sources sources){
        return new JavaCBuild(build, dependencies, sources);
    }


    public JUnit junit(Classes dependencies, Sources sources){
        return new JUnitBuild(build, dependencies, sources);
    }

    public JarFileOut jar(Classes classes, String filename){
        return new JarFileOutBuild(classes, filename);
    }


    public Antlr antlr(Classes antlr, File... grammars){
        return new AntrlBuild(build, antlr, grammars);
    }


    public Classes fetchJar(String url){
        try {
            return new RemoteJarClasses(build, new URL(url));
        } catch (Exception e) {
            throw new RuntimeException(url);
        }
    }

    public Java java(Classes classes, String mainClass, String... args){
        return new JavaTask(build, classes, mainClass, args );
    }

     public Classes fetchJar(String url, String relativePath ){
        try {
            return new NestedRemoteJarClasses(build, new URL(url), relativePath);
        } catch (Exception e) {
            throw new RuntimeException(url);
        }
    }

}
