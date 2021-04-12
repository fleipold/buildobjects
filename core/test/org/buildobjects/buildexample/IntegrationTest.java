package org.buildobjects.buildexample;

import org.apache.commons.io.FileUtils;
import org.buildobjects.MultipleBuildEnvironment;
import org.buildobjects.BuildState;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * User: fleipold
 * Date: Oct 24, 2008
 * Time: 11:09:01 AM
 */
public class IntegrationTest {
    private File tempDir;

    @Test
    public void builExample(){
        MultipleBuildEnvironment environment = new MultipleBuildEnvironment(tempDir);
        new BuildExampleMoreRefined(environment);
        assertEquals(1, environment.getResults().size());
        assertEquals(BuildState.SUCCEEDED, environment.getResults().get(0).getState());
    }

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
