/*
 * Created on 19.07.2004
 *
 *
 */
package org.buildobjects.util;


import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 * User: Felix Leipold
 * Date: 01.04.2004
 * Time: 20:19:12
 * <p/>
 * This class provides functionality to start external processes, monitor them, kill them and capture their stdout/ stderr
 * using the AbstractOutputConsumer abstraction. For the most common usecases static convenience methods are provided.
 * <p/>
 * <h3>Using the Static Convenience Methods</h3>
 * <h4>Run</h4>
 * <pre>
 *      String ls=Process2.run("ls");*
 * </pre>
 *
 * <h4>Timeout</h4>
 * <pre>
 *      try{
 *          Process2.run("pdflatex test.tex",30000);
 *          showPdf();
 *      } catch (TimeOutException e){
 *          processError();
 *      }
 * </pre>

 * <h4>Filtering</h4>
 * <pre>
 *    String sorted=Process2.run("sort","B\nA");
 * </pre>
 *
 * <h3>Using Process2 Instances</h3>
 * <h4>No other processing</h4>
 * <pre>
 *   Process2 p=new Process2("cat foobar.txt");
 *   p.runBlocking();
 * </pre>
 * <h4>Get the other, but discard stderr</h4>
 * <pre>
 *   out= new Proc.StringOutputConsumer();
 *   Process2 p=new Process2("cat foobar.txt",out,new NullOutputConsumer());
 *   p.runBlocking();
 *   System.out.println("other from command:\n"+out.toString());
 * </pre>
 * <p/>
 * <h4>Specify a Timeout</h4>
 * <pre>
 *   Process2 p=new Process2("glpksolve input");
 *   try{
 *      p.runBlocking(10000); //aborts the operation after 10secs
 *      useResults();
 *    }
 *    catch(TimeOutException e){
 *       indicateError();
 *    }
 * <p/>
 * </pre>
 * <h4>Running it Asynchronously</h4>
 * <pre>
 *   Process2 p=new Process2("glpksolve input"); // very long running operation
 *   p.runAsynchronously();
 *   doSomethingElseInTheMeantime();
 * </pre>
 * <p/>
 * <h4>Getting notified</h4>
 * <pre>
 *   Process2 p=new Process2("glpksolve input"); // very long running operation
 *   proc.addExecutionListener(new Proc.Listener(){
 *       public void executionCompleted(int returnValue) {
 *          useResults();
 *       }
 *      });
 *   proc.runAsynchronously();
 *   doSomethingElseInTheMeantime();
 * </pre>
 * <p/>
 * <h3>Suggested Improvements</h3>
 * <ul>
 * <li> provide logging support </li>
 * <li> are inner classes appropriate here</li>
 * </ul>
 */
public final class Process2 {


    /**
     * Yes we do polling to check for timeout
     */
    private static final long DEFAULT_POLLING_INTERVAL = 50;
    private State state = State.NEW;

    private File workingDirectory;
    private final OutputConsumer out;
    private final OutputConsumer err;
    private InputProvider in;

    private final List listeners;
    private Process process;
    private Thread stdoutMuncher;
    private Thread stderrMuncher;
    private Thread stdinFeeder;
    private String[] args;


    /**
     * @param command the commandline to start the process
     * @param stdout  Consumer for the standard other of the process
     *                StdErr is discarded
     */
    public Process2(String command, OutputConsumer stdout) {
        parseCommand(command);
        listeners = new LinkedList();
        out = stdout;
        err = new NullOutputConsumer();
    }


    /**
     * @param command the commandline to start the process
     * @param stdout  Consumer for the standard other of the process
     * @param stderr  Consumer for the standard err of the process
     */
    public Process2(String command, OutputConsumer stdout, OutputConsumer stderr) {
        parseCommand(command);
        listeners = new LinkedList();
        out = stdout;
        err = stderr;
    }

    /**
     * @param command the commandline to start the process
     * @param stdout  Consumer for the standard other of the process
     * @param stderr  Consumer for the standard err of the process
     */
    public Process2(String command, OutputConsumer stdout, OutputConsumer stderr, InputProvider stdin) {
        parseCommand(command);
        listeners = new LinkedList();
        out = stdout;
        this.in = stdin;
        err = stderr;
    }

    public Process2(String command, OutputConsumer stdout, InputProvider stdin) {
        this(command, stdout, new NullOutputConsumer(), stdin);
    }

    /**
     * StdErr and StdOut is taken care of and thrown away!
     *
     * @param command          the commandline to start the process
     * @param workingDirectory the working directory for the process
     */
    public Process2(String command, File workingDirectory) {
        this(command, new NullOutputConsumer(), new NullOutputConsumer());
        setWorkingDirectory(workingDirectory);
    }

