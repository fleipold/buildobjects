package org.buildobjects.tasklet;

import org.buildobjects.artifacts.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * User: fleipold
 * Date: Oct 21, 2008
 * Time: 3:46:12 PM
 */
public class TaskletStartupParameters {
    private final String sourceFolder;
    private final boolean recurse;
    private final String taskletName;
    private Sources sources;

    public TaskletStartupParameters(String sourceFolder, boolean recurse, String taskletName) {
        this.sourceFolder = sourceFolder;
        this.recurse = recurse;
        if (new File(taskletName).exists() && new File(taskletName).isFile()) {
            File tasklet = new File(taskletName);
            this.taskletName = tasklet.getName().substring(0, tasklet.getName().lastIndexOf('.'));

            try {
                final FileInputStream inputStream = new FileInputStream(tasklet);
                sources = new StringSourcesBuilder()
                        .addClass(this.taskletName, StringUtils.join(IOUtils.readLines(inputStream),"\n")).toSources();
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            sources = isRecurse() ?
                    new FileSources(getSourceFolder(), "")
                    : new FlatFileSources(getSourceFolder());
            this.taskletName = taskletName;
        }
    }

    private String getSourceFolder() {
        return sourceFolder;
    }

    private boolean isRecurse() {
        return recurse;
    }

    public String getTaskletName() {
        return taskletName;
    }

    public Sources getSources() {

        return sources;
    }
}
