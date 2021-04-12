package org.buildobjects.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import java.util.regex.Matcher;

/**
 * User: fleipold
 * Date: Oct 21, 2008
 * Time: 4:47:32 PM
 */
public class SubversionWrapperTest {
    private Matcher matcher;

    @Test
    public void revisionPattern(){
        matcher = SVNWrapper.REVISION_PATTERN.matcher("klj kj kj revision 123.");
        assertTrue(matcher.matches() );
        assertEquals("123", matcher.group(1));
    }
}