    /**
     * StdErr and StdOut is taken care of and thrown away!
     *
     * @param command the commandline to start the process
     */
    public Process2(String command) {
        this(command, new NullOutputConsumer(), new NullOutputConsumer());
    }

    private void parseCommand(String command) {
        Escaper esc = new Escaper(command);
        args = esc.getChunks();

    }

    /**
     * @return working directory
     */
    public final File getWorkingDirectory() {
        return workingDirectory;
    }

    /**
     * Set the working dir of the process can only be called on processes in state new, defaults to the current dir
     */
    public final void setWorkingDirectory(File workingDirectory) {
        if (getState() != State.NEW) {
            throw new IllegalStateException("Cannot set working dir in state " + getState().toString());
        }
        this.workingDirectory = workingDirectory;
    }

    /**
     * @param e gets notified, when the process fails or when the process terminates
     */
    public final void addExecutionListener(Listener e) {
        if (getState() != State.NEW) {
            throw new IllegalStateException("Adding listeners in state " + getState().toString() + " not allowed");
        }
        listeners.add(e);
    }

    /**
     * @return state of the current process
     */
    public final State getState() {
        return state;
    }

    private void broadCastExecutionCompleted(int returnValue) {
        for (Iterator iterator = listeners.iterator(); iterator.hasNext();) {
            Listener listener = (Listener) iterator.next();
            listener.executionCompleted(returnValue);
        }
    }

    /**
     * if the process is not terminated, it will be killed.
     */
    public final void kill() {
        if (getState() != State.RUNNING) {
            throw new IllegalStateException("Cannot kill process in state " + getState().toString());
        }
        if (process != null) {
            process.destroy();
        }
        state = State.KILLED;
        waitAndBroadcastReturncode();
    }

    /**
     * change state and broadCastEvent
     */
    private void exited() {
        state = State.FINISHED;
        waitAndBroadcastReturncode();
    }

    private String getCommand() {
        return args[0];
    }

    /**
     * @return string representation
     */
    public final String toString() {
        return getState().toString() + " Process " + "Command: " + args[0];
    }

    private void waitAndBroadcastReturncode() {
        boolean broadCastDone = false;
        while (!broadCastDone) {
            try {
                broadCastExecutionCompleted(process.waitFor());
                broadCastDone = true;
            } catch (InterruptedException e) {
                //We will do a retry, as we expect the whole thing to terminate
            }
        }
    }


    /**
     * Start a process blocking the calling thread until the process exits.
     * If the calling thread is interrupted,
     * the process is destroyed.
     *
     * @throws illusion.util.Process2.StartupException
     *
     */
    public final int runBlocked() throws StartupException, InterruptedException {

        try {
            triggerExecution();
        } catch (IOException ex) {
            throw new StartupException("IOException during startup: " + ex.getMessage());
        }

        // Wait for timeout and kill process if timout is reached.
        // If timeout == 0 then the process is to run in background
        int ret;
        try {
            ret = process.waitFor();
            state = State.FINISHED;
        } catch (InterruptedException e) {
            kill();
            state = State.INTERRUPTED;
            throw e;
        }
        return ret;
    }


    /**
     * Start a process blocking the calling thread with a timeout limit.
     * Timeout >0 is expected means no limit. If the calling thread is interrupted,
     * the process is destroyed.
     *
     * @param timeout in milliseconds
     * @throws illusion.util.Process2.StartupException
     *
     */
    public final int runBlocked(long timeout) throws StartupException, TimeoutException, InterruptedException {
        if (timeout <= 0) {
            throw new IllegalArgumentException("Timeout must be positive");
        }

        try {
            triggerExecution();
        } catch (IOException ex) {
            throw new StartupException("IOException during startup: " + ex.getMessage());
        }
        long startTime = System.currentTimeMillis();
        do {
            try {
                Thread.sleep(DEFAULT_POLLING_INTERVAL);
            } catch (InterruptedException e) {
                kill();
                state = State.INTERRUPTED;
                throw e;
            }
        } while (stderrMuncher.isAlive() && System.currentTimeMillis() - startTime < timeout);
        if (stderrMuncher.isAlive()) {
            kill();
            throw new TimeoutException("External Process: " + getCommand() + " could not be finished in time (" + timeout + ")");
        } else {
            exited();
        }
        return process.exitValue();
    }

