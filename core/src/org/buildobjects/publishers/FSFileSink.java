package org.buildobjects.publishers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * User: fleipold
 * Date: Oct 14, 2008
 * Time: 4:31:16 PM
 */
public class FSFileSink implements FileSink {
    final File baseDir;
    private final PublicationListener listener;

    public FSFileSink(File baseDir, PublicationListener listener) {
        this.baseDir = baseDir;


        this.listener = listener;
    }

    public FSFileSink(File file) {
        this(file, new PublicationListener() {
            public void published(PublicationInfo publication) {
                // nada                
            }
        });
    }

    public OutputStream getOutputStream(String fileName) {
        File output = new File(baseDir, fileName);
        output.getParentFile().mkdirs();
        try {
            listener.published(new PublicationInfo(fileName, output));
            return new FileOutputStream(output);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public FileSink getChild(String name) {
        return new FSFileSink(new File(baseDir,name), listener);
    }
}
