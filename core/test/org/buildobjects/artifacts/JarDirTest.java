package org.buildobjects.artifacts;

import org.buildobjects.artifacts.resources.Path;
import org.buildobjects.artifacts.resources.Resource;
import org.junit.Assert;
import org.junit.Test;


public class JarDirTest {

    private static final Path STRING_UTILS_PATH = new Path("org","apache","commons","lang","StringUtils.class");
    private static final Path TEST_PATH = new Path("org","junit","Test.class");



    @Test
    public void readFilesFromJars() throws ClassNotFoundException {
        JarDirClasses jarDir = new JarDirClasses("example/testproject-multiple-modules/lib");

        Assert.assertTrue(jarDir.getResources().hasResource(STRING_UTILS_PATH));
            Assert.assertTrue(jarDir.getResources().hasResource(TEST_PATH));
        Resource res = jarDir.getResources().getResource(STRING_UTILS_PATH);
        Assert.assertNotNull(res.getBytes());
        Assert.assertFalse(jarDir.getResources().hasResource(new Path("I love being a programmer")));


    }
}