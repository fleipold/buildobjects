package org.buildobjects.tasklet;

import org.buildobjects.tasklet.metamodel.TestTasklet;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * User: fleipold
 * Date: Oct 17, 2008
 * Time: 3:49:55 PM
 */
public class TaskletRunnerTest {
    @Test
    public void runIt(){

        final TestTasklet tasklet = new TestTasklet();

        new TaskletRunner(tasklet).run("-name","felix","-friendly", "sayGoodbye");

        assertEquals("felix",tasklet.getName());
        assertEquals(true, tasklet.isFriendly());
        assertEquals(1, tasklet.getGreetcount());


    }


    @Test
    public void setEnumValue(){

        final TestTasklet tasklet = new TestTasklet();

        new TaskletRunner(tasklet).run("-name","felix","-friendly", "sayGoodbye", "-language","GERMAN");

        assertEquals(TestTasklet.Language.GERMAN, tasklet.getLanguage());

    }


    @Test
    public void showHelpMessage(){

        final TestTasklet tasklet = new TestTasklet();

        new TaskletRunner(tasklet).run("-h");

        assertEquals(0, tasklet.getGreetcount());


    }

}
