package org.buildobjects.tasks.build;

import org.buildobjects.artifacts.Classes;
import org.buildobjects.classpath.ClassesLoader;
import org.buildobjects.tasks.Java;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;


/**
 * User: fleipold
 * Date: Oct 18, 2009
 * Time: 9:19:12 PM
 */
public class JavaTask extends AbstractTask implements Java {
    private final Classes classes;
    private final String mainClass;
    private final String[] args;


    public JavaTask(org.buildobjects.Build build, Classes classes, String mainClass, String[] args ) {
        super(build);
        this.classes = classes;
        this.mainClass = mainClass;
        this.args = args;
    }


    protected void buildInternal() {
        ClassesLoader loader = new ClassesLoader(classes);
                try {
                    Class<?> generator = loader.loadClass(mainClass);

                    Method mainMethod = generator.getMethod("main", new Class[]{String[].class});

                    mainMethod.invoke(null, new Object[]{args});
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
    }

    public String getName() {
        return "Java";
    }


}
