package org.buildobjects.internal;

import org.buildobjects.BuildResult;

import java.util.ArrayList;
import java.util.List;

/**
 * User: fleipold
 * Date: Oct 21, 2008
 * Time: 11:26:11 PM
 */
public class BuildEnvironmentState {
    private List<BuildResult> results = new ArrayList<BuildResult>();
    private int nextBuildNo;

    public List<BuildResult> getResults() {
        return results;
    }



    public void setResults(List<BuildResult> results) {
        this.results = results;
    }

    public void addResult(BuildResult result){
        results.add(result);
    }

    public int getNextBuildNo() {
        return nextBuildNo;
    }

    public void setNextBuildNo(int nextBuildNo) {
        this.nextBuildNo = nextBuildNo;
    }
}
