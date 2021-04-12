package org.buildobjects.artifacts;

import org.buildobjects.artifacts.resources.Resources;

import java.io.File;
import java.util.List;

/**
 *
 * Change to use resources instead..
 *
 */
public class FlatFileSources implements Sources {
    final File baseDir;

    List<File> otherFiles;
    FileSources wrappedSources;

    public FlatFileSources(File file) {
        if (!file.exists()) {
            throw new IllegalArgumentException("Not a valid source baseDir " + file.getAbsolutePath());
        }
        this.baseDir = file;
        wrappedSources = new FileSources(file);
    }


    public FlatFileSources(String filePath) {
        this(new File(filePath));
    }

    public File getBaseDir() {
        return baseDir;
    }

    public String getName() {
        return wrappedSources.getName();
    }

    public Resources getResources() {
        //todo: filter resources
        return wrappedSources.getResources();
    }
}