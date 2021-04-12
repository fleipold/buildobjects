package org.buildobjects.classpath;

import org.buildobjects.artifacts.Classes;

/**
 * User: fleipold
 * Date: Oct 20, 2008
 * Time: 12:30:41 PM
 *
 * This classloader allows to block the delegation to the parent classloader for a package. This is ued to avoid
 * the test of buildobjects to test against the bootstrap version.
 */
public class DelegationBlockingClassesLoader extends ClassesLoader{

    final String packageToBlock;
    private int buildNo;

    public DelegationBlockingClassesLoader(Classes classes, String packageToBlock) {
        this(classes, null, packageToBlock);
    }

    public DelegationBlockingClassesLoader(Classes classes, ClassLoader classLoader, String packageToBlock) {
        super(classes, classLoader);
        this.packageToBlock = packageToBlock;
    }

    public DelegationBlockingClassesLoader(Classes classes, ClassLoader parentLoader, String packageToBlock, int buildNo) {
        this(classes, parentLoader, packageToBlock);
        this.buildNo = buildNo;
    }

    public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {

        Class c = findLoadedClass(name);
        if (c!= null) {
            return c;
        }

        if (name.startsWith(packageToBlock)){
            return findClass(name);
        } else {
            return super.loadClass(name, resolve);
        }
    }
}
