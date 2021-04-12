package org.buildobjects.artifacts.resources;

import static org.junit.Assert.*;
import org.junit.Test;
import static org.junit.matchers.JUnitMatchers.hasItems;




/**
 * User: fleipold
 * Date: Oct 31, 2008
 * Time: 2:43:29 PM
 */
public class InMemoryResourcesTest {
    @Test
    public void testAddAndRetrieveResources(){
        WritableResources res = new InMemoryResources();
        final Resource class1 = new InMemoryResource(new Path("felix", "Writer.java"), "Content");
        final Resource class2 = new InMemoryResource(new Path("felix", "test", "TestWriter.java"), "Content");
        final Resource class3 = new InMemoryResource(new Path("felix", "Reader.java"), "Content");

        res.add(class1);
        res.add(class2);
        res.add(class3);

        assertThat(res.getAll(), hasItems(class1, class2, class3));
        assertThat(res.getResourcesInPath(new Path("felix")), hasItems(class1, class3));
        assertEquals(2, res.getResourcesInPath(new Path("felix")).size());
        assertEquals(3, res.getResourcesInPathRescursively(new Path("felix")).size());

        assertFalse(res.hasResource(new Path("felix","NonExistingFile.java")));
        assertFalse(res.hasResource(new Path("felix")));
        assertTrue(res.hasResource(new Path("felix","Writer.java")));

        assertEquals(class1, res.getResource(new Path("felix","Writer.java")));
        assertEquals(class2, res.getResource(new Path("felix","test", "TestWriter.java")));

        res.deleteResource(new Path("felix","Writer.java"));
        assertEquals(2, res.getAll().size());



    }


}
