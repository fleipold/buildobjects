package felix;

import org.mockito.Mockito.*;
import org.mockito.Mockito;
import org.junit.Assert;
import org.junit.Test;

/**
 * User: fleipold
 * Date: Oct 15, 2008
 * Time: 2:55:34 PM
 */
public class BTest {

    @Test
    public void testSqrOfSums(){
        A a = Mockito.mock(A.class);
        Mockito.stub(a.sum(Mockito.anyDouble(), Mockito.anyDouble())).toReturn(10.0);
        B b = new B(a);
        Assert.assertEquals(100.0, b.squareOfSum(2,1),0.000001);


    }

    @Test
    public void testProduct(){
        B b = new B(new A());
        Assert.assertEquals(6.0, b.product(2,3), 0.000001);
    }

}
