package org.buildobjects.buildexample;

import org.buildobjects.artifacts.*;
import org.buildobjects.classpath.ClassesLoader;
import org.buildobjects.compiler.CompilerWrapper;
import org.buildobjects.publishers.JarFileOutBuild;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * User: fleipold
 * Date: Oct 24, 2008
 * Time: 11:28:53 AM
 */

public class UsageOfCompilerAPITest {
    @Test
    public void compile() throws IOException, ClassNotFoundException {
        Classes dependencies = new JarDirClasses("example/testproject-multiple-modules/lib");
        Sources sources = new FileSources(new File("example/testproject-multiple-modules/module_a/src"));
        CompilerWrapper compiler = new CompilerWrapper(sources, dependencies);
        compiler.doit();

        ClassesLoader loader = new ClassesLoader(compiler.getResult());
        loader.loadClass("felix.A");

    }


    @Test
    public void compileFromString() throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Classes dependencies = new NullClasses();
        Sources sources = new StringSourcesBuilder()                
                .addClass("Runner",
                          "public class Runner implements Runnable {public void run(){System.out.println(\"Hello\");}}"
                )
                .toSources();
        CompilerWrapper compiler = new CompilerWrapper(sources, dependencies);
        compiler.doit();

        ClassesLoader loader = new ClassesLoader(compiler.getResult());
        Class<?> runnerClass = loader.loadClass("Runner");
        Runnable runner = (Runnable) runnerClass.newInstance();
        runner.run();
    }

    public static void main(String[] args) throws IOException {
        Classes dependencies = new JarDirClasses("example/testproject/lib");
        Sources sources = new FileSources(new File("example/testproject/module_a/src"));
        CompilerWrapper compiler = new CompilerWrapper(sources, dependencies);
        compiler.doit();
        new JarFileOutBuild(compiler.getResult(), "module_a.jar").write();
    }

}
