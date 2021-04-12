package org.buildobjects.publishers;

import org.buildobjects.artifacts.Sources;
import org.buildobjects.artifacts.resources.Resource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

/**
 * User: fleipold
 * Date: Oct 28, 2008
 * Time: 9:51:32 AM
 */
public class SourceFileOut implements Publishable {
    private final Sources sources;

    public SourceFileOut(Sources sources) {
        this.sources = sources;
    }


    public void publish(FileSink publisher) {
        for (Resource file : sources.getResources().getAll()) {
            OutputStream out = publisher.getOutputStream(file.getPath().toRelativePathString());
            try {
                out.write(file.getBytes());
                out.close();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
