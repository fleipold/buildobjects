package felix;

import org.junit.Assert;
import org.junit.Test;

/**
 * User: fleipold
 * Date: Oct 15, 2008
 * Time: 2:55:34 PM
 */
public class BTest {


    @Test
    public void testProduct(){
        B b = new B(new A());
        Assert.assertEquals(6.0, b.product(2,3), 0.000001);
    }

}
