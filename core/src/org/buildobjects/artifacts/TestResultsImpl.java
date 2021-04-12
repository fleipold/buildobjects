package org.buildobjects.artifacts;

import org.junit.runner.Result;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * User: fleipold
 * Date: Nov 3, 2008
 * Time: 3:45:00 PM
 */
public class TestResultsImpl implements TestResults {
    final List<SuiteResult> suitResults;
    int nTests;
    int nFailed;
    int nError;

    public TestResultsImpl(List<SuiteResult> suitResults) {

        this.suitResults =(suitResults);
        Collections.sort(suitResults, new Comparator<SuiteResult>() {
            public int compare(SuiteResult o1, SuiteResult o2) {
                final Result r1 = o1.getResult();
                final Result r2 = o2.getResult();
                return new Integer(r1.getFailureCount()).compareTo(
                       new Integer(r2.getFailureCount())) * -1;
            }
        });
        for (SuiteResult suiteResult : suitResults) {
            nTests += suiteResult.getResult().getRunCount();
            nFailed += suiteResult.getResult().getFailureCount();

        }


    }

    public List<SuiteResult> getSuiteResults() {
        return suitResults;
    }

    public int getNTests() {
        return nTests;
    }

    public int getNFailed() {
        return nFailed;
    }

}
