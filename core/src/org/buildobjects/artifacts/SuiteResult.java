package org.buildobjects.artifacts;

import org.junit.runner.Result;

/**
 * User: fleipold
 * Date: Oct 14, 2008
 * Time: 5:14:35 PM
 */
public class SuiteResult  {
    private final String className;
    private final Result result;

    public SuiteResult(String className, Result result) {

        this.className = className;
        this.result = result;
    }

    public String getClassName() {
        return className;
    }

    public Result getResult() {
        return result;
    }
}