    /**
     * Triggers the execution
     */
    private void triggerExecution() throws IOException {
        if (getState() != State.NEW) {
            throw new IllegalStateException("Cannot rerun process in state " + getState().toString());
        }

        process = Runtime.getRuntime().exec(args, null, workingDirectory);
        state = State.RUNNING;

        final InputStream outputStream = process.getInputStream();
        stdoutMuncher = new Thread(new Runnable() {
            public void run() {
                out.process(outputStream);
            }
        });

        final InputStream errorStream = process.getErrorStream();
        stderrMuncher = new Thread(new Runnable() {
            public void run() {
                err.process(errorStream);
            }
        });

        final OutputStream stdInStream = process.getOutputStream();
        if (in != null) {
            stdinFeeder = new Thread(new Runnable() {
                public void run() {
                    in.process(stdInStream);
                }
            });
            stdinFeeder.start();
        }

        stdoutMuncher.start();
        stderrMuncher.start();
    }

    /**
     * @return the process wrapped by this Proc Object
     */
    public Process getProcess() {
        return process;
    }

    /**
     * runs the process in the background
     */
    public final void runAsynchronously() throws StartupException {
        try {
            triggerExecution();
        } catch (IOException e) {
            throw new StartupException("Caused by: " + e.getMessage());
        }
        Thread monitor = new Thread(new Runnable() {
            public void run() {
                do {
                    try {
                        Thread.sleep(DEFAULT_POLLING_INTERVAL);
                    } catch (InterruptedException e) {
                    }
                } while (stderrMuncher.isAlive());
                exited();
            }
        });

        monitor.start();// monitors the state of the process and starts
    }

    /**
     * Represents the state of the process
     */
    public static final class State {
        /**
         * Process not yet started
         */
        public static final State NEW = new State("new");
        /**
         * Process is running
         */
        public static final State RUNNING = new State("running");
        /**
         * Process is finished
         */
        public static final State FINISHED = new State("finished");
        /**
         * Process has been killed
         */
        public static final State KILLED = new State("killed");
        final String id;
        /**
         * Process has been killed due to interruption of the busy waiting script
         */
        public static final State INTERRUPTED = new State("interrupted");

        public String toString() {
            return id;
        }

        private State(String id) {
            this.id = id;
        }
    }


    /**
     * Interface for a listener
     */
    public static interface Listener {
        void executionCompleted(int returnValue);
    }

    /**
     * Indicates that the process could not be started
     */
    public static final class StartupException extends Exception {


        public StartupException(String message) {
            super(message);
        }
    }


    /**
     * Process has been killed due to the specified timeout
     */
    public static final class TimeoutException extends Exception {
        public TimeoutException(String message) {
            super(message);
        }
    }

    public static interface OutputConsumer {
        public void process(InputStream is);
    }

    public static interface InputProvider {
        public void process(OutputStream os);
    }

    public static class StringInputProvider implements InputProvider {
        String input;

        public StringInputProvider(String input) {
            this.input = input;
        }

