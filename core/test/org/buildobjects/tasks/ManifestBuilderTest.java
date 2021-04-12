package org.buildobjects.tasks;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.jar.Manifest;

/**
 * User: fleipold
 * Date: Oct 16, 2008
 * Time: 12:34:49 PM
 */
public class ManifestBuilderTest {
    @Test
    public void testBuildManifest() throws IOException {
        Manifest mf = new ManifestBuilder().mainClass("test.me.Class").toManifest();
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        mf.write(bout);

        assertEquals("Manifest-Version: 1.0\r\n" +
                "Main-Class: test.me.Class\r\n\r\n", bout.toString());

    }

}
