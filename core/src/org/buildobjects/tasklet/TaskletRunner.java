package org.buildobjects.tasklet;

import org.apache.commons.cli.*;
import org.buildobjects.tasklet.metamodel.ActionInfo;
import org.buildobjects.tasklet.metamodel.ParameterInfo;
import org.buildobjects.tasklet.metamodel.TaskletReflector;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: fleipold
 * Date: Oct 17, 2008
 * Time: 2:39:43 PM
 */
public class TaskletRunner {
    private final Object tasklet;
    private Map<ParameterInfo, Option> params = new HashMap<ParameterInfo, Option>();
    private Options options;
    private TaskletReflector reflector;


    public TaskletRunner(Object tasklet) {
        this.tasklet = tasklet;

        reflector = new TaskletReflector(tasklet.getClass());
        createOptions();
    }

    private void createOptions() {
        options = new Options();

        Option showHelp = new Option("h", "Show help");
        options.addOption(showHelp);

        for (ParameterInfo parameterInfo : this.reflector.getTaskletInfo().getParameters()) {
            {
                createOption(parameterInfo);
            }
        }
    }

    private void createOption(ParameterInfo parameterInfo) {
        Option option = new Option(parameterInfo.getName(), parameterInfo.getDescription());


        if (parameterInfo.getType() != boolean.class) {
            option.setArgs(1);
            option.setArgName(parameterInfo.getName());
            option.setOptionalArg(false);
        }


        options.addOption(option);
        params.put(parameterInfo, option);

    }

    public void run(String... args) {
        CommandLineParser parser = new GnuParser();
        try {
            CommandLine line = parser.parse(options, args);

            if (isHelpAction(line)) return;

            bindCommandLine(line);


            final List<ActionInfo> actions = reflector.getTaskletInfo().getActions();
            if (actions.size() == 0) throw new RuntimeException("No actions defined!");

            if (actions.size() == 1) {
                actions.get(0).getMethod().invoke(tasklet);
            } else {
                if (line.getArgs().length < 1) {
                    System.err.println("Tasklet has more than one action please specify action as commandline parameter");
                    this.printHelp();
                    return;
                }


                String actionName = line.getArgs()[0];
                for (ActionInfo action : actions) {
                    if (action.getName().equals(actionName)) {
                        action.getMethod().invoke(tasklet);
                        return;
                    }

                }

                throw new RuntimeException("Action '" + actionName + "' not found.");
            }


        } catch (ParseException e) {
            System.out.println(e.getMessage());
            printHelp();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private void bindCommandLine(CommandLine line) throws IllegalAccessException, InvocationTargetException {
        for (ParameterInfo parameterInfo : this.reflector.getTaskletInfo().getParameters()) {
            if (!hasParameter(line, parameterInfo)) {
                continue;
            }

            if (parameterInfo.getType() == boolean.class) {
                boolean value = hasParameter(line, parameterInfo);
                set(parameterInfo, value);
            } else if (parameterInfo.getType() == String.class) {
                String value = getStringValue(line, parameterInfo);
                set(parameterInfo, value);
            } else if (Enum.class.isAssignableFrom(parameterInfo.getType())) {
                final String stringValue = getStringValue(line, parameterInfo);
                Enum value = Enum.valueOf(parameterInfo.getType(), stringValue);
                set(parameterInfo, value);
            }

        }
    }

    private boolean hasParameter(CommandLine line, ParameterInfo parameterInfo) {
        return line.hasOption(params.get(parameterInfo).getOpt());
    }

    private String getStringValue(CommandLine line, ParameterInfo parameterInfo) {
        return line.getOptionValue(params.get(parameterInfo).getOpt());
    }

    private void set(ParameterInfo parameterInfo, Object value) throws IllegalAccessException, InvocationTargetException {
        parameterInfo.getSetter().setAccessible(true);
        parameterInfo.getSetter().invoke(tasklet, value);
    }

    private boolean isHelpAction(CommandLine line) {


        boolean value = line.hasOption("h");
        if (!value) return false;

        printHelp();
        return true;


    }

    private void printHelp() {
        final List<ActionInfo> actions = reflector.getTaskletInfo().getActions();
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(tasklet.getClass().getName() + " [options] action", options);
        System.out.println("Available actions:");
        for (ActionInfo action : actions) {
            System.out.println(" " + action.getName() + ": " + action.getDescription());

        }
    }


    public static void main(String[] args) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, ParseException {
        final TaskletStartupCommandlineParser parser = new TaskletStartupCommandlineParser(args);

        final TaskletLoader loader = new TaskletLoader(parser.getParameters());
        Object tasklet = loader.getTasklet();
        String[] remainingArgs = parser.getRemainingArgs();

        TaskletRunner runner = new TaskletRunner(tasklet);
        runner.run(remainingArgs);
    }

}
