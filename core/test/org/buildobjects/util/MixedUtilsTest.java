package org.buildobjects.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.buildobjects.artifacts.resources.Path;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * User: fleipold
 * Date: Oct 21, 2008
 * Time: 2:10:59 PM
 */
public class MixedUtilsTest {


    @Test
    public void testStupidSunCode() throws IOException {
        assertEquals("buildobjects", new File(".").getCanonicalFile().getName());
    }


    @Test
    public void arrayToList(){
        List list = MixedUtils.arrayToList(new String[]{"Hallo","Welt"});
        assertEquals(2, list.size());
        assertEquals("Hallo", list.get(0));
        assertEquals("Welt", list.get(1));
    }

    @Test
    public void testPathCleaner(){
        assertEquals("felix/test", MixedUtils.relativePathCleaner("felix//test"));
        assertEquals("felix/test", MixedUtils.relativePathCleaner("felix///test"));
        assertEquals("felix/test", MixedUtils.relativePathCleaner("/felix/test"));
        assertEquals("felix/test", MixedUtils.relativePathCleaner("/felix/test/"));



    }

    @Test
    public void testPath(){
        Path felix = new Path("felix");
        Path child = felix.child("test");
        assertEquals("felix/test", child.toRelativePathString());


    }

    @Test
    public void testToFullyQualifiedClassName(){
        assertEquals("GreeterTest", new Path("GreeterTest.java").toFullyQualifiedClassName());
    }

    @Test
    public void testMD5Stuff(){
        String data = "Hallo Welt, mal sehen, was das hier fuer einen Hashwert ergibt.";
        assertEquals("0f388c42c1e83061d21331a10fe4cdec", DigestUtils.md5Hex(data));
    }
    
}
