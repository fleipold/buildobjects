package felix;

import junit.framework.TestCase;
import junit.framework.Assert;


public class CTest extends TestCase {
    public void testSum(){
        Assert.assertEquals(2.0,new A().sum(1.0,1.0),0000.1);
    }
}
