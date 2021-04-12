package felix;

import junit.framework.TestCase;

public class ATest extends TestCase {
    public void testSum(){
        assertEquals(2.0,new A().sum(1.0,1.0),0000.1);
    }

    public void testCommaSeparated() {
        assertEquals("Jae, Felix", new A().commaSeparated("Jae","Felix"));
    }

}
