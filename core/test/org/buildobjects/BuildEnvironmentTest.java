package org.buildobjects;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * User: fleipold
 * Date: Nov 4, 2008
 * Time: 10:52:32 AM
 */
public class BuildEnvironmentTest {
    @Test

    @Ignore
    public void testFetchFile() throws MalformedURLException {
        BuildEnvironment env = new MultipleBuildEnvironment(tempDir);

        
        File file = env.fetchFile(new URL("http://mirrors.enquira.co.uk/apache/commons/lang/commons-lang-current-bin.zip"));
        assertEquals(("commons-lang-current-bin.zip"), file.getName());

        file = env.fetchFile(new URL("http://mirrors.enquira.co.uk/apache/commons/lang/commons-lang-current-bin.zip"));


    }


     private File tempDir;


    @After
    public void deleteTempFolder() throws IOException {
        FileUtils.deleteDirectory(tempDir);
    }

    @Before
    public  void createTempFolder() throws Exception {
        tempDir = File.createTempFile("temp", "JarFileOutTest");
        tempDir.delete();
        tempDir.mkdirs();
    }
}
