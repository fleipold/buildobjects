package org.buildobjects.util;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * User: fleipold
 * Date: Oct 16, 2008
 * Time: 10:35:04 PM
 */
public class DelegatingProxyTest {
    @Test
    public void testLazyProxy(){
        Calculator actualCalculator = new Calculator(){
            public int calculateA() {
                return 5;
            }

            public int calculateB() {
                return 4;
            }
        };


        
        Object mock=Mockito.mock(DelegateProvider.class);

        DelegateProvider<Calculator> calculatorFunction = (DelegateProvider<Calculator>) mock;

        Mockito.when(calculatorFunction.delegate())
                .thenReturn(actualCalculator);

        Calculator calculator = DelegatingProxy.create(Calculator.class, calculatorFunction, this.getClass().getClassLoader());

        Mockito.verify(calculatorFunction,Mockito.times(0)).delegate();

        assertEquals(5, calculator.calculateA());
        assertEquals(4, calculator.calculateB());
        Mockito.verify(calculatorFunction, Mockito.times(2)).delegate();


    }



    interface Calculator  {
        public int calculateA();
        public int calculateB();


    }

}
