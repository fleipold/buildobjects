package org.buildobjects.publishers;

import org.buildobjects.artifacts.SuiteResult;
import org.buildobjects.artifacts.TestResults;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.io.PrintWriter;

/**
 * User: fleipold
 * Date: Oct 14, 2008
 * Time: 5:17:54 PM
 */
public class TestResultFileOut implements Publishable {
    final private TestResults results;
    private final String fileName;

    public TestResultFileOut(TestResults results, String fileName) {
        this.results = results;
        this.fileName = fileName;
    }


    public void publish(FileSink publisher) {
        PrintWriter writer = new PrintWriter(publisher.getOutputStream(fileName));
        renderHeader(writer);
        for (SuiteResult suiteResult : results.getSuiteResults()) {
            renderResult(writer, suiteResult);
        }
        writer.close();
    }

    private void renderHeader(PrintWriter writer) {
        writer.println("Tests run: "+results.getNTests() + "  Failures: " + results.getNFailed()); 
    }

    private void renderResult(PrintWriter writer, SuiteResult suiteResult) {
        writer.print(suiteResult.getClassName());
        Result result = suiteResult.getResult();
             writer.print(" run: " + result.getRunCount());
                    writer.println(" failed: " + result.getFailureCount());
                    for (Failure failure : result.getFailures()) {
                        writer.println(failure.getTrace());
                    }
        writer.println();
    }
}
