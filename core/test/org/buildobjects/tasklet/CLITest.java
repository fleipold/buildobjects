package org.buildobjects.tasklet;

import org.apache.commons.cli.*;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * User: fleipold
 * Date: Oct 21, 2008
 * Time: 12:01:29 PM
 */
public class CLITest {
    @Test
    public void cli() throws ParseException {
        Options opts = new Options();
        opts.addOption(new Option("h","help"));
        Parser parser = new GnuParser();
        CommandLine line = parser.parse(opts, new String[]{"-h", "xyz", "-c"},
                true
        );
        assertEquals("xyz", line.getArgs()[0]);
        assertEquals("-c", line.getArgs()[1]);


        


    }
}
