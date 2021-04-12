package org.buildobjects.tasklet;

import org.apache.commons.cli.ParseException;
import org.buildobjects.artifacts.*;
import org.buildobjects.classpath.ClassesLoader;
import org.buildobjects.compiler.CompilerWrapper;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * User: fleipold
 * Date: Oct 21, 2008
 * Time: 11:13:16 AM
 */
public class TaskletLoader extends Object {
    private Object tasklet;

    final TaskletStartupParameters startupParameters;

    public TaskletLoader(TaskletStartupParameters startupParameters) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, ParseException {
        this.startupParameters = startupParameters;


        Sources sources = startupParameters.getSources();

        URLClassLoader parentLoader = (URLClassLoader) TaskletRunner.class.getClassLoader();
        URL url = parentLoader.getURLs()[0];

        Classes classpath = new JarClasses(url.getFile());

        CompilerWrapper cw = new CompilerWrapper(sources, classpath);
        cw.doit();

        Classes compiledBuildScript = cw.getResult();
        ClassLoader buildScriptLoader = new ClassesLoader(compiledBuildScript, parentLoader);
        tasklet = buildScriptLoader.loadClass(this.startupParameters.getTaskletName()).newInstance();

    }

    public Object getTasklet() {
        return tasklet;
    }



}
