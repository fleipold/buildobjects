package org.buildobjects.artifacts;

import java.util.List;

/**
 * User: fleipold
 * Date: Nov 3, 2008
 * Time: 4:03:52 PM
 */
public interface TestResults {
    List<SuiteResult> getSuiteResults();

    int getNTests();

    int getNFailed();

    
}
