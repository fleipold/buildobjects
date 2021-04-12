package org.buildobjects.artifacts.resources;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.hasItems;


/**
 * User: fleipold
 * Date: Oct 31, 2008
 * Time: 2:43:29 PM
 */
public class ResourcesPrefixerTest {
    @Test
    public void testAddAndRetrieveResources(){

        WritableResources resources = new InMemoryResources(
                new InMemoryResource(new Path("doc", "readme.txt"), "This is the readme."),
                new InMemoryResource(new Path("doc", "manual.txt"), "This is the manual."),
                new InMemoryResource(new Path("src", "felix", "Source.java"), "There shall be source."));
        assertTrue(resources.hasResource(new Path("doc", "readme.txt")));

        ResourcesPrefixer prefixer = new ResourcesPrefixer(new Path("prefix"), resources);

        assertEquals(3, prefixer.getAll().size());
        assertEquals(new Path("prefix", "doc", "manual.txt"), prefixer.getAll().iterator().next().getPath());

        assertTrue(prefixer.hasResource(new Path("prefix", "doc", "readme.txt")));
        assertFalse(prefixer.hasResource(new Path("prefix", "doc", "readme.rtf")));
        assertFalse(prefixer.hasResource(new Path("doc", "readme.txt")));

        assertEquals(new Path("prefix", "doc", "readme.txt"),
                     prefixer.getResource(new Path("prefix", "doc", "readme.txt")).getPath());

        assertEquals(1, prefixer.getResourcesInPathRescursively(new Path("prefix", "src")).size());
        assertEquals(new Path("prefix", "src", "felix", "Source.java"),
                prefixer.getResourcesInPathRescursively(new Path("prefix", "src")).iterator().next().getPath());

        assertEquals(1, prefixer.getResourcesInPath(new Path("prefix", "src", "felix")).size());
        assertEquals(new Path("prefix", "src", "felix", "Source.java"),
                prefixer.getResourcesInPath(new Path("prefix","src", "felix")).iterator().next().getPath());

    }


}                                                                                                       