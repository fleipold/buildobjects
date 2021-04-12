package org.buildobjects.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Loads class and runs main method by reflection, hiding all the ugly exception stuff..
 */
public class MainRunner {
    final String className;
    final ClassLoader loader;


    public MainRunner(String className, ClassLoader loader) {
        this.className = className;
        this.loader = loader;
    }


    public MainRunner(String className) {
        this(className, MainRunner.class.getClassLoader());
    }

    public Object run(String... args){
        try {
            Class<?> clazz = loader.loadClass(className);
            Method main = clazz.getMethod("main", new Class[]{String[].class});
            return main.invoke(null, new Object[]{args});
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
}
