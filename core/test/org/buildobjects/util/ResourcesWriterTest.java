package org.buildobjects.util;
import org.apache.commons.io.FileUtils;
import org.buildobjects.artifacts.resources.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.io.IOException;

import static org.buildobjects.artifacts.resources.InMemoryResource.resource;
import static org.buildobjects.artifacts.resources.InMemoryResources.resources;
import static org.buildobjects.artifacts.resources.Path.path;
import static org.buildobjects.util.MixedUtils.combine;
import static org.buildobjects.util.MixedUtils.prefix;
import static org.junit.Assert.assertTrue;

/**
 * Felix
 * Date: 30.05.2010
 * Time: 15:01:47
 */
public class ResourcesWriterTest {

    @Test
    public void testFileOutSimpleInMemoryResource(){
     Resources resources = resources(
                resource(path("doc", "readme.txt"), "This is the readme."),
                resource(path("doc", "manual.txt"), "This is the manual."),
                resource(path("src", "felix", "Source.java"), "There shall be source."));
     new ResourcesWriter(tempDir, resources);

     assertTrue(new File(tempDir, path("doc", "readme.txt").toRelativePathString()).exists());
     assertTrue(new File(tempDir, path("doc", "manual.txt").toRelativePathString()).exists());
     assertTrue(new File(tempDir, path("src", "felix", "Source.java").toRelativePathString()).exists());
    }

    @Test
    public void testFileOutOfWarLikeScenario(){
     Resources classes = resources(
                resource(path("test", "A.class"), "Class A binary"),
                resource(path("test", "B.class"), "Class B binary"),
                resource(path("test", "nested", "C.class"), "Class C binary"));

     Resources webcontent = resources(
                        resource(path("WEB-INF", "web.xml"), "Oh you lovely web.xml"),
                        resource(path("jsp", "a.jsp"), "JSP content")
                                                                   );

     Resource jarFile = new Zip("myapp.jar", classes);

     Resources warContent = combine(webcontent, prefix(path("WEB-INF/lib"), jarFile));

     new ResourcesWriter(new File("C:\\warcontent"),warContent);

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
