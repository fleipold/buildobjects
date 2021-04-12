package org.buildobjects.artifacts;

import org.buildobjects.artifacts.resources.Path;
import org.buildobjects.artifacts.resources.Resource;
import org.buildobjects.classpath.ClassesLoader;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;


public class JarClassesTest {
    private static final String SOME_CLASS = "org/junit/Test.class";


    @Test
    public void readFilesFromJar() throws ClassNotFoundException {
        JarClasses junitJar = new JarClasses(new File("core/lib/run/junit-4.4.jar"));
        Assert.assertTrue(junitJar.getResources().hasResource(new Path(SOME_CLASS)));
        Resource res = junitJar.getResources().getResource(new Path(SOME_CLASS));
        Assert.assertNotNull(res.getBytes());
        Assert.assertFalse(junitJar.getResources().hasResource(new Path("I love being a programmer")));

        ClassesLoader loader = new ClassesLoader(junitJar);
        loader.loadClass("org.junit.Test");

    }
}
