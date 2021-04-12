package org.buildobjects;

import org.buildobjects.util.Revision;

import java.io.File;
import java.util.Date;

/**
 * User: fleipold
 * Date: Oct 21, 2008
 * Time: 6:44:24 PM
 */
public class BuildResult  {
    private BuildState state;
    private File folder;

    private Date timestamp;
    private int buildNumber;

    private  Revision revision;
    private  long time;

    public BuildResult(BuildState state, File folder, Date timestamp, int buildNumber, Revision revision, long time) {
        this.state = state;
        this.folder = folder;
        this.timestamp = timestamp;
        this.buildNumber = buildNumber;

        this.revision = revision;
        this.time = time;
    }

    // This is for our friend xstream
    public BuildResult() {
    }

    public BuildState getState() {
        return state;
    }

    public File getFolder() {
        return folder;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setState(BuildState state) {
        this.state = state;
    }

    public void setFolder(File folder) {
        this.folder = folder;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getBuildNumber() {
        return buildNumber;
    }

    public void setBuildNumber(int buildNumber) {
        this.buildNumber = buildNumber;
    }

    public Revision getRevision() {
        return revision;
    }

    public void setRevision(Revision revision) {
        this.revision = revision;
    }

    public long getTime() {
    return time;
}

    public void setTime(long time) {
        this.time = time;
    }
}
