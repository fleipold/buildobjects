package org.buildobjects.publishers;

import java.io.File;

/**
 * User: fleipold
 * Date: Oct 23, 2008
 * Time: 4:56:50 PM
 */
public class PublicationInfo {
    private final String fileName;
    private final File output;


    public PublicationInfo(String fileName, File output) {

        this.fileName = fileName;
        this.output = output;
    }

    public String getFileName() {
        return fileName;
    }

    public File getOutput() {
        return output;
    }
}
