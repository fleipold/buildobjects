package org.buildobjects.artifacts.resources;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PathTest {
    @Test
    public void testEquals(){
        assertTrue(new Path("felix","test").equals(new Path("felix", "test")));
        assertFalse(new Path("felix","other").equals(new Path("felix", "test")));

    }

    @Test
    public void testParent(){
        assertEquals(new Path("felix"), new Path("felix", "test").getParent());
    }

    @Test
    public void testStartsWith(){
        assertTrue(new Path("felix", "test", "me").startsWith(new Path("felix")));
        assertTrue(new Path("felix", "test", "me").startsWith(new Path("felix", "test")));
        assertTrue(new Path("felix", "test", "me").startsWith(new Path("felix", "test", "me")));

        assertFalse(new Path("felix", "test").startsWith(new Path("felix", "test", "me")));
        assertFalse(new Path("felix", "test", "me").startsWith(new Path("felix", "other")));
        assertFalse(new Path("felix", "test", "me").startsWith(new Path("other", "test")));
    }

    @Test
    public void testRemovePrefix(){
        assertEquals(new Path("felix","test"), new Path("com", "testme", "felix", "test").removePrefix(new Path("com", "testme")));
    }

    @Test
    public void testConcatenation(){
        assertEquals(new Path("a", "b", "c", "d"), new Path("a", "b").concatenate(new Path("c", "d")));
    }

}
