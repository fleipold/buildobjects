package org.buildobjects.artifacts.resources;

import static org.junit.Assert.*;
import org.junit.Test;
import static org.junit.matchers.JUnitMatchers.hasItems;


/**
 * User: fleipold
 * Date: Oct 31, 2008
 * Time: 2:43:29 PM
 */
public class ResourcesCombinerTest {
    @Test
    public void testAddAndRetrieveResources(){
        WritableResources res = new InMemoryResources();
        final Resource class1 = new InMemoryResource(new Path("felix", "Writer.java"), "Content");
        final Resource class2 = new InMemoryResource(new Path("felix", "test", "TestWriter.java"), "Content");
        final Resource class3 = new InMemoryResource(new Path("felix", "Reader.java"), "Content");
        final Resource class5 = new InMemoryResource(new Path("org", "Reader.java"), "Content");


        final Resource class4 = new InMemoryResource(new Path("felix", "thirdpacakge","Reader.java"), "Content");

        res.add(class1);
        res.add(class2);
        res.add(class3);
        res.add(class5);

        WritableResources secondRes = new InMemoryResources();
        secondRes.add(class4);

        ResourcesCombiner combiner = new ResourcesCombiner(res, secondRes);
        assertThat(combiner.getAll(), hasItems(class1, class2, class3, class4, class5));
        assertThat(combiner.getResourcesInPath(new Path("felix")), hasItems(class1, class3));
        assertEquals(2, combiner.getResourcesInPath(new Path("felix")).size());
        assertEquals(4, combiner.getResourcesInPathRescursively(new Path("felix")).size());

        assertFalse(combiner.hasResource(new Path("felix","NonExistingFile.java")));
        assertFalse(combiner.hasResource(new Path("felix")));
        assertTrue(combiner.hasResource(new Path("felix","Writer.java")));

        assertEquals(class1, combiner.getResource(new Path("felix","Writer.java")));
        assertEquals(class2, combiner.getResource(new Path("felix","test", "TestWriter.java")));




    }


}