package org.buildobjects.tasklet;

import org.apache.commons.cli.*;

import java.util.Arrays;

/**
 * User: fleipold
 * Date: Oct 21, 2008
 * Time: 3:50:06 PM
 */
public class TaskletStartupCommandlineParser {
    private String[] remainingArgs;
    private TaskletStartupParameters parameters;

    public TaskletStartupCommandlineParser(String... args) {

        Options options = new Options();

        Option sourceFolderOpt = new Option("source", "Source Directory");
        sourceFolderOpt.setArgs(1);
        sourceFolderOpt.setArgName("Source Directory");
        options.addOption(sourceFolderOpt);

        Option recurseOpt = new Option("recurse", "Recurse source folders");
        options.addOption(recurseOpt);

        CommandLineParser parser = new GnuParser();

        try {
            CommandLine line = parser.parse(options, args, true);

            boolean recurse = (line.hasOption("recurse")) || line.hasOption("source");
            String sourceFolder = (line.hasOption("source") ? line.getOptionValue("source") : ".");

            if (line.getArgs().length == 0){
                throw new RuntimeException("No tasklet name specified.");
            }
            String taskletName = (line.getArgs()[0]);

            remainingArgs = Arrays.copyOfRange(line.getArgs(), 1, line.getArgs().length);
            parameters = new TaskletStartupParameters(sourceFolder, recurse, taskletName);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public String[] getRemainingArgs() {
        return remainingArgs;
    }

    public TaskletStartupParameters getParameters() {
        return parameters;
    }
}
