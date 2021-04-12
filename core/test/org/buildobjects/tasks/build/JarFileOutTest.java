package org.buildobjects.tasks.build;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.buildobjects.artifacts.NullClasses;
import org.buildobjects.publishers.*;
import org.buildobjects.tasks.ManifestBuilder;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * User: fleipold
 * Date: Oct 16, 2008
 * Time: 1:58:53 PM
 */
public class JarFileOutTest {

    @Test
    public void fileOutManifest() throws IOException {
        final Manifest manifest = new ManifestBuilder().toManifest();
        JarFileOut jarOut = new JarFileOutBuild(new NullClasses(), "test.jar", manifest);
        jarOut.publish(new  FSFileSink(tempDir, new PublicationListener() {
            public void published(PublicationInfo publication) {
            }
        }));

        ZipFile zip = new ZipFile(new File(tempDir, "test.jar"));
        ZipEntry entry = zip.getEntry("META-INF/MANIFEST.MF");
        InputStream inputStream = zip.getInputStream(entry);
        String manifestStr = IOUtils.toString(inputStream);
        inputStream.close();
        zip.close();
        assertEquals("Manifest-Version: 1.0\r\n\r\n",manifestStr);

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