        public void process(OutputStream os) {
            Writer w = new OutputStreamWriter(os);
            try {
                w.write(input);
                w.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class StringOutputConsumer implements OutputConsumer {
        StringBuffer output = new StringBuffer();
        boolean ready = false;
        private String prefix;

        public String toString() {
            return getOutputWait().toString();
        }

        public StringOutputConsumer() {
            this("");
        }

        public StringOutputConsumer(String prefix) {
            this.prefix = prefix;
        }

        public void process(InputStream is) {
            BufferedReader r = new BufferedReader(new InputStreamReader(is));
            String line;
            try {
                while ((line = r.readLine()) != null) {
                    output.append(prefix);
                    output.append(line);
                    output.append("\n");
                }
                is.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ready = true;
        }

        public StringBuffer getOutputWait(){
            while (!ready){
                try {
                    Thread.sleep(1,0);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
            return output;
        }
        public StringBuffer getOutput() {
            if (!ready) {
                throw new IllegalStateException("Process has not finished. Can't provide output at this point");
            }
            return output;
        }
    }

    public static class NullOutputConsumer implements OutputConsumer {

        public void process(InputStream is) {
            byte[] buffer = new byte[1000];
            try {
                while (1 == 1) {
                    if (is.read(buffer) == -1) {
                        break;
                    }
                }
                is.close();
            } catch (IOException e) {
                throw new RuntimeException(e);  //todo: ExceptionHandling
            }
        }
    }

    public class Escaper {
        String input;
        int pos = 0;

        public Escaper(String input) {
            this.input = input;
        }

        char current() {
            return input.charAt(pos);
        }

        char peak() {
            if (pos < input.length()) {
                return input.charAt(pos);
            } else {
                return 'X';
            }
        }

        char overpeak() {
            if (pos + 1 < input.length()) {
                return input.charAt(pos + 1);
            } else {
                return 'X';
            }
        }


        char next() {
            char c = current();
            pos++;
            return c;

        }


        public String[] getChunks() {
            List chunks = new LinkedList();
            while (pos < input.length()) {
                char c = next();
                if (c == '\'') {
                    if (peak() != '\'') {
                        chunks.add(quoted());
                        continue;
                    } else {
                        if (overpeak() == '\'') {
                            chunks.add(quoted());
                            continue;
                        }
                        next();
                        chunks.add(unqoted('\''));
                        continue;
                    }
                }
                if (c == ' ') {
                    continue;
                }
                chunks.add(unqoted(c));
            }

            String[]  temp = new String[chunks.size()];
            int i = 0;
            for (Iterator iterator = chunks.iterator(); iterator.hasNext();) {
                String ch = (String) iterator.next();
                temp[i] = ch;
                i++;
            }
            return temp;
        }

        private String unqoted(char first) {
            StringBuffer temp = new StringBuffer();
            temp.append(first);
            while (pos < input.length()) {
                char c = next();
                if (c == ' ') {
                    break;
                }
                if (c == '\'') {
                    if (peak() == '\'') {
                        temp.append('\'');
                        next();
                        continue;
                    } else {
                        pos--;// we need the ' to recognize the start of the quoted part..
                        break;
                    }
                }
                temp.append(c);
            }
            return temp.toString();
        }

        private String quoted() {
            StringBuffer temp = new StringBuffer();
            while (pos < input.length()) {
                char c = next();
                if (c == '\'') {
                    if (peak() == '\'') {
                        temp.append('\'');
                        next();
                        continue;
                    } else {
                        break;
                    }
                }
                temp.append(c);
            }
            return temp.toString();
        }
    }

    //static methods

    /**
     * runs the given external program and returns on completion.
     *
     * @return The standard output of the program
     * @throws RuntimeException 1 when the program cannot be started or when the program returns a non zero error code.
     *                          In the latter case the output of stderr is appended to the message
     * @params command The program an its parameters to escape spaces use double quotes
     */
    public static String run(String command) {
        return run(command, "");
    }

    /**
     * runs the given external program and returns on completion.
     *
     * @param command The program an its parameters to escape spaces use double quotes
     * @param input   A String to be fed into the standard input
     * @return The standard output of the program
     * @throws RuntimeException 1 when the program cannot be started or when the program returns a non zero error code.
     *                          In the latter case the output of stderr is appended to the message
     */
    public static String run(String command, String input) {
        final StringOutputConsumer err = new StringOutputConsumer();
        final StringOutputConsumer out = new StringOutputConsumer();
        Process2 proc = new Process2(command, out, err, new Process2.StringInputProvider(input));
        try {
            int ret = proc.runBlocked();
            if (ret != 0) throw new RuntimeException("Process returned error code " + ret + "\n" + err.toString());
        } catch (StartupException e) {
            throw new RuntimeException("Process could not be started", e);
        } catch (InterruptedException e) {
            throw new RuntimeException("Process has been interrupted", e);
        }
        return out.toString();
    }

    /**
     * runs the given external program and returns on completion.
     *
     * @param command The program an its parameters to escape spaces use double quotes
     * @param input   A String to be fed into the standard input
     * @param timeout The timeout for the external process in milliseconds
     * @return The standard output of the program
     * @throws RuntimeException 1 when the program cannot be started or when the program returns a non zero error code.
     *                          In the latter case the output of stderr is appended to the message
     * @throws TimeoutException is thrown, when the process is terminated by the timeout
     */
    public static String run(String command, String input, long timeout) throws TimeoutException {
        final StringOutputConsumer err = new StringOutputConsumer();
        final StringOutputConsumer out = new StringOutputConsumer();
        Process2 proc = new Process2(command, out, err, new Process2.StringInputProvider(input));
        try {
            int ret = proc.runBlocked(timeout);
            if (ret != 0) throw new RuntimeException("Process returned error code " + ret + "\n" + err.toString());
        } catch (StartupException e) {
            throw new RuntimeException("Process could not be started", e);
        } catch (InterruptedException e) {
            throw new RuntimeException("Process has been interrupted", e);
        }
        return out.toString();
    }

    /**
     * runs the given external program and returns on completion.
     *
     * @param command The program an its parameters to escape spaces use double quotes
     * @param timeout The timeout for the external process in milliseconds
     * @return The standard output of the program
     * @throws RuntimeException 1 when the program cannot be started or when the program returns a non zero error code.
     *                          In the latter case the output of stderr is appended to the message
     * @throws TimeoutException is thrown, when the process is terminated by the timeout
     */
    public static String run(String command, long timeout) throws TimeoutException {
        return run(command, "", timeout);
    }
}