package org.buildobjects.tasklet.metamodel;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * User: fleipold
 * Date: Oct 17, 2008
 * Time: 2:55:21 PM
 */
public class ReflectorTest {

    @Test
    public void testReflector(){
        TaskletReflector reflector = new TaskletReflector(TestTasklet.class);
        assertEquals(3, reflector.getTaskletInfo().getParameters().size());
        assertEquals(2, reflector.getTaskletInfo().getActions().size());
    }


}
